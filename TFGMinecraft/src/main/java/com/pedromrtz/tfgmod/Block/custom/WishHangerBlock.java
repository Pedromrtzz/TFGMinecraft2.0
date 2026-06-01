package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.OpenChapter2QuizS2CPacket;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;

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
            sp.displayClientMessage(Component.literal("§7It is not the right time to hang your wish yet."), true);
            return InteractionResult.SUCCESS;
        }

        String wish = progress.getWish();

        if (wish == null || wish.isBlank()) {
            sp.displayClientMessage(Component.literal("§cYou must write your wish on the ema first."), true);
            return InteractionResult.SUCCESS;
        }

        spawnFloatingWishText(level, pos, wish);

        progress.setChapter2Task(8);

        ItemStack finalCard = new ItemStack(ModItems.CARD.get());
        CardItem.setCardId(finalCard, "card_japan_ema_wish");
        sp.addItem(finalCard);

        sp.displayClientMessage(Component.literal("§6You hung your wish at the temple."), false);
        sp.displayClientMessage(Component.literal("§eYour wish: §f\"" + wish + "\""), false);
        sp.displayClientMessage(Component.literal("§7Now answer the cultural test to complete Chapter 2."), false);

        level.playSound(
                null,
                pos,
                SoundEvents.PLAYER_LEVELUP,
                sp.getSoundSource(),
                1.0f,
                1.0f
        );

        ProgressSync.syncChapter1(sp);

        ModNetwork.CHANNEL.send(
                new OpenChapter2QuizS2CPacket(),
                PacketDistributor.PLAYER.with(sp)
        );

        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ignored) {}

            if (level.getServer() != null) {
                level.getServer().execute(() -> {
                    level.getServer().overworld().setDayTime(1000);
                });
            }
        }).start();

        return InteractionResult.SUCCESS;
    }

    private void spawnFloatingWishText(Level level, BlockPos pos, String wish) {
        ArmorStand text = new ArmorStand(
                level,
                pos.getX() + 0.5,
                pos.getY() + 1.4,
                pos.getZ() + 0.5
        );

        text.setInvisible(true);
        text.setNoGravity(true);
        text.setCustomName(Component.literal("§e\"" + wish + "\""));
        text.setCustomNameVisible(true);
        text.setSilent(true);
        text.setInvulnerable(true);
        text.addTag("tfg_wish_text");

        level.addFreshEntity(text);
    }
}