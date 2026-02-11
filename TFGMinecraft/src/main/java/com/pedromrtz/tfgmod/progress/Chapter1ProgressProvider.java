package com.pedromrtz.tfgmod.progress;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class Chapter1ProgressProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "chapter1_progress");

    public static final Capability<IChapter1Progress> CHAPTER1_PROGRESS =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final Chapter1Progress backend = new Chapter1Progress();
    private final LazyOptional<IChapter1Progress> optional = LazyOptional.of(() -> backend);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == CHAPTER1_PROGRESS ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }
}