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
    private int bubbleTicks = 0;

    private String message = "Lava el arroz hasta que el agua quede clara.";
    private int messageColor = 0xEEEEEE;

    public RiceWashingGameScreen() {
        super(Component.literal("Lavar arroz"));
    }

    @Override
    public void tick() {
        super.tick();

        if (bubbleTicks > 0) {
            bubbleTicks--;
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 440;
        int boxH = 285;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minijuego: Lavar arroz", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "El arroz se lava para quitar el exceso de almidón.",
                this.width / 2,
                y + 38,
                0xDDDDDD
        );

        gg.drawCenteredString(
                this.font,
                "Cuanto más clara esté el agua, mejor será la textura del sushi.",
                this.width / 2,
                y + 54,
                0xAAAAAA
        );

        int bowlX = this.width / 2 - 85;
        int bowlY = y + 88;
        int bowlW = 170;
        int bowlH = 75;

        // Cuenco
        gg.fill(bowlX, bowlY, bowlX + bowlW, bowlY + bowlH, 0xFF2B2B2B);
        gg.fill(bowlX + 5, bowlY + 5, bowlX + bowlW - 5, bowlY + bowlH - 5, 0xFF555555);

        // Agua
        int waterColor = getWaterColor();
        gg.fill(bowlX + 12, bowlY + 12, bowlX + bowlW - 12, bowlY + bowlH - 12, waterColor);

        // Arroz visual
        drawRiceDots(gg, bowlX, bowlY);

        // Burbujas / movimiento visual
        if (bubbleTicks > 0) {
            drawBubbles(gg, bowlX, bowlY);
        }

        gg.drawCenteredString(
                this.font,
                getWaterStateText(),
                this.width / 2,
                bowlY + bowlH + 10,
                getWaterStateColor()
        );

        int barX = x + 50;
        int barY = bowlY + bowlH + 30;
        int barW = boxW - 100;
        int barH = 13;

        gg.fill(barX, barY, barX + barW, barY + barH, 0xFF222222);
        gg.fill(barX, barY, barX + (barW * washProgress / 100), barY + barH, 0xFF66CCFF);

        gg.drawCenteredString(
                this.font,
                "Claridad del agua: " + washProgress + "%",
                this.width / 2,
                barY + 18,
                0xFFFFFF
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 222, messageColor);

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 42;

        int washBtnX = this.width / 2 - btnW - 10;
        int checkBtnX = this.width / 2 + 10;

        drawButton(gg, mouseX, mouseY, washBtnX, btnY, btnW, btnH, "Lavar arroz");
        drawButton(gg, mouseX, mouseY, checkBtnX, btnY, btnW, btnH, washProgress >= 100 ? "Terminar" : "Revisar");

        super.render(gg, mouseX, mouseY, pt);
    }

    private int getWaterColor() {
        if (washProgress < 20) return 0xFFD2C9A5; // muy turbia
        if (washProgress < 40) return 0xFFBFCBB7;
        if (washProgress < 60) return 0xFF9ED0D8;
        if (washProgress < 80) return 0xFF76D8EF;
        return 0xFF45CFFF; // clara
    }

    private String getWaterStateText() {
        if (washProgress < 20) return "Agua muy turbia";
        if (washProgress < 40) return "Agua todavía turbia";
        if (washProgress < 60) return "El agua empieza a aclararse";
        if (washProgress < 80) return "Agua casi limpia";
        if (washProgress < 100) return "Agua muy clara";
        return "El arroz está listo";
    }

    private int getWaterStateColor() {
        if (washProgress < 40) return 0xFFFFAA00;
        if (washProgress < 80) return 0xFF66CCFF;
        return 0xFF55FF55;
    }

    private void drawRiceDots(GuiGraphics gg, int bowlX, int bowlY) {
        int[][] dots = {
                {35, 35}, {50, 48}, {70, 36}, {88, 50},
                {105, 37}, {125, 49}, {140, 35}, {78, 58}
        };

        for (int[] dot : dots) {
            int dx = bowlX + dot[0];
            int dy = bowlY + dot[1];

            gg.fill(dx, dy, dx + 3, dy + 2, 0xFFFFFFFF);
        }
    }

    private void drawBubbles(GuiGraphics gg, int bowlX, int bowlY) {
        int tick = bubbleTicks;

        int[][] bubbles = {
                {35, 20 + tick % 8},
                {65, 25 + tick % 6},
                {98, 18 + tick % 9},
                {130, 24 + tick % 7}
        };

        for (int[] b : bubbles) {
            int bx = bowlX + b[0];
            int by = bowlY + b[1];

            gg.fill(bx, by, bx + 4, by + 4, 0xAAFFFFFF);
            gg.fill(bx + 1, by + 1, bx + 3, by + 3, 0xAA66CCFF);
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
        int boxW = 440;
        int boxH = 285;
        int y = (this.height - boxH) / 2;

        int btnW = 150;
        int btnH = 24;
        int btnY = y + boxH - 42;

        int washBtnX = this.width / 2 - btnW - 10;
        int checkBtnX = this.width / 2 + 10;

        if (isMouseOver(mouseX, mouseY, washBtnX, btnY, btnW, btnH)) {
            handleWashClick();
            return true;
        }

        if (isMouseOver(mouseX, mouseY, checkBtnX, btnY, btnW, btnH)) {
            handleCheckClick();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleWashClick() {
        playWaterSound();

        bubbleTicks = 18;

        if (washProgress < 100) {
            washProgress += 20;
            if (washProgress > 100) washProgress = 100;

            message = switch (washProgress) {
                case 20 -> "El agua sigue turbia. Continúa lavando.";
                case 40 -> "Empiezas a eliminar el almidón.";
                case 60 -> "El agua se está aclarando.";
                case 80 -> "Casi está listo.";
                case 100 -> "El agua está clara. Puedes terminar.";
                default -> "Sigues lavando el arroz con cuidado.";
            };

            messageColor = washProgress >= 100 ? 0xFF55FF55 : 0xEEEEEE;
        } else {
            message = "El arroz ya está limpio.";
            messageColor = 0xFF55FF55;
        }
    }

    private void handleCheckClick() {
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
                message = "Pista: lava hasta que el agua deje de verse turbia.";
                messageColor = 0xFFFFAA00;
            } else {
                message = "Todavía hay demasiado almidón. Sigue lavando.";
                messageColor = 0xFFFF5555;
            }
        }
    }

    private void playWaterSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.WATER_AMBIENT, 0.8f, 1.2f);
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