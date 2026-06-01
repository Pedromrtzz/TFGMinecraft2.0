package com.pedromrtz.tfgmod.capitulo2;

import com.pedromrtz.tfgmod.Item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Chapter2IngredientHelper {

    public static boolean hasAllIngredients(ServerPlayer player) {
        return hasAtLeast(player, ModItems.FIDEOS.get(), 1)
                && hasAtLeast(player, Items.KELP, 1)
                && hasAtLeast(player, ModItems.CALDO.get(), 1)
                && hasAtLeast(player, ModItems.CEBOLLA.get(), 1)
                && hasAtLeast(player, Items.BEEF, 1);
    }

    public static List<String> getMissingIngredients(ServerPlayer player) {
        List<String> missing = new ArrayList<>();

        if (!hasAtLeast(player, ModItems.FIDEOS.get(), 1)) missing.add("fideos");
        if (!hasAtLeast(player, Items.KELP, 1)) missing.add("alga");
        if (!hasAtLeast(player, ModItems.CALDO.get(), 1)) missing.add("caldo");
        if (!hasAtLeast(player, ModItems.CEBOLLA.get(), 1)) missing.add("cebolla");
        if (!hasAtLeast(player, Items.BEEF, 1)) missing.add("carne");

        return missing;
    }

    public static void consumeIngredients(ServerPlayer player) {
        removeItems(player, ModItems.FIDEOS.get(), 1);
        removeItems(player, Items.KELP, 1);
        removeItems(player, ModItems.CALDO.get(), 1);
        removeItems(player, ModItems.CEBOLLA.get(), 1);
        removeItems(player, Items.BEEF, 1);
    }

    private static boolean hasAtLeast(ServerPlayer player, Item item, int amount) {
        int total = 0;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                total += stack.getCount();
                if (total >= amount) return true;
            }
        }

        return false;
    }

    private static void removeItems(ServerPlayer player, Item item, int amount) {
        int remaining = amount;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);

            if (!stack.is(item)) continue;

            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;

            if (remaining <= 0) break;
        }

        player.getInventory().setChanged();
    }
}