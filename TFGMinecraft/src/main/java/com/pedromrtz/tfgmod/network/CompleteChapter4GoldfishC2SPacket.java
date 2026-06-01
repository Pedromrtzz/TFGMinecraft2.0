package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

public class CompleteChapter4GoldfishC2SPacket {

    public CompleteChapter4GoldfishC2SPacket() {}

    public static void encode(CompleteChapter4GoldfishC2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter4GoldfishC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter4GoldfishC2SPacket();
    }

    public static void handle(CompleteChapter4GoldfishC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter4Active() || progress.getChapter4Task() != 2) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot test the goldfish stall right now."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter4Task(3);

                        sp.displayClientMessage(
                                Component.literal("§6The goldfish scooping stall works perfectly."),
                                false
                        );

                        sp.displayClientMessage(
                                Component.literal("§7Now test the target shooting stall."),
                                false
                        );

                        sp.level().playSound(
                                null,
                                sp.blockPosition(),
                                SoundEvents.PLAYER_LEVELUP,
                                sp.getSoundSource(),
                                1.0f,
                                1.0f
                        );

                        ProgressSync.syncChapter1(sp);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }
}