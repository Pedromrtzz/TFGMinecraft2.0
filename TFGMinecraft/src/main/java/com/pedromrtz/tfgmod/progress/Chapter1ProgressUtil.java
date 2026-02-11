package com.pedromrtz.tfgmod.progress;

import net.minecraft.world.entity.player.Player;

public class Chapter1ProgressUtil {

    public static IChapter1Progress get(Player player) {
        return player.getCapability(Chapter1ProgressProvider.CHAPTER1_PROGRESS)
                .orElseThrow(() -> new IllegalStateException("Chapter1Progress capability missing"));
    }
}