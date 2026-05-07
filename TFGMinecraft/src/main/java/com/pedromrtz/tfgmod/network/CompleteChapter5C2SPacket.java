package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class CompleteChapter5C2SPacket {

    public CompleteChapter5C2SPacket() {}

    public static void encode(CompleteChapter5C2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter5C2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter5C2SPacket();
    }

    public static void handle(CompleteChapter5C2SPacket msg, Object ctxObj) {
        try {
            var ctx = ctxObj;
            var enqueueWork = ctx.getClass().getMethod("enqueueWork", Runnable.class);
            var getSender = ctx.getClass().getMethod("getSender");
            var setHandled = ctx.getClass().getMethod("setPacketHandled", boolean.class);

            enqueueWork.invoke(ctx, (Runnable) () -> {
                try {
                    ServerPlayer sp = (ServerPlayer) getSender.invoke(ctx);
                    if (sp == null) return;

                    sp.getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(progress -> {
                        if (!progress.isChapter5Active() || progress.getChapter5Task() != 3) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot complete Chapter 5 right now."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter5Active(false);
                        progress.setChapter5Completed(true);
                        progress.setChapter5Task(3);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_daimyo_castle");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6The daimyo recognises your discipline and respect."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§aChapter 5 completed. You received the Daimyo Castle card."),
                                false
                        );

                        sp.level().playSound(
                                null,
                                sp.blockPosition(),
                                SoundEvents.PLAYER_LEVELUP,
                                sp.getSoundSource(),
                                1.0f,
                                1.0f
                        );

                        ProgressSync.syncChapter1(sp);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }
}