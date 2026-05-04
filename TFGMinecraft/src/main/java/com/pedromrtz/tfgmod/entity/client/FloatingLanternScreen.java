package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter4C2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class FloatingLanternScreen extends Screen {

    private enum LanternType {
        PEACE,
        FAMILY,
        FUTURE
    }

    private EditBox wishBox;

    private LanternType selectedType = null;
    private boolean released = false;
    private boolean completed = false;

    private int ticks = 0;
    private int ambientSoundCooldown = 0;

    private String message = "Write a wish and choose a lantern type.";
    private int messageColor = 0xEEEEEE;

    private final List<ButtonArea> buttons = new ArrayList<>();

    private record ButtonArea(int x, int y, int w, int h, String id, String label) {}

    public FloatingLanternScreen() {
        super(Component.literal("Floating Lanterns"));
    }

    @Override
    protected void init() {
        super.init();

        int boxW = 520;
        int boxH = 350;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        wishBox = new EditBox(
                this.font,
                x + 80,
                y + 78,
                boxW - 160,
                22,
                Component.literal("Wish")
        );

        wishBox.setMaxLength(70);
        wishBox.setValue("");
        wishBox.setHint(Component.literal("Write your wish here..."));

        this.addRenderableWidget(wishBox);
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;

        if (wishBox != null) {
        }

        if (released) {
            ambientSoundCooldown--;

            if (ambientSoundCooldown <= 0) {
                playAmbientSound();
                ambientSoundCooldown = 55;
            }
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 520;
        int boxH = 350;
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
                "Choose the meaning of your lantern and release it into the river.",
                this.width / 2,
                y + 35,
                0xDDDDDD
        );

        if (!released) {
            renderPreparation(gg, mouseX, mouseY, x, y, boxW, boxH);
        } else {
            renderReleasedScene(gg, mouseX, mouseY, x, y, boxW, boxH);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    private void renderPreparation(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        gg.drawString(this.font, "Your wish:", x + 80, y + 64, 0xFFFFFF);

        buttons.clear();

        int btnW = 120;
        int btnH = 26;
        int btnY = y + 120;
        int gap = 18;
        int startX = this.width / 2 - (btnW * 3 + gap * 2) / 2;

        addButton(startX, btnY, btnW, btnH, "peace", "Peace");
        addButton(startX + btnW + gap, btnY, btnW, btnH, "family", "Family");
        addButton(startX + (btnW + gap) * 2, btnY, btnW, btnH, "future", "Future");

        for (ButtonArea btn : buttons) {
            boolean selected = isSelected(btn.id);

            int bg;
            if (selected) {
                bg = 0xFF666633;
            } else {
                bg = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                        ? 0xFF555555 : 0xFF333333;
            }



            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, bg);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 9, 0xFFFFFF);
        }

        String culturalText = switch (selectedType == null ? LanternType.PEACE : selectedType) {
            case PEACE -> "Peace lanterns represent calm, reflection and harmony.";
            case FAMILY -> "Family lanterns represent gratitude and connection.";
            case FUTURE -> "Future lanterns represent hope, dreams and new beginnings.";
        };

        gg.drawCenteredString(this.font, culturalText, this.width / 2, y + 172, 0xAAAAAA);

        gg.drawCenteredString(this.font, message, this.width / 2, y + 205, messageColor);

        int releaseW = boxW - 90;
        int releaseH = 24;
        int releaseX = x + 45;
        int releaseY = y + boxH - 48;

        int releaseColor = isMouseOver(mouseX, mouseY, releaseX, releaseY, releaseW, releaseH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(releaseX, releaseY, releaseX + releaseW, releaseY + releaseH, releaseColor);
        gg.drawCenteredString(this.font, "Release the lantern", releaseX + releaseW / 2, releaseY + 8, 0xFFFFFF);

        buttons.add(new ButtonArea(releaseX, releaseY, releaseW, releaseH, "release", "Release the lantern"));
    }

    private void renderReleasedScene(GuiGraphics gg, int mouseX, int mouseY, int x, int y, int boxW, int boxH) {
        drawRiverScene(gg, x, y, boxW);

        String typeText = switch (selectedType) {
            case PEACE -> "Lantern of Peace";
            case FAMILY -> "Lantern of Family";
            case FUTURE -> "Lantern of the Future";
        };

        gg.drawCenteredString(this.font, typeText, this.width / 2, y + 210, 0xFFFFAA55);

        String wish = wishBox.getValue().trim();
        if (!wish.isEmpty()) {
            gg.drawCenteredString(this.font, "\"" + wish + "\"", this.width / 2, y + 230, 0xEEEEEE);
        }

        gg.drawCenteredString(
                this.font,
                "The lanterns drift away, carrying hope across the river.",
                this.width / 2,
                y + 252,
                0xAAAAAA
        );

        gg.drawCenteredString(this.font, message, this.width / 2, y + 275, messageColor);

        buttons.clear();

        int btnW = boxW - 80;
        int btnH = 24;
        int btnX = x + 40;
        int btnY = y + boxH - 42;

        int color = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, color);
        gg.drawCenteredString(this.font, "Complete Matsuri chapter", btnX + btnW / 2, btnY + 8, 0xFFFFFF);

        buttons.add(new ButtonArea(btnX, btnY, btnW, btnH, "complete", "Complete Matsuri chapter"));
    }

    private void drawRiverScene(GuiGraphics gg, int x, int y, int boxW) {
        int riverX = x + 45;
        int riverY = y + 68;
        int riverW = boxW - 90;
        int riverH = 125;

        gg.fill(riverX, riverY, riverX + riverW, riverY + riverH, 0xFF123747);
        gg.fill(riverX + 4, riverY + 4, riverX + riverW - 4, riverY + riverH - 4, 0xFF1D6B82);

        for (int i = 0; i < 5; i++) {
            int waveY = riverY + 18 + i * 22;
            gg.fill(riverX + 20, waveY, riverX + riverW - 20, waveY + 1, 0x6655CCFF);
        }

        drawFloatingLantern(gg, riverX + getLanternOffset(0), riverY + 40, getLanternColor(0));
        drawFloatingLantern(gg, riverX + getLanternOffset(1), riverY + 68, getLanternColor(1));
        drawFloatingLantern(gg, riverX + getLanternOffset(2), riverY + 25, getLanternColor(2));

        drawParticles(gg, riverX, riverY, riverW, riverH);
    }

    private int getLanternOffset(int index) {
        int base = switch (index) {
            case 0 -> 70;
            case 1 -> 145;
            default -> 230;
        };

        return base + Math.min(160, ticks / 2 + index * 18);
    }

    private int getLanternColor(int index) {
        if (selectedType == LanternType.PEACE) {
            return index == 0 ? 0xFFAAEEFF : 0xFFFFDD99;
        }

        if (selectedType == LanternType.FAMILY) {
            return index == 0 ? 0xFFFFAA99 : 0xFFFFDD99;
        }

        return index == 0 ? 0xFFCCAAFF : 0xFFFFDD99;
    }

    private void drawFloatingLantern(GuiGraphics gg, int x, int y, int mainColor) {
        int floatY = y + (int) (Math.sin((ticks + x) / 12.0) * 4);

        gg.fill(x - 14, floatY - 12, x + 30, floatY + 32, 0x44FFCC66);

        gg.fill(x, floatY, x + 18, floatY + 22, mainColor);
        gg.fill(x + 3, floatY + 4, x + 15, floatY + 18, 0xFFFFFF99);
        gg.fill(x + 6, floatY + 8, x + 12, floatY + 14, 0xFFFFDD55);
    }

    private void drawParticles(GuiGraphics gg, int riverX, int riverY, int riverW, int riverH) {
        for (int i = 0; i < 10; i++) {
            int px = riverX + 20 + ((ticks * 2 + i * 43) % (riverW - 40));
            int py = riverY + 15 + ((i * 19 + ticks / 2) % (riverH - 30));

            gg.fill(px, py, px + 2, py + 2, 0xAAFFFFAA);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonArea area : buttons) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                handleButton(area.id);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleButton(String id) {
        playClickSound();

        switch (id) {
            case "peace" -> {
                selectedType = LanternType.PEACE;
                message = "Peace lantern selected.";
                messageColor = 0xFFAAEEFF;
            }

            case "family" -> {
                selectedType = LanternType.FAMILY;
                message = "Family lantern selected.";
                messageColor = 0xFFFFAA99;
            }

            case "future" -> {
                selectedType = LanternType.FUTURE;
                message = "Future lantern selected.";
                messageColor = 0xFFCCAAFF;
            }

            case "release" -> {
                String wish = wishBox.getValue().trim();

                if (wish.isEmpty()) {
                    message = "Please write a wish before releasing the lantern.";
                    messageColor = 0xFFFF5555;
                    return;
                }

                if (selectedType == null) {
                    message = "Please choose a lantern type.";
                    messageColor = 0xFFFF5555;
                    return;
                }

                released = true;
                ticks = 0;
                message = "Your lantern has been released.";
                messageColor = 0xFF55FF55;

                if (wishBox != null) {
                    wishBox.setVisible(false);
                    wishBox.setEditable(false);
                }

                playReleaseSound();
            }

            case "complete" -> completeChapter();
        }
    }

    private boolean isSelected(String id) {
        return switch (id) {
            case "peace" -> selectedType == LanternType.PEACE;
            case "family" -> selectedType == LanternType.FAMILY;
            case "future" -> selectedType == LanternType.FUTURE;
            default -> false;
        };
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

    private void playReleaseSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.BEACON_ACTIVATE, 0.6f, 1.4f);
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);
        }
    }

    private void playAmbientSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.WATER_AMBIENT, 0.4f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private void addButton(int x, int y, int w, int h, String id, String label) {
        buttons.add(new ButtonArea(x, y, w, h, id, label));
    }
}