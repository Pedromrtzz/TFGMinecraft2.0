package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CompleteChapter3SalmonCutC2SPacket {

    public CompleteChapter3SalmonCutC2SPacket() {}

    public static void encode(CompleteChapter3SalmonCutC2SPacket msg, FriendlyByteBuf buf) {}

    public static CompleteChapter3SalmonCutC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter3SalmonCutC2SPacket();
    }

    public static void handle(CompleteChapter3SalmonCutC2SPacket msg, Object ctxObj) {
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
                        if (!progress.isChapter3Active() || progress.getChapter3Task() != 3) {
                            sp.displayClientMessage(
                                    Component.literal("§cAhora no puedes cortar el salmón."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter3Task(4);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_salmon");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6Has cortado el salmón correctamente. Ahora prepara el sushi."),
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