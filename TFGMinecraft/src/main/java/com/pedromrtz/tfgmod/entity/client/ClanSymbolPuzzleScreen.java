package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter5PuzzleC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;

public class ClanSymbolPuzzleScreen extends Screen {

    private int correct = 1;
    private boolean solved = false;

    public ClanSymbolPuzzleScreen() {
        super(Component.literal("Clan Symbol Puzzle"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {

        renderBackground(gg, mouseX, mouseY, pt);

        int centerX = width / 2;
        int centerY = height / 2;

        gg.drawCenteredString(
                font,
                "Choose the correct clan symbol",
                centerX,
                centerY - 90,
                0xFFFFFF
        );

        int size = 60;

        for (int i = 0; i < 3; i++) {

            int x = centerX - 110 + i * 80;
            int y = centerY - 10;

            int color;

            if (i == 1) {
                color = 0xFFAA0000;
            } else if (i == 0) {
                color = 0xFF4444AA;
            } else {
                color = 0xFFAAAA44;
            }

            gg.fill(x, y, x + size, y + size, color);

            gg.drawCenteredString(
                    font,
                    "" + (i + 1),
                    x + size / 2,
                    y + 25,
                    0xFFFFFF
            );
        }

        if (solved) {

            gg.drawCenteredString(
                    font,
                    "Correct Symbol",
                    centerX,
                    centerY + 90,
                    0x00FF00
            );
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        int centerX = width / 2;
        int centerY = height / 2;

        int size = 60;

        for (int i = 0; i < 3; i++) {

            int x = centerX - 110 + i * 80;
            int y = centerY - 10;

            if (mouseX >= x
                    && mouseX <= x + size
                    && mouseY >= y
                    && mouseY <= y + size) {

                if (i == correct) {

                    solved = true;

                    ModNetwork.CHANNEL.send(
                            new CompleteChapter5PuzzleC2SPacket(),
                            PacketDistributor.SERVER.noArg()
                    );

                    onClose();
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}