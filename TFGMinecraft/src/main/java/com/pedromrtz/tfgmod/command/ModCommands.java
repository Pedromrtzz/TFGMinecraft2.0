package com.pedromrtz.tfgmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.pedromrtz.tfgmod.network.ProgressSync;
import com.pedromrtz.tfgmod.progress.Chapter1ProgressUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("tfg")

                        // ===== RESET CAPÍTULO 1 =====
                        .then(Commands.literal("chapter1")
                                .then(Commands.literal("reset")
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            var prog = Chapter1ProgressUtil.get(sp);

                                            prog.setHasAlbum(false);
                                            prog.setMission1Active(false);
                                            prog.setHasFamilyCard(false);
                                            prog.setMission1Completed(false);

                                            ProgressSync.syncChapter1(sp);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Capítulo 1 reseteado."),
                                                    false
                                            );
                                            return 1;
                                        })
                                )
                        )

                        // ===== CAPÍTULO 2 =====
                        .then(Commands.literal("chapter2")

                                // 🔁 RESET
                                .then(Commands.literal("reset")
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            var prog = Chapter1ProgressUtil.get(sp);

                                            prog.setChapter2Active(false);
                                            prog.setChapter2Completed(false);
                                            prog.setChapter2Task(0);
                                            prog.setChapter2TableStage(0);
                                            prog.setWish("");

                                            ProgressSync.syncChapter1(sp);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Capítulo 2 reseteado."),
                                                    false
                                            );
                                            return 1;
                                        })
                                )

                                // ▶️ START
                                .then(Commands.literal("start")
                                        .executes(ctx -> {
                                            ServerPlayer sp = ctx.getSource().getPlayerOrException();
                                            var prog = Chapter1ProgressUtil.get(sp);

                                            prog.setChapter2Active(true);
                                            prog.setChapter2Completed(false);
                                            prog.setChapter2Task(1);
                                            prog.setChapter2TableStage(0);

                                            ProgressSync.syncChapter1(sp);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Capítulo 2 iniciado."),
                                                    false
                                            );
                                            return 1;
                                        })
                                )
                        )
        );
    }
}