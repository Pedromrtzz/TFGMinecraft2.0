// com.pedromrtz.tfgmod.entity.client.CardRegistry
package com.pedromrtz.tfgmod.entity.client;

import net.minecraft.resources.ResourceLocation;
import java.util.*;
import java.util.stream.Collectors;

public class CardRegistry {

    public record Card(String id, String culture, String title,
                       String thumbTexPath, String fullTexPath) {}

    private static final List<Card> ALL = new ArrayList<>();

    static {
        // JAPÓN
        ALL.add(new Card(
                "card_japan_fushimi_inari",
                "japan",
                "Fushimi Inari Taisha",
                "textures/gui/cards/japan/fushimi_inari_thumb.png",           // ✅ CORRECTO
                "gui/cards/japan/fushimi_inari_full"
        ));

        ALL.add(new Card(
                "card_japan_kinkakuji",                  // id
                "japan",                                 // cultura (tab)
                "Templo Kinkaku-ji",                     // título (por si luego lo muestras en grid)
                "textures/gui/cards/japan/kinkakuji_thumb.png",  // mini
                "textures/gui/cards/japan/kinkakuji_full.png"    // (guardamos también la grande)
        ));

        // aquí irás añadiendo más cartas/culturas
    }

    public static List<Card> byCulture(String culture) {
        return ALL.stream().filter(c -> c.culture.equals(culture)).collect(Collectors.toList());
    }
    public static List<String> cultures() {
        return ALL.stream().map(Card::culture).distinct().toList();
    }
    public static Card byId(String id) {
        for (Card c : ALL) if (c.id.equals(id)) return c;
        return null;
    }

    public static ResourceLocation tex(String ns, String path) {
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }
}