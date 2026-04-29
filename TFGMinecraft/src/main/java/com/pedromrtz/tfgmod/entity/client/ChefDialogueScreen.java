package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChefDialogueScreen extends Screen {

    public record DialogueOption(String text, String nextId) {}
    public record DialogueNode(String id, String title, List<String> bodyLines, List<DialogueOption> options) {}

    private static final Map<String, DialogueNode> NODES = Map.of(
            "intro", new DialogueNode(
                    "intro",
                    "Chef de sushi",
                    List.of(
                            "¡Bienvenido a mi cocina!",
                            "El sushi no es solo comida rápida japonesa.",
                            "Detrás de cada pieza hay historia, técnica y respeto",
                            "por los ingredientes."
                    ),
                    List.of(
                            new DialogueOption("Cuéntame la historia del sushi", "history"),
                            new DialogueOption("¿Qué tipos de sushi existen?", "types"),
                            new DialogueOption("Quiero practicar en la cocina", "practice"),
                            new DialogueOption("Nada por ahora, gracias", "exit")
                    )
            ),
            "history", new DialogueNode(
                    "history",
                    "Historia del sushi",
                    List.of(
                            "El origen del sushi se remonta a técnicas antiguas",
                            "para conservar el pescado usando arroz fermentado.",
                            "Con el tiempo, en Japón se transformó en el sushi",
                            "moderno que se prepara con arroz avinagrado y pescado fresco."
                    ),
                    List.of(
                            new DialogueOption("¿Y qué tipos de sushi hay?", "types"),
                            new DialogueOption("Quiero practicar en la cocina", "practice"),
                            new DialogueOption("Volver al principio", "intro")
                    )
            ),
            "types", new DialogueNode(
                    "types",
                    "Tipos de sushi",
                    List.of(
                            "Existen muchos tipos de sushi, pero algunos de los más",
                            "conocidos son:",
                            "- Nigiri: bola de arroz con una lámina de pescado encima.",
                            "- Maki: rollo de arroz y relleno envuelto en alga nori.",
                            "- Temaki: cono de alga relleno de arroz e ingredientes."
                    ),
                    List.of(
                            new DialogueOption("Quiero practicar preparando maki", "practice"),
                            new DialogueOption("Cuéntame la historia del sushi", "history"),
                            new DialogueOption("Volver al principio", "intro")
                    )
            ),
            "practice", new DialogueNode(
                    "practice",
                    "Práctica en la cocina",
                    List.of(
                            "Perfecto. Empezaremos con algo sencillo:",
                            "un maki básico de arroz, alga y salmón.",
                            "Primero te enseñaré el orden de los ingredientes,",
                            "y luego podrás practicar en el minijuego."
                    ),
                    List.of(
                            new DialogueOption("Abrir minijuego Sushi Maker", "open_sushi_maker"),
                            new DialogueOption("Volver al principio", "intro")
                    )
            )
    );

    private DialogueNode currentNode;

    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public ChefDialogueScreen() {
        super(Component.literal("Chef de sushi"));
        this.currentNode = NODES.get("intro");
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 340;
        int boxH = 260; // <- un poco más alto
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xCC000000);

        gg.drawCenteredString(this.font,
                currentNode.title(),
                this.width / 2,
                y + 12,
                0xFFFFFF);

        int textY = y + 38;
        int lineHeight = 12;

        for (String line : currentNode.bodyLines()) {
            gg.drawString(this.font, line, x + 14, textY, 0xEEEEEE);
            textY += lineHeight;
        }

        optionAreas.clear();

        int optionHeight = 18;
        int optionWidth = boxW - 28;
        int optionX = x + 14;

        int optionsBlockHeight = currentNode.options().size() * (optionHeight + 6) - 6;

        int optionYStart = y + boxH - optionsBlockHeight - 16;

        int idx = 0;
        for (DialogueOption opt : currentNode.options()) {
            int oy = optionYStart + idx * (optionHeight + 6);

            int bgColor = isMouseOver(mouseX, mouseY, optionX, oy, optionWidth, optionHeight)
                    ? 0xFF555555 : 0xFF333333;

            gg.fill(optionX, oy, optionX + optionWidth, oy + optionHeight, bgColor);

            gg.drawCenteredString(this.font,
                    opt.text(),
                    optionX + optionWidth / 2,
                    oy + 5,
                    0xFFFFFF);

            optionAreas.add(new OptionArea(optionX, oy, optionWidth, optionHeight, opt));

            idx++;
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Miramos en qué opción ha hecho click
        for (OptionArea area : optionAreas) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                handleOptionClick(area.option);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleOptionClick(DialogueOption option) {
        playClickSound();

        String next = option.nextId();

        if (next == null) return;

        if (next.equals("exit")) {
            onClose();
            return;
        }

        if (next.equals("open_sushi_maker")) {
            Minecraft.getInstance().setScreen(
                    new OnigiriGameScreen()
            );
            return;
        }

        DialogueNode node = NODES.get(next);
        if (node != null) {
            this.currentNode = node;
        }
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}