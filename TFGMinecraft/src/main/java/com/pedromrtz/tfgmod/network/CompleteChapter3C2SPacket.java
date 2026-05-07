package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class CompleteChapter3C2SPacket {

    public CompleteChapter3C2SPacket() {}

    public static void encode(CompleteChapter3C2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter3C2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter3C2SPacket();
    }

    public static void handle(CompleteChapter3C2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter3Active() || progress.getChapter3Task() != 5) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot deliver the sushi right now."),
                                    false
                            );
                            return;
                        }

                        if (!hasItem(sp, ModItems.SUSHI.get(), 1)) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou need to have the sushi to deliver it."),
                                    false
                            );
                            return;
                        }

                        removeItem(sp, ModItems.SUSHI.get(), 1);

                        // The chapter stays active until the quiz is passed.
                        progress.setChapter3Active(true);
                        progress.setChapter3Completed(false);
                        progress.setChapter3Task(5);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_sushi_master");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6The Itamae Master tastes your sushi and smiles."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§7Now answer the cultural test to complete Chapter 3."),
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
                                new OpenChapter3QuizS2CPacket(),
                                PacketDistributor.PLAYER.with(sp)
                        );
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }

    private static boolean hasItem(ServerPlayer sp, Item item, int amount) {
        int total = 0;

        for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
            ItemStack stack = sp.getInventory().getItem(i);

            if (stack.is(item)) {
                total += stack.getCount();
                if (total >= amount) return true;
            }
        }

        return false;
    }

    private static void removeItem(ServerPlayer sp, Item item, int amount) {
        int remaining = amount;

        for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
            ItemStack stack = sp.getInventory().getItem(i);

            if (!stack.is(item)) continue;

            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;

            if (remaining <= 0) break;
        }

        sp.getInventory().setChanged();
    }
}