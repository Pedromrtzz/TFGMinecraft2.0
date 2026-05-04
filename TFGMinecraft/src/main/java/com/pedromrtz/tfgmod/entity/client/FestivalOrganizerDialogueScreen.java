package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.StartChapter4C2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class FestivalOrganizerDialogueScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/guide.png");

    public record DialogueOption(String text, String action) {}
    public record DialogueNode(String title, List<String> lines, List<DialogueOption> options) {}

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public FestivalOrganizerDialogueScreen() {
        super(Component.literal("Festival Organizer"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (ClientChapter1Data.chapter4Completed) {
            return new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "You did it. The Matsuri was a success because of your help.",
                            "The lanterns guided the visitors, the stalls were full of life,",
                            "and the floating lights carried wishes across the river.",
                            "A Matsuri is not only a celebration.",
                            "It is a moment where people share tradition, community and joy."
                    ),
                    List.of(
                            new DialogueOption("I'm glad I could help.", "exit")
                    )
            );
        }

        if (!ClientChapter1Data.chapter4Active) {
            return new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "Welcome! You arrived just in time.",
                            "Tonight, Sakura Town celebrates its Matsuri.",
                            "A Matsuri is a traditional Japanese festival",
                            "often connected to shrines, seasons and local community.",
                            "People gather to enjoy food, games, music and light.",
                            "But before the festival begins, we need your help."
                    ),
                    List.of(
                            new DialogueOption("What is a Matsuri?", "explain_matsuri"),
                            new DialogueOption("How can I help?", "start_chapter4"),
                            new DialogueOption("Maybe later.", "exit")
                    )
            );
        }

        return switch (ClientChapter1Data.chapter4Task) {
            case 1 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "The first problem is with the festival lanterns.",
                            "Lanterns are important because they create the warm",
                            "night atmosphere that makes a Matsuri feel alive.",
                            "Please speak with the electrician near the entrance.",
                            "He knows the old wiring system better than anyone."
                    ),
                    List.of(
                            new DialogueOption("I'll speak with him.", "exit")
                    )
            );

            case 2 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "The lanterns are shining again. Wonderful work.",
                            "Next, we need to test the goldfish scooping stall.",
                            "Festival games are not only entertainment;",
                            "they also bring families and visitors together.",
                            "Please speak with the stall owner near the games area."
                    ),
                    List.of(
                            new DialogueOption("I'll go to the stall.", "exit")
                    )
            );

            case 3 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "The fishing stall is ready now.",
                            "Next, we must test the target shooting game.",
                            "Games like this reward focus, timing and precision.",
                            "Please speak with the game attendant.",
                            "Make sure the stall is ready before visitors arrive."
                    ),
                    List.of(
                            new DialogueOption("I'll test it.", "exit")
                    )
            );

            case 4 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "Only one final activity remains.",
                            "At the river, people release floating lanterns.",
                            "They can represent wishes, memories, hope or gratitude.",
                            "It is a quiet moment after the noise of the festival.",
                            "Please speak with the lantern keeper near the river."
                    ),
                    List.of(
                            new DialogueOption("I'll go to the river.", "exit")
                    )
            );

            default -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "Please continue helping the festival staff.",
                            "The Matsuri cannot begin until everything is ready.",
                            "Every small task helps the whole community celebrate."
                    ),
                    List.of(
                            new DialogueOption("Understood.", "exit")
                    )
            );
        };
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 315;
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

        gg.blit(PORTRAIT,
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

            gg.drawCenteredString(
                    this.font,
                    opt.text(),
                    optionX + optionWidth / 2,
                    oy + 7,
                    0xFFFFFF
            );

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

        switch (option.action()) {
            case "exit" -> onClose();

            case "explain_matsuri" -> {
                this.currentNode = new DialogueNode(
                        "What is a Matsuri?",
                        List.of(
                                "Matsuri festivals have existed for centuries in Japan.",
                                "Many are linked to Shinto shrines, local legends or seasonal events.",
                                "They can celebrate harvests, honour spirits or bring communities together.",
                                "Food stalls, lanterns and games make the festival enjoyable,",
                                "but preparation and respect are just as important.",
                                "That is why I need help before the celebration begins."
                        ),
                        List.of(
                                new DialogueOption("I understand. How can I help?", "start_chapter4"),
                                new DialogueOption("Maybe later.", "exit")
                        )
                );
            }

            case "start_chapter4" -> {
                ModNetwork.CHANNEL.send(
                        new StartChapter4C2SPacket(),
                        PacketDistributor.SERVER.noArg()
                );
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