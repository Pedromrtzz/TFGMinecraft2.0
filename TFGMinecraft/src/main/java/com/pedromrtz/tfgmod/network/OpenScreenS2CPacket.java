package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.entity.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class OpenScreenS2CPacket {

    public enum ScreenType {
        CHEF_DIALOGUE,
        ELDER_DIALOGUE,
        MOTHER_DIALOGUE,
        FATHER_DIALOGUE,
        SISTER_DIALOGUE,
        AMBIENT_DIALOGUE,
        MONK_DIALOGUE,
        DESIRE_SCREEN,
        MERCHANT_SHOP,
        ITAMAE_DIALOGUE,
        FISH_MERCHANT_SHOP,
        FESTIVAL_ORGANIZER_DIALOGUE,
        ELECTRICIAN_DIALOGUE,
        GOLDFISH_SELLER_DIALOGUE,
        TARGET_ATTENDANT_DIALOGUE,
        LANTERN_KEEPER_DIALOGUE
    }

    private final ScreenType type;
    private final String npcId;

    public OpenScreenS2CPacket(ScreenType type) {
        this(type, "");
    }

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
            case MOTHER_DIALOGUE -> mc.setScreen(new MotherDialogueScreen());
            case FATHER_DIALOGUE -> mc.setScreen(new FatherDialogueScreen());
            case SISTER_DIALOGUE -> mc.setScreen(new SisterDialogueScreen());
            case AMBIENT_DIALOGUE -> mc.setScreen(new AmbientDialogueScreen(msg.npcId));
            case MONK_DIALOGUE -> mc.setScreen(new MonkDialogueScreen());
            case DESIRE_SCREEN -> mc.setScreen(new DesireScreen());
            case MERCHANT_SHOP -> mc.setScreen(new MerchantShopScreen());
            case ITAMAE_DIALOGUE -> mc.setScreen(new ItamaeDialogueScreen());
            case FISH_MERCHANT_SHOP -> mc.setScreen(new FishMerchantShopScreen());
            case FESTIVAL_ORGANIZER_DIALOGUE -> mc.setScreen(new FestivalOrganizerDialogueScreen());
            case ELECTRICIAN_DIALOGUE -> mc.setScreen(new ElectricianDialogueScreen());
            case GOLDFISH_SELLER_DIALOGUE -> mc.setScreen(new GoldfishSellerDialogueScreen());
            case TARGET_ATTENDANT_DIALOGUE -> mc.setScreen(new TargetAttendantDialogueScreen());
            case LANTERN_KEEPER_DIALOGUE -> mc.setScreen(new LanternKeeperDialogueScreen());
        }
    }
}