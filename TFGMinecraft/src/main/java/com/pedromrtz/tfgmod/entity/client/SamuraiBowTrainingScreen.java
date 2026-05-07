package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter5BowTrainingC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class SamuraiBowTrainingScreen extends Screen {

    private int markerX = 0;
    private boolean movingRight = true;

    private int score = 0;
    private int attempts = 0;

    private boolean finished = false;
    private String resultText = "";

    public SamuraiBowTrainingScreen() {
        super(Component.literal("Samurai Focus Training"));
    }

    @Override
    public void tick() {

        if (finished) return;

        if (movingRight) {

            markerX += 5;

            if (markerX >= 240) {
                movingRight = false;
            }

        } else {

            markerX -= 5;

            if (markerX <= 0) {
                movingRight = true;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (finished) {

            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                onClose();
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        if (keyCode == GLFW.GLFW_KEY_SPACE) {

            attempts++;

            boolean success =
                    markerX >= 95 && markerX <= 145;

            if (success) {

                score++;

                if (minecraft.player != null) {
                    minecraft.player.playSound(
                            SoundEvents.EXPERIENCE_ORB_PICKUP,
                            1f,
                            1f
                    );
                }

            } else {

                if (minecraft.player != null) {
                    minecraft.player.playSound(
                            SoundEvents.VILLAGER_NO,
                            1f,
                            1f
                    );
                }
            }

            if (attempts >= 5) {

                finished = true;

                if (score >= 3) {

                    resultText = "Training Complete";

                    ModNetwork.CHANNEL.send(
                            new CompleteChapter5BowTrainingC2SPacket(),
                            PacketDistributor.SERVER.noArg()
                    );

                } else {

                    resultText = "Training Failed";
                }
            }

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {

        renderBackground(gg, mouseX, mouseY, pt);

        int centerX = width / 2;
        int centerY = height / 2;

        gg.drawCenteredString(
                font,
                "Samurai Bow Training",
                centerX,
                centerY - 90,
                0xFFFFFF
        );

        gg.drawCenteredString(
                font,
                "Press SPACE inside the green zone",
                centerX,
                centerY - 72,
                0xAAAAAA
        );

        gg.drawCenteredString(
                font,
                "Score: " + score + "/5",
                centerX,
                centerY - 52,
                0xFFD700
        );

        int barWidth = 260;
        int barHeight = 20;

        int startX = centerX - barWidth / 2;

        gg.fill(
                startX,
                centerY,
                startX + barWidth,
                centerY + barHeight,
                0xFF333333
        );

        gg.fill(
                startX + 95,
                centerY,
                startX + 145,
                centerY + barHeight,
                0xFF00AA00
        );

        gg.fill(
                startX + markerX,
                centerY - 4,
                startX + markerX + 8,
                centerY + barHeight + 4,
                0xFFFFFFFF
        );

        if (finished) {

            gg.drawCenteredString(
                    font,
                    resultText,
                    centerX,
                    centerY + 50,
                    score >= 3 ? 0x00FF00 : 0xFF5555
            );

            gg.drawCenteredString(
                    font,
                    "Press ENTER to close",
                    centerX,
                    centerY + 70,
                    0xFFFFFF
            );
        }

        super.render(gg, mouseX, mouseY, pt);
    }
}