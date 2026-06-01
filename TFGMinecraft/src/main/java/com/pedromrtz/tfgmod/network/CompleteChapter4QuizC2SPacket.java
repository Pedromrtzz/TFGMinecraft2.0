package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.Item.CardItem;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CompleteChapter4QuizC2SPacket {

    private final boolean passed;

    public CompleteChapter4QuizC2SPacket(boolean passed) {
        this.passed = passed;
    }

    public static void encode(CompleteChapter4QuizC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.passed);
    }

    public static CompleteChapter4QuizC2SPacket decode(FriendlyByteBuf buf) {
        return new CompleteChapter4QuizC2SPacket(buf.readBoolean());
    }

    public static void handle(CompleteChapter4QuizC2SPacket msg, Object ctxObj) {
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

                        if (msg.passed) {
                            progress.setChapter4Active(false);
                            progress.setChapter4Completed(true);
                            progress.setChapter4Task(4);

                            sp.displayClientMessage(
                                    Component.literal("§aChapter 4 test passed. Matsuri chapter completed."),
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

                        } else {
                            progress.setChapter4Active(false);
                            progress.setChapter4Completed(false);
                            progress.setChapter4Task(0);

                            removeChapter4Cards(sp);

                            sp.displayClientMessage(
                                    Component.literal("§cChapter 4 test failed. You must repeat the Matsuri chapter."),
                                    false
                            );

                            sp.displayClientMessage(
                                    Component.literal("§7The cultural cards from this chapter have been removed."),
                                    false
                            );

                            sp.level().playSound(
                                    null,
                                    sp.blockPosition(),
                                    SoundEvents.VILLAGER_NO,
                                    sp.getSoundSource(),
                                    1.0f,
                                    1.0f
                            );
                        }

                        ProgressSync.syncChapter1(sp);
                    });

                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);

        } catch (Exception ignored) {}
    }

    private static void removeChapter4Cards(ServerPlayer sp) {
        List<String> chapter4CardIds = List.of(
                "card_japan_matsuri"
        );

        for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
            ItemStack stack = sp.getInventory().getItem(i);

            if (!stack.is(ModItems.CARD.get())) continue;

            String cardId = CardItem.getCardId(stack);

            if (chapter4CardIds.contains(cardId)) {
                stack.setCount(0);
            }
        }

        sp.getInventory().setChanged();
    }
}