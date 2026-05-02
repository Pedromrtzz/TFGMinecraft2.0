package com.pedromrtz.tfgmod.entity.client;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class AmbientNPCData {

    public record AmbientNPCInfo(
            String id,
            String displayName,
            String[] lines,
            ResourceLocation texture,
            ResourceLocation portrait
    ) {}

    public static final Map<String, AmbientNPCInfo> NPCS = Map.of(
            "chef", new AmbientNPCInfo(
                    "chef",
                    "Chef",
                    new String[]{
                            "Sushi is not just food.",
                            "It also represents technique, balance",
                            "and respect for the ingredients."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/chef.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/chef.png")
            ),

            "fisherman", new AmbientNPCInfo(
                    "fisherman",
                    "Fisherman",
                    new String[]{
                            "Koi fish are very important in Japan.",
                            "They symbolise perseverance, effort",
                            "and good fortune."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman.png")
            ),

            "neighbor1", new AmbientNPCInfo(
                    "neighbor1",
                    "Villager",
                    new String[]{
                            "Sakura cherry trees bloom",
                            "only for a short time each year."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor.png")
            ),

            "neighbor2", new AmbientNPCInfo(
                    "neighbor2",
                    "Villager",
                    new String[]{
                            "In Japan, removing your shoes before",
                            "entering a home is a sign of respect."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor2.png")
            ),

            "neighbor3", new AmbientNPCInfo(
                    "neighbor3",
                    "Villager",
                    new String[]{
                            "Many traditional Japanese houses",
                            "use tatami mats on the floor."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor3.png")
            ),

            "neighbor4", new AmbientNPCInfo(
                    "neighbor4",
                    "Villager",
                    new String[]{
                            "Everyday life also teaches culture:",
                            "food, homes and traditions."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor4.png")
            ),

            "nami_fisherman1", new AmbientNPCInfo(
                    "nami_fisherman1",
                    "Fisherman",
                    new String[]{
                            "In Nami No Ura, we go out to sea very early.",
                            "Fresh fish is essential for making good sushi.",
                            "In Japan, we deeply respect what the sea provides."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman1.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman1.png")
            ),

            "nami_fisherman2", new AmbientNPCInfo(
                    "nami_fisherman2",
                    "Harbour Resident",
                    new String[]{
                            "Sushi does not depend only on fish.",
                            "Rice is just as important.",
                            "It must be washed and prepared with great care."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman2.png")
            ),

            "nami_fisherman3", new AmbientNPCInfo(
                    "nami_fisherman3",
                    "Coastal Resident",
                    new String[]{
                            "Nami No Ura is a village connected to the sea.",
                            "Many families here live from fishing",
                            "and the market that arrives at the harbour each morning."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman3.png")
            ),

            "nami_fisherman4", new AmbientNPCInfo(
                    "nami_fisherman4",
                    "Apprentice Cook",
                    new String[]{
                            "An itamae trains for many years.",
                            "The fish must be cut cleanly and precisely.",
                            "Presentation is also part of the art of sushi."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman4.png")
            )
    );

    public static AmbientNPCInfo get(String id) {
        return NPCS.getOrDefault(id, NPCS.get("neighbor1"));
    }
}