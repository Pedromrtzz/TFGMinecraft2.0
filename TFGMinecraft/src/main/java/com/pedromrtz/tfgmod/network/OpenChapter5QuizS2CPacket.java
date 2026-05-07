package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.ChapterQuizScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenChapter5QuizS2CPacket {

    public OpenChapter5QuizS2CPacket() {}

    public static void encode(OpenChapter5QuizS2CPacket msg, FriendlyByteBuf buf) {}

    public static OpenChapter5QuizS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenChapter5QuizS2CPacket();
    }

    public static void handleClient(OpenChapter5QuizS2CPacket msg) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().setScreen(
                        new ChapterQuizScreen(ChapterQuizScreen.QuizChapter.CHAPTER_5)
                );
            }
        });
    }
}