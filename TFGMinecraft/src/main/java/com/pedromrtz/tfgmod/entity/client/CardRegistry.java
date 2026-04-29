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
                "card_japan_family_house",
                "japan",
                "Family House Sakura Town",
                "textures/gui/cards/japan/family_house_thumb.png",
                "gui/cards/japan/family_house_full"
        ));

        ALL.add(new Card(
                "card_japan_toshikoshi_soba",
                "japan",
                "Toshikoshi Soba",
                "textures/gui/cards/japan/toshikoshi_soba_thumb.png",
                "gui/cards/japan/toshikoshi_soba_thumb"
        ));

        ALL.add(new Card(
                "card_japan_omisoka_dinner",
                "japan",
                "Omisoka Dinner",
                "textures/gui/cards/japan/omisoka_dinner_full.png",
                "gui/cards/japan/omisoka_dinner_thumb"
        ));

        ALL.add(new Card(
                "card_japan_ema_wish",
                "japan",
                "Ema Wish at the Temple",
                "textures/gui/cards/japan/ema_wish_full.png",
                "gui/cards/japan/ema_wish_thumb"
        ));

        // aquí ire añadiendo más cartas/culturas
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