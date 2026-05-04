package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.GoldfishSellerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GoldfishSellerRenderer extends MobRenderer<GoldfishSellerEntity, HumanoidModel<GoldfishSellerEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "textures/entity/goldfish_seller.png");

    public GoldfishSellerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldfishSellerEntity entity) {
        return TEXTURE;
    }
}