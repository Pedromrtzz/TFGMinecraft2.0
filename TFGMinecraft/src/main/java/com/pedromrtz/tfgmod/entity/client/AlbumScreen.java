// com.pedromrtz.tfgmod.entity.client.AlbumScreen
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

    // Grid config
    private static final int CELL = 128;   // tamaño de celda (antes 96)
    private static final int PADDING = 12; // margen entre celdas
    private static final int COLS = 4;     // 4 columnas

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
        int startX = (this.width - (COLS * CELL + (COLS - 1) * PADDING)) / 2;
        int startY = 60;

        for (int i = 0; i < cards.size(); i++) {
            int row = i / COLS;
            int col = i % COLS;
            int x = startX + col * (CELL + PADDING);
            int y = startY + row * (CELL + PADDING);

            if (mouseX >= x && mouseX <= x + CELL && mouseY >= y && mouseY <= y + CELL) {
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

        // --- tabs ---
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

        // --- grid ---
        Player p = mc.player;
        if (p == null) { super.render(gg, mouseX, mouseY, pt); return; }

        List<CardRegistry.Card> cards = CardRegistry.byCulture(activeCulture);
        int startX = (this.width - (COLS * CELL + (COLS - 1) * PADDING)) / 2;
        int startY = 60;

        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            boolean unlocked = CardUtils.playerHasCard(p, card.id());

            int row = i / COLS;
            int col = i % COLS;
            int x = startX + col * (CELL + PADDING);
            int y = startY + row * (CELL + PADDING);

            // marco
            gg.fill(x - 2, y - 2, x + CELL + 2, y + CELL + 2, 0x66000000);

            // miniatura 128x128, pixel-perfect
            ResourceLocation tex = CardRegistry.tex("tfgmod", card.thumbTexPath());
            var texObj = mc.getTextureManager().getTexture(tex);
            if (texObj != null) texObj.setFilter(false, false);
            RenderSystem.enableBlend();
            gg.blit(tex, x, y, 0, 0, CELL, CELL, 128, 128); // ← aquí el cambio importante
            RenderSystem.disableBlend();

            if (!unlocked) {
                gg.fill(x, y, x + CELL, y + CELL, 0xAA000000);
                // Puedes dibujar aquí un candado si quieres
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