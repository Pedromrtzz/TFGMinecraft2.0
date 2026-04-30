package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.network.BuyFishC2SPacket;
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

public class FishMerchantShopScreen extends Screen {

    private static final ResourceLocation PORTRAIT =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/portraits/fish_merchant.png");

    private final List<ShopItem> shopItems = List.of(
            new ShopItem("salmon", "Salmón fresco", 2),
            new ShopItem("cod", "Bacalao", 1),
            new ShopItem("tropical_fish", "Pez tropical", 3),
            new ShopItem("pufferfish", "Pez globo", 4),
            new ShopItem("kelp", "Alga marina", 1),
            new ShopItem("ink", "Saco de tinta", 2)
    );

    private final List<ButtonArea> visibleButtons = new ArrayList<>();

    private int scrollOffset = 0;
    private static final int VISIBLE_ITEMS = 5;

    private record ShopItem(String id, String name, int price) {}
    private record ButtonArea(int x, int y, int w, int h, String itemId) {}

    public FishMerchantShopScreen() {
        super(Component.literal("Mercado de pescado"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 400;
        int boxH = 300;
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

        gg.blit(PORTRAIT,
                portraitX, portraitY,
                0, 0,
                portraitSize, portraitSize,
                portraitSize, portraitSize);

        int textX = x + 14 + portraitSize + 14;

        gg.drawString(this.font, "Mercado de Nami No Ura", textX, y + 14, 0xFF66CCFF);
        gg.drawString(this.font, "Pescado fresco de la costa.", textX, y + 38, 0xEEEEEE);
        gg.drawString(this.font, "Para la misión necesitas salmón.", textX, y + 52, 0xFFFFAA00);

        int listX = x + 14;
        int listY = y + 92;
        int btnW = boxW - 42;
        int btnH = 22;
        int gap = 8;

        visibleButtons.clear();

        int maxVisible = Math.min(VISIBLE_ITEMS, shopItems.size() - scrollOffset);

        for (int i = 0; i < maxVisible; i++) {
            int itemIndex = scrollOffset + i;
            ShopItem item = shopItems.get(itemIndex);

            int btnX = listX;
            int btnY = listY + i * (btnH + gap);

            int color = isMouseOver(mouseX, mouseY, btnX, btnY, btnW, btnH)
                    ? 0xFF555555 : 0xFF333333;

            gg.fill(btnX, btnY, btnX + btnW, btnY + btnH, color);

            String label = item.name() + " - " + item.price() + (item.price() == 1 ? " yen" : " yenes");

            gg.drawCenteredString(this.font,
                    label,
                    btnX + btnW / 2,
                    btnY + 7,
                    0xFFFFFF);

            visibleButtons.add(new ButtonArea(btnX, btnY, btnW, btnH, item.id()));
        }

        int scrollBarX = x + boxW - 20;
        int scrollBarY = listY;
        int scrollBarH = VISIBLE_ITEMS * (btnH + gap) - gap;

        gg.fill(scrollBarX, scrollBarY, scrollBarX + 6, scrollBarY + scrollBarH, 0xFF222222);

        if (shopItems.size() > VISIBLE_ITEMS) {
            int maxOffset = shopItems.size() - VISIBLE_ITEMS;
            int handleH = Math.max(18, scrollBarH / 3);
            int handleY = scrollBarY + (int) ((scrollBarH - handleH) * (scrollOffset / (float) maxOffset));

            gg.fill(scrollBarX, handleY, scrollBarX + 6, handleY + handleH, 0xFFAAAAAA);
        }

        int closeW = boxW - 28;
        int closeH = 22;
        int closeX = x + 14;
        int closeY = y + boxH - 36;

        int closeColor = isMouseOver(mouseX, mouseY, closeX, closeY, closeW, closeH)
                ? 0xFF555555 : 0xFF333333;

        gg.fill(closeX, closeY, closeX + closeW, closeY + closeH, closeColor);
        gg.drawCenteredString(this.font, "Cerrar", closeX + closeW / 2, closeY + 7, 0xFFFFFF);

        visibleButtons.add(new ButtonArea(closeX, closeY, closeW, closeH, "exit"));

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int maxOffset = Math.max(0, shopItems.size() - VISIBLE_ITEMS);

        if (scrollY < 0) {
            scrollOffset = Math.min(maxOffset, scrollOffset + 1);
        } else if (scrollY > 0) {
            scrollOffset = Math.max(0, scrollOffset - 1);
        }

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonArea b : visibleButtons) {
            if (isMouseOver(mouseX, mouseY, b.x, b.y, b.w, b.h)) {
                playClickSound();

                if (b.itemId.equals("exit")) {
                    onClose();
                    return true;
                }

                ModNetwork.CHANNEL.send(
                        new BuyFishC2SPacket(b.itemId),
                        PacketDistributor.SERVER.noArg()
                );

                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
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