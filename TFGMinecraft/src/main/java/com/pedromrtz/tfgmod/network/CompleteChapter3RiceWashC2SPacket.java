package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CompleteChapter3RiceWashC2SPacket {

    public CompleteChapter3RiceWashC2SPacket() {}

    public static void encode(CompleteChapter3RiceWashC2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter3RiceWashC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter3RiceWashC2SPacket();
    }

    public static void handle(CompleteChapter3RiceWashC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter3Active() || progress.getChapter3Task() != 2) {
                            sp.displayClientMessage(Component.literal("§cAhora no puedes lavar arroz."), false);
                            return;
                        }

                        progress.setChapter3Task(3);

                        ItemStack rice = new ItemStack(ModItems.ARROZ.get(), 1);
                        sp.addItem(rice);

                        sp.displayClientMessage(
                                Component.literal("§6Has lavado el arroz correctamente. Has recibido un cromo."),
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