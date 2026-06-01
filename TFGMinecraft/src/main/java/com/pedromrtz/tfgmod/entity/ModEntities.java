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

    public static final RegistryObject<EntityType<AmbientNPCEntity>> AMBIENT_NPC =
            ENTITY_TYPES.register("ambient_npc", () ->
                    EntityType.Builder.<AmbientNPCEntity>of(AmbientNPCEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:ambient_npc")
            );

    public static final RegistryObject<EntityType<MotherEntity>> MOTHER =
            ENTITY_TYPES.register("mother", () ->
                    EntityType.Builder.<MotherEntity>of(MotherEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:mother")
            );

    public static final RegistryObject<EntityType<MonkEntity>> MONK =
            ENTITY_TYPES.register("monk", () ->
                    EntityType.Builder.<MonkEntity>of(MonkEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:monk")
            );

    public static final RegistryObject<EntityType<MerchantEntity>> MERCHANT =
            ENTITY_TYPES.register("merchant", () ->
                    EntityType.Builder.<MerchantEntity>of(MerchantEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:merchant")
            );

    public static final RegistryObject<EntityType<ItamaeEntity>> ITAMAE =
            ENTITY_TYPES.register("itamae", () ->
                    EntityType.Builder.<ItamaeEntity>of(ItamaeEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:itamae")
            );

    public static final RegistryObject<EntityType<FishMerchantEntity>> FISH_MERCHANT =
            ENTITY_TYPES.register("fish_merchant", () ->
                    EntityType.Builder.<FishMerchantEntity>of(FishMerchantEntity::new, MobCategory.MISC)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("tfgmod:fish_merchant")
            );

    public static final RegistryObject<EntityType<FestivalOrganizerEntity>> FESTIVAL_ORGANIZER =
            ENTITY_TYPES.register("festival_organizer",
                    () -> EntityType.Builder.of(FestivalOrganizerEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("festival_organizer"));

    public static final RegistryObject<EntityType<ElectricianEntity>> ELECTRICIAN =
            ENTITY_TYPES.register("electrician",
                    () -> EntityType.Builder.of(ElectricianEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("electrician"));

    public static final RegistryObject<EntityType<GoldfishSellerEntity>> GOLDFISH_SELLER =
            ENTITY_TYPES.register("goldfish_seller",
                    () -> EntityType.Builder.of(GoldfishSellerEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("goldfish_seller"));

    public static final RegistryObject<EntityType<TargetAttendantEntity>> TARGET_ATTENDANT =
            ENTITY_TYPES.register("target_attendant",
                    () -> EntityType.Builder.of(TargetAttendantEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("target_attendant"));

    public static final RegistryObject<EntityType<LanternKeeperEntity>> LANTERN_KEEPER =
            ENTITY_TYPES.register("lantern_keeper",
                    () -> EntityType.Builder.of(LanternKeeperEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("lantern_keeper"));

    public static final RegistryObject<EntityType<SamuraiGuardEntity>> SAMURAI_GUARD =
            ENTITY_TYPES.register("samurai_guard",
                    () -> EntityType.Builder.of(SamuraiGuardEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("samurai_guard"));

    public static final RegistryObject<EntityType<ArcheryInstructorEntity>> ARCHERY_INSTRUCTOR =
            ENTITY_TYPES.register("archery_instructor",
                    () -> EntityType.Builder.of(ArcheryInstructorEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("archery_instructor"));

    public static final RegistryObject<EntityType<DaimyoEntity>> DAIMYO =
            ENTITY_TYPES.register("daimyo",
                    () -> EntityType.Builder.of(DaimyoEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("daimyo"));

    public static final RegistryObject<EntityType<CastleGatekeeperEntity>> CASTLE_GATEKEEPER =
            ENTITY_TYPES.register("castle_gatekeeper",
                    () -> EntityType.Builder.of(CastleGatekeeperEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("castle_gatekeeper"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
