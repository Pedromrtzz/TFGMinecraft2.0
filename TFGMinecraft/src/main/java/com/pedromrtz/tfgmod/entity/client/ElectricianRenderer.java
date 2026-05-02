package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.ElectricianEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ElectricianRenderer extends MobRenderer<ElectricianEntity, HumanoidModel<ElectricianEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "textures/entity/electrician.png");

    public ElectricianRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricianEntity entity) {
        return TEXTURE;
    }
}