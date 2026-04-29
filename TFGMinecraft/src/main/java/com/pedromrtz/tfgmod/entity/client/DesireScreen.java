package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.SaveWishC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;

public class DesireScreen extends Screen {

    private static final ResourceLocation EMA_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/ema_background.png");

    private static final int MAX_CHARS = 60;

    private EditBox textBox;
    private String errorMessage = "";

    public DesireScreen() {
        super(Component.literal("Escribe tu deseo"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        textBox = new EditBox(
                this.font,
                centerX - 115,
                centerY - 5,
                230,
                20,
                Component.literal("Deseo")
        );

        textBox.setMaxLength(MAX_CHARS);
        textBox.setFocused(true);
        this.setInitialFocus(textBox);

        this.addRenderableWidget(textBox);

        this.addRenderableWidget(
                Button.builder(Component.literal("Confirmar"), btn -> {
                    String wish = textBox.getValue().trim();

                    if (wish.isEmpty()) {
                        errorMessage = "No puedes dejar el deseo vacío.";
                        return;
                    }

                    ModNetwork.CHANNEL.send(
                            new SaveWishC2SPacket(wish),
                            PacketDistributor.SERVER.noArg()
                    );

                    onClose();

                }).bounds(centerX - 55, centerY + 35, 110, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int bgW = 300;
        int bgH = 190;
        int bgX = (this.width - bgW) / 2;
        int bgY = (this.height - bgH) / 2;

        // Fondo oscuro general
        gg.fill(bgX - 8, bgY - 8, bgX + bgW + 8, bgY + bgH + 8, 0xCC000000);

        // Textura del ema como fondo
        gg.blit(
                EMA_TEXTURE,
                bgX,
                bgY,
                0,
                0,
                bgW,
                bgH,
                bgW,
                bgH
        );

        gg.drawCenteredString(
                this.font,
                "Escribe tu deseo para el nuevo año",
                this.width / 2,
                bgY + 18,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                textBox.getValue().length() + "/" + MAX_CHARS,
                this.width / 2,
                bgY + 130,
                0xEEEEEE
        );

        if (!errorMessage.isEmpty()) {
            gg.drawCenteredString(
                    this.font,
                    errorMessage,
                    this.width / 2,
                    bgY + 150,
                    0xFF5555
            );
        }

        super.render(gg, mouseX, mouseY, pt);
    }
}