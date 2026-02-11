package com.pedromrtz.tfgmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class SyncChapter1ProgressS2CPacket {

    private final boolean hasAlbum;
    private final boolean mission1Active;
    private final boolean hasFamilyCard;
    private final boolean mission1Completed;

    public SyncChapter1ProgressS2CPacket(boolean hasAlbum, boolean mission1Active, boolean hasFamilyCard, boolean mission1Completed) {
        this.hasAlbum = hasAlbum;
        this.mission1Active = mission1Active;
        this.hasFamilyCard = hasFamilyCard;
        this.mission1Completed = mission1Completed;
    }

    public static void encode(SyncChapter1ProgressS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.hasAlbum);
        buf.writeBoolean(msg.mission1Active);
        buf.writeBoolean(msg.hasFamilyCard);
        buf.writeBoolean(msg.mission1Completed);
    }

    public static SyncChapter1ProgressS2CPacket decode(FriendlyByteBuf buf) {
        return new SyncChapter1ProgressS2CPacket(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean()
        );
    }

    // Handler CLIENTE (sin NetworkEvent)
    public static void handleClient(SyncChapter1ProgressS2CPacket msg) {
        Minecraft.getInstance().execute(() -> {
            com.pedromrtz.tfgmod.client.ClientChapter1Data.apply(
                    msg.hasAlbum, msg.mission1Active, msg.hasFamilyCard, msg.mission1Completed
            );
        });
    }
}