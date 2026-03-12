package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.StartMission1C2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElderDialogueScreen extends Screen {

    private static final ResourceLocation ELDER_PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/elder.png");

    public record DialogueOption(String text, String nextId) {}
    public record DialogueNode(String id, String title, List<String> bodyLines, List<DialogueOption> options) {}

    private static final Map<String, DialogueNode> NODES = Map.of(
            "intro", new DialogueNode(
                    "intro",
                    "Anciano del pueblo",
                    List.of(
                            "Bienvenido a Sakura Town, viajero.",
                            "Este lugar guarda tradiciones, historia y respeto por la cultura.",
                            "Si quieres aprender de Japón, primero debes explorar y escuchar."
                    ),
                    List.of(
                            new DialogueOption("¿Qué es Sakura Town?", "town"),
                            new DialogueOption("¿Cuál es mi misión?", "mission"),
                            new DialogueOption("¿Qué es el Álbum Cultural?", "album"),
                            new DialogueOption("Gracias, me iré a explorar", "exit")
                    )
            ),

            "town", new DialogueNode(
                    "town",
                    "Sakura Town",
                    List.of(
                            "Sakura Town es un pueblo inspirado en la vida tradicional japonesa.",
                            "Aquí encontrarás casas, templos y lugares cotidianos.",
                            "Cada zona es una oportunidad para aprender algo real."
                    ),
                    List.of(
                            new DialogueOption("¿Cuál es mi misión?", "mission"),
                            new DialogueOption("¿Qué es el Álbum Cultural?", "album"),
                            new DialogueOption("Volver", "intro")
                    )
            ),

            "mission", new DialogueNode(
                    "mission",
                    "Misión 1: Explorar el pueblo",
                    List.of(
                            "Tu primera misión es sencilla:",
                            "Explora Sakura Town y encuentra la casa de la familia.",
                            "Cuando entres en la casa, obtendrás un cromo cultural."
                    ),
                    List.of(
                            new DialogueOption("Entendido. ¿Qué es el Álbum Cultural?", "album"),
                            new DialogueOption("Volver", "intro"),
                            new DialogueOption("Salir", "exit")
                    )
            ),

            "album", new DialogueNode(
                    "album",
                    "Álbum Cultural",
                    List.of(
                            "El Álbum Cultural guarda los cromos que desbloqueas.",
                            "Cada cromo representa una parte de la cultura: gastronomía, historia, festividades…",
                            "Completa misiones para conseguirlos."
                    ),
                    List.of(
                            new DialogueOption("Perfecto, empezaré la misión", "start_mission"),
                            new DialogueOption("Volver", "intro"),
                            new DialogueOption("Salir", "exit")
                    )
            )
    );

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();
    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public ElderDialogueScreen() {
        super(Component.literal("Anciano"));
        this.currentNode = NODES.get("intro");
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 340;
        int boxH = 260;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xCC000000);

        int portraitSize = 64;
        int portraitX = x + 12;
        int portraitY = y + 12;

        gg.fill(portraitX - 2, portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111);
        
        gg.blit(ELDER_PORTRAIT,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textStartX = x + 14 + portraitSize + 14;

        gg.drawString(this.font,
                currentNode.title(),
                textStartX,
                y + 14,
                0xFFFFFF);

        int textY = y + 38;
        int lineHeight = 12;

        for (String line : currentNode.bodyLines()) {
            gg.drawString(this.font, line, textStartX, textY, 0xEEEEEE);
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

        if (next.equals("start_mission")) {
            ModNetwork.CHANNEL.send(new StartMission1C2SPacket(), PacketDistributor.SERVER.noArg());
            onClose();
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