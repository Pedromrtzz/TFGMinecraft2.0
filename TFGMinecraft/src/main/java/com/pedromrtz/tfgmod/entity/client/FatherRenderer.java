package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.FatherEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FatherRenderer extends MobRenderer<FatherEntity, HumanoidModel<FatherEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "textures/entity/father.png");

    public FatherRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FatherEntity entity) {
        return TEXTURE;
    }
}