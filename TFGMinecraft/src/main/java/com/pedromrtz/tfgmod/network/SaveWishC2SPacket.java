package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
                        if (!progress.isChapter2Active()) return;

                        if (progress.getChapter2Task() != 7) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot write the wish right now."),
                                    false
                            );
                            return;
                        }

                        String cleanWish = msg.wish.trim();

                        if (cleanWish.isEmpty()) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot leave the wish empty."),
                                    false
                            );
                            return;
                        }

                        if (cleanWish.length() > 60) {
                            cleanWish = cleanWish.substring(0, 60);
                        }

                        progress.setWish(cleanWish);
                        progress.setChapter2Task(8);

                        sp.displayClientMessage(
                                Component.literal("§7A whisper tells you: §fTry hanging the wish on the tree outside."),
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