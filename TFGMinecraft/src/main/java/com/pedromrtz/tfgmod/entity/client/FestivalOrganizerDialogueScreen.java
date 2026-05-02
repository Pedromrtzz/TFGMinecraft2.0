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
                            "The Matsuri was a success thanks to you.",
                            "The lanterns, games and floating lights are ready.",
                            "Festivals like this celebrate community, tradition and joy."
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
                            "Hello! Welcome to Sakura Town's Matsuri.",
                            "A Matsuri is a traditional Japanese festival",
                            "with food, games, lanterns and cultural activities.",
                            "But we still need to prepare a few things.",
                            "Could you help us before the festival begins?"
                    ),
                    List.of(
                            new DialogueOption("Yes, I will help.", "start_chapter4"),
                            new DialogueOption("Maybe later.", "exit")
                    )
            );
        }

        return switch (ClientChapter1Data.chapter4Task) {
            case 1 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "First, the festival lanterns are not working.",
                            "Please speak with the electrician near the entrance.",
                            "He will help you restore the lights."
                    ),
                    List.of(
                            new DialogueOption("I'll speak with him.", "exit")
                    )
            );

            case 2 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "The lanterns are ready now.",
                            "Next, we need to test the goldfish scooping stall.",
                            "Please speak with the stall owner near the games area."
                    ),
                    List.of(
                            new DialogueOption("I'll go to the stall.", "exit")
                    )
            );

            case 3 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "Good work with the fishing game.",
                            "Now we need to test the target shooting stall.",
                            "Please speak with the game attendant."
                    ),
                    List.of(
                            new DialogueOption("I'll test it.", "exit")
                    )
            );

            case 4 -> new DialogueNode(
                    "Festival Organizer",
                    List.of(
                            "Only one final activity remains.",
                            "At the river, people release floating lanterns",
                            "as a peaceful and symbolic moment of the festival.",
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
                            "The Matsuri cannot begin until everything is ready."
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

        int boxW = 460;
        int boxH = 300;
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