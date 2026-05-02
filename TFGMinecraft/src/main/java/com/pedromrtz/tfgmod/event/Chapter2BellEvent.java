package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID)
public class Chapter2BellEvent {

    @SubscribeEvent
    public static void onRightClickBell(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;

        if (!event.getLevel().getBlockState(event.getPos()).is(Blocks.BELL)) return;

        var progress = Chapter1ProgressUtil.get(sp);

        if (!progress.isChapter2Active() || progress.getChapter2Task() != 6) {
            return;
        }

        int current = progress.getChapter2BellCount();

        if (current >= 108) {
            sp.displayClientMessage(
                    Component.literal("§7You have already completed the 108 bell chimes."),
                    true
            );
            return;
        }

        int next = Math.min(108, current + 12);
        progress.setChapter2BellCount(next);

        event.getLevel().playSound(
                null,
                event.getPos(),
                SoundEvents.BELL_BLOCK,
                sp.getSoundSource(),
                1.0f,
                0.8f
        );

        sp.displayClientMessage(
                Component.literal("§6Bell chimes: " + next + " / 108"),
                true
        );

        if (next >= 108) {
            progress.setChapter2Task(7);

            ItemStack ema = new ItemStack(ModItems.EMA.get());
            sp.addItem(ema);

            sp.displayClientMessage(
                    Component.literal("§6You have completed the 108 bell chimes. The monk gives you an ema."),
                    false
            );

            sp.displayClientMessage(
                    Component.literal("§7Write your wish and then hang it on the tree outside."),
                    false
            );
        }

        ProgressSync.syncChapter1(sp);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}