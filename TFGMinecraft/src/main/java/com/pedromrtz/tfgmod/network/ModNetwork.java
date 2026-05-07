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

        CHANNEL.messageBuilder(StartChapter2C2SPacket.class, id++)
                .encoder(StartChapter2C2SPacket::encode)
                .decoder(StartChapter2C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> StartChapter2C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CheckChapter2IngredientsC2SPacket.class, id++)
                .encoder(CheckChapter2IngredientsC2SPacket::encode)
                .decoder(CheckChapter2IngredientsC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CheckChapter2IngredientsC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter2CookingC2SPacket.class, id++)
                .encoder(CompleteChapter2CookingC2SPacket::encode)
                .decoder(CompleteChapter2CookingC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter2CookingC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(AdvanceChapter2TaskC2SPacket.class, id++)
                .encoder(AdvanceChapter2TaskC2SPacket::encode)
                .decoder(AdvanceChapter2TaskC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> AdvanceChapter2TaskC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(SaveWishC2SPacket.class, id++)
                .encoder(SaveWishC2SPacket::encode)
                .decoder(SaveWishC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> SaveWishC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(BuyIngredientC2SPacket.class, id++)
                .encoder(BuyIngredientC2SPacket::encode)
                .decoder(BuyIngredientC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> BuyIngredientC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(OpenFamilyDinnerScreenS2CPacket.class, id++)
                .encoder(OpenFamilyDinnerScreenS2CPacket::encode)
                .decoder(OpenFamilyDinnerScreenS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> OpenFamilyDinnerScreenS2CPacket.handleClient(msg))
                .add();

        CHANNEL.messageBuilder(GiveEmaAndAdvanceTaskC2SPacket.class, id++)
                .encoder(GiveEmaAndAdvanceTaskC2SPacket::encode)
                .decoder(GiveEmaAndAdvanceTaskC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> GiveEmaAndAdvanceTaskC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(StartChapter3C2SPacket.class, id++)
                .encoder(StartChapter3C2SPacket::encode)
                .decoder(StartChapter3C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> StartChapter3C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CheckChapter3IngredientsC2SPacket.class, id++)
                .encoder(CheckChapter3IngredientsC2SPacket::encode)
                .decoder(CheckChapter3IngredientsC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CheckChapter3IngredientsC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(BuyFishC2SPacket.class, id++)
                .encoder(BuyFishC2SPacket::encode)
                .decoder(BuyFishC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> BuyFishC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter3RiceWashC2SPacket.class, id++)
                .encoder(CompleteChapter3RiceWashC2SPacket::encode)
                .decoder(CompleteChapter3RiceWashC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter3RiceWashC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter3SalmonCutC2SPacket.class, id++)
                .encoder(CompleteChapter3SalmonCutC2SPacket::encode)
                .decoder(CompleteChapter3SalmonCutC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter3SalmonCutC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter3SushiAssemblyC2SPacket.class, id++)
                .encoder(CompleteChapter3SushiAssemblyC2SPacket::encode)
                .decoder(CompleteChapter3SushiAssemblyC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter3SushiAssemblyC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter3C2SPacket.class, id++)
                .encoder(CompleteChapter3C2SPacket::encode)
                .decoder(CompleteChapter3C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter3C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(StartChapter4C2SPacket.class, id++)
                .encoder(StartChapter4C2SPacket::encode)
                .decoder(StartChapter4C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> StartChapter4C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter4LanternsC2SPacket.class, id++)
                .encoder(CompleteChapter4LanternsC2SPacket::encode)
                .decoder(CompleteChapter4LanternsC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter4LanternsC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter4GoldfishC2SPacket.class, id++)
                .encoder(CompleteChapter4GoldfishC2SPacket::encode)
                .decoder(CompleteChapter4GoldfishC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter4GoldfishC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter4TargetsC2SPacket.class, id++)
                .encoder(CompleteChapter4TargetsC2SPacket::encode)
                .decoder(CompleteChapter4TargetsC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter4TargetsC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter4C2SPacket.class, id++)
                .encoder(CompleteChapter4C2SPacket::encode)
                .decoder(CompleteChapter4C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter4C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter2QuizC2SPacket.class, id++)
                .encoder(CompleteChapter2QuizC2SPacket::encode)
                .decoder(CompleteChapter2QuizC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter2QuizC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(OpenChapter2QuizS2CPacket.class, id++)
                .encoder(OpenChapter2QuizS2CPacket::encode)
                .decoder(OpenChapter2QuizS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> OpenChapter2QuizS2CPacket.handleClient(msg))
                .add();

        CHANNEL.messageBuilder(CompleteChapter3QuizC2SPacket.class, id++)
                .encoder(CompleteChapter3QuizC2SPacket::encode)
                .decoder(CompleteChapter3QuizC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter3QuizC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(OpenChapter3QuizS2CPacket.class, id++)
                .encoder(OpenChapter3QuizS2CPacket::encode)
                .decoder(OpenChapter3QuizS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> OpenChapter3QuizS2CPacket.handleClient(msg))
                .add();

        CHANNEL.messageBuilder(CompleteChapter4QuizC2SPacket.class, id++)
                .encoder(CompleteChapter4QuizC2SPacket::encode)
                .decoder(CompleteChapter4QuizC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter4QuizC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(OpenChapter4QuizS2CPacket.class, id++)
                .encoder(OpenChapter4QuizS2CPacket::encode)
                .decoder(OpenChapter4QuizS2CPacket::decode)
                .consumerMainThread((msg, ctx) -> OpenChapter4QuizS2CPacket.handleClient(msg))
                .add();

        CHANNEL.messageBuilder(StartChapter5C2SPacket.class, id++)
                .encoder(StartChapter5C2SPacket::encode)
                .decoder(StartChapter5C2SPacket::decode)
                .consumerMainThread((msg, ctx) -> StartChapter5C2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter5BowTrainingC2SPacket.class, id++)
                .encoder(CompleteChapter5BowTrainingC2SPacket::encode)
                .decoder(CompleteChapter5BowTrainingC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter5BowTrainingC2SPacket.handle(msg, ctx))
                .add();

        CHANNEL.messageBuilder(CompleteChapter5PuzzleC2SPacket.class, id++)
                .encoder(CompleteChapter5PuzzleC2SPacket::encode)
                .decoder(CompleteChapter5PuzzleC2SPacket::decode)
                .consumerMainThread((msg, ctx) -> CompleteChapter5PuzzleC2SPacket.handle(msg, ctx))
                .add();


    }
}