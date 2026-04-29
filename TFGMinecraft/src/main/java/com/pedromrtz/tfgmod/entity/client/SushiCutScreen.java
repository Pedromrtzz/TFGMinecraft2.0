package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class SushiCutScreen extends Screen {

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame_bg.png");

    private static final ResourceLocation MAKI =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushicut/maki_entero.png");

    private static final int MAKI_W = 64;
    private static final int MAKI_H = 64;

    private static final int BUTTON_W = 80;
    private static final int BUTTON_H = 18;

    private int matX, matY;
    private int makiX, makiY;
    private int buttonX, buttonY;

    private boolean hasCut = false;
    private int cutX;

    private Component resultMessage = Component.empty();
    private int resultColor = 0xFFFFFF;

    public SushiCutScreen() {
        super(Component.literal("Corte de sushi"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        matX = centerX - 128;
        matY = centerY - 128;

        makiX = matX + (256 - MAKI_W) / 2;
        makiY = matY + (256 - MAKI_H) / 2;

        buttonX = matX + 256 - BUTTON_W - 8;
        buttonY = matY + 8;
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        RenderSystem.enableBlend();
        gg.blit(BG, matX, matY, 0, 0, 256, 256, 256, 256);

        gg.blit(MAKI, makiX, makiY, 0, 0, MAKI_W, MAKI_H, MAKI_W, MAKI_H);

        if (!hasCut && isInside(mouseX, mouseY, makiX, makiY, MAKI_W, MAKI_H)) {
            drawCutLine(gg, mouseX, 0x66FFFFFF);
        }

        if (hasCut) {
            drawCutLine(gg, cutX, 0xFFFF5555);
        }

        RenderSystem.disableBlend();

        gg.fill(buttonX, buttonY, buttonX + BUTTON_W, buttonY + BUTTON_H, 0xFF444444);
        String label = hasCut ? "REINTENTAR" : "SALIR";
        gg.drawCenteredString(this.font, label,
                buttonX + BUTTON_W / 2, buttonY + 4, 0xFFFFFF);

        gg.drawCenteredString(this.font, "CORTE DE SUSHI",
                this.width / 2, matY - 16, 0xFFFFFF);

        Component text;
        if (hasCut && !resultMessage.getString().isEmpty()) {
            text = resultMessage;
        } else {
            text = Component.literal("Haz clic sobre el maki para cortarlo por la mitad.");
        }
        gg.drawCenteredString(this.font, text,
                this.width / 2, matY + 256 + 8, resultColor);

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawCutLine(GuiGraphics gg, int x, int color) {
        int top = makiY - 6;
        int bottom = makiY + MAKI_H + 6;
        gg.fill(x - 1, top, x + 1, bottom, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        if (isInside(mouseX, mouseY, buttonX, buttonY, BUTTON_W, BUTTON_H)) {
            if (hasCut) {
                hasCut = false;
                resultMessage = Component.empty();
                resultColor = 0xFFFFFF;
                playClick();
            } else {
                onClose();
            }
            return true;
        }

        if (!hasCut && isInside(mouseX, mouseY, makiX, makiY, MAKI_W, MAKI_H)) {
            hasCut = true;
            cutX = (int) mouseX;

            int idealX = makiX + MAKI_W / 2;
            int dist = Math.abs(cutX - idealX);

            if (dist <= 2) {
                resultMessage = Component.literal("¡Corte PERFECTO! \uD83C\uDF63");
                resultColor = 0xFF55FF55;
                playSuccess();
            } else if (dist <= 5) {
                resultMessage = Component.literal("¡Muy bien! Casi perfecto.");
                resultColor = 0xFFFFFF55;
                playClick();
            } else {
                resultMessage = Component.literal("Corte torcido... inténtalo otra vez.");
                resultColor = 0xFFFF5555;
                playFail();
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void playClick() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private void playSuccess() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7f, 1.4f);
        }
    }

    private void playFail() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.7f, 1.0f);
        }
    }

    private boolean isInside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}