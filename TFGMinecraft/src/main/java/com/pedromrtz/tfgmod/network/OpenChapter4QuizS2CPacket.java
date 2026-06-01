package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.ChapterQuizScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenChapter4QuizS2CPacket {

    public OpenChapter4QuizS2CPacket() {}

    public static void encode(OpenChapter4QuizS2CPacket msg, FriendlyByteBuf buf) {}

    public static OpenChapter4QuizS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenChapter4QuizS2CPacket();
    }

    public static void handleClient(OpenChapter4QuizS2CPacket msg) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().setScreen(
                        new ChapterQuizScreen(ChapterQuizScreen.QuizChapter.CHAPTER_4)
                );
            }
        });
    }
}