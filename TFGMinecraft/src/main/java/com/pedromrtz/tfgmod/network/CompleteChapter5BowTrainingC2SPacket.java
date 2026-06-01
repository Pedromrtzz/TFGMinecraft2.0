package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CompleteChapter5BowTrainingC2SPacket {

    public CompleteChapter5BowTrainingC2SPacket() {}

    public static void encode(CompleteChapter5BowTrainingC2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter5BowTrainingC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter5BowTrainingC2SPacket();
    }

    public static void handle(CompleteChapter5BowTrainingC2SPacket msg, Object ctxObj) {

        try {

            var ctx = ctxObj;

            var enqueueWork =
                    ctx.getClass().getMethod("enqueueWork", Runnable.class);

            var getSender =
                    ctx.getClass().getMethod("getSender");

            var setHandled =
                    ctx.getClass().getMethod("setPacketHandled", boolean.class);

            enqueueWork.invoke(ctx, (Runnable) () -> {

                try {

                    ServerPlayer sp =
                            (ServerPlayer) getSender.invoke(ctx);

                    if (sp == null) return;

                    sp.getCapability(
                            Chapter1ProgressProvider.CHAPTER1_PROGRESS
                    ).ifPresent(progress -> {

                        progress.setChapter5Task(2);

                        sp.displayClientMessage(
                                Component.literal(
                                        "§aYou completed the samurai training."
                                ),
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