package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.ChapterQuizScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenChapter2QuizS2CPacket {

    public OpenChapter2QuizS2CPacket() {}

    public static void encode(OpenChapter2QuizS2CPacket msg, FriendlyByteBuf buf) {}

    public static OpenChapter2QuizS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenChapter2QuizS2CPacket();
    }

    public static void handleClient(OpenChapter2QuizS2CPacket msg) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().setScreen(new ChapterQuizScreen());
            }
        });
    }
}