package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.client.ClientChapter1Data;
import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.StartChapter5C2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SamuraiGuardDialogueScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/samurai_guard.png");

    public record DialogueOption(String text, String action) {}
    public record DialogueNode(String title, List<String> lines, List<DialogueOption> options) {}

    private DialogueNode currentNode;
    private final List<OptionArea> optionAreas = new ArrayList<>();

    private record OptionArea(int x, int y, int w, int h, DialogueOption option) {}

    public SamuraiGuardDialogueScreen() {
        super(Component.literal("Samurai Guard"));
        this.currentNode = getInitialNode();
    }

    private DialogueNode getInitialNode() {
        if (ClientChapter1Data.chapter5Completed) {
            return new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "You have already earned the daimyo's respect.",
                            "You showed discipline, focus and understanding.",
                            "Walk through this castle with honour."
                    ),
                    List.of(
                            new DialogueOption("Thank you.", "exit")
                    )
            );
        }

        if (!ClientChapter1Data.chapter5Active) {
            return new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "Halt, traveller.",
                            "You stand before the castle of the daimyo.",
                            "During the Edo period, samurai served as warriors,",
                            "guards and officials under powerful lords.",
                            "If you wish to enter, you must first understand discipline."
                    ),
                    List.of(
                            new DialogueOption("Tell me about samurai.", "samurai_info"),
                            new DialogueOption("I am ready.", "start_chapter5"),
                            new DialogueOption("Not now.", "exit")
                    )
            );
        }

        return switch (ClientChapter1Data.chapter5Task) {
            case 1 -> new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "Your first task is training.",
                            "Many people think only of the katana,",
                            "but the bow, or yumi, was also important.",
                            "A samurai must control breathing, timing and focus.",
                            "Go to the training yard and speak with the archery instructor."
                    ),
                    List.of(
                            new DialogueOption("I will find the instructor.", "exit")
                    )
            );

            case 2 -> new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "You have shown focus with the bow.",
                            "Now the inner castle gate must be opened.",
                            "Speak with the castle gatekeeper.",
                            "He will test your understanding of clan symbols."
                    ),
                    List.of(
                            new DialogueOption("I will go to the gatekeeper.", "exit")
                    )
            );

            case 3 -> new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "The way is open.",
                            "You may now enter the audience hall.",
                            "The daimyo will explain his role",
                            "and the structure of power during the Edo period."
                    ),
                    List.of(
                            new DialogueOption("I will meet the daimyo.", "exit")
                    )
            );

            default -> new DialogueNode(
                    "Samurai Guard",
                    List.of(
                            "Keep your posture steady.",
                            "A samurai's strength begins with self-control."
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

        int boxW = 510;
        int boxH = 315;
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

            case "samurai_info" -> this.currentNode = new DialogueNode(
                    "Samurai and Bushido",
                    List.of(
                            "Samurai were members of Japan's warrior class.",
                            "In the Edo period, they also served as administrators",
                            "and protectors of their lord's domain.",
                            "Their ideals were linked to discipline, loyalty and honour.",
                            "This code of conduct is often associated with bushido."
                    ),
                    List.of(
                            new DialogueOption("I understand.", "start_chapter5"),
                            new DialogueOption("Not now.", "exit")
                    )
            );

            case "start_chapter5" -> {
                ModNetwork.CHANNEL.send(
                        new StartChapter5C2SPacket(),
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