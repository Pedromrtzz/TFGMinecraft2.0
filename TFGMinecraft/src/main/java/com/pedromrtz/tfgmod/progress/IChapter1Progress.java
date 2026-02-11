package com.pedromrtz.tfgmod.progress;

import net.minecraft.nbt.CompoundTag;

public interface IChapter1Progress {
    boolean hasAlbum();
    void setHasAlbum(boolean v);

    boolean isMission1Active();
    void setMission1Active(boolean v);

    boolean isMission1Completed();
    void setMission1Completed(boolean v);

    boolean hasFamilyCard();
    void setHasFamilyCard(boolean v);

    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag tag);
}