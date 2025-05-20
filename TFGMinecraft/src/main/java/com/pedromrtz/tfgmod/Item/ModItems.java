package com.pedromrtz.tfgmod.Item;

import com.pedromrtz.tfgmod.Block.ModBlocks;
import com.pedromrtz.tfgmod.Block.custom.GalletaSuerteItem;
import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TFGMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

        public static final RegistryObject<Item> RAMEN = ITEMS.register("ramen",
                () -> new BlockItem(ModBlocks.RAMEN.get(), new Item.Properties()
                        .food(ModFoodPropierties.RAMEN))
        );

    // RAMEN STALL

        // RAMEN

            public static final RegistryObject<Item> MORTERO = ITEMS.register("mortero",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> HARINA = ITEMS.register("harina",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> SAL = ITEMS.register("sal",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> PASTA = ITEMS.register("pasta",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> FIDEOS = ITEMS.register("fideos",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> CALDO = ITEMS.register("caldo",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO))
            );

        // RAMEN DE POLLO

            public static final RegistryObject<Item> CALDO_POLLO = ITEMS.register("caldo_pollo",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO_POLLO))
            );

            public static final RegistryObject<Item> RAMEN_POLLO = ITEMS.register("ramen_pollo",
                    () -> new BlockItem(ModBlocks.RAMEN_POLLO.get(), new Item.Properties()
                            .food(ModFoodPropierties.RAMEN_POLLO))
            );

        // RAMEN DE CARNE

            public static final RegistryObject<Item> CALDO_CARNE = ITEMS.register("caldo_carne",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO_CARNE))
            );

            public static final RegistryObject<Item> RAMEN_CARNE = ITEMS.register("ramen_carne",
                    () -> new BlockItem(ModBlocks.RAMEN_CARNE.get(), new Item.Properties()
                            .food(ModFoodPropierties.RAMEN_CARNE))
            );



        // RAMEN DE PESCADO

            public static final RegistryObject<Item> CALDO_PESCADO = ITEMS.register("caldo_pescado",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO_PESCADO))
            );

            public static final RegistryObject<Item> RAMEN_PESCADO = ITEMS.register("ramen_pescado",
                    () -> new BlockItem(ModBlocks.RAMEN_PESCADO.get(), new Item.Properties()
                            .food(ModFoodPropierties.RAMEN_PESCADO))
            );

    // OKONOMIYAKI

        // OKONOMIYAKI POLLO

            public static final RegistryObject<Item> OKONOMIYAKI_POLLO = ITEMS.register("okonomiyaki_pollo",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.OKONOMIYAKI_POLLO))
            );

        // OKONOMIYAKI CARNE

            public static final RegistryObject<Item> OKONOMIYAKI_CARNE = ITEMS.register("okonomiyaki_carne",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.OKONOMIYAKI_CARNE))
            );

        // OKONOMIYAKI VERDURAS

            public static final RegistryObject<Item> OKONOMIYAKI_VERDURAS = ITEMS.register("okonomiyaki_verduras",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.OKONOMIYAKI_VERDURAS))
            );

    // SUSHI BAR

        // DUMPLING

            public static final RegistryObject<Item> MASA = ITEMS.register("masa",
                    () -> new Item(new Item.Properties())
            );


            public static final RegistryObject<Item> DUMPLING = ITEMS.register("dumpling",
                    () -> new BlockItem(ModBlocks.DUMPLING.get(), new Item.Properties()
                            .food(ModFoodPropierties.DUMPLING))
            );


        // SUSHI

            public static final RegistryObject<Item> ARROZ = ITEMS.register("arroz",
                        () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> SUSHI = ITEMS.register("sushi",
                    () -> new BlockItem(ModBlocks.SUSHI.get(), new Item.Properties()
                            .food(ModFoodPropierties.SUSHI))
            );

    // GREEN TEA SHOP

        // TE MATCHA

            public static final RegistryObject<Item> TAZA = ITEMS.register("taza",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> TE = ITEMS.register("te",
                    () -> new Item(new Item.Properties())
            );

            public static final RegistryObject<Item> TE_MATCHA = ITEMS.register("te_matcha",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.TE_MATCHA))
            );

        // MOCHI

            public static final RegistryObject<Item> PASTA_ARROZ = ITEMS.register("pasta_arroz",
                    () -> new Item(new Item.Properties())
            );

        // MOCHI VERDE

            public static final RegistryObject<Item> MOCHI_VERDE = ITEMS.register("mochi_verde",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.MOCHI_VERDE))
            );

        // MOCHI FRESA

            public static final RegistryObject<Item> FRESA = ITEMS.register("fresa",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.FRESA))
            );

            public static final RegistryObject<Item> MOCHI_FRESA = ITEMS.register("mochi_fresa",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.MOCHI_FRESA))
            );

        // MOCHI CHOCOLATE

            public static final RegistryObject<Item> CHOCOLATE = ITEMS.register("chocolate",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CHOCOLATE))
            );

            public static final RegistryObject<Item> MOCHI_CHOCOLATE = ITEMS.register("mochi_chocolate",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.MOCHI_CHOCOLATE))
            );

        // MOCHI PLATANO

            public static final RegistryObject<Item> PLATANO = ITEMS.register("platano",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.PLATANO))
            );

            public static final RegistryObject<Item> MOCHI_PLATANO = ITEMS.register("mochi_platano",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.MOCHI_PLATANO))
            );

    // YAKITORY SHOP

            public static final RegistryObject<Item> TERIYAKI = ITEMS.register("teriyaki",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.TERIYAKI))
            );

            public static final RegistryObject<Item> CEBOLLA = ITEMS.register("cebolla",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CEBOLLA))
            );

            public static final RegistryObject<Item> BROCHETA_POLLO = ITEMS.register("brocheta_pollo",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.BROCHETA_POLLO))
            );

    // JAPANESE POT RICE

        // ARROZ CON POLLO

            public static final RegistryObject<Item> ARROZ_POLLO = ITEMS.register("arroz_pollo",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.ARROZ_POLLO))
            );

        // ARROZ CON VERDURAS

            public static final RegistryObject<Item> ARROZ_VERDURAS = ITEMS.register("arroz_verduras",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.ARROZ_VERDURAS))
            );

        // ARROZ FRITO

            public static final RegistryObject<Item> ARROZ_FRITO = ITEMS.register("arroz_frito",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.ARROZ_FRITO))
            );

        // ONIGIRI

            public static final RegistryObject<Item> ONIGIRI = ITEMS.register("onigiri",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.ONIGIRI))
            );


        public static final RegistryObject<Item> CEBOLLA_SEMILLA = ITEMS.register("cebolla_semilla",
                () -> new BlockItem(ModBlocks.CEBOLLA_CULTIVO.get(), new Item.Properties())
        );

        public static final RegistryObject<Item> FRESA_SEMILLA = ITEMS.register("fresa_semilla",
                () -> new BlockItem(ModBlocks.FRESA_CULTIVO.get(), new Item.Properties())
        );

        public static final RegistryObject<Item> ARROZ_SEMILLA = ITEMS.register("arroz_semilla",
                () -> new BlockItem(ModBlocks.ARROZ_CULTIVO.get(), new Item.Properties())
        );

        public static final RegistryObject<Item> TEVERDE_SEMILLA = ITEMS.register("teverde_semilla",
                () -> new BlockItem(ModBlocks.TEVERDE_CULTIVO.get(), new Item.Properties())
        );

        public static final RegistryObject<Item> CHOCOLATE_POLVO = ITEMS.register("chocolate_polvo",
                () -> new Item(new Item.Properties())
        );

        public static final RegistryObject<Item> GALLETA_SUERTE = ITEMS.register("galleta_suerte",
                () -> new GalletaSuerteItem(new Item.Properties())
        );








}
