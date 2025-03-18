package com.pedromrtz.tfgmod.entity;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.SillaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TFGMod.MOD_ID);

    public static final RegistryObject<EntityType<SillaEntity>> SILLA =
            ENTITY_TYPES.register("silla", () -> EntityType.Builder.of(SillaEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f) .build("silla_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
