package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class OnigiriGameScreen extends Screen {

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame_bg.png");

    private static final ResourceLocation RICE_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/rice.png");
    private static final ResourceLocation SALT_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/salt.png");
    private static final ResourceLocation FILLING_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/filling.png");

    private static final ResourceLocation BOWL_EMPTY =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/bowl_empty.png");
    private static final ResourceLocation BOWL_RICE =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/bowl_rice.png");
    private static final ResourceLocation BOWL_RICE_SALT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/bowl_rice_salt.png");
    private static final ResourceLocation BOWL_RICE_SALT_FILLING =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/onigiri/bowl_full.png");

    private static final int ICON_SIZE = 32;
    private static final int BOWL_SIZE = 96;

    private int riceAmount = 0;
    private boolean saltAdded = false;
    private boolean fillingAdded = false;

    private boolean isForming = false;
    private int formingProgress = 0;    // 0-100

    private String resultMessage = "";

    public OnigiriGameScreen() {
        super(Component.literal("Onigiri Maker"));
    }

    @Override
    public void tick() {
        super.tick();
        if (isForming) {
            formingProgress += 3; // velocidad de llenado de la barra
            if (formingProgress >= 100) {
                formingProgress = 100;
                isForming = false;
                evaluarOnigiri();
            }
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int matX = centerX - 128;
        int matY = centerY - 128;
        RenderSystem.enableBlend();
        gg.blit(BG, matX, matY, 0, 0, 256, 256, 256, 256);
        RenderSystem.disableBlend();

        int iconsX = matX - 40;
        int riceY = matY + 40;
        int saltY = riceY + 40;
        int fillingY = saltY + 40;

        drawIcon(gg, RICE_ICON, iconsX, riceY);
        drawIcon(gg, SALT_ICON, iconsX, saltY);
        drawIcon(gg, FILLING_ICON, iconsX, fillingY);

        int bowlX = centerX - BOWL_SIZE / 2;
        int bowlY = centerY - BOWL_SIZE / 2;

        ResourceLocation bowlTex = BOWL_EMPTY;
        if (riceAmount > 0 && !saltAdded && !fillingAdded) {
            bowlTex = BOWL_RICE;
        } else if (riceAmount > 0 && saltAdded && !fillingAdded) {
            bowlTex = BOWL_RICE_SALT;
        } else if (riceAmount > 0 && saltAdded && fillingAdded) {
            bowlTex = BOWL_RICE_SALT_FILLING;
        }

        drawBowl(gg, bowlTex, bowlX, bowlY);

        int buttonWidth = 140;
        int buttonHeight = 20;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = matY + 256 - 40;

        gg.fill(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight,
                0xFF444444);
        gg.drawCenteredString(this.font, "Formar onigiri",
                buttonX + buttonWidth / 2, buttonY + 6, 0xFFFFFF);

        if (isForming) {
            int barWidth = 120;
            int barHeight = 8;
            int barX = centerX - barWidth / 2;
            int barY = buttonY - 16;

            gg.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF222222);
            int filled = (barWidth * formingProgress) / 100;
            gg.fill(barX, barY, barX + filled, barY + barHeight, 0xFF88C57F);
        }

        gg.drawCenteredString(this.font, "ONIGIRI MAKER", this.width / 2, matY - 10, 0xFFFFFF);

        if (!resultMessage.isEmpty() && !isForming) {
            gg.drawCenteredString(this.font, resultMessage,
                    this.width / 2, buttonY + buttonHeight + 6, 0xFFFFFF);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int matX = centerX - 128;
        int matY = centerY - 128;

        int iconsX = matX - 40;
        int riceY = matY + 40;
        int saltY = riceY + 40;
        int fillingY = saltY + 40;

        int buttonWidth = 140;
        int buttonHeight = 20;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = matY + 256 - 40;

        if (isForming) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        if (inside(mouseX, mouseY, iconsX, riceY, ICON_SIZE, ICON_SIZE)) {
            riceAmount = Math.min(riceAmount + 1, 4); // limite 4
            resultMessage = "";
            return true;
        }

        if (inside(mouseX, mouseY, iconsX, saltY, ICON_SIZE, ICON_SIZE)) {
            saltAdded = true;
            resultMessage = "";
            return true;
        }

        if (inside(mouseX, mouseY, iconsX, fillingY, ICON_SIZE, ICON_SIZE)) {
            fillingAdded = true;
            resultMessage = "";
            return true;
        }

        if (inside(mouseX, mouseY, buttonX, buttonY, buttonWidth, buttonHeight)) {
            if (riceAmount > 0) {
                isForming = true;
                formingProgress = 0;
                resultMessage = "";
            } else {
                resultMessage = "Todavía no has puesto arroz en el bol.";
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void evaluarOnigiri() {
        if (riceAmount < 2) {
            resultMessage = "Hay muy poco arroz, el onigiri se deshace.";
        } else if (riceAmount > 3) {
            resultMessage = "Has puesto demasiado arroz, es difícil de comer.";
        } else if (!saltAdded && !fillingAdded) {
            resultMessage = "Falta la sal y el relleno. ¡Sabe a arroz solo!";
        } else if (!saltAdded) {
            resultMessage = "Falta un toque de sal para realzar el sabor.";
        } else if (!fillingAdded) {
            resultMessage = "Te falta el relleno dentro del onigiri.";
        } else {
            resultMessage = "¡Onigiri perfecto! 🍙";
            // Aquí más adelante: dar recompensa (item/cromo)
        }

        riceAmount = 0;
        saltAdded = false;
        fillingAdded = false;
    }

    private void drawIcon(GuiGraphics gg, ResourceLocation tex, int x, int y) {
        RenderSystem.enableBlend();
        gg.blit(tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        RenderSystem.disableBlend();
    }

    private void drawBowl(GuiGraphics gg, ResourceLocation tex, int x, int y) {
        RenderSystem.enableBlend();
        gg.blit(tex, x, y, 0, 0, BOWL_SIZE, BOWL_SIZE, BOWL_SIZE, BOWL_SIZE);
        RenderSystem.disableBlend();
    }

    private boolean inside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}