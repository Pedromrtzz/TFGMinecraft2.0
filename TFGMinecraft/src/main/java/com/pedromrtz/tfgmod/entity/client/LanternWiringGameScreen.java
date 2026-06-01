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

    private enum Difficulty {
        EASY,
        HARD
    }

    private Difficulty difficulty = null;

    private final List<String> connectedOrder = new ArrayList<>();
    private List<String> correctOrder = new ArrayList<>();

    private String message = "Choose a difficulty to repair the festival lanterns.";
    private int messageColor = 0xEEEEEE;

    private int mistakes = 0;
    private int sparkTicks = 0;
    private int successGlowTicks = 0;

    private record ButtonArea(int x, int y, int w, int h, String id, String label, int color) {}

    private final List<ButtonArea> wireButtons = new ArrayList<>();
    private final List<ButtonArea> actionButtons = new ArrayList<>();

    public LanternWiringGameScreen() {
        super(Component.literal("Lantern Wiring"));
    }

    @Override
    public void tick() {
        super.tick();

        if (sparkTicks > 0) {
            sparkTicks--;
        }

        if (successGlowTicks > 0) {
            successGlowTicks--;
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 540;
        int boxH = 355;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Minigame: Festival Lantern Wiring",
                this.width / 2,
                y + 14,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Connect the wires in the correct order to restore the Matsuri lights.",
                this.width / 2,
                y + 35,
                0xDDDDDD
        );

        if (difficulty == null) {
            drawDifficultySelection(gg, mouseX, mouseY, x, y, boxW, boxH);
        } else {
            drawPuzzle(gg, mouseX, mouseY, x, y, boxW, boxH);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void drawDifficultySelection(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        actionButtons.clear();
        wireButtons.clear();

        gg.drawCenteredString(
                this.font,
                "Easy mode uses 4 wires. Hard mode uses 6 wires.",
                this.width / 2,
                y + 70,
                0xAAAAAA
        );

        int btnW = 190;
        int btnH = 30;
        int btnY = y + 125;

        addActionButton(this.width / 2 - btnW - 15, btnY, btnW, btnH, "easy", "Easy: 4 wires");
        addActionButton(this.width / 2 + 15, btnY, btnW, btnH, "hard", "Hard: 6 wires");

        addActionButton(this.width / 2 - 95, y + boxH - 42, 190, 24, "exit", "Exit");

        for (ButtonArea btn : actionButtons) {
            int bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 10, 0xFFFFFF);
        }

        gg.drawCenteredString(this.font, message, this.width / 2, y + 205, messageColor);
    }

    private void drawPuzzle(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        int totalWires = correctOrder.size();

        gg.drawCenteredString(
                this.font,
                difficulty == Difficulty.EASY
                        ? "Difficulty: Easy"
                        : "Difficulty: Hard",
                this.width / 2,
                y + 55,
                difficulty == Difficulty.EASY ? 0xFF55FF55 : 0xFFFFAA00
        );

        gg.drawCenteredString(
                this.font,
                difficulty == Difficulty.EASY
                        ? "Hint: red begins the circuit, green completes it."
                        : "Hint: start warm, then cold, then finish with festival light.",
                this.width / 2,
                y + 72,
                0xFFFFAA00
        );

        drawLanterns(gg, x, y, totalWires);
        drawSparks(gg, x, y);

        wireButtons.clear();

        int btnW = difficulty == Difficulty.EASY ? 100 : 78;
        int btnH = 28;
        int gap = difficulty == Difficulty.EASY ? 16 : 10;
        int btnY = y + 165;

        int totalButtonsWidth = totalWires * btnW + (totalWires - 1) * gap;
        int startX = x + (boxW - totalButtonsWidth) / 2;

        for (int i = 0; i < totalWires; i++) {
            String id = getWireIdByIndex(i);
            addWireButton(
                    startX + i * (btnW + gap),
                    btnY,
                    btnW,
                    btnH,
                    id,
                    getShortWireLabel(id),
                    getWireColor(id)
            );
        }

        for (ButtonArea btn : wireButtons) {
            boolean alreadyConnected = connectedOrder.contains(btn.id);

            int bg;
            if (alreadyConnected) {
                bg = 0xFF222222;
            } else {
                bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                        ? 0xFF666666 : 0xFF333333;
            }

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.fill(btn.x + 5, btn.y + 8, btn.x + 18, btn.y + 20, btn.color);

            gg.drawCenteredString(
                    this.font,
                    btn.label,
                    btn.x + btn.w / 2 + 8,
                    btn.y + 10,
                    alreadyConnected ? 0xFF777777 : 0xFFFFFF
            );
        }

        drawConnectionPanel(gg, x, y, totalWires);

        gg.drawCenteredString(this.font, message, this.width / 2, y + 275, messageColor);

        actionButtons.clear();

        int actionY = y + boxH - 38;

        addActionButton(x + 45, actionY, 120, 22, "clear", "Clear");
        addActionButton(x + (boxW - 120) / 2, actionY, 120, 22, "confirm", "Confirm");
        addActionButton(x + boxW - 165, actionY, 120, 22, "exit", "Exit");

        for (ButtonArea btn : actionButtons) {
            int bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 7, 0xFFFFFF);
        }
    }

    private void drawLanterns(GuiGraphics gg, int x, int y, int totalLanterns) {
        int lanternY = y + 95;
        int lanternW = 24;
        int gap = totalLanterns == 4 ? 72 : 48;

        int totalWidth = totalLanterns * lanternW + (totalLanterns - 1) * gap;
        int startX = this.width / 2 - totalWidth / 2;

        for (int i = 0; i < totalLanterns; i++) {
            int lx = startX + i * (lanternW + gap);

            boolean lit = i < connectedOrder.size();
            boolean successGlow = successGlowTicks > 0 && lit;

            int lanternColor = lit ? 0xFFFFAA33 : 0xFF444444;
            int insideColor = lit ? 0xFFFFFF99 : 0xFF222222;

            if (lit || successGlow) {
                gg.fill(lx - 10, lanternY - 10, lx + 34, lanternY + 44, 0x44FFAA33);
            }

            gg.fill(lx, lanternY, lx + 24, lanternY + 34, lanternColor);
            gg.fill(lx + 4, lanternY + 4, lx + 20, lanternY + 30, insideColor);
            gg.drawCenteredString(
                    this.font,
                    String.valueOf(i + 1),
                    lx + 12,
                    lanternY + 11,
                    lit ? 0xFF000000 : 0xFFFFFFFF
            );
        }
    }

    private void drawSparks(GuiGraphics gg, int x, int y) {
        if (sparkTicks <= 0) return;

        int sparkX = this.width / 2;
        int sparkY = y + 118;

        gg.drawCenteredString(this.font, "✦  ✕  ✦", sparkX, sparkY - 18, 0xFFFF5555);
        gg.fill(sparkX - 30, sparkY, sparkX - 18, sparkY + 2, 0xFFFF5555);
        gg.fill(sparkX + 18, sparkY, sparkX + 30, sparkY + 2, 0xFFFF5555);
        gg.fill(sparkX - 2, sparkY - 18, sparkX + 2, sparkY - 6, 0xFFFFAA00);
        gg.fill(sparkX - 2, sparkY + 8, sparkX + 2, sparkY + 20, 0xFFFFAA00);
    }

    private void drawConnectionPanel(GuiGraphics gg, int x, int y, int totalWires) {
        int panelX = x + 50;
        int panelY = y + 220;
        int panelW = 440;
        int panelH = 42;

        gg.drawString(this.font, "Connected circuit:", panelX, panelY - 14, 0xFFFFFF);
        gg.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xAA111111);

        int slotSize = 28;
        int gap = totalWires == 4 ? 22 : 12;

        int totalWidth = totalWires * slotSize + (totalWires - 1) * gap;
        int startX = panelX + (panelW - totalWidth) / 2;

        for (int i = 0; i < totalWires; i++) {
            int sx = startX + i * (slotSize + gap);

            gg.fill(sx, panelY + 7, sx + slotSize, panelY + 7 + slotSize, 0xFF222222);
            gg.drawCenteredString(this.font, String.valueOf(i + 1), sx + slotSize / 2, panelY - 8, 0xAAAAAA);

            if (i < connectedOrder.size()) {
                int color = getWireColor(connectedOrder.get(i));
                gg.fill(sx + 6, panelY + 13, sx + slotSize - 6, panelY + slotSize + 1, color);
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

        if (difficulty == null) return;

        if (connectedOrder.size() >= correctOrder.size()) {
            message = "All wires are already connected.";
            messageColor = 0xFFFFAA00;
            return;
        }

        if (connectedOrder.contains(id)) {
            message = "This wire is already connected.";
            messageColor = 0xFFFFAA00;
            return;
        }

        int expectedIndex = connectedOrder.size();
        String expectedWire = correctOrder.get(expectedIndex);

        if (!id.equals(expectedWire)) {
            mistakes++;
            sparkTicks = 22;

            message = "Wrong connection! Sparks fly from the circuit.";
            messageColor = 0xFFFF5555;

            playFailSound();

            if (mistakes % 2 == 0) {
                message = getHint();
                messageColor = 0xFFFFAA00;
            }

            return;
        }

        connectedOrder.add(id);
        successGlowTicks = 12;

        message = "Correct connection: " + formatWireName(id);
        messageColor = 0xFF55FF55;

        playConnectSound();

        if (connectedOrder.size() == correctOrder.size()) {
            message = "All lanterns are connected. Confirm the circuit.";
            messageColor = 0xFF55FF55;
        }
    }

    private void handleActionClick(String id) {
        playClickSound();

        switch (id) {
            case "easy" -> {
                difficulty = Difficulty.EASY;
                correctOrder = List.of("red", "blue", "yellow", "green");
                connectedOrder.clear();
                message = "Easy mode selected. Connect the four wires.";
                messageColor = 0xEEEEEE;
            }

            case "hard" -> {
                difficulty = Difficulty.HARD;
                correctOrder = List.of("red", "orange", "blue", "purple", "yellow", "green");
                connectedOrder.clear();
                message = "Hard mode selected. Connect all six wires.";
                messageColor = 0xEEEEEE;
            }

            case "clear" -> {
                connectedOrder.clear();
                sparkTicks = 0;
                successGlowTicks = 0;
                message = "Circuit reset. Try again.";
                messageColor = 0xFFCCCCCC;
            }

            case "confirm" -> {
                if (difficulty == null) return;

                if (connectedOrder.size() < correctOrder.size()) {
                    message = "The circuit is incomplete.";
                    messageColor = 0xFFFF5555;
                    sparkTicks = 18;
                    playFailSound();
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
                    sparkTicks = 22;
                    connectedOrder.clear();
                    mistakes++;

                    message = "The circuit failed. Start again carefully.";
                    messageColor = 0xFFFF5555;
                    playFailSound();
                }
            }

            case "exit" -> onClose();
        }
    }

    private String getHint() {
        if (difficulty == Difficulty.EASY) {
            return "Hint: red, blue, yellow, green.";
        }

        return "Hint: red, orange, blue, purple, yellow, green.";
    }

    private String getWireIdByIndex(int index) {
        if (difficulty == Difficulty.EASY) {
            return switch (index) {
                case 0 -> "red";
                case 1 -> "blue";
                case 2 -> "yellow";
                case 3 -> "green";
                default -> "red";
            };
        }

        return switch (index) {
            case 0 -> "red";
            case 1 -> "orange";
            case 2 -> "blue";
            case 3 -> "purple";
            case 4 -> "yellow";
            case 5 -> "green";
            default -> "red";
        };
    }

    private String getShortWireLabel(String id) {
        return switch (id) {
            case "red" -> "Red";
            case "orange" -> "Orange";
            case "blue" -> "Blue";
            case "purple" -> "Purple";
            case "yellow" -> "Yellow";
            case "green" -> "Green";
            default -> id;
        };
    }

    private String formatWireName(String id) {
        return getShortWireLabel(id) + " Wire";
    }

    private int getWireColor(String id) {
        return switch (id) {
            case "red" -> 0xFFFF5555;
            case "orange" -> 0xFFFFAA33;
            case "blue" -> 0xFF55AAFF;
            case "purple" -> 0xFFAA55FF;
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

    private void playConnectSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.REDSTONE_TORCH_BURNOUT, 0.35f, 1.6f);
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.45f, 1.3f);
        }
    }

    private void playSuccessSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.PLAYER_LEVELUP, 0.8f, 1.2f);
        }
    }

    private void playFailSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.8f, 1.0f);
            player.playSound(SoundEvents.REDSTONE_TORCH_BURNOUT, 0.6f, 0.8f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}