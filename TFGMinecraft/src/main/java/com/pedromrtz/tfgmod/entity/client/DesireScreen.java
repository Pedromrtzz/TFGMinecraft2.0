package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.ModNetwork;
import com.pedromrtz.tfgmod.network.SaveWishC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;

public class DesireScreen extends Screen {

    private EditBox textBox;

    public DesireScreen() {
        super(Component.literal("Escribe tu deseo"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        textBox = new EditBox(this.font, centerX - 100, centerY - 10, 200, 20, Component.literal(""));
        textBox.setMaxLength(100);

        this.addRenderableWidget(textBox);

        this.addRenderableWidget(
                Button.builder(Component.literal("Confirmar"), btn -> {
                    String text = textBox.getValue();

                    ModNetwork.CHANNEL.send(
                            new SaveWishC2SPacket(text),
                            PacketDistributor.SERVER.noArg()
                    );

                    onClose();
                }).bounds(centerX - 50, centerY + 20, 100, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        gg.drawCenteredString(this.font, "Escribe tu deseo", this.width / 2, this.height / 2 - 40, 0xFFFFFF);

        textBox.render(gg, mouseX, mouseY, pt);

        super.render(gg, mouseX, mouseY, pt);
    }
}
