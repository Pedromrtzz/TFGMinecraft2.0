package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter2QuizC2SPacket;
import com.pedromrtz.tfgmod.network.CompleteChapter3QuizC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.quiz.Chapter2QuizData;
import com.pedromrtz.tfgmod.quiz.Chapter3QuizData;
import com.pedromrtz.tfgmod.quiz.QuizQuestion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ChapterQuizScreen extends Screen {

    public enum QuizChapter {
        CHAPTER_2,
        CHAPTER_3
    }

    private final QuizChapter quizChapter;
    private final List<QuizQuestion> questions;
    private final int requiredCorrect;
    private final String title;

    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;

    private boolean finished = false;
    private boolean passed = false;

    private String feedback = "Choose the correct answer.";
    private int feedbackColor = 0xEEEEEE;

    private final List<ButtonArea> buttons = new ArrayList<>();

    private record ButtonArea(int x, int y, int w, int h, int answerIndex, String action) {}

    public ChapterQuizScreen(QuizChapter quizChapter) {
        super(Component.literal("Cultural Test"));

        this.quizChapter = quizChapter;

        if (quizChapter == QuizChapter.CHAPTER_3) {
            this.questions = Chapter3QuizData.QUESTIONS;
            this.requiredCorrect = 6;
            this.title = "Chapter 3 Cultural Test";
        } else {
            this.questions = Chapter2QuizData.QUESTIONS;
            this.requiredCorrect = 7;
            this.title = "Chapter 2 Cultural Test";
        }
    }

    public ChapterQuizScreen() {
        this(QuizChapter.CHAPTER_2);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 520;
        int boxH = 340;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                title,
                this.width / 2,
                y + 14,
                0xFFFFFF
        );

        if (finished) {
            renderResult(gg, mouseX, mouseY, x, y, boxW, boxH);
        } else {
            renderQuestion(gg, mouseX, mouseY, x, y, boxW, boxH);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void renderQuestion(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        buttons.clear();

        QuizQuestion q = questions.get(currentQuestion);

        gg.drawCenteredString(
                this.font,
                "Question " + (currentQuestion + 1) + " / " + questions.size(),
                this.width / 2,
                y + 38,
                0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                q.question(),
                this.width / 2,
                y + 68,
                0xFFFFAA00
        );

        int btnW = boxW - 80;
        int btnH = 26;
        int btnX = x + 40;
        int startY = y + 105;
        int gap = 12;

        for (int i = 0; i < q.answers().size(); i++) {
            int btnY = startY + i * (btnH + gap);

            int bg = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                    ? 0xFF555555 : 0xFF333333;

            gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, bg);

            gg.drawCenteredString(
                    this.font,
                    q.answers().get(i),
                    btnX + btnW / 2,
                    btnY + 9,
                    0xFFFFFF
            );

            buttons.add(new ButtonArea(btnX, btnY, btnW, btnH, i, "answer"));
        }

        gg.drawCenteredString(
                this.font,
                feedback,
                this.width / 2,
                y + 275,
                feedbackColor
        );

        gg.drawCenteredString(
                this.font,
                "Correct: " + correctAnswers + "   Wrong: " + wrongAnswers,
                this.width / 2,
                y + 298,
                0xDDDDDD
        );
    }

    private void renderResult(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        buttons.clear();

        String resultTitle = passed ? "Test Passed!" : "Test Failed";
        int titleColor = passed ? 0xFF55FF55 : 0xFFFF5555;

        gg.drawCenteredString(this.font, resultTitle, this.width / 2, y + 70, titleColor);

        gg.drawCenteredString(
                this.font,
                "Final score: " + correctAnswers + " / " + questions.size(),
                this.width / 2,
                y + 100,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Required score: " + requiredCorrect + " / " + questions.size(),
                this.width / 2,
                y + 118,
                0xAAAAAA
        );

        if (passed) {
            gg.drawCenteredString(
                    this.font,
                    getPassedMessage(),
                    this.width / 2,
                    y + 150,
                    0xEEEEEE
            );

            gg.drawCenteredString(
                    this.font,
                    "You may keep your cultural cards.",
                    this.width / 2,
                    y + 168,
                    0xAAAAAA
            );
        } else {
            gg.drawCenteredString(
                    this.font,
                    "You missed too many questions.",
                    this.width / 2,
                    y + 150,
                    0xEEEEEE
            );

            gg.drawCenteredString(
                    this.font,
                    "The chapter will need to be repeated.",
                    this.width / 2,
                    y + 168,
                    0xAAAAAA
            );
        }

        int btnW = boxW - 100;
        int btnH = 26;
        int btnX = x + 50;
        int btnY = y + boxH - 55;

        int bg = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, bg);
        gg.drawCenteredString(this.font, "Continue", btnX + btnW / 2, btnY + 9, 0xFFFFFF);

        buttons.add(new ButtonArea(btnX, btnY, btnW, btnH, -1, "continue"));
    }

    private String getPassedMessage() {
        return switch (quizChapter) {
            case CHAPTER_2 -> "You understood the key traditions of Omisoka.";
            case CHAPTER_3 -> "You understood the sushi preparation process.";
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonArea area : buttons) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();

                if (area.action.equals("answer")) {
                    handleAnswer(area.answerIndex);
                } else if (area.action.equals("continue")) {
                    sendResultPacket();
                    onClose();
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleAnswer(int selectedIndex) {
        QuizQuestion q = questions.get(currentQuestion);

        if (selectedIndex == q.correctIndex()) {
            correctAnswers++;
            feedback = "Correct!";
            feedbackColor = 0xFF55FF55;
            playCorrectSound();
        } else {
            wrongAnswers++;
            feedback = "Wrong answer.";
            feedbackColor = 0xFFFF5555;
            playWrongSound();
        }

        currentQuestion++;

        if (currentQuestion >= questions.size()) {
            finished = true;
            passed = correctAnswers >= requiredCorrect;
        }
    }

    private void sendResultPacket() {
        if (quizChapter == QuizChapter.CHAPTER_3) {
            ModNetwork.CHANNEL.send(
                    new CompleteChapter3QuizC2SPacket(passed),
                    PacketDistributor.SERVER.noArg()
            );
        } else {
            ModNetwork.CHANNEL.send(
                    new CompleteChapter2QuizC2SPacket(passed),
                    PacketDistributor.SERVER.noArg()
            );
        }
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private void playCorrectSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7f, 1.4f);
        }
    }

    private void playWrongSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.7f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}