package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter3SalmonCutC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

public class SalmonCuttingGameScreen extends Screen {

    private int successfulCuts = 0;
    private int mistakes = 0;

    private String message = "Cut the salmon when the indicator is inside the green zone.";
    private int messageColor = 0xEEEEEE;

    private int cutAnimationTicks = 0;

    private static final int REQUIRED_CUTS = 5;

    public SalmonCuttingGameScreen() {
        super(Component.literal("Salmon Cutting"));
    }

    @Override
    public void tick() {
        super.tick();

        if (cutAnimationTicks > 0) {
            cutAnimationTicks--;
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 480;
        int boxH = 300;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Minigame: Salmon Cutting",
                this.width / 2,
                y + 14,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "A good itamae cuts with calm, precision and respect for the fish.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        // Visual icons
        gg.renderItem(new ItemStack(Items.IRON_SWORD), this.width / 2 - 34, y + 62);
        gg.renderItem(new ItemStack(Items.SALMON), this.width / 2 + 18, y + 62);

        gg.drawCenteredString(
                this.font,
                "Knife   +   Salmon",
                this.width / 2,
                y + 84,
                0xAAAAAA
        );

        // Simple clean cut animation
        if (cutAnimationTicks > 0) {
            int animX = this.width / 2 - 65;
            int animY = y + 102;

            gg.drawCenteredString(
                    this.font,
                    "✦ Clean Cut ✦",
                    this.width / 2,
                    animY,
                    0x55FF55
            );

            gg.fill(animX, animY + 14, animX + 130, animY + 16, 0xFF55FF55);
        }

        int barX = x + 55;
        int barY = y + 130;
        int barW = boxW - 110;
        int barH = 18;

        gg.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);

        int targetStart = getTargetStart();
        int targetEnd = getTargetEnd();

        int targetX1 = barX + (barW * targetStart / 100);
        int targetX2 = barX + (barW * targetEnd / 100);

        gg.fill(targetX1, barY, targetX2, barY + barH, 0xFF2ECC71);

        int markerValue = getMarkerValue();
        int markerX = barX + (barW * markerValue / 100);

        gg.fill(markerX - 2, barY - 5, markerX + 2, barY + barH + 5, 0xFFFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Successful cuts: " + successfulCuts + " / " + REQUIRED_CUTS,
                this.width / 2,
                y + 162,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "The green zone becomes smaller after each successful cut.",
                this.width / 2,
                y + 178,
                0xAAAAAA
        );

        gg.drawCenteredString(
                this.font,
                message,
                this.width / 2,
                y + 205,
                messageColor
        );

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 45;

        int cutBtnX = this.width / 2 - btnW - 10;
        int exitBtnX = this.width / 2 + 10;

        drawButton(gg, mouseX, mouseY, cutBtnX, btnY, btnW, btnH, "Cut");
        drawButton(gg, mouseX, mouseY, exitBtnX, btnY, btnW, btnH, "Exit");

        super.render(gg, mouseX, mouseY, pt);
    }

    private int getTargetStart() {
        return switch (successfulCuts) {
            case 0 -> 42;
            case 1 -> 44;
            case 2 -> 46;
            case 3 -> 48;
            default -> 50;
        };
    }

    private int getTargetEnd() {
        return switch (successfulCuts) {
            case 0 -> 62;
            case 1 -> 60;
            case 2 -> 58;
            case 3 -> 56;
            default -> 55;
        };
    }

    private int getMarkerValue() {
        long time = System.currentTimeMillis() % 1800L;
        float progress = time / 1800.0f;

        if (progress <= 0.5f) {
            return (int) (progress * 2.0f * 100.0f);
        } else {
            return (int) ((1.0f - ((progress - 0.5f) * 2.0f)) * 100.0f);
        }
    }

    private void drawButton(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int w, int h, String text) {
        int color = isMouseOver(mouseX, mouseY, x, y, w, h)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(x, y, x + w, y + h, color);
        gg.drawCenteredString(this.font, text, x + w / 2, y + 8, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 480;
        int boxH = 300;
        int y = (this.height - boxH) / 2;

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 45;

        int cutBtnX = this.width / 2 - btnW - 10;
        int exitBtnX = this.width / 2 + 10;

        if (isMouseOver(mouseX, mouseY, cutBtnX, btnY, btnW, btnH)) {
            handleCut();
            return true;
        }

        if (isMouseOver(mouseX, mouseY, exitBtnX, btnY, btnW, btnH)) {
            playClickSound();
            onClose();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleCut() {
        playClickSound();

        int marker = getMarkerValue();

        if (marker >= getTargetStart() && marker <= getTargetEnd()) {
            successfulCuts++;
            cutAnimationTicks = 18;

            message = "Clean cut! Your precision is improving.";
            messageColor = 0xFF55FF55;

            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.2f);
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8f, 1.4f);
            }

            if (successfulCuts >= REQUIRED_CUTS) {
                ModNetwork.CHANNEL.send(
                        new CompleteChapter3SalmonCutC2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );

                onClose();
            }

        } else {
            mistakes++;

            if (mistakes % 2 == 0) {
                message = "Hint: wait until the white line is fully inside the green zone.";
                messageColor = 0xFFFFAA00;
            } else {
                message = "The cut was not precise. Breathe and wait for the right moment.";
                messageColor = 0xFFFF5555;
            }

            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(SoundEvents.VILLAGER_NO, 0.8f, 1.0f);
            }
        }
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}