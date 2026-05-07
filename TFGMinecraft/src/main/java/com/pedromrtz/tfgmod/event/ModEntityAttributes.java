package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.ModEntities;
import com.pedromrtz.tfgmod.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CHEF.get(), ChefEntity.createAttributes().build());
        event.put(ModEntities.ELDER.get(), ElderEntity.createAttributes().build());
        event.put(ModEntities.FATHER.get(), FatherEntity.createAttributes().build());
        event.put(ModEntities.SISTER.get(), SisterEntity.createAttributes().build());
        event.put(ModEntities.AMBIENT_NPC.get(), AmbientNPCEntity.createAttributes().build());
        event.put(ModEntities.MOTHER.get(), MotherEntity.createAttributes().build());
        event.put(ModEntities.MONK.get(), MonkEntity.createAttributes().build());
        event.put(ModEntities.MERCHANT.get(), MerchantEntity.createAttributes().build());
        event.put(ModEntities.ITAMAE.get(), ItamaeEntity.createAttributes().build());
        event.put(ModEntities.FISH_MERCHANT.get(), FishMerchantEntity.createAttributes().build());
        event.put(ModEntities.FESTIVAL_ORGANIZER.get(), FestivalOrganizerEntity.createAttributes().build());
        event.put(ModEntities.ELECTRICIAN.get(), ElectricianEntity.createAttributes().build());
        event.put(ModEntities.GOLDFISH_SELLER.get(), GoldfishSellerEntity.createAttributes().build());
        event.put(ModEntities.TARGET_ATTENDANT.get(), TargetAttendantEntity.createAttributes().build());
        event.put(ModEntities.LANTERN_KEEPER.get(), LanternKeeperEntity.createAttributes().build());
        event.put(ModEntities.SAMURAI_GUARD.get(), SamuraiGuardEntity.createAttributes().build());
        event.put(ModEntities.ARCHERY_INSTRUCTOR.get(), ArcheryInstructorEntity.createAttributes().build());
        event.put(ModEntities.CASTLE_GATEKEEPER.get(), CastleGatekeeperEntity.createAttributes().build());
    }
}