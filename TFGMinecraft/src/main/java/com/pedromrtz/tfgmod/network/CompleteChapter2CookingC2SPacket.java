package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompleteChapter2CookingC2SPacket {

    private final List<String> selectedOrder;

    public CompleteChapter2CookingC2SPacket(List<String> selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public static void encode(CompleteChapter2CookingC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.selectedOrder.size());
        for (String entry : msg.selectedOrder) {
            buf.writeUtf(entry);
        }
    }

    public static CompleteChapter2CookingC2SPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<String> order = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            order.add(buf.readUtf());
        }

        return new CompleteChapter2CookingC2SPacket(order);
    }

    public static void handle(CompleteChapter2CookingC2SPacket msg, Object ctxObj) {
        try {
            var ctx = ctxObj;
            var enqueueWork = ctx.getClass().getMethod("enqueueWork", Runnable.class);
            var getSender = ctx.getClass().getMethod("getSender");
            var setHandled = ctx.getClass().getMethod("setPacketHandled", boolean.class);

            enqueueWork.invoke(ctx, (Runnable) () -> {
                try {
                    ServerPlayer sp = (ServerPlayer) getSender.invoke(ctx);
                    if (sp == null) return;

                    List<String> correctOrder = List.of("caldo", "fideos", "carne", "cebolla", "alga");

                    sp.getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(progress -> {
                        if (!progress.isChapter2Active()) return;
                        if (progress.getChapter2Task() != 3) return;

                        if (!msg.selectedOrder.equals(correctOrder)) {
                            sp.displayClientMessage(
                                    Component.literal("§cThe ingredient order was not correct."),
                                    false
                            );
                            return;
                        }

                        progress.setChapter2Task(4);

                        ItemStack cardStack = new ItemStack(ModItems.CARD.get());
                        CardItem.setCardId(cardStack, "card_japan_toshikoshi_soba");
                        sp.addItem(cardStack);

                        sp.displayClientMessage(
                                Component.literal("§6You have cooked the toshikoshi soba! You earned a card."),
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