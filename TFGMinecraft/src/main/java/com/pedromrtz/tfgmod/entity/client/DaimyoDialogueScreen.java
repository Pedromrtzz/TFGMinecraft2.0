package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.CompleteChapter5C2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class DaimyoDialogueScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/daimyo.png");

    public record DialogueOption(String text, String action) {}
    public record DialogueNode(String title, List<String> lines, List<DialogueOption> options) {}

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public DaimyoDialogueScreen() {
        super(Component.literal("Daimyo"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (ClientChapter1Data.chapter5Completed) {
            return new DialogueNode(
                    "Daimyo",
                    List.of(
                            "You have already completed your audience.",
                            "Remember what you learned inside this castle:",
                            "discipline, loyalty, identity and responsibility."
                    ),
                    List.of(
                            new DialogueOption("I remember.", "exit")
                    )
            );
        }

        if (!ClientChapter1Data.chapter5Active || ClientChapter1Data.chapter5Task != 3) {
            return new DialogueNode(
                    "Daimyo",
                    List.of(
                            "This audience hall is not open to visitors yet.",
                            "Only those who have proven discipline",
                            "may speak before the daimyo."
                    ),
                    List.of(
                            new DialogueOption("Understood.", "exit")
                    )
            );
        }

        return new DialogueNode(
                "Daimyo",
                List.of(
                        "You have passed the castle trials.",
                        "Before this audience ends, you must understand",
                        "the role of this castle and the people within it.",
                        "Ask what you wish to learn."
                ),
                List.of(
                        new DialogueOption("What is a daimyo?", "daimyo_role"),
                        new DialogueOption("How did territories work?", "territory"),
                        new DialogueOption("Why were castles important?", "castles"),
                        new DialogueOption("Tell me about samurai.", "samurai_relation"),
                        new DialogueOption("Complete the audience", "complete")
                )
        );
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 540;
        int boxH = 350;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        int portraitSize = 64;
        int portraitX = x + 14;
        int portraitY = y + 14;

        gg.fill(
                portraitX - 2,
                portraitY - 2,
                portraitX + portraitSize + 2,
                portraitY + portraitSize + 2,
                0xFF111111
        );

        gg.blit(
                PORTRAIT,
                portraitX,
                portraitY,
                0,
                0,
                portraitSize,
                portraitSize,
                portraitSize,
                portraitSize
        );

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

            int color = isMouseOver(mouseX, mouseY, optionX, oy, optionWidth, optionHeight)
                    ? 0xFF555555 : 0xFF333333;

            gg.fill(optionX, oy, optionX + optionWidth, oy + optionHeight, color);

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

            case "daimyo_role" -> currentNode = new DialogueNode(
                    "What is a Daimyo?",
                    List.of(
                            "A daimyo was a powerful regional lord.",
                            "During the Edo period, daimyos governed domains",
                            "across Japan under the authority of the shogun.",
                            "They controlled land, collected resources",
                            "and were responsible for order in their territory."
                    ),
                    List.of(
                            new DialogueOption("How did territories work?", "territory"),
                            new DialogueOption("Back", "main")
                    )
            );

            case "territory" -> currentNode = new DialogueNode(
                    "Territorial Control",
                    List.of(
                            "Each daimyo ruled a domain, known as a han.",
                            "These domains had villages, roads, fields, castles",
                            "and people who depended on local governance.",
                            "The daimyo had to manage taxes, security",
                            "and loyalty to the wider political system."
                    ),
                    List.of(
                            new DialogueOption("Why were castles important?", "castles"),
                            new DialogueOption("Back", "main")
                    )
            );

            case "castles" -> currentNode = new DialogueNode(
                    "Castles and Power",
                    List.of(
                            "A castle was more than a defensive building.",
                            "It was the centre of administration and authority.",
                            "From here, a daimyo could protect the domain,",
                            "organise samurai and show political power.",
                            "Its size and position reflected status and control."
                    ),
                    List.of(
                            new DialogueOption("Tell me about samurai.", "samurai_relation"),
                            new DialogueOption("Back", "main")
                    )
            );

            case "samurai_relation" -> currentNode = new DialogueNode(
                    "Samurai and Hierarchy",
                    List.of(
                            "Samurai served the daimyo through loyalty and duty.",
                            "They acted as warriors, guards and officials.",
                            "During the Edo period, hierarchy was very important:",
                            "the daimyo ruled the domain, and samurai supported",
                            "that rule through service, discipline and honour."
                    ),
                    List.of(
                            new DialogueOption("Complete the audience", "complete"),
                            new DialogueOption("Back", "main")
                    )
            );

            case "main" -> currentNode = getInitialNode();

            case "complete" -> {
                if (ClientChapter1Data.chapter5Active && ClientChapter1Data.chapter5Task == 3) {
                    ModNetwork.CHANNEL.send(
                            new CompleteChapter5C2SPacket(),
                            PacketDistributor.SERVER.noArg()
                    );
                }

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