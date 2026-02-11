package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.ElderEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ElderRenderer extends MobRenderer<ElderEntity, HumanoidModel<ElderEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "textures/entity/elder.png");

    public ElderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ElderEntity entity) {
        return TEXTURE;
    }
}