package com.pedromrtz.tfgmod.Item;

import com.pedromrtz.tfgmod.entity.client.ChefDialogueScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChefBookItem extends Item {

    public ChefBookItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new ChefDialogueScreen());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}