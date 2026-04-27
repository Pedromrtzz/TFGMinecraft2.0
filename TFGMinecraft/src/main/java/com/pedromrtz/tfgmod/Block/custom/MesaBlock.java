package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MesaBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public MesaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        System.out.println("CLICK EN MESA");
        System.out.println("level.isClientSide = " + level.isClientSide);

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer sp)) {
            System.out.println("NO ES SERVER PLAYER");
            return InteractionResult.SUCCESS;
        }

        var progress = Chapter1ProgressUtil.get(sp);

        System.out.println("chapter2Active = " + progress.isChapter2Active());
        System.out.println("chapter2Task = " + progress.getChapter2Task());
        System.out.println("chapter2TableStage = " + progress.getChapter2TableStage());

        if (!progress.isChapter2Active() || progress.getChapter2Task() != 4) {
            sp.displayClientMessage(Component.literal("§7Ahora mismo no necesitas preparar la mesa."), false);
            return InteractionResult.SUCCESS;
        }

        int stage = progress.getChapter2TableStage();

        switch (stage) {
            case 0 -> {
                progress.setChapter2TableStage(1);
                sp.displayClientMessage(Component.literal("§6Has colocado el cuenco principal."), false);
            }
            case 1 -> {
                progress.setChapter2TableStage(2);
                sp.displayClientMessage(Component.literal("§6Has colocado los palillos."), false);
            }
            case 2 -> {
                progress.setChapter2TableStage(3);
                sp.displayClientMessage(Component.literal("§6Has colocado la bebida."), false);
            }
            case 3 -> {
                progress.setChapter2TableStage(4);
                progress.setChapter2Task(5);

                sp.displayClientMessage(Component.literal("§6¡La mesa está lista para la cena familiar!"), false);

                level.playSound(
                        null,
                        pos,
                        SoundEvents.PLAYER_LEVELUP,
                        sp.getSoundSource(),
                        1.0f,
                        1.0f
                );
            }
            default -> {
                sp.displayClientMessage(Component.literal("§7La mesa ya está preparada."), false);
            }
        }

        ProgressSync.syncChapter1(sp);
        return InteractionResult.SUCCESS;
    }
}