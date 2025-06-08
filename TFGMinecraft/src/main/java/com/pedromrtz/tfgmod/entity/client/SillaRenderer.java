package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.entity.custom.SillaEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SillaRenderer extends EntityRenderer<SillaEntity> {

    public SillaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(SillaEntity pEntity) {
        return null;
    }

    @Override
    public boolean shouldRender(SillaEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
