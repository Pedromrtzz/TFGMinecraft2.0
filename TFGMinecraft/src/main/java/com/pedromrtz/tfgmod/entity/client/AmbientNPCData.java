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
                            "El sushi no es solo comida.",
                            "También representa técnica, equilibrio",
                            "y respeto por los ingredientes."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/chef.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/chef.png")
            ),

            "fisherman", new AmbientNPCInfo(
                    "fisherman",
                    "Pescador",
                    new String[]{
                            "Los peces koi son muy importantes en Japón.",
                            "Simbolizan perseverancia, esfuerzo",
                            "y buena fortuna."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman.png")
            ),

            "neighbor1", new AmbientNPCInfo(
                    "neighbor1",
                    "Vecino",
                    new String[]{
                            "Los cerezos sakura florecen solo",
                            "durante un tiempo muy corto del año."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor.png")
            ),

            "neighbor2", new AmbientNPCInfo(
                    "neighbor2",
                    "Vecina",
                    new String[]{
                            "En Japón, quitarse los zapatos al entrar",
                            "en casa es una muestra de respeto."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor2.png")
            ),

            "neighbor3", new AmbientNPCInfo(
                    "neighbor3",
                    "Vecino",
                    new String[]{
                            "Muchas casas tradicionales japonesas",
                            "usan tatami en el suelo."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor3.png")
            ),

            "neighbor4", new AmbientNPCInfo(
                    "neighbor4",
                    "Vecina",
                    new String[]{
                            "La vida cotidiana también enseña cultura:",
                            "comida, casas y costumbres."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/neighbor4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/neighbor4.png")
            )
    );

    public static AmbientNPCInfo get(String id) {
        return NPCS.getOrDefault(id, NPCS.get("neighbor1"));
    }
}