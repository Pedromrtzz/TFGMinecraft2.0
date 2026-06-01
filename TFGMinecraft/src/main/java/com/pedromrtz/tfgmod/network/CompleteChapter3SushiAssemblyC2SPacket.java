package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CompleteChapter3SushiAssemblyC2SPacket {

    public CompleteChapter3SushiAssemblyC2SPacket() {}

    public static void encode(CompleteChapter3SushiAssemblyC2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter3SushiAssemblyC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter3SushiAssemblyC2SPacket();
    }

    public static void handle(CompleteChapter3SushiAssemblyC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter3Active() || progress.getChapter3Task() != 4) {
                            sp.displayClientMessage(
                                    Component.literal("§cYou cannot assemble sushi right now."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter3Task(5);

                        ItemStack sushi = new ItemStack(ModItems.SUSHI.get(), 1);
                        sp.addItem(sushi);

                        sp.displayClientMessage(
                                Component.literal("§6You have assembled the sushi correctly. Deliver it to the Itamae Master."),
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