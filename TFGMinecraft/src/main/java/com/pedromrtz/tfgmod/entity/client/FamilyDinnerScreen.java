package com.pedromrtz.tfgmod.entity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class FamilyDinnerScreen extends Screen {

    private int step = 0;

    private final List<DialogueLine> dialogue = List.of(
            new DialogueLine("Mother", "Thank you for helping prepare dinner.", 0xFFFF99CC),
            new DialogueLine("Father", "Toshikoshi Soba symbolises leaving the past year behind.", 0xFF99CCFF),
            new DialogueLine("Sister", "It's delicious! I'm happy we're having dinner together.", 0xFFFFFF99),
            new DialogueLine("Mother", "During Omisoka, families share this moment before the new year.", 0xFFFF99CC),
            new DialogueLine("Father", "After dinner, many people go to the temple.", 0xFF99CCFF),
            new DialogueLine("Mother", "Now we should go to the temple outside the village.", 0xFFFF99CC)
    );

    private final List<OptionArea> options = new ArrayList<>();

    private record DialogueLine(String speaker, String text, int color) {}
    private record OptionArea(int x, int y, int w, int h, String action) {}

    public FamilyDinnerScreen() {
        super(Component.literal("Family Dinner"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 430;
        int boxH = 250;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font,
                "Omisoka Family Dinner",
                this.width / 2,
                y + 14,
                0xFFFFFF);

        int startY = y + 42;
        int lineHeight = 18;

        for (int i = 0; i <= step && i < dialogue.size(); i++) {
            DialogueLine line = dialogue.get(i);

            int lineY = startY + i * lineHeight;

            gg.drawString(this.font,
                    line.speaker() + ":",
                    x + 20,
                    lineY,
                    line.color());

            gg.drawString(this.font,
                    line.text(),
                    x + 85,
                    lineY,
                    0xEEEEEE);
        }

        options.clear();

        int btnW = boxW - 40;
        int btnH = 22;
        int btnX = x + 20;
        int btnY = y + boxH - 42;

        int bgColor = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, bgColor);

        String buttonText = step < dialogue.size() - 1
                ? "Continue"
                : "Go to the temple";

        gg.drawCenteredString(this.font,
                buttonText,
                btnX + btnW / 2,
                btnY + 7,
                0xFFFFFF);

        options.add(new OptionArea(btnX, btnY, btnW, btnH, "next"));

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : options) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();

                if (step < dialogue.size() - 1) {
                    step++;
                } else {
                    onClose();
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f, 1f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}