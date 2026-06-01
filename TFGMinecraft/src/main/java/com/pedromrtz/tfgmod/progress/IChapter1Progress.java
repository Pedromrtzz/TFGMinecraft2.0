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

    boolean isChapter2Active();
    void setChapter2Active(boolean v);

    boolean isChapter2Completed();
    void setChapter2Completed(boolean v);

    int getChapter2Task();
    void setChapter2Task(int task);

    int getChapter2TableStage();
    void setChapter2TableStage(int stage);

    String getWish();

    void setWish(String wish);

    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag tag);

    int getChapter2BellCount();
    void setChapter2BellCount(int count);

    boolean isChapter3Active();
    void setChapter3Active(boolean v);

    boolean isChapter3Completed();
    void setChapter3Completed(boolean v);

    int getChapter3Task();
    void setChapter3Task(int task);

    boolean isChapter4Active();
    void setChapter4Active(boolean v);

    boolean isChapter4Completed();
    void setChapter4Completed(boolean v);

    int getChapter4Task();
    void setChapter4Task(int task);

    boolean isChapter5Active();
    void setChapter5Active(boolean v);

    boolean isChapter5Completed();
    void setChapter5Completed(boolean v);

    int getChapter5Task();
    void setChapter5Task(int task);

}