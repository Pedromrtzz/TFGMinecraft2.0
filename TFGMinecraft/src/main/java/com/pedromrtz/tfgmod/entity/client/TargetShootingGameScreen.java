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

    private int hits = 0;
    private int shots = 0;
    private int targetX = 0;
    private int targetY = 0;
    private int targetSize = 34;

    private String message = "Hit 5 targets to test the shooting stall.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_HITS = 5;
    private static final int MAX_SHOTS = 9;

    public TargetShootingGameScreen() {
        super(Component.literal("Target Shooting"));
    }

    @Override
    protected void init() {
        super.init();
        moveTarget();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 480;
        int boxH = 320;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Target Shooting", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Click the targets before you run out of shots.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        int areaX = x + 45;
        int areaY = y + 65;
        int areaW = boxW - 90;
        int areaH = 160;

        gg.fill(areaX, areaY, areaX + areaW, areaY + areaH, 0xFF202020);
        gg.fill(areaX + 4, areaY + 4, areaX + areaW - 4, areaY + areaH - 4, 0xFF333333);

        drawTarget(gg, areaX + targetX, areaY + targetY);

        gg.drawCenteredString(
                this.font,
                "Hits: " + hits + " / " + REQUIRED_HITS,
                this.width / 2,
                y + 238,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Shots left: " + (MAX_SHOTS - shots) + " / " + MAX_SHOTS,
                this.width / 2,
                y + 254,
                shots >= 6 ? 0xFFFFAA00 : 0xAAAAAA
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 276, messageColor);

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

    private void drawTarget(GuiGraphics gg, int x, int y) {
        gg.fill(x, y, x + targetSize, y + targetSize, 0xFFFF5555);
        gg.fill(x + 6, y + 6, x + targetSize - 6, y + targetSize - 6, 0xFFFFFFFF);
        gg.fill(x + 12, y + 12, x + targetSize - 12, y + targetSize - 12, 0xFFFF5555);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 480;
        int boxH = 320;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        int areaX = x + 45;
        int areaY = y + 65;

        int targetRealX = areaX + targetX;
        int targetRealY = areaY + targetY;

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 32;

        if (isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)) {
            playClickSound();
            onClose();
            return true;
        }

        shots++;

        if (isMouseOver(mouseX, mouseY, targetRealX, targetRealY, targetSize, targetSize)) {
            hits++;
            message = "Good hit! Keep your focus.";
            messageColor = 0xFF55FF55;
            playHitSound();

            if (hits >= REQUIRED_HITS) {
                ModNetwork.CHANNEL.send(
                        new CompleteChapter4TargetsC2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );
                onClose();
                return true;
            }

            if (hits == 2) targetSize = 30;
            if (hits == 4) targetSize = 26;

            moveTarget();
            return true;
        }

        message = "Missed shot. Aim carefully.";
        messageColor = 0xFFFF5555;
        playMissSound();

        if (shots >= MAX_SHOTS) {
            hits = 0;
            shots = 0;
            targetSize = 34;
            message = "You ran out of shots. Try again.";
            messageColor = 0xFFFFAA00;
        }

        moveTarget();
        return true;
    }

    private void moveTarget() {
        int areaW = 480 - 90;
        int areaH = 160;

        targetX = 20 + (int) (Math.random() * (areaW - targetSize - 40));
        targetY = 20 + (int) (Math.random() * (areaH - targetSize - 40));
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