package com.pedromrtz.tfgmod.client;

import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID, value = Dist.CLIENT)
public class HudOverlay {

    @SubscribeEvent
    public static void onRenderGui(CustomizeGuiOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        GuiGraphics gg = event.getGuiGraphics();

        String text;

        if (ClientChapter1Data.chapter3Completed) {
            text = "Chapter 3 (Sushi) — Completed §a✔";
        } else if (ClientChapter1Data.chapter3Active) {
            text = "Chapter 3 (Sushi) — Task "
                    + ClientChapter1Data.chapter3Task
                    + "/5: "
                    + getChapter3Text(ClientChapter1Data.chapter3Task)
                    + " §b●";
        }

        else if (ClientChapter1Data.chapter2Completed) {
            text = "Chapter 2 (Omisoka) — Completed §a✔";
        } else if (ClientChapter1Data.chapter2Active) {
            if (ClientChapter1Data.chapter2Task == 4) {
                text = "Chapter 2 (Omisoka) — Task 4/8: Set the table ("
                        + ClientChapter1Data.chapter2TableStage + "/4) §e●";
            } else {
                text = "Chapter 2 (Omisoka) — Task "
                        + ClientChapter1Data.chapter2Task
                        + "/8: "
                        + Chapter2TaskTexts.getTaskText(ClientChapter1Data.chapter2Task)
                        + " §e●";
            }
        }

        else if (ClientChapter1Data.mission1Completed) {
            text = "Chapter 1: Discovering Japan — Mission 1: Explore the village — Completed §a✔";
        } else if (ClientChapter1Data.mission1Active) {
            text = "Chapter 1: Discovering Japan — Mission 1: Explore the village — Started §e●";
        } else {
            text = "Chapter 1: Discovering Japan — Mission 1: Explore the village — Not Started §7○";
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x = (screenWidth - mc.font.width(text)) / 2;
        int y = 8;

        gg.drawString(mc.font, text, x, y, 0xFFFFFF, true);
    }

    private static String getChapter3Text(int task) {
        return switch (task) {
            case 1 -> "Collect rice and fish";
            case 2 -> "Wash the rice";
            case 3 -> "Cut the salmon";
            case 4 -> "Prepare the sushi";
            case 5 -> "Deliver the sushi";
            default -> "Begin the sushi experience";
        };
    }
}