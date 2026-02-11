package com.pedromrtz.tfgmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tfg")
                        .then(Commands.literal("chapter1")
                                .then(Commands.literal("reset")
                                        .executes(ctx -> {
                                            CommandSourceStack src = ctx.getSource();
                                            Player p = src.getPlayerOrException();

                                            var prog = Chapter1ProgressUtil.get(p);

                                            prog.setHasAlbum(false);
                                            prog.setMission1Active(false);
                                            prog.setHasFamilyCard(false);
                                            prog.setMission1Completed(false);

                                            if (p instanceof ServerPlayer sp) {
                                                removeChapter1Items(sp);

                                                ProgressSync.syncChapter1(sp);
                                            }

                                            src.sendSuccess(() -> Component.literal("Capítulo 1 reseteado."), false);
                                            return 1;
                                        })
                                )
                        )
        );
    }

    private static void removeChapter1Items(ServerPlayer sp) {
        var inv = sp.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.is(ModItems.ALBUM.get())) {
                inv.setItem(i, ItemStack.EMPTY);
                continue;
            }

            if (stack.is(ModItems.CARD.get())) {
                inv.setItem(i, ItemStack.EMPTY);
            }
        }

        inv.setChanged();
    }
}