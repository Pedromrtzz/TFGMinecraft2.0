package com.pedromrtz.tfgmod.Item;

import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.world.item.Item;
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

    public static final RegistryObject<Item> EJEMPLO = ITEMS.register("ejemplo",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> RAMEN = ITEMS.register("ramen",
            () -> new Item(new Item.Properties().food(ModFoodPropierties.RAMEN))
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
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.RAMEN_POLLO))
            );

        // RAMEN DE CARNE

            public static final RegistryObject<Item> CALDO_CARNE = ITEMS.register("caldo_carne",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO_CARNE))
            );

            public static final RegistryObject<Item> RAMEN_CARNE = ITEMS.register("ramen_carne",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.RAMEN_CARNE))
            );

        // RAMEN DE PESCADO

            public static final RegistryObject<Item> CALDO_PESCADO = ITEMS.register("caldo_pescado",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.CALDO_PESCADO))
            );

            public static final RegistryObject<Item> RAMEN_PESCADO = ITEMS.register("ramen_pescado",
                    () -> new Item(new Item.Properties().food(ModFoodPropierties.RAMEN_PESCADO))
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













}
