package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class SaveWishC2SPacket {

    private final String wish;

    public SaveWishC2SPacket(String wish) {
        this.wish = wish;
    }

    public static void encode(SaveWishC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.wish);
    }

    public static SaveWishC2SPacket decode(FriendlyByteBuf buf) {
        return new SaveWishC2SPacket(buf.readUtf());
    }

    public static void handle(SaveWishC2SPacket msg, Object ctxObj) {
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

                        progress.setWish(msg.wish);
                        progress.setChapter2Task(8);

                        ProgressSync.syncChapter1(sp);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }
}