package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.ModEntities;
import com.pedromrtz.tfgmod.entity.custom.ChefEntity;
import com.pedromrtz.tfgmod.entity.custom.ElderEntity;
import com.pedromrtz.tfgmod.entity.custom.FatherEntity;
import com.pedromrtz.tfgmod.entity.custom.SisterEntity;
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
    }
}