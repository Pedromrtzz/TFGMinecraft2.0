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
            ),

            "nami_fisherman1", new AmbientNPCInfo(
                    "nami_fisherman1",
                    "Pescador",
                    new String[]{
                            "En Nami No Ura salimos al mar muy temprano.",
                            "El pescado fresco es esencial para preparar buen sushi.",
                            "En Japón se valora mucho respetar lo que ofrece el mar."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman1.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman1.png")
            ),

            "nami_fisherman2", new AmbientNPCInfo(
                    "nami_fisherman2",
                    "Vecina del puerto",
                    new String[]{
                            "El sushi no depende solo del pescado.",
                            "El arroz es igual de importante.",
                            "Debe lavarse y prepararse con mucho cuidado."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman2.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman2.png")
            ),

            "nami_fisherman3", new AmbientNPCInfo(
                    "nami_fisherman3",
                    "Habitante costero",
                    new String[]{
                            "Nami No Ura es un pueblo unido al mar.",
                            "Aquí muchas familias viven de la pesca",
                            "y del mercado que llega cada mañana al puerto."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman3.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman3.png")
            ),

            "nami_fisherman4", new AmbientNPCInfo(
                    "nami_fisherman4",
                    "Aprendiz de cocina",
                    new String[]{
                            "Un itamae aprende durante años.",
                            "El corte del pescado debe ser limpio y preciso.",
                            "La presentación también forma parte del arte del sushi."
                    },
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/entity/ambient/fisherman4.png"),
                    ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fisherman4.png")
            )
    );

    public static AmbientNPCInfo get(String id) {
        return NPCS.getOrDefault(id, NPCS.get("neighbor1"));
    }
}