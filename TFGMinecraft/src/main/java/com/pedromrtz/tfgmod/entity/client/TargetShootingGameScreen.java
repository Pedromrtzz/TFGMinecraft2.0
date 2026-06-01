package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter4TargetsC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

public class TargetShootingGameScreen extends Screen {

    private int score = 0;
    private int shots = 0;
    private int combo = 0;
    private int consecutiveMisses = 0;

    private float targetX = 0;
    private float targetY = 0;
    private float targetSpeedX = 1.4f;
    private float targetSpeedY = 0.8f;

    private int targetSize = 34;
    private boolean smallTarget = false;
    private int missWarningTicks = 0;
    private int comboFlashTicks = 0;

    private String message = "Score 10 points to test the shooting stall.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_SCORE = 10;
    private static final int MAX_SHOTS = 12;

    public TargetShootingGameScreen() {
        super(Component.literal("Target Shooting"));
    }

    @Override
    protected void init() {
        super.init();
        spawnTarget();
    }

    @Override
    public void tick() {
        super.tick();

        moveTarget();

        if (missWarningTicks > 0) {
            missWarningTicks--;
        }

        if (comboFlashTicks > 0) {
            comboFlashTicks--;
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 335;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Target Shooting", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Hit moving targets. Small targets give more points, and combos give bonuses.",
                this.width / 2,
                y + 34,
                0xDDDDDD
        );

        int areaX = x + 45;
        int areaY = y + 65;
        int areaW = boxW - 90;
        int areaH = 165;

        gg.fill(areaX, areaY, areaX + areaW, areaY + areaH, 0xFF202020);
        gg.fill(areaX + 4, areaY + 4, areaX + areaW - 4, areaY + areaH - 4, 0xFF333333);

        if (missWarningTicks > 0) {
            gg.fill(areaX, areaY, areaX + areaW, areaY + areaH, 0x33FF0000);
            gg.drawCenteredString(
                    this.font,
                    "Focus! Too many missed shots.",
                    this.width / 2,
                    areaY + 8,
                    0xFFFF5555
            );
        }

        drawTarget(gg, areaX + (int) targetX, areaY + (int) targetY);

        if (comboFlashTicks > 0) {
            gg.drawCenteredString(
                    this.font,
                    "COMBO BONUS!",
                    this.width / 2,
                    areaY + areaH - 18,
                    0xFFFFAA00
            );
        }

        gg.drawCenteredString(
                this.font,
                "Score: " + score + " / " + REQUIRED_SCORE,
                this.width / 2,
                y + 242,
                score >= REQUIRED_SCORE ? 0xFF55FF55 : 0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Shots left: " + (MAX_SHOTS - shots) + " / " + MAX_SHOTS,
                this.width / 2,
                y + 258,
                shots >= 8 ? 0xFFFFAA00 : 0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                "Combo: " + combo + " hits in a row",
                this.width / 2,
                y + 274,
                combo >= 3 ? 0xFFFFAA00 : 0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                smallTarget ? "Current target: Small moving target (+2 points)" : "Current target: Large moving target (+1 point)",
                this.width / 2,
                y + 290,
                smallTarget ? 0xFFFFAA00 : 0xFF55FF55
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 307, messageColor);

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 24;

        int color = isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(exitX, exitY, exitX + exitW, exitY + exitH, color);
        gg.drawCenteredString(this.font, "Exit", exitX + exitW / 2, exitY + 7, 0xFFFFFF);

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawTarget(GuiGraphics gg, int x, int y) {
        gg.fill(x, y, x + targetSize, y + targetSize, 0xFFFF5555);
        gg.fill(x + 5, y + 5, x + targetSize - 5, y + targetSize - 5, 0xFFFFFFFF);
        gg.fill(x + 10, y + 10, x + targetSize - 10, y + targetSize - 10, 0xFFFF5555);

        int centerPadding = smallTarget ? 14 : 13;
        gg.fill(
                x + centerPadding,
                y + centerPadding,
                x + targetSize - centerPadding,
                y + targetSize - centerPadding,
                0xFFFFFFFF
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 500;
        int boxH = 335;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        int areaX = x + 45;
        int areaY = y + 65;

        int targetRealX = areaX + (int) targetX;
        int targetRealY = areaY + (int) targetY;

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 24;

        if (isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)) {
            playClickSound();
            onClose();
            return true;
        }

        shots++;

        if (isMouseOver(mouseX, mouseY, targetRealX, targetRealY, targetSize, targetSize)) {
            int points = smallTarget ? 2 : 1;

            combo++;
            consecutiveMisses = 0;

            if (combo > 0 && combo % 3 == 0) {
                points += 2;
                comboFlashTicks = 25;
                message = "Combo bonus! Three hits in a row.";
                messageColor = 0xFFFFAA00;
            } else {
                message = smallTarget
                        ? "Great shot! Small targets are worth 2 points."
                        : "Good hit! Keep your focus.";
                messageColor = 0xFF55FF55;
            }

            score += points;
            playHitSound();

            if (score >= REQUIRED_SCORE) {
                ModNetwork.CHANNEL.send(
                        new CompleteChapter4TargetsC2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );
                onClose();
                return true;
            }

            spawnTarget();
            return true;
        }

        combo = 0;
        consecutiveMisses++;

        message = "Missed shot. Combo reset.";
        messageColor = 0xFFFF5555;

        playMissSound();

        if (consecutiveMisses >= 2) {
            missWarningTicks = 35;
            message = "Careful! Two misses in a row.";
            messageColor = 0xFFFFAA00;
        }

        if (shots >= MAX_SHOTS) {
            score = 0;
            shots = 0;
            combo = 0;
            consecutiveMisses = 0;
            message = "You ran out of shots. Try again.";
            messageColor = 0xFFFFAA00;
        }

        spawnTarget();
        return true;
    }

    private void moveTarget() {
        int areaW = 500 - 90;
        int areaH = 165;

        targetX += targetSpeedX;
        targetY += targetSpeedY;

        if (targetX <= 10 || targetX >= areaW - targetSize - 10) {
            targetSpeedX *= -1;
        }

        if (targetY <= 10 || targetY >= areaH - targetSize - 10) {
            targetSpeedY *= -1;
        }
    }

    private void spawnTarget() {
        int areaW = 500 - 90;
        int areaH = 165;

        smallTarget = Math.random() < 0.4;
        targetSize = smallTarget ? 26 : 38;

        targetX = 20 + (int) (Math.random() * (areaW - targetSize - 40));
        targetY = 20 + (int) (Math.random() * (areaH - targetSize - 40));

        float baseSpeed = smallTarget ? 2.2f : 1.4f;

        targetSpeedX = Math.random() < 0.5 ? baseSpeed : -baseSpeed;
        targetSpeedY = Math.random() < 0.5 ? baseSpeed * 0.6f : -baseSpeed * 0.6f;
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private void playHitSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 0.8f, 1.4f);
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.6f, 1.4f);
        }
    }

    private void playMissSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.7f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}