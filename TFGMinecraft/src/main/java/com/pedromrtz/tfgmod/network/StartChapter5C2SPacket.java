package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StartChapter5C2SPacket {

    public StartChapter5C2SPacket() {}

    public static void encode(StartChapter5C2SPacket msg, FriendlyByteBuf buf) {}

    public static StartChapter5C2SPacket decode(FriendlyByteBuf buf) {
        return new StartChapter5C2SPacket();
    }

    public static void handle(StartChapter5C2SPacket msg, Object ctxObj) {
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
                        progress.setChapter5Active(true);
                        progress.setChapter5Completed(false);
                        progress.setChapter5Task(1);

                        sp.displayClientMessage(
                                Component.literal("§6Chapter 5 started: The Daimyo Castle."),
                                false
                        );

                        ProgressSync.syncChapter1(sp);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }
}