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

        String status;
        if (ClientChapter1Data.mission1Completed) status = "Completada §a✔";
        else if (ClientChapter1Data.mission1Active) status = "Activa §e●";
        else status = "No iniciada §7○";

        String text = "Capítulo 1 (Japón) — Misión 1: Explorar el pueblo — " + status;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int x = (screenWidth - mc.font.width(text)) / 2;
        int y = 8;

        gg.drawString(mc.font, text, x, y, 0xFFFFFF, true);
    }
}