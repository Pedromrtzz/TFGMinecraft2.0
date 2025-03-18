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



}
