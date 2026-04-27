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

        if (ClientChapter1Data.chapter2Completed) {
            text = "Capítulo 2 (Omisoka) — Completado §a✔";
        } else if (ClientChapter1Data.chapter2Active) {
            if (ClientChapter1Data.chapter2Task == 4) {
                text = "Capítulo 2 (Omisoka) — Tarea 4/8: Pon la mesa para la cena ("
                        + ClientChapter1Data.chapter2TableStage + "/4) §e●";
            } else {
                text = "Capítulo 2 (Omisoka) — Tarea "
                        + ClientChapter1Data.chapter2Task
                        + "/8: "
                        + Chapter2TaskTexts.getTaskText(ClientChapter1Data.chapter2Task)
                        + " §e●";
            }
        } else if (ClientChapter1Data.mission1Completed) {
            text = "Capítulo 1 (Japón) — Misión 1: Explorar el pueblo — Completada §a✔";
        } else if (ClientChapter1Data.mission1Active) {
            text = "Capítulo 1 (Japón) — Misión 1: Explorar el pueblo — Activa §e●";
        } else {
            text = "Capítulo 1 (Japón) — Misión 1: Explorar el pueblo — No iniciada §7○";
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x = (screenWidth - mc.font.width(text)) / 2;
        int y = 8;

        gg.drawString(mc.font, text, x, y, 0xFFFFFF, true);
    }
}