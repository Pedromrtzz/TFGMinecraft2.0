package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.capitulo2.CookingGameScreen;
import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.CheckChapter2IngredientsC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.StartChapter2C2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MotherDialogueScreen extends Screen {

    private static final ResourceLocation MOTHER_PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/mother.png");

    public record DialogueOption(String text, String nextId) {}
    public record DialogueNode(String id, String title, List<String> bodyLines, List<DialogueOption> options) {}

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public MotherDialogueScreen() {
        super(Component.literal("Madre"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (!ClientChapter1Data.chapter2Active) {
            return new DialogueNode(
                    "intro",
                    "Madre",
                    List.of(
                            "Hoy es Omisoka, la nochevieja japonesa.",
                            "Para nuestra familia es un día muy especial.",
                            "Antes de cenar toshikoshi soba, necesitamos",
                            "preparar varias cosas. ¿Me ayudarías?"
                    ),
                    List.of(
                            new DialogueOption("Sí, te ayudaré", "start_chapter2"),
                            new DialogueOption("Ahora no", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 1) {
            return new DialogueNode(
                    "go_market",
                    "Madre",
                    List.of(
                            "Necesitamos ingredientes para el toshikoshi soba.",
                            "Ve al mercado que está a las afueras del pueblo.",
                            "Compra fideos, alga, caldo, cebolla y carne.",
                            "Te he dado yenes para que puedas comprarlos."
                    ),
                    List.of(
                            new DialogueOption("Revisar ingredientes", "check_ingredients"),
                            new DialogueOption("Voy al mercado", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 2) {
            return new DialogueNode(
                    "check_market",
                    "Madre",
                    List.of(
                            "¿Ya has vuelto del mercado?",
                            "Recuerda que necesitamos fideos, alga, caldo,",
                            "cebolla y carne para preparar el toshikoshi soba.",
                            "Si te falta algo, vuelve al mercado de las afueras."
                    ),
                    List.of(
                            new DialogueOption("Aquí tienes los ingredientes", "check_ingredients"),
                            new DialogueOption("Volveré cuando los tenga", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 3) {
            return new DialogueNode(
                    "ready_to_cook",
                    "Madre",
                    List.of(
                            "Perfecto, ya tenemos todos los ingredientes.",
                            "Ahora debemos cocinar el toshikoshi soba.",
                            "Hazlo en el orden correcto para que salga bien."
                    ),
                    List.of(
                            new DialogueOption("Empezar a cocinar", "open_cooking_game"),
                            new DialogueOption("Luego lo hago", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 4) {
            return new DialogueNode(
                    "after_cooking",
                    "Madre",
                    List.of(
                            "¡Ha quedado perfecto!",
                            "Ahora debemos preparar la mesa",
                            "para la cena familiar de Omisoka."
                    ),
                    List.of(
                            new DialogueOption("Entendido", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 5) {
            return new DialogueNode(
                    "dinner",
                    "Madre",
                    List.of(
                            "La mesa ya está preparada.",
                            "Ahora podemos cenar todos juntos",
                            "y compartir este momento en familia."
                    ),
                    List.of(
                            new DialogueOption("Vamos a cenar", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 6) {
            return new DialogueNode(
                    "temple",
                    "Madre",
                    List.of(
                            "Después de cenar, debemos ir al templo.",
                            "Está a las afueras del pueblo.",
                            "Allí escucharás el significado de las 108 campanadas."
                    ),
                    List.of(
                            new DialogueOption("Iré al templo", "exit")
                    )
            );
        }

        return new DialogueNode(
                "default",
                "Madre",
                List.of(
                        "Sigamos adelante poco a poco.",
                        "Todavía nos quedan cosas por preparar."
                ),
                List.of(
                        new DialogueOption("De acuerdo", "exit")
                )
        );
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 430;
        int boxH = 285;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        int portraitSize = 64;
        int portraitX = x + 14;
        int portraitY = y + 14;

        gg.fill(portraitX - 2, portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111);

        gg.blit(MOTHER_PORTRAIT,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textStartX = x + 14 + portraitSize + 18;

        gg.drawString(this.font,
                currentNode.title(),
                textStartX,
                y + 16,
                0xFFFFFF);

        int textY = y + 42;
        int lineHeight = 13;

        for (String line : currentNode.bodyLines()) {
            gg.drawString(this.font, line, textStartX, textY, 0xEEEEEE);
            textY += lineHeight;
        }

        optionAreas.clear();

        int optionHeight = 20;
        int optionWidth = boxW - 28;
        int optionX = x + 14;

        int optionsBlockHeight = currentNode.options().size() * (optionHeight + 7) - 7;
        int optionYStart = y + boxH - optionsBlockHeight - 16;

        int idx = 0;
        for (DialogueOption opt : currentNode.options()) {
            int oy = optionYStart + idx * (optionHeight + 7);

            int bgColor = isMouseOver(mouseX, mouseY, optionX, oy, optionWidth, optionHeight)
                    ? 0xFF555555 : 0xFF333333;

            gg.fill(optionX, oy, optionX + optionWidth, oy + optionHeight, bgColor);

            gg.drawCenteredString(this.font,
                    opt.text(),
                    optionX + optionWidth / 2,
                    oy + 6,
                    0xFFFFFF);

            optionAreas.add(new OptionArea(optionX, oy, optionWidth, optionHeight, opt));
            idx++;
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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

        if (next.equals("start_chapter2")) {
            ModNetwork.CHANNEL.send(new StartChapter2C2SPacket(), PacketDistributor.SERVER.noArg());
            onClose();
            return;
        }

        if (next.equals("check_ingredients")) {
            ModNetwork.CHANNEL.send(new CheckChapter2IngredientsC2SPacket(), PacketDistributor.SERVER.noArg());
            onClose();
            return;
        }

        if (next.equals("open_cooking_game")) {
            Minecraft.getInstance().setScreen(new CookingGameScreen());
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