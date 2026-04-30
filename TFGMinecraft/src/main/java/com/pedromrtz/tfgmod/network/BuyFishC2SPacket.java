package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BuyFishC2SPacket {

    private final String itemId;

    public BuyFishC2SPacket(String itemId) {
        this.itemId = itemId;
    }

    public static void encode(BuyFishC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.itemId);
    }

    public static BuyFishC2SPacket decode(FriendlyByteBuf buf) {
        return new BuyFishC2SPacket(buf.readUtf());
    }

    public static void handle(BuyFishC2SPacket msg, Object ctxObj) {
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
                            sp.displayClientMessage(Component.literal("§7Ahora no necesitas comprar pescado."), false);
                            return;
                        }

                        Item itemToBuy = getItem(msg.itemId);
                        String itemName = getItemName(msg.itemId);
                        int price = getPrice(msg.itemId);

                        if (itemToBuy == null) {
                            sp.displayClientMessage(Component.literal("§cEse producto no existe."), false);
                            return;
                        }

                        if (!removeYen(sp, price)) {
                            sp.displayClientMessage(Component.literal("§cNo tienes suficientes yenes."), false);
                            return;
                        }

                        sp.addItem(new ItemStack(itemToBuy, 1));
                        sp.displayClientMessage(Component.literal("§6Has comprado: " + itemName), false);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }

    private static Item getItem(String id) {
        return switch (id) {
            case "salmon" -> Items.SALMON;
            case "cod" -> Items.COD;
            case "tropical_fish" -> Items.TROPICAL_FISH;
            case "pufferfish" -> Items.PUFFERFISH;
            case "kelp" -> Items.KELP;
            case "ink" -> Items.INK_SAC;
            default -> null;
        };
    }

    private static String getItemName(String id) {
        return switch (id) {
            case "salmon" -> "Salmón fresco";
            case "cod" -> "Bacalao";
            case "tropical_fish" -> "Pez tropical";
            case "pufferfish" -> "Pez globo";
            case "kelp" -> "Alga marina";
            case "ink" -> "Saco de tinta";
            default -> id;
        };
    }

    private static int getPrice(String id) {
        return switch (id) {
            case "salmon" -> 2;
            case "cod" -> 1;
            case "tropical_fish" -> 3;
            case "pufferfish" -> 4;
            case "kelp" -> 1;
            case "ink" -> 2;
            default -> 1;
        };
    }

    private static boolean removeYen(ServerPlayer sp, int amount) {
        int remaining = amount;

        for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
            ItemStack stack = sp.getInventory().getItem(i);

            if (!stack.is(ModItems.YEN.get())) continue;

            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;

            if (remaining <= 0) {
                sp.getInventory().setChanged();
                return true;
            }
        }

        return false;
    }
}