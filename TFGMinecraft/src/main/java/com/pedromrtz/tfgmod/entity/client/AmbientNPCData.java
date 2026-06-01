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

    public static final Map<String, AmbientNPCInfo> NPCS = Map.ofEntries(
            Map.entry("chef", new AmbientNPCInfo(
                    "chef",
                    "Chef",
                    new String[]{
                            "Sushi is not just food.",
                            "It also represents technique, balance",
                            "and respect for the ingredients."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/chef.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/chef.png")
            )),

            Map.entry("fisherman", new AmbientNPCInfo(
                    "fisherman",
                    "Fisherman",
                    new String[]{
                            "Koi fish are very important in Japan.",
                            "They symbolise perseverance, effort",
                            "and good fortune."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman.png")
            )),

            Map.entry("neighbor1", new AmbientNPCInfo(
                    "neighbor1",
                    "Villager",
                    new String[]{
                            "Sakura cherry trees bloom",
                            "only for a short time each year."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor.png")
            )),

            Map.entry("neighbor2", new AmbientNPCInfo(
                    "neighbor2",
                    "Villager",
                    new String[]{
                            "In Japan, removing your shoes before",
                            "entering a home is a sign of respect."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor2.png")
            )),

            Map.entry("neighbor3", new AmbientNPCInfo(
                    "neighbor3",
                    "Villager",
                    new String[]{
                            "Many traditional Japanese houses",
                            "use tatami mats on the floor."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor3.png")
            )),

            Map.entry("neighbor4", new AmbientNPCInfo(
                    "neighbor4",
                    "Villager",
                    new String[]{
                            "Everyday life also teaches culture:",
                            "food, homes and traditions."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor4.png")
            )),

            Map.entry("nami_fisherman1", new AmbientNPCInfo(
                    "nami_fisherman1",
                    "Fisherman",
                    new String[]{
                            "In Nami No Ura, we go out to sea very early.",
                            "Fresh fish is essential for making good sushi.",
                            "In Japan, we deeply respect what the sea provides."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman1.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman1.png")
            )),

            Map.entry("nami_fisherman2", new AmbientNPCInfo(
                    "nami_fisherman2",
                    "Harbour Resident",
                    new String[]{
                            "Sushi does not depend only on fish.",
                            "Rice is just as important.",
                            "It must be washed and prepared with great care."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman2.png")
            )),

            Map.entry("nami_fisherman3", new AmbientNPCInfo(
                    "nami_fisherman3",
                    "Coastal Resident",
                    new String[]{
                            "Nami No Ura is a village connected to the sea.",
                            "Many families here live from fishing",
                            "and the market that arrives at the harbour each morning."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman3.png")
            )),

            Map.entry("nami_fisherman4", new AmbientNPCInfo(
                    "nami_fisherman4",
                    "Apprentice Cook",
                    new String[]{
                            "An itamae trains for many years.",
                            "The fish must be cut cleanly and precisely.",
                            "Presentation is also part of the art of sushi."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman4.png")
            )),

            Map.entry("matsuri_boy1", new AmbientNPCInfo(
                    "matsuri_boy1",
                    "Festival Visitor",
                    new String[]{
                            "I love Matsuri nights!",
                            "The lanterns, food stalls and games",
                            "make the whole town feel alive."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/boy1.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/boy1.png")
            )),

            Map.entry("matsuri_boy2", new AmbientNPCInfo(
                    "matsuri_boy2",
                    "Festival Player",
                    new String[]{
                            "Festival games look simple,",
                            "but they need patience and precision.",
                            "That is what makes them so fun!"
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/boy2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/boy2.png")
            )),

            Map.entry("matsuri_girl1", new AmbientNPCInfo(
                    "matsuri_girl1",
                    "Festival Visitor",
                    new String[]{
                            "During a Matsuri, people gather",
                            "to celebrate tradition, food and community.",
                            "It is one of my favourite nights of the year."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/girl1.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/girl1.png")
            )),

            Map.entry("matsuri_girl2", new AmbientNPCInfo(
                    "matsuri_girl2",
                    "Lantern Visitor",
                    new String[]{
                            "Floating lanterns feel peaceful.",
                            "People release them with wishes, memories",
                            "or hopes for the future."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/girl2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/girl2.png")
            ))
    );

    public static AmbientNPCInfo get(String id) {
        return NPCS.getOrDefault(id, NPCS.get("neighbor1"));
    }
}