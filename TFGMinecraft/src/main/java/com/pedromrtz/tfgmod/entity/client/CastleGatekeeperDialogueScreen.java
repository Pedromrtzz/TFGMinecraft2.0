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

public class CastleGatekeeperDialogueScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath(
                    "tfgmod",
                    "textures/gui/portraits/castle_gatekeeper.png"
            );

    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, String action) {}

    public CastleGatekeeperDialogueScreen() {
        super(Component.literal("Castle Gatekeeper"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {

        renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 470;
        int boxH = 300;

        int x = (width - boxW) / 2;
        int y = (height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.blit(
                PORTRAIT,
                x + 14,
                y + 14,
                0,
                0,
                64,
                64,
                64,
                64
        );

        gg.drawString(
                font,
                "Castle Gatekeeper",
                x + 100,
                y + 18,
                0xFFFFFF
        );

        int textY = y + 48;

        for (String line : getLines()) {

            gg.drawString(
                    font,
                    line,
                    x + 100,
                    textY,
                    0xEEEEEE
            );

            textY += 13;
        }

        optionAreas.clear();

        int btnW = boxW - 28;
        int btnH = 22;

        int btnX = x + 14;
        int btnY = y + boxH - 40;

        int color =
                isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                        ? 0xFF555555
                        : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, color);

        gg.drawCenteredString(
                font,
                getButtonText(),
                btnX + btnW / 2,
                btnY + 7,
                0xFFFFFF
        );

        optionAreas.add(
                new OptionArea(btnX, btnY, btnW, btnH, "continue")
        );

        super.render(gg, mouseX, mouseY, pt);
    }

    private List<String> getLines() {

        if (!ClientChapter1Data.chapter5Active
                || ClientChapter1Data.chapter5Task != 2) {

            return List.of(
                    "Only those who understand honour",
                    "may enter the inner castle."
            );
        }

        return List.of(
                "Before entering the castle,",
                "you must recognise the clan symbol.",
                "Japanese noble families used symbols called mon.",
                "These symbols represented identity, status",
                "and loyalty during the Edo period."
        );
    }

    private String getButtonText() {

        if (!ClientChapter1Data.chapter5Active
                || ClientChapter1Data.chapter5Task != 2) {

            return "Understood";
        }

        return "Solve the clan puzzle";
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        for (OptionArea area : optionAreas) {

            if (isMouseOver(mouseX, mouseY,
                    area.x, area.y, area.w, area.h)) {

                playClickSound();

                if (ClientChapter1Data.chapter5Task == 2) {

                    Minecraft.getInstance().setScreen(
                            new ClanSymbolPuzzleScreen()
                    );

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

            player.playSound(
                    SoundEvents.UI_BUTTON_CLICK.value(),
                    1f,
                    1f
            );
        }
    }

    private boolean isMouseOver(
            double mx,
            double my,
            int x,
            int y,
            int w,
            int h
    ) {
        return mx >= x
                && mx <= x + w
                && my >= y
                && my <= y + h;
    }
}