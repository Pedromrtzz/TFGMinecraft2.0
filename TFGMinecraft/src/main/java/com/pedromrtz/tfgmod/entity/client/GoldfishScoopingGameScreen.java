package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter4GoldfishC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

public class GoldfishScoopingGameScreen extends Screen {

    private int score = 0;
    private int misses = 0;

    private float fishX = 0;
    private float fishY = 0;
    private float fishSpeedX = 1.2f;
    private float fishSpeedY = 0.8f;

    private boolean hardFish = false;

    private int timeLeft = 600; // 30 seconds
    private int spawnCooldown = 0;

    private String message = "Catch enough fish before time runs out.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_SCORE = 8;
    private static final int MAX_MISSES = 5;

    public GoldfishScoopingGameScreen() {
        super(Component.literal("Goldfish Scooping"));
    }

    @Override
    protected void init() {
        super.init();
        spawnFish();
    }

    @Override
    public void tick() {
        super.tick();

        if (timeLeft > 0) {
            timeLeft--;
        }

        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        moveFish();

        if (timeLeft <= 0) {
            if (score >= REQUIRED_SCORE) {
                completeGame();
            } else {
                resetGame("Time is over. Try again and reach the minimum score.");
            }
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 480;
        int boxH = 330;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Goldfish Scooping", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Catch moving fish with the paper scoop. Hard fish are faster but worth more points.",
                this.width / 2,
                y + 34,
                0xDDDDDD
        );

        int pondX = x + 45;
        int pondY = y + 70;
        int pondW = boxW - 90;
        int pondH = 140;

        gg.fill(pondX, pondY, pondX + pondW, pondY + pondH, 0xFF1E5F74);
        gg.fill(pondX + 4, pondY + 4, pondX + pondW - 4, pondY + pondH - 4, 0xFF2A9DB8);

        drawFish(gg, pondX + (int) fishX, pondY + (int) fishY);

        drawTimeBar(gg, x, y, boxW);

        gg.drawCenteredString(
                this.font,
                "Score: " + score + " / " + REQUIRED_SCORE,
                this.width / 2,
                y + 232,
                score >= REQUIRED_SCORE ? 0xFF55FF55 : 0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Misses: " + misses + " / " + MAX_MISSES,
                this.width / 2,
                y + 248,
                misses >= 3 ? 0xFFFFAA00 : 0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                hardFish ? "Current fish: Fast koi (+2 points)" : "Current fish: Slow goldfish (+1 point)",
                this.width / 2,
                y + 264,
                hardFish ? 0xFFFFAA00 : 0xFF55FF55
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 284, messageColor);

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 32;

        int color = isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(exitX, exitY, exitX + exitW, exitY + exitH, color);
        gg.drawCenteredString(this.font, "Exit", exitX + exitW / 2, exitY + 7, 0xFFFFFF);

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawTimeBar(GuiGraphics gg, int x, int y, int boxW) {
        int barX = x + 60;
        int barY = y + 216;
        int barW = boxW - 120;
        int barH = 10;

        gg.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);

        int filled = Math.max(0, barW * timeLeft / 600);
        int color = timeLeft < 150 ? 0xFFFF5555 : 0xFF55AAFF;

        gg.fill(barX, barY, barX + filled, barY + barH, color);

        int seconds = timeLeft / 20;
        gg.drawCenteredString(this.font, "Time: " + seconds + "s", this.width / 2, barY - 12, 0xFFFFFF);
    }

    private void drawFish(GuiGraphics gg, int x, int y) {
        if (hardFish) {
            // Fast koi
            gg.fill(x, y + 5, x + 30, y + 18, 0xFFFF7733);
            gg.fill(x + 22, y + 2, x + 36, y + 21, 0xFFFFAA33);
            gg.fill(x + 6, y + 8, x + 9, y + 11, 0xFF000000);
            gg.fill(x + 12, y + 6, x + 16, y + 17, 0xFFFFFFFF);
        } else {
            // Slow goldfish
            gg.fill(x, y + 6, x + 26, y + 17, 0xFFFFAA33);
            gg.fill(x + 20, y + 3, x + 32, y + 20, 0xFFFFCC55);
            gg.fill(x + 5, y + 9, x + 8, y + 12, 0xFF000000);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 480;
        int boxH = 330;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        int pondX = x + 45;
        int pondY = y + 70;

        int fishRealX = pondX + (int) fishX;
        int fishRealY = pondY + (int) fishY;

        int fishW = hardFish ? 36 : 32;
        int fishH = 24;

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 32;

        if (isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)) {
            playClickSound();
            onClose();
            return true;
        }

        if (timeLeft <= 0) {
            return true;
        }

        if (isMouseOver(mouseX, mouseY, fishRealX, fishRealY, fishW, fishH)) {
            int points = hardFish ? 2 : 1;
            score += points;

            message = hardFish ? "Great catch! Fast koi gives 2 points." : "Nice scoop! Goldfish gives 1 point.";
            messageColor = 0xFF55FF55;

            playSuccessSound();

            if (score >= REQUIRED_SCORE) {
                completeGame();
                return true;
            }

            spawnCooldown = 6;
            spawnFish();
            return true;
        }

        misses++;
        score = Math.max(0, score - 1);

        message = "Missed! The paper scoop weakens and you lose 1 point.";
        messageColor = 0xFFFF5555;

        playFailSound();

        if (misses >= MAX_MISSES) {
            resetGame("The paper scoop broke. Try again.");
            return true;
        }

        spawnFish();
        return true;
    }

    private void moveFish() {
        int pondW = 480 - 90;
        int pondH = 140;

        fishX += fishSpeedX;
        fishY += fishSpeedY;

        if (fishX <= 10 || fishX >= pondW - 45) {
            fishSpeedX *= -1;
        }

        if (fishY <= 10 || fishY >= pondH - 35) {
            fishSpeedY *= -1;
        }
    }

    private void spawnFish() {
        int pondW = 480 - 90;
        int pondH = 140;

        hardFish = Math.random() < 0.35;

        fishX = 20 + (int) (Math.random() * (pondW - 80));
        fishY = 20 + (int) (Math.random() * (pondH - 60));

        float baseSpeed = hardFish ? 2.2f : 1.2f;

        fishSpeedX = Math.random() < 0.5 ? baseSpeed : -baseSpeed;
        fishSpeedY = Math.random() < 0.5 ? baseSpeed * 0.6f : -baseSpeed * 0.6f;
    }

    private void resetGame(String resetMessage) {
        score = 0;
        misses = 0;
        timeLeft = 600;
        spawnCooldown = 0;
        message = resetMessage;
        messageColor = 0xFFFFAA00;
        spawnFish();
        playFailSound();
    }

    private void completeGame() {
        ModNetwork.CHANNEL.send(
                new CompleteChapter4GoldfishC2SPacket(),
                PacketDistributor.SERVER.noArg()
        );

        onClose();
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private void playSuccessSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 1.0f, 1.2f);
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7f, 1.4f);
        }
    }

    private void playFailSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.7f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}