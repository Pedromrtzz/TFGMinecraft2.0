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

    private int caughtFish = 0;
    private int attempts = 0;
    private int fishX = 0;
    private int fishY = 0;

    private String message = "Catch 3 goldfish to test the stall.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_FISH = 3;
    private static final int MAX_ATTEMPTS = 6;

    public GoldfishScoopingGameScreen() {
        super(Component.literal("Goldfish Scooping"));
    }

    @Override
    protected void init() {
        super.init();
        moveFish();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 460;
        int boxH = 310;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Goldfish Scooping", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Click the moving goldfish gently before your paper scoop breaks.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        int pondX = x + 55;
        int pondY = y + 70;
        int pondW = boxW - 110;
        int pondH = 135;

        gg.fill(pondX, pondY, pondX + pondW, pondY + pondH, 0xFF1E5F74);
        gg.fill(pondX + 4, pondY + 4, pondX + pondW - 4, pondY + pondH - 4, 0xFF2A9DB8);

        drawFish(gg, pondX + fishX, pondY + fishY);

        gg.drawCenteredString(
                this.font,
                "Caught fish: " + caughtFish + " / " + REQUIRED_FISH,
                this.width / 2,
                y + 220,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Paper scoop durability: " + (MAX_ATTEMPTS - attempts) + " / " + MAX_ATTEMPTS,
                this.width / 2,
                y + 238,
                attempts >= 4 ? 0xFFFFAA00 : 0xAAAAAA
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 262, messageColor);

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 34;

        int color = isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(exitX, exitY, exitX + exitW, exitY + exitH, color);
        gg.drawCenteredString(this.font, "Exit", exitX + exitW / 2, exitY + 7, 0xFFFFFF);

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawFish(GuiGraphics gg, int x, int y) {
        gg.fill(x, y + 5, x + 26, y + 17, 0xFFFFAA33);
        gg.fill(x + 20, y + 2, x + 32, y + 20, 0xFFFF7733);
        gg.fill(x + 5, y + 8, x + 8, y + 11, 0xFF000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 460;
        int boxH = 310;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        int pondX = x + 55;
        int pondY = y + 70;

        int fishRealX = pondX + fishX;
        int fishRealY = pondY + fishY;

        int exitW = 120;
        int exitH = 22;
        int exitX = this.width / 2 - exitW / 2;
        int exitY = y + boxH - 34;

        if (isMouseOver(mouseX, mouseY, exitX, exitY, exitW, exitH)) {
            playClickSound();
            onClose();
            return true;
        }

        if (isMouseOver(mouseX, mouseY, fishRealX, fishRealY, 32, 22)) {
            attempts++;
            caughtFish++;
            message = "Nice scoop! Be gentle with the paper net.";
            messageColor = 0xFF55FF55;
            playSuccessSound();

            if (caughtFish >= REQUIRED_FISH) {
                ModNetwork.CHANNEL.send(
                        new CompleteChapter4GoldfishC2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );
                onClose();
                return true;
            }

            moveFish();
            return true;
        }

        attempts++;
        message = "You missed. The paper scoop is getting weaker.";
        messageColor = 0xFFFF5555;
        playFailSound();

        if (attempts >= MAX_ATTEMPTS) {
            caughtFish = 0;
            attempts = 0;
            message = "The scoop broke. Try again from the beginning.";
            messageColor = 0xFFFFAA00;
        }

        moveFish();
        return true;
    }

    private void moveFish() {
        int boxW = 460;
        int pondW = boxW - 110;
        int pondH = 135;

        fishX = 20 + (int) (Math.random() * (pondW - 70));
        fishY = 20 + (int) (Math.random() * (pondH - 50));
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