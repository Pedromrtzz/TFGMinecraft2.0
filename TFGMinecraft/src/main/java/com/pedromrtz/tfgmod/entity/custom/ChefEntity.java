package com.pedromrtz.tfgmod.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChefEntity extends PathfinderMob {
    public ChefEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    // ---------- IA: que se quede quieto, mire al jugador y al azar ----------
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // Mira al jugador cercano
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        // Mueve un poco la cabeza
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    // ---------- Atributos básicos ----------
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)      // 20 de vida
                .add(Attributes.MOVEMENT_SPEED, 0.25D)  // velocidad
                .add(Attributes.FOLLOW_RANGE, 16.0D);   // rango (por si acaso)
    }

    // ---------- Click derecho: abrir diálogo del chef ----------
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level().isClientSide) {
            // Abre tu pantalla de diálogo del chef
            Minecraft.getInstance().setScreen(
                    new com.pedromrtz.tfgmod.entity.client.ChefDialogueScreen()
            );
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    protected Component getTypeName() {
        // Nombre que sale si le pones etiqueta, etc.
        return Component.literal("Chef de sushi");
    }
}