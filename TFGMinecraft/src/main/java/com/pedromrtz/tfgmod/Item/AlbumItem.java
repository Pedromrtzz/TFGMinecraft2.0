// com.pedromrtz.tfgmod.Item.AlbumItem
package com.pedromrtz.tfgmod.Item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AlbumItem extends Item {
    public AlbumItem(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(
                    new com.pedromrtz.tfgmod.entity.client.AlbumScreen()
            );
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
