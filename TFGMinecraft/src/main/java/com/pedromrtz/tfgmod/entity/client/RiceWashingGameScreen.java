package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter3RiceWashC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

public class RiceWashingGameScreen extends Screen {

    private int washProgress = 0;
    private int mistakes = 0;
    private String message = "Lava el arroz hasta que el agua quede clara.";
    private int messageColor = 0xEEEEEE;

    public RiceWashingGameScreen() {
        super(Component.literal("Lavar arroz"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 420;
        int boxH = 260;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minijuego: Lavar arroz", this.width / 2, y + 15, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "En la cocina japonesa, lavar el arroz es importante",
                this.width / 2,
                y + 40,
                0xDDDDDD
        );

        gg.drawCenteredString(
                this.font,
                "para eliminar el exceso de almidón antes de cocinarlo.",
                this.width / 2,
                y + 55,
                0xDDDDDD
        );

        int bowlX = this.width / 2 - 70;
        int bowlY = y + 85;
        int bowlW = 140;
        int bowlH = 70;

        gg.fill(bowlX, bowlY, bowlX + bowlW, bowlY + bowlH, 0xFF3A3A3A);

        int waterColor = getWaterColor();
        gg.fill(bowlX + 8, bowlY + 8, bowlX + bowlW - 8, bowlY + bowlH - 8, waterColor);

        gg.drawCenteredString(
                this.font,
                "Claridad del agua: " + washProgress + "%",
                this.width / 2,
                bowlY + bowlH + 12,
                0xFFFFFF
        );

        int barX = x + 50;
        int barY = bowlY + bowlH + 30;
        int barW = boxW - 100;
        int barH = 12;

        gg.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);
        gg.fill(barX, barY, barX + (barW * washProgress / 100), barY + barH, 0xFF66CCFF);

        gg.drawCenteredString(this.font, message, this.width / 2, y + 190, messageColor);

        int btnW = 150;
        int btnH = 24;

        int washBtnX = this.width / 2 - btnW - 10;
        int btnY = y + boxH - 42;

        int clearBtnX = this.width / 2 + 10;

        drawButton(gg, mouseX, mouseY, washBtnX, btnY, btnW, btnH, "Lavar arroz");
        drawButton(gg, mouseX, mouseY, clearBtnX, btnY, btnW, btnH, washProgress >= 100 ? "Terminar" : "Revisar");

        super.render(gg, mouseX, mouseY, pt);
    }

    private int getWaterColor() {
        if (washProgress < 25) return 0xFFB8B8A0;
        if (washProgress < 50) return 0xFFAAD0D6;
        if (washProgress < 75) return 0xFF88D8EE;
        return 0xFF55CCFF;
    }

    private void drawButton(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int w, int h, String text) {
        int color = isMouseOver(mouseX, mouseY, x, y, w, h)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(x, y, x + w, y + h, color);
        gg.drawCenteredString(this.font, text, x + w / 2, y + 8, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxW = 420;
        int boxH = 260;
        int y = (this.height - boxH) / 2;

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 42;

        int washBtnX = this.width / 2 - btnW - 10;
        int checkBtnX = this.width / 2 + 10;

        if (isMouseOver(mouseX, mouseY, washBtnX, btnY, btnW, btnH)) {
            playClickSound();

            if (washProgress < 100) {
                washProgress += 20;
                if (washProgress > 100) washProgress = 100;

                message = "Sigues lavando el arroz con cuidado...";
                messageColor = 0xEEEEEE;
            } else {
                message = "El arroz ya está limpio.";
                messageColor = 0xFF55FF55;
            }

            return true;
        }

        if (isMouseOver(mouseX, mouseY, checkBtnX, btnY, btnW, btnH)) {
            playClickSound();

            if (washProgress >= 100) {
                ModNetwork.CHANNEL.send(
                        new CompleteChapter3RiceWashC2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );

                onClose();
            } else {
                mistakes++;

                if (mistakes % 2 == 0) {
                    message = "Pista: sigue lavando hasta que el agua sea clara.";
                    messageColor = 0xFFFFAA00;
                } else {
                    message = "Todavía está turbia. El arroz necesita más lavado.";
                    messageColor = 0xFFFF5555;
                }
            }

            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
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