package com.pedromrtz.tfgmod.Item;

import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.OpenScreenS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class EmaItem extends Item {

    public EmaItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer sp) {

            ModNetwork.CHANNEL.send(
                    new OpenScreenS2CPacket(OpenScreenS2CPacket.ScreenType.DESIRE_SCREEN),
                    PacketDistributor.PLAYER.with(sp)
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}