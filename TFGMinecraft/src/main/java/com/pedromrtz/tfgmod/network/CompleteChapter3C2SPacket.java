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
                                    Component.literal("§cAhora no puedes entregar el sushi."),
                                    false
                            );
                            return;
                        }

                        if (!hasItem(sp, ModItems.SUSHI.get(), 1)) {
                            sp.displayClientMessage(
                                    Component.literal("§cNecesitas tener el sushi para entregarlo."),
                                    false
                            );
                            return;
                        }

                        removeItem(sp, ModItems.SUSHI.get(), 1);

                        progress.setChapter3Active(false);
                        progress.setChapter3Completed(true);
                        progress.setChapter3Task(5);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_sushi_master");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6El Maestro Itamae prueba tu sushi y sonríe."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§aCapítulo 3 completado. Has recibido un cromo final."),
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