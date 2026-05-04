package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class GoldfishSellerDialogueScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/goldfish_seller.png");

    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, String action) {}

    public GoldfishSellerDialogueScreen() {
        super(Component.literal("Goldfish Stall Owner"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 460;
        int boxH = 285;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        int portraitSize = 64;
        int portraitX = x + 14;
        int portraitY = y + 14;

        gg.fill(portraitX - 2, portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111);

        gg.blit(PORTRAIT,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textX = x + 14 + portraitSize + 18;

        gg.drawString(this.font, "Goldfish Stall Owner", textX, y + 16, 0xFFFFFF);

        int textY = y + 44;
        for (String line : getLines()) {
            gg.drawString(this.font, line, textX, textY, 0xEEEEEE);
            textY += 13;
        }

        optionAreas.clear();

        int btnW = boxW - 28;
        int btnH = 22;
        int btnX = x + 14;
        int btnY = y + boxH - 42;

        int color = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, color);

        gg.drawCenteredString(
                this.font,
                getButtonText(),
                btnX + btnW / 2,
                btnY + 7,
                0xFFFFFF
        );

        optionAreas.add(new OptionArea(btnX, btnY, btnW, btnH, "continue"));

        super.render(gg, mouseX, mouseY, pt);
    }

    private List<String> getLines() {
        if (!ClientChapter1Data.chapter4Active || ClientChapter1Data.chapter4Task != 2) {
            return List.of(
                    "Welcome to the goldfish scooping stall.",
                    "This game is often seen at Japanese festivals.",
                    "Come back when the organizer asks you to test it."
            );
        }

        return List.of(
                "Perfect timing! We need to test this stall.",
                "Goldfish scooping is a traditional Matsuri game.",
                "Players use a small paper scoop to catch fish.",
                "The challenge is to be gentle and precise",
                "before the scoop breaks."
        );
    }

    private String getButtonText() {
        if (!ClientChapter1Data.chapter4Active || ClientChapter1Data.chapter4Task != 2) {
            return "Understood";
        }

        return "Test the game";
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : optionAreas) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();

                if (ClientChapter1Data.chapter4Active && ClientChapter1Data.chapter4Task == 2) {
                    Minecraft.getInstance().setScreen(new GoldfishScoopingGameScreen());
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
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}