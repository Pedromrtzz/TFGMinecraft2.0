package com.pedromrtz.tfgmod.progress;

import net.minecraft.nbt.CompoundTag;

public class Chapter1Progress implements IChapter1Progress {

    private boolean hasAlbum;
    private boolean mission1Active;
    private boolean mission1Completed;
    private boolean hasFamilyCard;

    private boolean chapter2Active;
    private boolean chapter2Completed;
    private int chapter2Task;
    private int chapter2TableStage;

    private String wish = "";
    private int chapter2BellCount;

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
    public int getChapter2TableStage() {
        return chapter2TableStage;
    }

    @Override
    public void setChapter2TableStage(int stage) {
        this.chapter2TableStage = stage;
    }

    @Override
    public String getWish() {
        return wish;
    }

    @Override
    public void setWish(String wish) {
        this.wish = wish;
    }

    @Override
    public int getChapter2BellCount() {
        return chapter2BellCount;
    }

    @Override
    public void setChapter2BellCount(int count) {
        this.chapter2BellCount = count;
    }

    // ===== NBT =====
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("hasAlbum", hasAlbum);
        tag.putBoolean("mission1Active", mission1Active);
        tag.putBoolean("mission1Completed", mission1Completed);
        tag.putBoolean("hasFamilyCard", hasFamilyCard);

        tag.putBoolean("chapter2Active", chapter2Active);
        tag.putBoolean("chapter2Completed", chapter2Completed);
        tag.putInt("chapter2Task", chapter2Task);
        tag.putInt("chapter2TableStage", chapter2TableStage);

        tag.putString("wish", wish);
        tag.putInt("chapter2BellCount", chapter2BellCount);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.hasAlbum = tag.getBoolean("hasAlbum");
        this.mission1Active = tag.getBoolean("mission1Active");
        this.mission1Completed = tag.getBoolean("mission1Completed");
        this.hasFamilyCard = tag.getBoolean("hasFamilyCard");

        this.chapter2Active = tag.getBoolean("chapter2Active");
        this.chapter2Completed = tag.getBoolean("chapter2Completed");
        this.chapter2Task = tag.getInt("chapter2Task");
        this.chapter2TableStage = tag.getInt("chapter2TableStage");

        this.wish = tag.getString("wish");
        this.chapter2BellCount = tag.getInt("chapter2BellCount");
    }
}