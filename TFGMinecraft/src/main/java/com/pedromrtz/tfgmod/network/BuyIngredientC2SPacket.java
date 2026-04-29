package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BuyIngredientC2SPacket {

    private final String itemId;

    public BuyIngredientC2SPacket(String itemId) {
        this.itemId = itemId;
    }

    public static void encode(BuyIngredientC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.itemId);
    }

    public static BuyIngredientC2SPacket decode(FriendlyByteBuf buf) {
        return new BuyIngredientC2SPacket(buf.readUtf());
    }

    public static void handle(BuyIngredientC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter2Active() || progress.getChapter2Task() > 2) {
                            sp.displayClientMessage(Component.literal("§7Ahora no necesitas comprar ingredientes."), false);
                            return;
                        }

                        Item itemToBuy = getItem(msg.itemId);
                        String itemName = getItemName(msg.itemId);

                        if (itemToBuy == null) {
                            sp.displayClientMessage(Component.literal("§cEse producto no existe."), false);
                            return;
                        }

                        int price = getPrice(msg.itemId);

                        if (!removeYen(sp, price)) {
                            sp.displayClientMessage(Component.literal("§cNo tienes suficientes yenes (" + price + ")."), false);
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
            case "fideos" -> ModItems.FIDEOS.get();
            case "alga" -> Items.KELP;
            case "caldo" -> ModItems.CALDO.get();
            case "cebolla" -> ModItems.CEBOLLA.get();
            case "carne" -> Items.BEEF;
            case "pollo" -> Items.CHICKEN;
            case "hierro" -> Items.IRON_INGOT;
            case "oro" -> Items.GOLD_INGOT;
            case "pan" -> Items.BREAD;
            case "manzana" -> Items.APPLE;
            default -> null;
        };
    }

    private static String getItemName(String id) {
        return switch (id) {
            case "fideos" -> "Fideos";
            case "alga" -> "Alga";
            case "caldo" -> "Caldo";
            case "cebolla" -> "Cebolla";
            case "carne" -> "Carne";
            case "pollo" -> "Pollo";
            case "hierro" -> "Lingote de hierro";
            case "oro" -> "Lingote de oro";
            case "pan" -> "Pan";
            case "manzana" -> "Manzana";
            default -> id;
        };
    }

    private static int getPrice(String id) {
        return switch (id) {
            case "hierro" -> 2;
            case "oro" -> 3;
            case "pollo" -> 2;
            case "pan" -> 4;
            case "manzana" -> 2;
            case "fideos" -> 1;
            case "alga" -> 1;
            case "caldo" -> 1;
            case "cebolla" -> 1;
            case "carne" -> 1;

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