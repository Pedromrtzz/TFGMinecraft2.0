package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.capitulo2.Chapter2IngredientHelper;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class CheckChapter2IngredientsC2SPacket {

    public CheckChapter2IngredientsC2SPacket() {}

    public static void encode(CheckChapter2IngredientsC2SPacket msg, FriendlyByteBuf buf) {}

    public static CheckChapter2IngredientsC2SPacket decode(FriendlyByteBuf buf) {
        return new CheckChapter2IngredientsC2SPacket();
    }

    public static void handle(CheckChapter2IngredientsC2SPacket msg, Object ctxObj) {
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
                        if (progress.getChapter2Task() != 1 && progress.getChapter2Task() != 2) return;

                        if (Chapter2IngredientHelper.hasAllIngredients(sp)) {
                            Chapter2IngredientHelper.consumeIngredients(sp);

                            progress.setChapter2Task(3);

                            sp.displayClientMessage(
                                    Component.literal("§6Perfecto. Ya tenemos todo para cocinar el toshikoshi soba."),
                                    false
                            );

                            ProgressSync.syncChapter1(sp);
                        } else {
                            progress.setChapter2Task(2);

                            List<String> missing = Chapter2IngredientHelper.getMissingIngredients(sp);
                            String missingText = String.join(", ", missing);

                            sp.displayClientMessage(
                                    Component.literal("§cAún faltan ingredientes: " + missingText),
                                    false
                            );

                            ProgressSync.syncChapter1(sp);
                        }
                    });
                } catch (Exception ignored) {}
            });

            setHandled.invoke(ctx, true);
        } catch (Exception ignored) {}
    }
}