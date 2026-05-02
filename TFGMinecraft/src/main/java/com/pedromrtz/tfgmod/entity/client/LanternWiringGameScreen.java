package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter4LanternsC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class LanternWiringGameScreen extends Screen {

    private final List<String> connectedOrder = new ArrayList<>();
    private final List<String> correctOrder = List.of("red", "blue", "yellow", "green");

    private String message = "Connect the wires to restore the festival lanterns.";
    private int messageColor = 0xEEEEEE;
    private int mistakes = 0;

    private record ButtonArea(int x, int y, int w, int h, String id, String label, int color) {}

    private final List<ButtonArea> wireButtons = new ArrayList<>();
    private final List<ButtonArea> actionButtons = new ArrayList<>();

    public LanternWiringGameScreen() {
        super(Component.literal("Lantern Wiring"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 330;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Festival Lantern Wiring", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Connect the wires in the correct order to light the Matsuri lanterns.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        gg.drawCenteredString(
                this.font,
                "Hint: warm light begins with red and ends with green.",
                this.width / 2,
                y + 52,
                0xFFFFAA00
        );

        drawLanterns(gg, x, y);

        wireButtons.clear();

        int btnW = 100;
        int btnH = 28;
        int gap = 16;
        int btnY = y + 150;
        int startX = x + 34;

        addWireButton(startX, btnY, btnW, btnH, "red", "Red Wire", 0xFFFF5555);
        addWireButton(startX + (btnW + gap), btnY, btnW, btnH, "blue", "Blue Wire", 0xFF55AAFF);
        addWireButton(startX + (btnW + gap) * 2, btnY, btnW, btnH, "yellow", "Yellow Wire", 0xFFFFFF55);
        addWireButton(startX + (btnW + gap) * 3, btnY, btnW, btnH, "green", "Green Wire", 0xFF55FF55);

        for (ButtonArea btn : wireButtons) {
            int bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.fill(btn.x + 6, btn.y + 8, btn.x + 20, btn.y + 20, btn.color);
            gg.drawString(this.font, btn.label, btn.x + 26, btn.y + 10, 0xFFFFFF);
        }

        drawConnectionPanel(gg, x, y);

        gg.drawCenteredString(this.font, message, this.width / 2, y + 252, messageColor);

        actionButtons.clear();

        int actionY = y + boxH - 38;

        addActionButton(x + 40, actionY, 120, 22, "clear", "Clear");
        addActionButton(x + (boxW - 120) / 2, actionY, 120, 22, "confirm", "Confirm");
        addActionButton(x + boxW - 160, actionY, 120, 22, "exit", "Exit");

        for (ButtonArea btn : actionButtons) {
            int bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 7, 0xFFFFFF);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawLanterns(GuiGraphics gg, int x, int y) {
        int lanternY = y + 82;
        int startX = x + 95;
        int gap = 75;

        for (int i = 0; i < 4; i++) {
            int lx = startX + i * gap;

            boolean lit = i < connectedOrder.size();

            int lanternColor = lit ? 0xFFFFAA33 : 0xFF444444;
            int glowColor = lit ? 0x55FFAA33 : 0x00000000;

            if (lit) {
                gg.fill(lx - 8, lanternY - 8, lx + 34, lanternY + 42, glowColor);
            }

            gg.fill(lx, lanternY, lx + 26, lanternY + 34, lanternColor);
            gg.fill(lx + 4, lanternY + 4, lx + 22, lanternY + 30, lit ? 0xFFFFFF99 : 0xFF222222);
            gg.drawCenteredString(this.font, String.valueOf(i + 1), lx + 13, lanternY + 11, lit ? 0xFF000000 : 0xFFFFFFFF);
        }
    }

    private void drawConnectionPanel(GuiGraphics gg, int x, int y) {
        int panelX = x + 55;
        int panelY = y + 195;
        int panelW = 390;
        int panelH = 42;

        gg.drawString(this.font, "Connected order:", panelX, panelY - 14, 0xFFFFFF);
        gg.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xAA111111);

        int slotSize = 30;
        int gap = 18;
        int startX = panelX + 70;

        for (int i = 0; i < 4; i++) {
            int sx = startX + i * (slotSize + gap);

            gg.fill(sx, panelY + 6, sx + slotSize, panelY + 6 + slotSize, 0xFF222222);
            gg.drawCenteredString(this.font, String.valueOf(i + 1), sx + slotSize / 2, panelY - 8, 0xAAAAAA);

            if (i < connectedOrder.size()) {
                int color = getWireColor(connectedOrder.get(i));
                gg.fill(sx + 6, panelY + 12, sx + slotSize - 6, panelY + slotSize, color);
            }
        }
    }

    private void addWireButton(int x, int y, int w, int h, String id, String label, int color) {
        wireButtons.add(new ButtonArea(x, y, w, h, id, label, color));
    }

    private void addActionButton(int x, int y, int w, int h, String id, String label) {
        actionButtons.add(new ButtonArea(x, y, w, h, id, label, 0xFFFFFFFF));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonArea b : wireButtons) {
            if (isMouseOver(mouseX, mouseY, b.x, b.y, b.w, b.h)) {
                handleWireClick(b.id);
                return true;
            }
        }

        for (ButtonArea b : actionButtons) {
            if (isMouseOver(mouseX, mouseY, b.x, b.y, b.w, b.h)) {
                handleActionClick(b.id);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleWireClick(String id) {
        playClickSound();

        if (connectedOrder.size() >= 4) {
            message = "All four wires are already connected.";
            messageColor = 0xFFFFAA00;
            return;
        }

        if (connectedOrder.contains(id)) {
            message = "This wire is already connected.";
            messageColor = 0xFFFFAA00;
            return;
        }

        connectedOrder.add(id);
        message = "Connected: " + formatWireName(id);
        messageColor = 0xEEEEEE;
    }

    private void handleActionClick(String id) {
        playClickSound();

        switch (id) {
            case "clear" -> {
                connectedOrder.clear();
                message = "Wiring reset. Try again.";
                messageColor = 0xFFCCCCCC;
            }

            case "confirm" -> {
                if (connectedOrder.size() < 4) {
                    message = "You must connect all four wires.";
                    messageColor = 0xFFFF5555;
                    return;
                }

                if (connectedOrder.equals(correctOrder)) {
                    playSuccessSound();

                    ModNetwork.CHANNEL.send(
                            new CompleteChapter4LanternsC2SPacket(),
                            PacketDistributor.SERVER.noArg()
                    );

                    onClose();
                } else {
                    mistakes++;
                    connectedOrder.clear();

                    if (mistakes % 2 == 0) {
                        message = "Hint: red comes first, and green completes the circuit.";
                        messageColor = 0xFFFFAA00;
                    } else {
                        message = "The circuit failed. Check the wire order and try again.";
                        messageColor = 0xFFFF5555;
                    }

                    playFailSound();
                }
            }

            case "exit" -> onClose();
        }
    }

    private String formatWireName(String id) {
        return switch (id) {
            case "red" -> "Red Wire";
            case "blue" -> "Blue Wire";
            case "yellow" -> "Yellow Wire";
            case "green" -> "Green Wire";
            default -> id;
        };
    }

    private int getWireColor(String id) {
        return switch (id) {
            case "red" -> 0xFFFF5555;
            case "blue" -> 0xFF55AAFF;
            case "yellow" -> 0xFFFFFF55;
            case "green" -> 0xFF55FF55;
            default -> 0xFFFFFFFF;
        };
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private void playSuccessSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.4f);
            player.playSound(SoundEvents.REDSTONE_TORCH_BURNOUT, 0.5f, 1.5f);
        }
    }

    private void playFailSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.8f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}