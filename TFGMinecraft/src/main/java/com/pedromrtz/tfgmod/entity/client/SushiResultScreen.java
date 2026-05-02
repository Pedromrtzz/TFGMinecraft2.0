package com.pedromrtz.tfgmod.entity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import com.pedromrtz.tfgmod.Item.ModItems;

public class SushiResultScreen extends Screen {

    private int ticks = 0;

    public SushiResultScreen() {
        super(Component.literal("Sushi completed"));
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;

        if (ticks > 50) {
            onClose();
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 300;
        int boxH = 200;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "✦ Nigiri completed ✦",
                this.width / 2,
                y + 20,
                0xFF55FF55
        );

        ItemStack sushi = new ItemStack(ModItems.SUSHI.get());

        gg.renderItem(sushi, this.width / 2 - 8, y + 80);

        gg.drawCenteredString(
                this.font,
                "You have prepared a perfect sushi.",
                this.width / 2,
                y + 110,
                0xEEEEEE
        );

        gg.drawCenteredString(
                this.font,
                "Precision, balance and respect.",
                this.width / 2,
                y + 125,
                0xAAAAAA
        );

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public void onClose() {
        super.onClose();

        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 1.2f);
        }
    }
}