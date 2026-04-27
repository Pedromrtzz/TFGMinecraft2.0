package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
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
import net.minecraft.world.item.ItemStack;

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
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer sp)) {
            return InteractionResult.SUCCESS;
        }

        var progress = Chapter1ProgressUtil.get(sp);

        if (!progress.isChapter2Active()) {
            sp.displayClientMessage(Component.literal("§7Ahora mismo no necesitas interactuar con la mesa."), true);
            return InteractionResult.SUCCESS;
        }

        // ===== TAREA 4: PREPARAR LA MESA =====
        if (progress.getChapter2Task() == 4) {
            int stage = progress.getChapter2TableStage();

            switch (stage) {
                case 0 -> {
                    progress.setChapter2TableStage(1);
                    sp.displayClientMessage(Component.literal("§6Has colocado el cuenco principal."), true);
                }
                case 1 -> {
                    progress.setChapter2TableStage(2);
                    sp.displayClientMessage(Component.literal("§6Has colocado los palillos."), true);
                }
                case 2 -> {
                    progress.setChapter2TableStage(3);
                    sp.displayClientMessage(Component.literal("§6Has colocado la bebida."), true);
                }
                case 3 -> {
                    progress.setChapter2TableStage(4);
                    progress.setChapter2Task(5);

                    sp.displayClientMessage(Component.literal("§6¡La mesa está lista para la cena familiar!"), true);

                    level.playSound(
                            null,
                            pos,
                            SoundEvents.PLAYER_LEVELUP,
                            sp.getSoundSource(),
                            1.0f,
                            1.0f
                    );
                }
                default -> sp.displayClientMessage(Component.literal("§7La mesa ya está preparada."), true);
            }

            ProgressSync.syncChapter1(sp);
            return InteractionResult.SUCCESS;
        }

        // ===== TAREA 5: CENAR CON LA FAMILIA =====
        if (progress.getChapter2Task() == 5) {
            progress.setChapter2Task(6);

            // Cromo de prueba para la cena familiar
            ItemStack cardStack = new ItemStack(ModItems.CARD.get());
            CardItem.setCardId(cardStack, "card_japan_fushimi_inari");
            sp.addItem(cardStack);

            sp.displayClientMessage(Component.literal("§6Has cenado con la familia. Has conseguido un nuevo cromo."), true);

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

        sp.displayClientMessage(Component.literal("§7Ahora mismo no necesitas preparar la mesa."), true);
        return InteractionResult.SUCCESS;
    }
}