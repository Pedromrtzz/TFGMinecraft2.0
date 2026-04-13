package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.entity.custom.AmbientNPCEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AmbientNPCRenderer extends MobRenderer<AmbientNPCEntity, HumanoidModel<AmbientNPCEntity>> {

    public AmbientNPCRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(AmbientNPCEntity entity) {
        return AmbientNPCData.get(entity.getNpcId()).texture();
    }
}