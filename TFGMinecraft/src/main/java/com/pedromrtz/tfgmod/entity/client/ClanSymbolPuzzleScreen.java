package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter5PuzzleC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

public class ClanSymbolPuzzleScreen extends Screen {

    private static final ResourceLocation TOKUGAWA =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/clan_symbols/tokugawa.png");

    private static final ResourceLocation TAKEDA =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/clan_symbols/takeda.png");

    private static final ResourceLocation MINAMOTO =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/clan_symbols/minamoto.png");

    private String feedback = "Choose the Tokugawa mon.";
    private int feedbackColor = 0xEEEEEE;

    private boolean solved = false;

    public ClanSymbolPuzzleScreen() {
        super(Component.literal("Clan Symbol Puzzle"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 300;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Clan Symbol Puzzle",
                this.width / 2,
                y + 16,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "A mon represented a clan's identity, honour and loyalty.",
                this.width / 2,
                y + 38,
                0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                "The gatekeeper said this castle serves the Tokugawa clan.",
                this.width / 2,
                y + 55,
                0xFFFFAA00
        );

        drawSymbolOption(gg, mouseX, mouseY, x + 55, y + 95, 0, TOKUGAWA, "Tokugawa");
        drawSymbolOption(gg, mouseX, mouseY, x + 205, y + 95, 1, TAKEDA, "Takeda");
        drawSymbolOption(gg, mouseX, mouseY, x + 355, y + 95, 2, MINAMOTO, "Minamoto");

        gg.drawCenteredString(
                this.font,
                feedback,
                this.width / 2,
                y + 235,
                feedbackColor
        );

        if (solved) {
            gg.drawCenteredString(
                    this.font,
                    "The inner castle gate opens.",
                    this.width / 2,
                    y + 255,
                    0xFF55FF55
            );
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawSymbolOption(
            GuiGraphics gg,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int index,
            ResourceLocation texture,
            String name
    ) {
        int size = 64;

        int bg = isMouseOver(mouseX, mouseY, x, y, size, size)
                ? 0xFF555555
                : 0xFF222222;

        gg.fill(x - 6, y - 6, x + size + 6, y + size + 24, bg);

        gg.blit(texture, x, y, 0, 0, size, size, size, size);

        gg.drawCenteredString(
                this.font,
                name,
                x + size / 2,
                y + size + 8,
                0xFFFFFF
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (solved) return true;

        int boxW = 500;
        int boxH = 300;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        if (clicked(mouseX, mouseY, x + 55, y + 95)) {
            solved = true;
            feedback = "Correct. This is the Tokugawa mon.";
            feedbackColor = 0xFF55FF55;
            playSuccess();

            ModNetwork.CHANNEL.send(
                    new CompleteChapter5PuzzleC2SPacket(),
                    PacketDistributor.SERVER.noArg()
            );

            onClose();
            return true;
        }

        if (clicked(mouseX, mouseY, x + 205, y + 95)) {
            feedback = "Wrong. That symbol belongs to another clan.";
            feedbackColor = 0xFFFF5555;
            playFail();
            return true;
        }

        if (clicked(mouseX, mouseY, x + 355, y + 95)) {
            feedback = "Wrong. Look carefully: the castle serves Tokugawa.";
            feedbackColor = 0xFFFFAA00;
            playFail();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean clicked(double mx, double my, int x, int y) {
        return isMouseOver(mx, my, x, y, 64, 64);
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

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}