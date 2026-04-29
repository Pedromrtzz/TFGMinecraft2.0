package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GiveEmaAndAdvanceTaskC2SPacket {

    public GiveEmaAndAdvanceTaskC2SPacket() {}

    public static void encode(GiveEmaAndAdvanceTaskC2SPacket msg, FriendlyByteBuf buf) {}

    public static GiveEmaAndAdvanceTaskC2SPacket decode(FriendlyByteBuf buf) {
        return new GiveEmaAndAdvanceTaskC2SPacket();
    }

    public static void handle(GiveEmaAndAdvanceTaskC2SPacket msg, Object ctxObj) {
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
                        if (progress.getChapter2Task() != 6) return;

                        progress.setChapter2Task(7);

                        ItemStack ema = new ItemStack(ModItems.EMA.get());
                        sp.addItem(ema);

                        sp.displayClientMessage(
                                Component.literal("§6El monje te ha dado un ema para escribir tu deseo."),
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