package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;

public class ArrozCropBlock extends CropBlock {

    public ArrozCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.ARROZ_SEMILLA.get();
    }
}

