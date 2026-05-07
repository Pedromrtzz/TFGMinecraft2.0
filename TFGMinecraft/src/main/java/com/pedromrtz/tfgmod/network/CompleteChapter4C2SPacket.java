package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class CompleteChapter4C2SPacket {

    public CompleteChapter4C2SPacket() {}

    public static void encode(CompleteChapter4C2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter4C2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter4C2SPacket();
    }

    public static void handle(CompleteChapter4C2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter4Active() || progress.getChapter4Task() != 4) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot complete the Matsuri chapter right now."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter4Active(true);
                        progress.setChapter4Completed(false);
                        progress.setChapter4Task(4);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_matsuri");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6The floating lantern drifts across the river."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§7Now answer the cultural test to complete Chapter 4."),
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

                        ModNetwork.CHANNEL.send(
                                new OpenChapter4QuizS2CPacket(),
                                PacketDistributor.PLAYER.with(sp)
                        );
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }
}