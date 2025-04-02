package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.ModItems;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.item.Item;

public class OnionCropBlock extends CropBlock {

    public OnionCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 2; // Solo 3 etapas: 0, 1 y 2
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.CEBOLLA_SEMILLA.get();
    }
}

