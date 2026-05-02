package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class StartChapter3C2SPacket {

    public StartChapter3C2SPacket() {}

    public static void encode(StartChapter3C2SPacket msg, FriendlyByteBuf buf) {}

    public static StartChapter3C2SPacket decode(FriendlyByteBuf buf) {
        return new StartChapter3C2SPacket();
    }

    public static void handle(StartChapter3C2SPacket msg, Object ctxObj) {
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
                        progress.setChapter3Active(true);
                        progress.setChapter3Completed(false);
                        progress.setChapter3Task(1);

                        sp.addItem(new ItemStack(ModItems.YEN.get(), 3));

                        sp.displayClientMessage(
                                Component.literal("§6The master has given you 3 yen to buy fish."),
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