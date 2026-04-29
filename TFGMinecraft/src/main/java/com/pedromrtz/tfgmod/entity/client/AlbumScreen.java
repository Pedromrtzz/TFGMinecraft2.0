package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class AlbumScreen extends Screen {

    private final Minecraft mc = Minecraft.getInstance();
    private String activeCulture;

    private static final int THUMB_W = 128;
    private static final int THUMB_H = 192;
    private static final int CELL_W  = THUMB_W;
    private static final int CELL_H  = THUMB_H;
    private static final int PADDING = 12;
    private static final int COLS    = 4;

    public AlbumScreen() {
        super(Component.literal("Álbum cultural"));
        List<String> cultures = CardRegistry.cultures();
        this.activeCulture = cultures.isEmpty() ? "japan" : cultures.get(0);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Player p = mc.player;
        if (p == null) return super.mouseClicked(mouseX, mouseY, button);

        List<CardRegistry.Card> cards = CardRegistry.byCulture(activeCulture);

        int gridW = COLS * CELL_W + (COLS - 1) * PADDING;
        int startX = (this.width - gridW) / 2;
        int startY = 60;

        for (int i = 0; i < cards.size(); i++) {
            int row = i / COLS;
            int col = i % COLS;
            int x = startX + col * (CELL_W + PADDING);
            int y = startY + row * (CELL_H + PADDING);

            if (mouseX >= x && mouseX <= x + CELL_W && mouseY >= y && mouseY <= y + CELL_H) {
                var card = cards.get(i);
                if (CardUtils.playerHasCard(p, card.id())) {
                    mc.setScreen(new CardViewerScreen(card.id()));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        gg.fill(0, 0, this.width, this.height, 0xAA000000);

        List<String> cultures = CardRegistry.cultures();
        int tabW = 90, tabH = 18;
        int totalW = cultures.size() * (tabW + 8) - 8;
        int tabsX = (this.width - totalW) / 2;
        int tabsY = 20;

        for (int i = 0; i < cultures.size(); i++) {
            String c = cultures.get(i);
            int x = tabsX + i * (tabW + 8);
            int y = tabsY;
            int bg = c.equals(activeCulture) ? 0xFF444444 : 0xFF222222;
            gg.fill(x, y, x + tabW, y + tabH, bg);
            gg.drawCenteredString(this.font, c.toUpperCase(), x + tabW / 2, y + 5, 0xFFFFFF);

            if (isMouseOver(mouseX, mouseY, x, y, tabW, tabH) && mc.mouseHandler.isLeftPressed()) {
                activeCulture = c;
            }
        }

        Player p = mc.player;
        if (p == null) { super.render(gg, mouseX, mouseY, pt); return; }

        List<CardRegistry.Card> cards = CardRegistry.byCulture(activeCulture);

        int gridW = COLS * CELL_W + (COLS - 1) * PADDING;
        int startX = (this.width - gridW) / 2;
        int startY = 60;

        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            boolean unlocked = CardUtils.playerHasCard(p, card.id());

            int row = i / COLS;
            int col = i % COLS;
            int x = startX + col * (CELL_W + PADDING);
            int y = startY + row * (CELL_H + PADDING);

            gg.fill(x - 2, y - 2, x + CELL_W + 2, y + CELL_H + 2, 0x66000000);

            ResourceLocation tex = CardRegistry.tex("tfgmod", card.thumbTexPath());

            var texObj = mc.getTextureManager().getTexture(tex);
            if (texObj != null) texObj.setFilter(false, false); // sin blur/mipmap

            RenderSystem.enableBlend();
            gg.blit(tex, x, y, 0, 0, THUMB_W, THUMB_H, THUMB_W, THUMB_H);
            RenderSystem.disableBlend();

            if (!unlocked) {
                gg.fill(x, y, x + CELL_W, y + CELL_H, 0xAA000000);
            }
        }

        gg.drawCenteredString(this.font, ("ÁLBUM — " + activeCulture.toUpperCase()),
                this.width / 2, this.height - 20, 0xFFFFFF);

        super.render(gg, mouseX, mouseY, pt);
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}