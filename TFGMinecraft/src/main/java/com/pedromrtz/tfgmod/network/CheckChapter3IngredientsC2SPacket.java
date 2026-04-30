package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CheckChapter3IngredientsC2SPacket {

    public CheckChapter3IngredientsC2SPacket() {}

    public static void encode(CheckChapter3IngredientsC2SPacket msg, FriendlyByteBuf buf) {}

    public static CheckChapter3IngredientsC2SPacket decode(FriendlyByteBuf buf) {
        return new CheckChapter3IngredientsC2SPacket();
    }

    public static void handle(CheckChapter3IngredientsC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter3Active() || progress.getChapter3Task() != 1) {
                            sp.displayClientMessage(Component.literal("§cNo puedes entregar esto ahora."), false);
                            return;
                        }

                        boolean hasRice = hasItem(sp, ModItems.ARROZ.get(), 1);
                        boolean hasFish = hasItem(sp, Items.SALMON, 1);

                        if (!hasRice || !hasFish) {
                            String missing = "";

                            if (!hasRice) missing += "arroz ";
                            if (!hasFish) missing += "salmón ";

                            sp.displayClientMessage(
                                    Component.literal("§cTe falta: " + missing),
                                    false
                            );
                            return;
                        }

                        removeItem(sp, ModItems.ARROZ.get(), 1);
                        removeItem(sp, Items.SALMON, 1);

                        progress.setChapter3Task(2);

                        sp.displayClientMessage(
                                Component.literal("§6Perfecto. Ya tenemos arroz y pescado para preparar sushi."),
                                false
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