// com.pedromrtz.tfgmod.entity.client.CardUtils
package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.Item.CardItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CardUtils {
    public static boolean playerHasCard(Player p, String cardId) {
        for (ItemStack s : p.getInventory().items) {
            if (!s.isEmpty() && s.getItem() instanceof com.pedromrtz.tfgmod.Item.CardItem) {
                String id = CardItem.getCardId(s);
                if (cardId.equals(id)) return true;
            }
        }
        return false;
    }
}