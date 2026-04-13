package com.pedromrtz.tfgmod.progress;

import net.minecraft.nbt.CompoundTag;

public class Chapter1Progress implements IChapter1Progress {
    private boolean hasAlbum;
    private boolean mission1Active;
    private boolean mission1Completed;
    private boolean hasFamilyCard;

    // ===== CAPÍTULO 2 =====
    private boolean chapter2Active;
    private boolean chapter2Completed;
    private int chapter2Task;

    @Override
    public boolean hasAlbum() {
        return hasAlbum;
    }

    @Override
    public void setHasAlbum(boolean v) {
        this.hasAlbum = v;
    }

    @Override
    public boolean isMission1Active() {
        return mission1Active;
    }

    @Override
    public void setMission1Active(boolean v) {
        this.mission1Active = v;
    }

    @Override
    public boolean isMission1Completed() {
        return mission1Completed;
    }

    @Override
    public void setMission1Completed(boolean v) {
        this.mission1Completed = v;
    }

    @Override
    public boolean hasFamilyCard() {
        return hasFamilyCard;
    }

    @Override
    public void setHasFamilyCard(boolean v) {
        this.hasFamilyCard = v;
    }

    // ===== CAPÍTULO 2 =====
    @Override
    public boolean isChapter2Active() {
        return chapter2Active;
    }

    @Override
    public void setChapter2Active(boolean v) {
        this.chapter2Active = v;
    }

    @Override
    public boolean isChapter2Completed() {
        return chapter2Completed;
    }

    @Override
    public void setChapter2Completed(boolean v) {
        this.chapter2Completed = v;
    }

    @Override
    public int getChapter2Task() {
        return chapter2Task;
    }

    @Override
    public void setChapter2Task(int task) {
        this.chapter2Task = task;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("hasAlbum", hasAlbum);
        tag.putBoolean("mission1Active", mission1Active);
        tag.putBoolean("mission1Completed", mission1Completed);
        tag.putBoolean("hasFamilyCard", hasFamilyCard);

        // ===== CAPÍTULO 2 =====
        tag.putBoolean("chapter2Active", chapter2Active);
        tag.putBoolean("chapter2Completed", chapter2Completed);
        tag.putInt("chapter2Task", chapter2Task);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.hasAlbum = tag.getBoolean("hasAlbum");
        this.mission1Active = tag.getBoolean("mission1Active");
        this.mission1Completed = tag.getBoolean("mission1Completed");
        this.hasFamilyCard = tag.getBoolean("hasFamilyCard");

        // ===== CAPÍTULO 2 =====
        this.chapter2Active = tag.getBoolean("chapter2Active");
        this.chapter2Completed = tag.getBoolean("chapter2Completed");
        this.chapter2Task = tag.getInt("chapter2Task");
    }
}