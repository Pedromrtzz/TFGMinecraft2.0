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

    // ===== CAPÍTULO 2 =====
    boolean isChapter2Active();
    void setChapter2Active(boolean v);

    boolean isChapter2Completed();
    void setChapter2Completed(boolean v);

    int getChapter2Task();
    void setChapter2Task(int task);

    int getChapter2TableStage();
    void setChapter2TableStage(int stage);

    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag tag);
}