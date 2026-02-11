package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class StartMission1C2SPacket {

    public StartMission1C2SPacket() {}

    public static void encode(StartMission1C2SPacket msg, FriendlyByteBuf buf) {}
    public static StartMission1C2SPacket decode(FriendlyByteBuf buf) { return new StartMission1C2SPacket(); }

    public static void handle(StartMission1C2SPacket msg, Object ctxObj) {

        try {
            var ctx = ctxObj;
            var enqueueWork = ctx.getClass().getMethod("enqueueWork", Runnable.class);
            var getSender = ctx.getClass().getMethod("getSender");
            var setHandled = ctx.getClass().getMethod("setPacketHandled", boolean.class);

            enqueueWork.invoke(ctx, (Runnable) () -> {
                try {
                    ServerPlayer sp = (ServerPlayer) getSender.invoke(ctx);
                    if (sp == null) return;

                    sp.getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(progress -> {
                        if (!progress.hasAlbum()) {
                            sp.addItem(new ItemStack(ModItems.ALBUM.get()));
                            progress.setHasAlbum(true);
                        }
                        progress.setMission1Active(true);

                        com.pedromrtz.tfgmod.network.ProgressSync.syncChapter1(sp);
                    });
                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }
}