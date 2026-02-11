package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.ChefDialogueScreen;
import com.pedromrtz.tfgmod.entity.client.ElderDialogueScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenScreenS2CPacket {

    public enum ScreenType {
        CHEF_DIALOGUE,
        ELDER_DIALOGUE,
        MOTHER_DIALOGUE
    }

    private final ScreenType type;

    public OpenScreenS2CPacket(ScreenType type) {
        this.type = type;
    }

    public static void encode(OpenScreenS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
    }

    public static OpenScreenS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenScreenS2CPacket(buf.readEnum(ScreenType.class));
    }

    public static void handleClient(OpenScreenS2CPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        switch (msg.type) {
            case CHEF_DIALOGUE -> mc.setScreen(new ChefDialogueScreen());
            case ELDER_DIALOGUE -> mc.setScreen(new ElderDialogueScreen());
            case MOTHER_DIALOGUE -> {
                mc.setScreen(new ChefDialogueScreen());
            }
        }
    }
}