package com.pedromrtz.tfgmod.Block;

import com.pedromrtz.tfgmod.Block.custom.SillaBlock;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TFGMod.MOD_ID);

    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    public static final RegistryObject<Block> EJEMPLO = registerBlock("ejemploblock",
            () -> new Block(Block.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)
            ));

    public static final RegistryObject<Block> SILLA = registerBlock("silla",
            () -> new SillaBlock(BlockBehaviour.Properties.of().noOcclusion()));


}
