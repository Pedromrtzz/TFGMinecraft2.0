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

    private String message = "Corta el salmón cuando el indicador esté en la zona verde.";
    private int messageColor = 0xEEEEEE;

    private static final int REQUIRED_CUTS = 5;

    public SalmonCuttingGameScreen() {
        super(Component.literal("Cortar salmón"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 460;
        int boxH = 280;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Minijuego: Corte del salmón",
                this.width / 2,
                y + 14,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "El corte debe ser limpio y preciso, como haría un itamae.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        gg.renderItem(new ItemStack(Items.SALMON), this.width / 2 - 8, y + 58);

        int barX = x + 55;
        int barY = y + 105;
        int barW = boxW - 110;
        int barH = 18;

        gg.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);

        int targetStart = 45;
        int targetEnd = 60;

        int targetX1 = barX + (barW * targetStart / 100);
        int targetX2 = barX + (barW * targetEnd / 100);

        gg.fill(targetX1, barY, targetX2, barY + barH, 0xFF2ECC71);

        int markerValue = getMarkerValue();
        int markerX = barX + (barW * markerValue / 100);

        gg.fill(markerX - 2, barY - 5, markerX + 2, barY + barH + 5, 0xFFFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Cortes correctos: " + successfulCuts + " / " + REQUIRED_CUTS,
                this.width / 2,
                y + 138,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                message,
                this.width / 2,
                y + 165,
                messageColor
        );

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 45;

        int cutBtnX = this.width / 2 - btnW - 10;
        int exitBtnX = this.width / 2 + 10;

        drawButton(gg, mouseX, mouseY, cutBtnX, btnY, btnW, btnH, "Cortar");
        drawButton(gg, mouseX, mouseY, exitBtnX, btnY, btnW, btnH, "Salir");

        super.render(gg, mouseX, mouseY, pt);
    }

    private int getMarkerValue() {
        long time = System.currentTimeMillis() % 2000L;
        float progress = time / 2000.0f;

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
        int boxW = 460;
        int boxH = 280;
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

        if (marker >= 45 && marker <= 60) {
            successfulCuts++;

            message = "¡Corte limpio! Sigue así.";
            messageColor = 0xFF55FF55;

            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
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
                message = "Pista: espera a que la línea blanca entre en la zona verde.";
                messageColor = 0xFFFFAA00;
            } else {
                message = "El corte no fue preciso. Inténtalo de nuevo.";
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