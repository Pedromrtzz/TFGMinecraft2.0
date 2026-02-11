package com.pedromrtz.tfgmod.network;

import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ModNetwork {
    private static final int PROTOCOL = 1;

    public static final SimpleChannel CHANNEL = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(TFGMod.MOD_ID, "main"))
            .networkProtocolVersion(PROTOCOL)
            .simpleChannel();

    private static int id = 0;
    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        registered = true;

        CHANNEL.messageBuilder(OpenScreenS2CPacket.class, id++)
                .encoder(OpenScreenS2CPacket::encode)
                .decoder(OpenScreenS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> OpenScreenS2CPacket.handleClient(msg))
                .add();

        CHANNEL.messageBuilder(StartMission1C2SPacket.class, id++)
                .encoder(StartMission1C2SPacket::encode)
                .decoder(StartMission1C2SPacket::decode)
                .consumerMainThread(StartMission1C2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(SyncChapter1ProgressS2CPacket.class, id++)
                .encoder(SyncChapter1ProgressS2CPacket::encode)
                .decoder(SyncChapter1ProgressS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> SyncChapter1ProgressS2CPacket.handleClient(msg))
                .add();
    }
}