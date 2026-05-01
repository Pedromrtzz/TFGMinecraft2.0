package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.CheckChapter3IngredientsC2SPacket;
import com.pedromrtz.tfgmod.network.CompleteChapter3C2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.StartChapter3C2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ItamaeDialogueScreen extends Screen {

    private static final ResourceLocation ITAMAE_PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/itamae.png");

    public record DialogueOption(String text, String nextId) {}
    public record DialogueNode(String title, List<String> lines, List<DialogueOption> options) {}

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public ItamaeDialogueScreen() {
        super(Component.literal("Maestro Itamae"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (!ClientChapter1Data.chapter3Active && !ClientChapter1Data.chapter3Completed) {
            return new DialogueNode(
                    "Maestro Itamae",
                    List.of(
                            "Bienvenido a Nami No Ura, la villa junto al mar.",
                            "Aquí el sushi no es solo comida: es técnica, calma y respeto.",
                            "El arroz es la base, el cuchillo requiere precisión",
                            "y la presentación refleja la estética japonesa.",
                            "¿Quieres aprender a preparar sushi conmigo?"
                    ),
                    List.of(
                            new DialogueOption("Sí, quiero aprender", "start_chapter3"),
                            new DialogueOption("Ahora no", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 1) {
            return new DialogueNode(
                    "Tarea 1: Arroz y pescado",
                    List.of(
                            "Para preparar sushi necesitamos dos ingredientes básicos.",
                            "En Nami No Ura hay un pequeño arrozal cerca de la costa.",
                            "Ve allí y recolecta arroz fresco.",
                            "Después, compra salmón en el mercado de pescado.",
                            "Cuando tengas arroz y salmón, vuelve conmigo."
                    ),
                    List.of(
                            new DialogueOption("Tengo arroz y pescado", "check_ingredients"),
                            new DialogueOption("Voy a buscarlos", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 2) {
            return new DialogueNode(
                    "Maestro Itamae",
                    List.of(
                            "Buen trabajo. Ya tenemos arroz y pescado.",
                            "Antes de preparar sushi, el arroz debe lavarse.",
                            "Este paso elimina el exceso de almidón",
                            "y ayuda a conseguir una textura más limpia.",
                            "Lávalo con calma hasta que el agua quede clara."
                    ),
                    List.of(
                            new DialogueOption("Lavar arroz", "open_rice_washing"),
                            new DialogueOption("Luego lo hago", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 3) {
            return new DialogueNode(
                    "Maestro Itamae",
                    List.of(
                            "Ahora trabajaremos el salmón.",
                            "En la cocina japonesa, el corte debe ser preciso.",
                            "No se trata solo de dividir el pescado,",
                            "sino de respetar su textura y presentación.",
                            "Corta el salmón con calma y buen pulso."
                    ),
                    List.of(
                            new DialogueOption("Cortar salmón", "open_salmon_cutting"),
                            new DialogueOption("Luego lo hago", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 4) {
            return new DialogueNode(
                    "Maestro Itamae",
                    List.of(
                            "Ya tenemos el arroz lavado y el salmón cortado.",
                            "Ahora toca montar el sushi.",
                            "Recuerda mi acertijo:",
                            "primero la base blanca, después el alga,",
                            "luego el corte del mar, y al final la forma."
                    ),
                    List.of(
                            new DialogueOption("Montar sushi", "open_sushi_assembly"),
                            new DialogueOption("Luego lo hago", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 5) {
            return new DialogueNode(
                    "Maestro Itamae",
                    List.of(
                            "Has completado todos los pasos.",
                            "Ahora entrégame el sushi que has preparado.",
                            "Un buen sushi no solo depende del sabor,",
                            "también de la paciencia, la precisión",
                            "y el respeto por cada ingrediente."
                    ),
                    List.of(
                            new DialogueOption("Entregar sushi", "complete_chapter3"),
                            new DialogueOption("Todavía no", "exit")
                    )
            );
        }

        return new DialogueNode(
                "Maestro Itamae",
                List.of(
                        "Sigue practicando con paciencia.",
                        "La cocina japonesa valora la precisión y el respeto por cada ingrediente."
                ),
                List.of(
                        new DialogueOption("Entendido", "exit")
                )
        );
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 450;
        int boxH = 290;
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

        gg.blit(ITAMAE_PORTRAIT,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textX = x + 14 + portraitSize + 18;

        gg.drawString(this.font, currentNode.title(), textX, y + 16, 0xFFFFFF);

        int textY = y + 44;
        for (String line : currentNode.lines()) {
            gg.drawString(this.font, line, textX, textY, 0xEEEEEE);
            textY += 13;
        }

        optionAreas.clear();

        int optionHeight = 22;
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
                    oy + 7,
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
                handleOption(area.option);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleOption(DialogueOption option) {
        playClickSound();

        switch (option.nextId()) {
            case "exit" -> onClose();

            case "start_chapter3" -> {
                ModNetwork.CHANNEL.send(new StartChapter3C2SPacket(), PacketDistributor.SERVER.noArg());
                onClose();
            }

            case "check_ingredients" -> {
                ModNetwork.CHANNEL.send(new CheckChapter3IngredientsC2SPacket(), PacketDistributor.SERVER.noArg());
                onClose();
            }

            case "open_rice_washing" -> {
                Minecraft.getInstance().setScreen(new RiceWashingGameScreen());
            }

            case "open_salmon_cutting" -> {
                Minecraft.getInstance().setScreen(new SalmonCuttingGameScreen());
            }

            case "open_sushi_assembly" -> {
                Minecraft.getInstance().setScreen(new SushiAssemblyGameScreen());
            }

            case "complete_chapter3" -> {
                ModNetwork.CHANNEL.send(new CompleteChapter3C2SPacket(), PacketDistributor.SERVER.noArg());
                onClose();
            }

        }
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
        }
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}