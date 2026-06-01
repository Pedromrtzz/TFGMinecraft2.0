package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.custom.ArcheryInstructorEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArcheryInstructorRenderer extends HumanoidMobRenderer<ArcheryInstructorEntity, HumanoidModel<ArcheryInstructorEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "textures/entity/archery_instructor.png");

    public ArcheryInstructorRenderer(EntityRendererProvider.Context context) {
        super(
                context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f
        );
    }

    @Override
    public ResourceLocation getTextureLocation(ArcheryInstructorEntity entity) {
        return TEXTURE;
    }
}