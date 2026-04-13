package com.pedromrtz.tfgmod.entity.client;

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
import java.util.Map;

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
                            "Antes de cenar toshikoshi soba, necesitamos preparar varias cosas.",
                            "¿Me ayudarías?"
                    ),
                    List.of(
                            new DialogueOption("Sí, te ayudaré", "start_chapter2"),
                            new DialogueOption("Ahora no", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter2Task == 1 || ClientChapter1Data.chapter2Task == 2) {
            return new DialogueNode(
                    "ingredients",
                    "Madre",
                    List.of(
                            "Necesitamos los ingredientes para el toshikoshi soba.",
                            "Tráeme fideos, alga, caldo, cebolla y carne.",
                            "Cuando los tengas, yo los revisaré."
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
                            "Ahora podemos empezar a cocinar el toshikoshi soba."
                    ),
                    List.of(
                            new DialogueOption("Entendido", "exit")
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

        int boxW = 360;
        int boxH = 270;
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

        gg.blit(MOTHER_PORTRAIT,
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