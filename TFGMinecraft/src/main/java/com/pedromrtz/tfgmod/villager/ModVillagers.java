package com.pedromrtz.tfgmod.villager;

import com.google.common.collect.ImmutableSet;
import com.pedromrtz.tfgmod.Block.ModBlocks;
import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, TFGMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, TFGMod.MOD_ID);

    public static final RegistryObject<PoiType> RAMEN_POI = POI_TYPES.register("ramen_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.RAMEN.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final RegistryObject<VillagerProfession> RAMEN = VILLAGER_PROFESSIONS.register("ramen",
            () -> new VillagerProfession("ramen", holder -> holder.value() == RAMEN_POI.get(),
                    holder -> holder.value() == RAMEN_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundType.AMETHYST.getBreakSound()));

    public static final RegistryObject<PoiType> ARROZ_POI = POI_TYPES.register("arroz_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.VITROCERAMICA.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final RegistryObject<VillagerProfession> ARROZ = VILLAGER_PROFESSIONS.register("arroz",
            () -> new VillagerProfession("arroz", holder -> holder.value() == ARROZ_POI.get(),
                    holder -> holder.value() == ARROZ_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundType.AMETHYST.getBreakSound()));

    public static final RegistryObject<PoiType> OKONOMIYAKI_POI = POI_TYPES.register("okonomiyaki_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.ARMARIO.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final RegistryObject<VillagerProfession> OKONOMIYAKI = VILLAGER_PROFESSIONS.register("okonomiyaki",
            () -> new VillagerProfession("okonomiyaki", holder -> holder.value() == OKONOMIYAKI_POI.get(),
                    holder -> holder.value() == OKONOMIYAKI_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundType.AMETHYST.getBreakSound()));

    public static final RegistryObject<PoiType> SUSHI_POI = POI_TYPES.register("sushi_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SUSHI.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final RegistryObject<VillagerProfession> SUSHI = VILLAGER_PROFESSIONS.register("sushi",
            () -> new VillagerProfession("sushi", holder -> holder.value() == SUSHI_POI.get(),
                    holder -> holder.value() == SUSHI_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundType.AMETHYST.getBreakSound()));

    public static final RegistryObject<PoiType> MOCHI_POI = POI_TYPES.register("mochi_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.MOCHI_MATCHA.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final RegistryObject<VillagerProfession> MOCHI = VILLAGER_PROFESSIONS.register("mochi",
            () -> new VillagerProfession("mochi", holder -> holder.value() == MOCHI_POI.get(),
                    holder -> holder.value() == MOCHI_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundType.AMETHYST.getBreakSound()));




    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
