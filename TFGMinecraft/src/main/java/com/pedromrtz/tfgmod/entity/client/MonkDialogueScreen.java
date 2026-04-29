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

public class MonkDialogueScreen extends Screen {

    private static final ResourceLocation MONK_PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/monk.png");

    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, String action) {}

    public MonkDialogueScreen() {
        super(Component.literal("Monje"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 430;
        int boxH = 285;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        int portraitSize = 64;
        int portraitX = x + 14;
        int portraitY = y + 14;

        gg.fill(
                portraitX - 2,
                portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111
        );

        gg.blit(
                MONK_PORTRAIT,
                portraitX,
                portraitY,
                0,
                0,
                portraitSize,
                portraitSize,
                portraitSize,
                portraitSize
        );

        int textX = x + 14 + portraitSize + 18;

        gg.drawString(this.font, "Monje del templo", textX, y + 16, 0xFFFFFF);

        List<String> lines = getLines();

        int textY = y + 44;
        for (String line : lines) {
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

        optionAreas.add(new OptionArea(btnX, btnY, btnW, btnH, "exit"));

        super.render(gg, mouseX, mouseY, pt);
    }

    private List<String> getLines() {
        if (!ClientChapter1Data.chapter2Active || ClientChapter1Data.chapter2Task != 6) {
            return List.of(
                    "Bienvenido al templo.",
                    "Cuando llegue el momento adecuado,",
                    "te explicaré el significado de las campanadas."
            );
        }

        return List.of(
                "Has llegado en la noche de Omisoka.",
                "En Japón, muchos templos realizan el Joya no Kane.",
                "Durante este ritual, la campana suena 108 veces.",
                "Cada campanada simboliza dejar atrás deseos,",
                "preocupaciones e impurezas humanas.",
                "Haz sonar la campana del templo para completar el ritual."
        );
    }

    private String getButtonText() {
        if (!ClientChapter1Data.chapter2Active || ClientChapter1Data.chapter2Task != 6) {
            return "Entendido";
        }

        return "Entendido, tocaré la campana";
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : optionAreas) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();
                onClose();
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