package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter4C2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class FloatingLanternScreen extends Screen {

    private int step = 0;
    private int ticks = 0;
    private boolean completed = false;

    private final List<String> lines = List.of(
            "The lantern touches the water softly.",
            "Its warm light begins to move with the river.",
            "Around you, the Matsuri feels peaceful and alive.",
            "Lanterns can represent hope, memory and gratitude.",
            "Tonight, Sakura Town is ready to celebrate."
    );

    private final List<OptionArea> options = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, String action) {}

    public FloatingLanternScreen() {
        super(Component.literal("Floating Lanterns"));
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 315;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Final Activity: Floating Lanterns",
                this.width / 2,
                y + 14,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Release the lantern and complete the Matsuri preparation.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        drawRiverScene(gg, x, y);

        int textY = y + 210;
        for (int i = 0; i <= step && i < lines.size(); i++) {
            gg.drawCenteredString(
                    this.font,
                    lines.get(i),
                    this.width / 2,
                    textY + i * 14,
                    0xEEEEEE
            );
        }

        options.clear();

        int btnW = boxW - 60;
        int btnH = 22;
        int btnX = x + 30;
        int btnY = y + boxH - 38;

        int color = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, color);

        String buttonText = step < lines.size() - 1
                ? "Continue"
                : "Complete Matsuri chapter";

        gg.drawCenteredString(this.font, buttonText, btnX + btnW / 2, btnY + 7, 0xFFFFFF);

        options.add(new OptionArea(btnX, btnY, btnW, btnH, "next"));

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawRiverScene(GuiGraphics gg, int x, int y) {
        int riverX = x + 55;
        int riverY = y + 70;
        int riverW = 390;
        int riverH = 115;

        gg.fill(riverX, riverY, riverX + riverW, riverY + riverH, 0xFF123747);
        gg.fill(riverX + 4, riverY + 4, riverX + riverW - 4, riverY + riverH - 4, 0xFF1D6B82);

        int lanternX = riverX + 80 + Math.min(ticks, 120);
        int lanternY = riverY + 45 + (int) (Math.sin(ticks / 10.0) * 4);

        gg.fill(lanternX - 12, lanternY - 10, lanternX + 28, lanternY + 30, 0x44FFCC66);

        gg.fill(lanternX, lanternY, lanternX + 18, lanternY + 22, 0xFFFFAA55);
        gg.fill(lanternX + 3, lanternY + 4, lanternX + 15, lanternY + 18, 0xFFFFFF99);
        gg.fill(lanternX + 6, lanternY + 8, lanternX + 12, lanternY + 14, 0xFFFFDD55);

        for (int i = 0; i < 4; i++) {
            int waveY = riverY + 20 + i * 22;
            gg.fill(riverX + 20, waveY, riverX + riverW - 20, waveY + 1, 0x6655CCFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : options) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();

                if (step < lines.size() - 1) {
                    step++;
                } else {
                    completeChapter();
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void completeChapter() {
        if (completed) return;
        completed = true;

        ModNetwork.CHANNEL.send(
                new CompleteChapter4C2SPacket(),
                PacketDistributor.SERVER.noArg()
        );

        onClose();
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