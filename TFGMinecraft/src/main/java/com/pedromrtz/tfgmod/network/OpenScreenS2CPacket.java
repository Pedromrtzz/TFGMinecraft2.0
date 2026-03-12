package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.AmbientDialogueScreen;
import com.pedromrtz.tfgmod.entity.client.ChefDialogueScreen;
import com.pedromrtz.tfgmod.entity.client.ElderDialogueScreen;
import com.pedromrtz.tfgmod.entity.client.FatherDialogueScreen;
import com.pedromrtz.tfgmod.entity.client.SisterDialogueScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenScreenS2CPacket {

    public enum ScreenType {
        CHEF_DIALOGUE,
        ELDER_DIALOGUE,
        MOTHER_DIALOGUE,
        FATHER_DIALOGUE,
        SISTER_DIALOGUE,
        AMBIENT_DIALOGUE
    }

    private final ScreenType type;
    private final String npcId;

    // Constructor normal para pantallas que no necesitan npcId
    public OpenScreenS2CPacket(ScreenType type) {
        this(type, "");
    }

    // Constructor para ambient NPC
    public OpenScreenS2CPacket(ScreenType type, String npcId) {
        this.type = type;
        this.npcId = npcId;
    }

    public static void encode(OpenScreenS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeUtf(msg.npcId);
    }

    public static OpenScreenS2CPacket decode(FriendlyByteBuf buf) {
        ScreenType type = buf.readEnum(ScreenType.class);
        String npcId = buf.readUtf();
        return new OpenScreenS2CPacket(type, npcId);
    }

    public static void handleClient(OpenScreenS2CPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        switch (msg.type) {
            case CHEF_DIALOGUE -> mc.setScreen(new ChefDialogueScreen());

            case ELDER_DIALOGUE -> mc.setScreen(new ElderDialogueScreen());

            case MOTHER_DIALOGUE -> mc.setScreen(new ChefDialogueScreen());

            case FATHER_DIALOGUE -> mc.setScreen(new FatherDialogueScreen());

            case SISTER_DIALOGUE -> mc.setScreen(new SisterDialogueScreen());

            case AMBIENT_DIALOGUE -> mc.setScreen(new AmbientDialogueScreen(msg.npcId));
        }
    }
}