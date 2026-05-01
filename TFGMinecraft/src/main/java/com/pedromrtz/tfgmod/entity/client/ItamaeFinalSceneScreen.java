package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.CompleteChapter3C2SPacket;
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

public class ItamaeFinalSceneScreen extends Screen {

    private static final ResourceLocation ITAMAE_PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/itamae.png");

    private int step = 0;
    private boolean completed = false;

    private final List<DialogueLine> dialogue = List.of(
            new DialogueLine("Maestro Itamae", "Déjame probar el sushi que has preparado.", 0xFF66CCFF),
            new DialogueLine("Maestro Itamae", "El arroz está limpio y bien tratado.", 0xFF66CCFF),
            new DialogueLine("Maestro Itamae", "El salmón tiene un corte preciso y respetuoso.", 0xFF66CCFF),
            new DialogueLine("Maestro Itamae", "Has entendido que el sushi no es solo comida.", 0xFF66CCFF),
            new DialogueLine("Maestro Itamae", "Es equilibrio entre técnica, paciencia y estética.", 0xFF66CCFF),
            new DialogueLine("Maestro Itamae", "Has completado tu aprendizaje en Nami No Ura.", 0xFF66CCFF)
    );

    private final List<OptionArea> options = new ArrayList<>();

    private record DialogueLine(String speaker, String text, int color) {}
    private record OptionArea(int x, int y, int w, int h, String action) {}

    public ItamaeFinalSceneScreen() {
        super(Component.literal("Entrega del sushi"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 460;
        int boxH = 285;
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
                ITAMAE_PORTRAIT,
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

        gg.drawString(this.font, "Entrega final del sushi", textX, y + 16, 0xFFFFFF);
        gg.drawString(this.font, "Capítulo 3: Maestro del sushi", textX, y + 32, 0xFFAAAAAA);

        int startY = y + 85;
        int lineHeight = 18;

        for (int i = 0; i <= step && i < dialogue.size(); i++) {
            DialogueLine line = dialogue.get(i);
            int lineY = startY + i * lineHeight;

            gg.drawString(this.font, line.speaker() + ":", x + 24, lineY, line.color());
            gg.drawString(this.font, line.text(), x + 135, lineY, 0xEEEEEE);
        }

        options.clear();

        int btnW = boxW - 40;
        int btnH = 22;
        int btnX = x + 20;
        int btnY = y + boxH - 42;

        int bgColor = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, bgColor);

        String buttonText = step < dialogue.size() - 1
                ? "Continuar"
                : "Completar capítulo";

        gg.drawCenteredString(
                this.font,
                buttonText,
                btnX + btnW / 2,
                btnY + 7,
                0xFFFFFF
        );

        options.add(new OptionArea(btnX, btnY, btnW, btnH, "next"));

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (OptionArea area : options) {
            if (isMouseOver(mouseX, mouseY, area.x, area.y, area.w, area.h)) {
                playClickSound();

                if (step < dialogue.size() - 1) {
                    step++;
                } else {
                    completeChapter();
                }

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void completeChapter() {
        if (completed) return;
        completed = true;

        ModNetwork.CHANNEL.send(
                new CompleteChapter3C2SPacket(),
                PacketDistributor.SERVER.noArg()
        );

        onClose();
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