package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.entity.custom.FatherEntity;
import com.pedromrtz.tfgmod.entity.custom.MotherEntity;
import com.pedromrtz.tfgmod.entity.custom.SisterEntity;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.OpenFamilyDinnerScreenS2CPacket;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

public class MesaBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);

    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public MesaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(STAGE, 0)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STAGE, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
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

        if (progress.getChapter2Task() == 4) {
            int stage = state.getValue(STAGE);

            if (stage == 0) {
                level.setBlock(pos, state.setValue(STAGE, 1), 3);
                progress.setChapter2TableStage(1);
                sp.displayClientMessage(Component.literal("§6Has colocado el cuenco principal."), true);
                playStepSound(level, pos, sp);

            } else if (stage == 1) {
                level.setBlock(pos, state.setValue(STAGE, 2), 3);
                progress.setChapter2TableStage(2);
                sp.displayClientMessage(Component.literal("§6Has colocado los palillos."), true);
                playStepSound(level, pos, sp);

            } else if (stage == 2) {
                level.setBlock(pos, state.setValue(STAGE, 3), 3);
                progress.setChapter2TableStage(3);
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

            } else {
                sp.displayClientMessage(Component.literal("§7La mesa ya está preparada."), true);
            }

            ProgressSync.syncChapter1(sp);
            return InteractionResult.SUCCESS;
        }

        if (progress.getChapter2Task() == 5) {

            teleportFamilyToTable(sp, pos);

            ModNetwork.CHANNEL.send(
                    new OpenFamilyDinnerScreenS2CPacket(),
                    PacketDistributor.PLAYER.with(sp)
            );

            progress.setChapter2Task(6);

            ItemStack cardStack = new ItemStack(ModItems.CARD.get());
            CardItem.setCardId(cardStack, "card_japan_omisoka_dinner");
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

            if (level.getServer() != null) {
                level.getServer().overworld().setDayTime(14000);
            }

            sp.displayClientMessage(
                    Component.literal("§7La noche cae sobre Sakura Town. Es hora de ir al templo."),
                    false
            );

            ProgressSync.syncChapter1(sp);
            return InteractionResult.SUCCESS;
        }

        sp.displayClientMessage(Component.literal("§7Ahora mismo no necesitas preparar la mesa."), true);
        return InteractionResult.SUCCESS;
    }

    private void teleportFamilyToTable(ServerPlayer sp, BlockPos tablePos) {
        var level = sp.serverLevel();

        for (Entity entity : level.getEntities().getAll()) {

            if (entity instanceof MotherEntity) {
                entity.teleportTo(
                        tablePos.getX() + 1.5,
                        tablePos.getY(),
                        tablePos.getZ() + 0.5
                );
            }

            if (entity instanceof FatherEntity) {
                entity.teleportTo(
                        tablePos.getX() - 1.5,
                        tablePos.getY(),
                        tablePos.getZ() + 0.5
                );
            }

            if (entity instanceof SisterEntity) {
                entity.teleportTo(
                        tablePos.getX() + 0.5,
                        tablePos.getY(),
                        tablePos.getZ() + 1.5
                );
            }
        }
    }

    private void playStepSound(Level level, BlockPos pos, ServerPlayer sp) {
        level.playSound(
                null,
                pos,
                SoundEvents.WOOD_PLACE,
                sp.getSoundSource(),
                0.8f,
                1.2f
        );
    }
}