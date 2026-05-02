package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID)
public class Chapter1MissionEvents {

    private static final int MIN_X = 1435;
    private static final int MAX_X = 1445;
    private static final int MIN_Y = 90;
    private static final int MAX_Y = 105;
    private static final int MIN_Z = 850;
    private static final int MAX_Z = 865;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;

        var progress = Chapter1ProgressUtil.get(sp);

        if (!progress.isMission1Active()) return;
        if (progress.hasFamilyCard()) return;

        var pos = sp.blockPosition();

        boolean inside =
                pos.getX() >= MIN_X && pos.getX() <= MAX_X &&
                        pos.getY() >= MIN_Y && pos.getY() <= MAX_Y &&
                        pos.getZ() >= MIN_Z && pos.getZ() <= MAX_Z;

        if (!inside) return;

        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
        CardItem.setCardId(cardStack, "card_japan_family_house");

        sp.addItem(cardStack);

        progress.setHasFamilyCard(true);
        progress.setMission1Completed(true);

        com.pedromrtz.tfgmod.network.ProgressSync.syncChapter1(sp);

        sp.displayClientMessage(
                Component.literal("§6Mission completed! You have unlocked a new cultural card."),
                false
        );

        sp.level().playSound(
                null,
                sp.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                sp.getSoundSource(),
                1.0f,
                1.0f
        );
    }
}