package com.pedromrtz.tfgmod.entity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class AmbientDialogueScreen extends Screen {

    private final AmbientNPCData.AmbientNPCInfo npcInfo;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, String action) {}

    public AmbientDialogueScreen(String npcId) {
        super(Component.literal("Ambient NPC"));
        this.npcInfo = AmbientNPCData.get(npcId);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 340;
        int boxH = 220;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xCC000000);

        // Retrato
        int portraitSize = 64;
        int portraitX = x + 12;
        int portraitY = y + 12;

        gg.fill(portraitX - 2, portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111);

        ResourceLocation portrait = npcInfo.portrait();

        gg.blit(portrait,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textStartX = x + 14 + portraitSize + 14;

        gg.drawString(this.font,
                npcInfo.displayName(),
                textStartX,
                y + 14,
                0xFFFFFF);

        int textY = y + 38;
        int lineHeight = 12;

        for (String line : npcInfo.lines()) {
            gg.drawString(this.font, line, textStartX, textY, 0xEEEEEE);
            textY += lineHeight;
        }

        optionAreas.clear();

        int optionHeight = 18;
        int optionWidth = boxW - 28;
        int optionX = x + 14;
        int optionY = y + boxH - 34;

        int bgColor = isMouseOver(mouseX, mouseY, optionX, optionY, optionWidth, optionHeight)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(optionX, optionY, optionX + optionWidth, optionY + optionHeight, bgColor);

        gg.drawCenteredString(this.font,
                "Entendido",
                optionX + optionWidth / 2,
                optionY + 5,
                0xFFFFFF);

        optionAreas.add(new OptionArea(optionX, optionY, optionWidth, optionHeight, "exit"));

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : optionAreas) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                handleClick(area.action);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleClick(String action) {
        playClickSound();

        if ("exit".equals(action)) {
            onClose();
        }
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}