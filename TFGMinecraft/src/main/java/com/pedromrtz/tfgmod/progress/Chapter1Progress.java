package com.pedromrtz.tfgmod.progress;

import net.minecraft.nbt.CompoundTag;

public class Chapter1Progress implements IChapter1Progress {
    private boolean hasAlbum;
    private boolean mission1Active;
    private boolean mission1Completed;
    private boolean hasFamilyCard;

    @Override public boolean hasAlbum() { return hasAlbum; }
    @Override public void setHasAlbum(boolean v) { this.hasAlbum = v; }

    @Override public boolean isMission1Active() { return mission1Active; }
    @Override public void setMission1Active(boolean v) { this.mission1Active = v; }

    @Override public boolean isMission1Completed() { return mission1Completed; }
    @Override public void setMission1Completed(boolean v) { this.mission1Completed = v; }

    @Override public boolean hasFamilyCard() { return hasFamilyCard; }
    @Override public void setHasFamilyCard(boolean v) { this.hasFamilyCard = v; }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("hasAlbum", hasAlbum);
        tag.putBoolean("mission1Active", mission1Active);
        tag.putBoolean("mission1Completed", mission1Completed);
        tag.putBoolean("hasFamilyCard", hasFamilyCard);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.hasAlbum = tag.getBoolean("hasAlbum");
        this.mission1Active = tag.getBoolean("mission1Active");
        this.mission1Completed = tag.getBoolean("mission1Completed");
        this.hasFamilyCard = tag.getBoolean("hasFamilyCard");
    }
}