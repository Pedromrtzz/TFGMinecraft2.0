package com.pedromrtz.tfgmod.Item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;

public class ModFoodPropierties {

    public static final FoodProperties RAMEN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.2F)
            .effect(new MobEffectInstance(MobEffects.JUMP, 400), 0.20f).build();

    public static final FoodProperties CALDO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties CALDO_POLLO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties RAMEN_POLLO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties CALDO_CARNE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties RAMEN_CARNE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties CALDO_PESCADO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties RAMEN_PESCADO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties OKONOMIYAKI_POLLO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties OKONOMIYAKI_CARNE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties OKONOMIYAKI_VERDURAS = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties DUMPLING = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties SUSHI = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties TE_MATCHA = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();


    public static final FoodProperties MOCHI_VERDE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties FRESA = new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F)
            .build();

    public static final FoodProperties MOCHI_FRESA = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties CHOCOLATE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties MOCHI_CHOCOLATE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties PLATANO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();

    public static final FoodProperties MOCHI_PLATANO = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F)
            .build();
}
