package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter5BowTrainingC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class SamuraiBowTrainingScreen extends Screen {

    private int markerX = 0;
    private boolean movingRight = true;

    private int score = 0;
    private int attempts = 0;
    private int hits = 0;
    private int missesInRow = 0;

    private boolean finished = false;
    private boolean completed = false;

    private String message = "Control your breathing before releasing the arrow.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_HITS = 3;
    private static final int MAX_ATTEMPTS = 6;

    public SamuraiBowTrainingScreen() {
        super(Component.literal("Samurai Focus Training"));
    }

    @Override
    public void tick() {
        if (finished) return;

        int speed = 4 + hits;

        if (movingRight) {
            markerX += speed;

            if (markerX >= 260) {
                markerX = 260;
                movingRight = false;
            }

        } else {
            markerX -= speed;

            if (markerX <= 0) {
                markerX = 0;
                movingRight = true;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (finished) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                onClose();
            }
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            handleShot();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void handleShot() {
        attempts++;

        int greenStart = getGreenStart();
        int greenEnd = getGreenEnd();

        boolean perfect = markerX >= greenStart + 8 && markerX <= greenEnd - 8;
        boolean success = markerX >= greenStart && markerX <= greenEnd;

        if (perfect) {
            score += 2;
            hits++;
            missesInRow = 0;

            message = "Perfect shot. Breath, focus and release.";
            messageColor = 0xFF55FF55;
            playSuccess();

        } else if (success) {
            score++;
            hits++;
            missesInRow = 0;

            message = "Good shot. Your focus is improving.";
            messageColor = 0xFFAAFFAA;
            playSuccess();

        } else {
            score = Math.max(0, score - 1);
            missesInRow++;

            if (missesInRow >= 2) {
                message = "Calm your breathing. A samurai does not rush.";
                messageColor = 0xFFFFAA00;
            } else {
                message = "Missed shot. Wait for the right moment.";
                messageColor = 0xFFFF5555;
            }

            playFail();
        }

        if (hits >= REQUIRED_HITS) {
            finished = true;
            completed = true;

            ModNetwork.CHANNEL.send(
                    new CompleteChapter5BowTrainingC2SPacket(),
                    PacketDistributor.SERVER.noArg()
            );

            message = "Training complete. You broke all three targets.";
            messageColor = 0xFF55FF55;
            playLevelUp();
            return;
        }

        if (attempts >= MAX_ATTEMPTS) {
            finished = true;
            completed = false;

            message = "Training failed. Try again with better focus.";
            messageColor = 0xFFFF5555;
            playFail();
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {

        this.renderBackground(gg, mouseX, mouseY, pt);

        int panelW = 420;
        int panelH = 260;

        int panelX = (this.width - panelW) / 2;
        int panelY = (this.height - panelH) / 2;

        gg.fill(
                panelX,
                panelY,
                panelX + panelW,
                panelY + panelH,
                0xDD000000
        );

        gg.fill(
                panelX - 2,
                panelY - 2,
                panelX + panelW + 2,
                panelY + panelH + 2,
                0xFF222222
        );

        int centerX = width / 2;
        int centerY = panelY + panelH / 2;
        
        gg.drawCenteredString(
                font,
                "Samurai Bow Training",
                centerX,
                centerY - 125,
                0xFFFFFF
        );

        gg.drawCenteredString(
                font,
                "Press SPACE when your breathing and focus align.",
                centerX,
                centerY - 105,
                0xAAAAAA
        );

        drawTargets(gg, centerX, centerY - 72);

        gg.drawCenteredString(
                font,
                "Hits: " + hits + " / " + REQUIRED_HITS +
                        "   Attempts: " + attempts + " / " + MAX_ATTEMPTS +
                        "   Score: " + score,
                centerX,
                centerY - 28,
                0xFFD700
        );

        drawBreathingBar(gg, centerX, centerY + 5);

        gg.drawCenteredString(
                font,
                message,
                centerX,
                centerY + 55,
                messageColor
        );

        if (finished) {
            gg.drawCenteredString(
                    font,
                    completed ? "Training Complete" : "Training Failed",
                    centerX,
                    centerY + 85,
                    completed ? 0xFF55FF55 : 0xFFFF5555
            );

            gg.drawCenteredString(
                    font,
                    "Press ENTER to close",
                    centerX,
                    centerY + 105,
                    0xFFFFFF
            );
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawBreathingBar(GuiGraphics gg, int centerX, int y) {
        int barWidth = 260;
        int barHeight = 18;
        int startX = centerX - barWidth / 2;

        int greenStart = getGreenStart();
        int greenEnd = getGreenEnd();

        gg.fill(startX, y, startX + barWidth, y + barHeight, 0xFF222222);

        gg.fill(startX, y, startX + greenStart, y + barHeight, 0xFF552222);

        gg.fill(startX + greenStart, y, startX + greenEnd, y + barHeight, 0xFF228833);

        gg.fill(startX + greenEnd, y, startX + barWidth, y + barHeight, 0xFF552222);

        gg.fill(
                startX + markerX,
                y - 5,
                startX + markerX + 7,
                y + barHeight + 5,
                0xFFFFFFFF
        );

        gg.drawCenteredString(
                font,
                "Breathing focus",
                centerX,
                y - 16,
                0xAAAAAA
        );
    }

    private void drawTargets(GuiGraphics gg, int centerX, int y) {
        int size = 34;
        int gap = 22;
        int startX = centerX - (size * 3 + gap * 2) / 2;

        for (int i = 0; i < 3; i++) {
            int x = startX + i * (size + gap);

            if (i < hits) {
                // Diana rota
                gg.fill(x, y, x + size, y + size, 0xFF331111);
                gg.drawCenteredString(font, "X", x + size / 2, y + 12, 0xFFFF5555);
            } else {
                // Diana activa
                gg.fill(x, y, x + size, y + size, 0xFFEEEEEE);
                gg.fill(x + 5, y + 5, x + size - 5, y + size - 5, 0xFFAA3333);
                gg.fill(x + 12, y + 12, x + size - 12, y + size - 12, 0xFFFFFFFF);
                gg.fill(x + 16, y + 16, x + size - 16, y + size - 16, 0xFF222222);
            }
        }
    }

    private int getGreenStart() {
        return switch (hits) {
            case 0 -> 85;
            case 1 -> 95;
            default -> 105;
        };
    }

    private int getGreenEnd() {
        return switch (hits) {
            case 0 -> 175;
            case 1 -> 165;
            default -> 155;
        };
    }

    private void playSuccess() {
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8f, 1.3f);
        }
    }

    private void playFail() {
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.playSound(SoundEvents.VILLAGER_NO, 0.8f, 1.0f);
        }
    }

    private void playLevelUp() {
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.playSound(SoundEvents.PLAYER_LEVELUP, 0.9f, 1.2f);
        }
    }
}