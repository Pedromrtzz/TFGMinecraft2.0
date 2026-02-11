package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID)
public class ProgressEvents {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(Chapter1ProgressProvider.ID, new Chapter1ProgressProvider());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        event.getOriginal().getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(oldData -> {
            event.getEntity().getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(newData -> {
                newData.deserializeNBT(oldData.serializeNBT());
            });
        });
    }

}