package com.pedromrtz.tfgmod.Block.custom;

import com.pedromrtz.tfgmod.Item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;

public class TeVerdeCropBlock extends CropBlock {

    public TeVerdeCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.TEVERDE_SEMILLA.get();
    }
}

