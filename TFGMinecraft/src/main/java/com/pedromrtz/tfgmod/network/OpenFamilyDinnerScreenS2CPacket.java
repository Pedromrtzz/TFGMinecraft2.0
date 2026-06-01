package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.FamilyDinnerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenFamilyDinnerScreenS2CPacket {

    public static void encode(OpenFamilyDinnerScreenS2CPacket msg, FriendlyByteBuf buf) {}

    public static OpenFamilyDinnerScreenS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenFamilyDinnerScreenS2CPacket();
    }

    public static void handleClient(OpenFamilyDinnerScreenS2CPacket msg) {
        Minecraft.getInstance().setScreen(new FamilyDinnerScreen());
    }
}