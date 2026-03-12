package com.pedromrtz.tfgmod.entity;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<EntityType<ChefEntity>> CHEF =
            ENTITY_TYPES.register("chef", () ->
                    EntityType.Builder.<ChefEntity>of(ChefEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f) // tamaño tipo jugador
                            .build("tfgmod:chef")
            );

    public static final RegistryObject<EntityType<ElderEntity>> ELDER =
            ENTITY_TYPES.register("elder", () ->
                    EntityType.Builder.<ElderEntity>of(ElderEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .build("tfgmod:elder")
            );

    public static final RegistryObject<EntityType<FatherEntity>> FATHER =
            ENTITY_TYPES.register("father", () ->
                    EntityType.Builder.<FatherEntity>of(FatherEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:father")
            );

    public static final RegistryObject<EntityType<SisterEntity>> SISTER =
            ENTITY_TYPES.register("sister", () ->
                    EntityType.Builder.<SisterEntity>of(SisterEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:sister")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
