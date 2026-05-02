package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AdvanceChapter2TaskC2SPacket {

    private final int expectedTask;
    private final int nextTask;

    public AdvanceChapter2TaskC2SPacket(int expectedTask, int nextTask) {
        this.expectedTask = expectedTask;
        this.nextTask = nextTask;
    }

    public static void encode(AdvanceChapter2TaskC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.expectedTask);
        buf.writeInt(msg.nextTask);
    }

    public static AdvanceChapter2TaskC2SPacket decode(FriendlyByteBuf buf) {
        return new AdvanceChapter2TaskC2SPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(AdvanceChapter2TaskC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter2Active()) return;
                        if (progress.getChapter2Task() != msg.expectedTask) {
                            sp.displayClientMessage(Component.literal("§cYou cannot advance this task yet."), false);
                            return;
                        }

                        progress.setChapter2Task(msg.nextTask);
                        ProgressSync.syncChapter1(sp);

                        sp.displayClientMessage(
                                Component.literal("§6New task unlocked."),
                                true
                        );
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }
}