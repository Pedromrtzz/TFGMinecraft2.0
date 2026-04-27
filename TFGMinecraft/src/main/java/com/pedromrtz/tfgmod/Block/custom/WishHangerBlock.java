package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WishHangerBlock extends Block {

    public WishHangerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer sp)) {
            return InteractionResult.SUCCESS;
        }

        var progress = Chapter1ProgressUtil.get(sp);

        if (!progress.isChapter2Active() || progress.getChapter2Task() != 8) {
            sp.displayClientMessage(Component.literal("§7Todavía no es el momento de colgar tu deseo."), true);
            return InteractionResult.SUCCESS;
        }

        String wish = progress.getWish();

        if (wish == null || wish.isBlank()) {
            sp.displayClientMessage(Component.literal("§cPrimero debes escribir tu deseo en el ema."), false);
            return InteractionResult.SUCCESS;
        }

        progress.setChapter2Completed(true);
        progress.setChapter2Active(false);
        progress.setChapter2Task(8);

        ItemStack finalCard = new ItemStack(ModItems.CARD.get());
        CardItem.setCardId(finalCard, "card_japan_fushimi_inari");
        sp.addItem(finalCard);

        sp.displayClientMessage(Component.literal("§6Has colgado tu deseo en el templo."), false);
        sp.displayClientMessage(Component.literal("§eTu deseo: §f\"" + wish + "\""), false);
        sp.displayClientMessage(Component.literal("§aCapítulo 2 completado. Has recibido un cromo final."), false);

        level.playSound(
                null,
                pos,
                SoundEvents.PLAYER_LEVELUP,
                sp.getSoundSource(),
                1.0f,
                1.0f
        );

        ProgressSync.syncChapter1(sp);
        return InteractionResult.SUCCESS;
    }
}