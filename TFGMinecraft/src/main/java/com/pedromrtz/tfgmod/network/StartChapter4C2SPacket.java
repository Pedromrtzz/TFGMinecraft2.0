package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StartChapter4C2SPacket {

    public StartChapter4C2SPacket() {}

    public static void encode(StartChapter4C2SPacket msg, FriendlyByteBuf buf) {}

    public static StartChapter4C2SPacket decode(FriendlyByteBuf buf) {
        return new StartChapter4C2SPacket();
    }

    public static void handle(StartChapter4C2SPacket msg, Object ctxObj) {
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
                        if (progress.isChapter4Completed()) {
                            sp.displayClientMessage(
                                    Component.literal("§7You have already completed the Matsuri chapter."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter4Active(true);
                        progress.setChapter4Completed(false);
                        progress.setChapter4Task(1);

                        sp.displayClientMessage(
                                Component.literal("§6Chapter 4 started: Matsuri Festival."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§7Speak with the electrician near the festival entrance."),
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