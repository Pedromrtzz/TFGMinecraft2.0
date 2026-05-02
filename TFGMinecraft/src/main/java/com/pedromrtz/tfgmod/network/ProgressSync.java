package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.progress.Chapter1ProgressProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ProgressSync {

    public static void syncChapter1(ServerPlayer sp) {
        sp.getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS).ifPresent(p -> {
            ModNetwork.CHANNEL.send(
                    new SyncChapter1ProgressS2CPacket(
                            p.hasAlbum(),
                            p.isMission1Active(),
                            p.hasFamilyCard(),
                            p.isMission1Completed(),

                            p.isChapter2Active(),
                            p.isChapter2Completed(),
                            p.getChapter2Task(),
                            p.getChapter2TableStage(),

                            p.isChapter3Active(),
                            p.isChapter3Completed(),
                            p.getChapter3Task(),

                            p.isChapter4Active(),
                            p.isChapter4Completed(),
                            p.getChapter4Task()
                    ),
                    PacketDistributor.PLAYER.with(sp)
            );
        });
    }
}