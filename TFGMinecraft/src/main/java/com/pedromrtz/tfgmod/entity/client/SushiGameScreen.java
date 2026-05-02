package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class SushiGameScreen extends Screen {

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame_bg.png");

    private static final ResourceLocation ARROZ_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/arroz.png");
    private static final ResourceLocation ALGA_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/alga.png");
    private static final ResourceLocation SALMON_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/salmon.png");

    private static final int ICON_SIZE = 32;

    private static final int ARROZ_X = 20;
    private static final int ARROZ_Y = 40;

    private static final int ALGA_X  = 20;
    private static final int ALGA_Y  = 90;

    private static final int SALMON_X = 20;
    private static final int SALMON_Y = 140;

    private static final int CLEAR_W = 60;
    private static final int CLEAR_H = 18;

    private static final List<String> CORRECT_RECIPE = List.of("arroz", "alga", "salmon");

    private final List<String> ingredients = new ArrayList<>();

    private String hudMessage = "";
    private long hudMessageExpire = 0L;

    public SushiGameScreen() {
        super(Component.literal("Sushi Maker"));
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

        if (isInside(mouseX, mouseY, ARROZ_X, ARROZ_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(ARROZ_X - 2, ARROZ_Y - 2, ARROZ_X + ICON_SIZE + 2, ARROZ_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }
        if (isInside(mouseX, mouseY, ALGA_X, ALGA_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(ALGA_X - 2, ALGA_Y - 2, ALGA_X + ICON_SIZE + 2, ALGA_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }
        if (isInside(mouseX, mouseY, SALMON_X, SALMON_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(SALMON_X - 2, SALMON_Y - 2, SALMON_X + ICON_SIZE + 2, SALMON_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }

        drawIngredientIcon(gg, ARROZ_ICON, ARROZ_X, ARROZ_Y);
        drawIngredientIcon(gg, ALGA_ICON, ALGA_X, ALGA_Y);
        drawIngredientIcon(gg, SALMON_ICON, SALMON_X, SALMON_Y);

        int totalWidth = ingredients.size() * (ICON_SIZE + 8) - 8;
        if (totalWidth < 0) totalWidth = 0;

        int barX = matX + (256 - totalWidth) / 2;
        int barY = matY + 256 / 2 - ICON_SIZE / 2;

        int offset = 0;
        for (String ing : ingredients) {
            ResourceLocation tex = switch (ing) {
                case "arroz"  -> ARROZ_ICON;
                case "alga"   -> ALGA_ICON;
                case "salmon" -> SALMON_ICON;
                default       -> null;
            };
            if (tex != null) {
                drawIngredientIcon(gg, tex, barX + offset, barY);
                offset += ICON_SIZE + 8;
            }
        }

        int clearX = matX + 256 - CLEAR_W - 8;
        int clearY = matY + 8;

        if (isInside(mouseX, mouseY, clearX, clearY, CLEAR_W, CLEAR_H)) {
            gg.fill(clearX, clearY, clearX + CLEAR_W, clearY + CLEAR_H, 0xFF666666);
        } else {
            gg.fill(clearX, clearY, clearX + CLEAR_W, clearY + CLEAR_H, 0xFF444444);
        }

        gg.drawCenteredString(this.font, "CLEAR",
                clearX + CLEAR_W / 2, clearY + 5, 0xFFFFFF);

        gg.drawCenteredString(this.font, "SUSHI MAKER", this.width / 2, 10, 0xFFFFFF);

        String stepText = "Step " + ingredients.size() + " / " + CORRECT_RECIPE.size();

        gg.drawCenteredString(this.font, stepText, this.width / 2, 34, 0xCCCCCC);

        if (!hudMessage.isEmpty() && System.currentTimeMillis() < hudMessageExpire) {
            gg.drawCenteredString(this.font, hudMessage, this.width / 2, 50, 0xFFFFFF);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int matX = centerX - 128;
        int matY = centerY - 128;

        int clearX = matX + 256 - CLEAR_W - 8;
        int clearY = matY + 8;

        if (isInside(mouseX, mouseY, clearX, clearY, CLEAR_W, CLEAR_H)) {
            ingredients.clear();
            playClick();
            showHudMessage("Ingredients cleared");
            return true;
        }

        if (isInside(mouseX, mouseY, ARROZ_X, ARROZ_Y, ICON_SIZE, ICON_SIZE)) {
            if (canAddIngredient()) {
                ingredients.add("arroz");
                playClick();
                checkRecipe();
            }
            return true;
        }

        if (isInside(mouseX, mouseY, ALGA_X, ALGA_Y, ICON_SIZE, ICON_SIZE)) {
            if (canAddIngredient()) {
                ingredients.add("alga");
                playClick();
                checkRecipe();
            }
            return true;
        }

        if (isInside(mouseX, mouseY, SALMON_X, SALMON_Y, ICON_SIZE, ICON_SIZE)) {
            if (canAddIngredient()) {
                ingredients.add("salmon");
                playClick();
                checkRecipe();
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean canAddIngredient() {
        return ingredients.size() < CORRECT_RECIPE.size();
    }

    private void checkRecipe() {
        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return;

        if (ingredients.size() == CORRECT_RECIPE.size()) {
            boolean ok = true;
            for (int i = 0; i < CORRECT_RECIPE.size(); i++) {
                if (!CORRECT_RECIPE.get(i).equals(ingredients.get(i))) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                showHudMessage("Perfect sushi! 🍣");
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else {
                showHudMessage("The recipe is not correct...");
                player.playSound(SoundEvents.VILLAGER_NO, 1.0f, 1.0f);
            }

            ingredients.clear();
        }
    }

    private void showHudMessage(String txt) {
        this.hudMessage = txt;
        this.hudMessageExpire = System.currentTimeMillis() + 2500;
    }

    private void playClick() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        player.playSound(
                SoundEvents.UI_BUTTON_CLICK.value(),
                1.0f,
                1.0f
        );
    }

    private void drawIngredientIcon(GuiGraphics gg, ResourceLocation tex, int x, int y) {
        RenderSystem.enableBlend();
        gg.blit(tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        RenderSystem.disableBlend();
    }

    private boolean isInside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}