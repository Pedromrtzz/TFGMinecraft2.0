package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.CheckChapter3IngredientsC2SPacket;
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
        super(Component.literal("Itamae Master"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (!ClientChapter1Data.chapter3Active && !ClientChapter1Data.chapter3Completed) {
            return new DialogueNode(
                    "Itamae Master",
                    List.of(
                            "Welcome to Nami No Ura, the village by the sea.",
                            "Here, sushi is not just food: it is technique, calm and respect.",
                            "Rice is the foundation, the knife requires precision,",
                            "and presentation reflects Japanese aesthetics.",
                            "Would you like to learn how to prepare sushi with me?"
                    ),
                    List.of(
                            new DialogueOption("Yes, I want to learn", "start_chapter3"),
                            new DialogueOption("Not now", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 1) {
            return new DialogueNode(
                    "Task 1: Rice and Fish",
                    List.of(
                            "To prepare sushi, we need two essential ingredients.",
                            "In Nami No Ura, there is a small rice field near the coast.",
                            "Go there and collect fresh rice.",
                            "Then, buy salmon at the fish market.",
                            "When you have both, return to me."
                    ),
                    List.of(
                            new DialogueOption("I have rice and fish", "check_ingredients"),
                            new DialogueOption("I'll go get them", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 2) {
            return new DialogueNode(
                    "Itamae Master",
                    List.of(
                            "Good work. We now have rice and fish.",
                            "Before preparing sushi, the rice must be washed.",
                            "This step removes excess starch",
                            "and helps achieve a cleaner texture.",
                            "Wash it carefully until the water becomes clear."
                    ),
                    List.of(
                            new DialogueOption("Wash rice", "open_rice_washing"),
                            new DialogueOption("I'll do it later", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 3) {
            return new DialogueNode(
                    "Itamae Master",
                    List.of(
                            "Now we will work with the salmon.",
                            "In Japanese cuisine, cutting must be precise.",
                            "It is not just about dividing the fish,",
                            "but respecting its texture and presentation.",
                            "Cut the salmon calmly and with steady hands."
                    ),
                    List.of(
                            new DialogueOption("Cut salmon", "open_salmon_cutting"),
                            new DialogueOption("I'll do it later", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 4) {
            return new DialogueNode(
                    "Itamae Master",
                    List.of(
                            "We now have the rice prepared and the salmon cut.",
                            "It is time to assemble the sushi.",
                            "Remember my riddle:",
                            "first the white base, then the seaweed,",
                            "then the cut from the sea, and finally the form."
                    ),
                    List.of(
                            new DialogueOption("Assemble sushi", "open_sushi_assembly"),
                            new DialogueOption("I'll do it later", "exit")
                    )
            );
        }

        if (ClientChapter1Data.chapter3Active && ClientChapter1Data.chapter3Task == 5) {
            return new DialogueNode(
                    "Itamae Master",
                    List.of(
                            "You have completed all the steps.",
                            "Now, give me the sushi you have prepared.",
                            "Good sushi depends not only on taste,",
                            "but also on patience, precision",
                            "and respect for every ingredient."
                    ),
                    List.of(
                            new DialogueOption("Deliver sushi", "complete_chapter3"),
                            new DialogueOption("Not yet", "exit")
                    )
            );
        }

        return new DialogueNode(
                "Itamae Master",
                List.of(
                        "Keep practising with patience.",
                        "Japanese cuisine values precision and respect for each ingredient."
                ),
                List.of(
                        new DialogueOption("Understood", "exit")
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
                Minecraft.getInstance().setScreen(new ItamaeFinalSceneScreen());
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