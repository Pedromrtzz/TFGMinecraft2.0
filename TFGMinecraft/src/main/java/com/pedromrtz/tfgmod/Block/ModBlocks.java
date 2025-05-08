package com.pedromrtz.tfgmod.Block;

import com.pedromrtz.tfgmod.Block.custom.*;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

    public static final RegistryObject<Block> VITROCERAMICA = registerBlock("vitroceramica",
            () -> new VitroceramicaBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> OLLA = registerBlock("olla",
            () -> new OllaBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> SARTEN = registerBlock("sarten",
            () -> new SartenBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> ARMARIO = registerBlock("armario",
            () -> new ArmarioBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> ESPECIAS = registerBlock("especias",
            () -> new EspeciasBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> BALDA = registerBlock("balda",
            () -> new BaldaBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> BASURA = registerBlock("basura",
            () -> new BasuraBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> ENCIMERA = registerBlock("encimera",
            () -> new EncimeraBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> FREGADERO = registerBlock("fregadero",
            () -> new FregaderoBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> NEVERA = registerBlock("nevera",
            () -> new NeveraBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> EXTRACTOR = registerBlock("extractor",
            () -> new ExtractorBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> MICROONDAS = registerBlock("microondas",
            () -> new MicroondasBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> MESA = registerBlock("mesa",
            () -> new MicroondasBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> BOTE = registerBlock("bote",
            () -> new BoteBlock(BlockBehaviour.Properties.of().noOcclusion()));




    public static final RegistryObject<Block> SUSHI = registerBlock("sushi_bloque",
            () -> new SushiBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> RAMEN = registerBlock("ramen_bloque",
            () -> new RamenBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<Block> DUMPLING = registerBlock("dumpling_bloque",
            () -> new DumplingBlock(BlockBehaviour.Properties.of().noOcclusion()));




    public static final RegistryObject<Block> CEBOLLA_CULTIVO = BLOCKS.register("cebolla_cultivo",
            () -> new OnionCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noCollission().randomTicks().instabreak())
    );

    public static final RegistryObject<Block> FRESA_CULTIVO = BLOCKS.register("fresa_cultivo",
            () -> new FresaCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noCollission().randomTicks().instabreak())
    );

    public static final RegistryObject<Block> ARROZ_CULTIVO = BLOCKS.register("arroz_cultivo",
            () -> new ArrozCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noCollission().randomTicks().instabreak())
    );

    public static final RegistryObject<Block> TEVERDE_CULTIVO = BLOCKS.register("teverde_cultivo",
            () -> new TeVerdeCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noCollission().randomTicks().instabreak())
    );



}
