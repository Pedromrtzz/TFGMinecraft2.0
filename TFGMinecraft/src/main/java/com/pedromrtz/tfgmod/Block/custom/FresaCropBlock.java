package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;

public class FresaCropBlock extends CropBlock {

    public FresaCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.FRESA_SEMILLA.get();
    }
}

