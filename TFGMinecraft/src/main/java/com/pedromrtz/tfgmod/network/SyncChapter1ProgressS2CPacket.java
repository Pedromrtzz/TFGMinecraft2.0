package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class SyncChapter1ProgressS2CPacket {

    private final boolean hasAlbum;
    private final boolean mission1Active;
    private final boolean hasFamilyCard;
    private final boolean mission1Completed;

    private final boolean chapter2Active;
    private final boolean chapter2Completed;
    private final int chapter2Task;

    public SyncChapter1ProgressS2CPacket(
            boolean hasAlbum,
            boolean mission1Active,
            boolean hasFamilyCard,
            boolean mission1Completed,
            boolean chapter2Active,
            boolean chapter2Completed,
            int chapter2Task
    ) {
        this.hasAlbum = hasAlbum;
        this.mission1Active = mission1Active;
        this.hasFamilyCard = hasFamilyCard;
        this.mission1Completed = mission1Completed;
        this.chapter2Active = chapter2Active;
        this.chapter2Completed = chapter2Completed;
        this.chapter2Task = chapter2Task;
    }

    public static void encode(SyncChapter1ProgressS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.hasAlbum);
        buf.writeBoolean(msg.mission1Active);
        buf.writeBoolean(msg.hasFamilyCard);
        buf.writeBoolean(msg.mission1Completed);

        buf.writeBoolean(msg.chapter2Active);
        buf.writeBoolean(msg.chapter2Completed);
        buf.writeInt(msg.chapter2Task);
    }

    public static SyncChapter1ProgressS2CPacket decode(FriendlyByteBuf buf) {
        return new SyncChapter1ProgressS2CPacket(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readInt()
        );
    }

    public static void handleClient(SyncChapter1ProgressS2CPacket msg) {
        Minecraft.getInstance().execute(() -> {
            ClientChapter1Data.apply(
                    msg.hasAlbum,
                    msg.mission1Active,
                    msg.hasFamilyCard,
                    msg.mission1Completed,
                    msg.chapter2Active,
                    msg.chapter2Completed,
                    msg.chapter2Task
            );
        });
    }
}