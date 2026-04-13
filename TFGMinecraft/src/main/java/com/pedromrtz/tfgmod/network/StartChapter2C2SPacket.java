package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class StartChapter2C2SPacket {

    public StartChapter2C2SPacket() {}

    public static void encode(StartChapter2C2SPacket msg, FriendlyByteBuf buf) {}

    public static StartChapter2C2SPacket decode(FriendlyByteBuf buf) {
        return new StartChapter2C2SPacket();
    }

    public static void handle(StartChapter2C2SPacket msg, Object ctxObj) {
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
                        progress.setChapter2Active(true);
                        progress.setChapter2Completed(false);
                        progress.setChapter2Task(1);

                        ProgressSync.syncChapter1(sp);
                    });
                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }
}