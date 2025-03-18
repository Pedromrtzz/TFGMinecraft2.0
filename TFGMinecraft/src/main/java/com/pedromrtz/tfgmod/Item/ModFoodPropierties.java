package com.pedromrtz.tfgmod.Item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodPropierties {

    public static final FoodProperties RAMEN = new FoodProperties.Builder().nutrition(3).saturationModifier(0.2F)
            .effect(new MobEffectInstance(MobEffects.JUMP, 400), 0.20f).build();
}
