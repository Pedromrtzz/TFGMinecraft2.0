package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.network.ProgressSync;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID)
public class ProgressLoginSyncEvent {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            ProgressSync.syncChapter1(sp);
        }
    }
}