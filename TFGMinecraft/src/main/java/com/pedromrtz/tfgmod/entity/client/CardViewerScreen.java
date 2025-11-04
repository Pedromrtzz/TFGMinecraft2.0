package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CardViewerScreen extends Screen {

    private final String cardId;

    public CardViewerScreen(String cardId) {
        super(Component.literal("Card Viewer"));
        this.cardId = cardId;
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        gg.fill(0, 0, this.width, this.height, 0xAA000000);

        String id = (cardId == null || cardId.isEmpty()) ? "card_japan_fushimi_inari" : cardId;
        CardData data = DataMvp.get(id);

        if (data != null) {
            // --- textura real ---
            final int texW = 512, texH = 512;

            // ¿cabe a 512 con márgenes? si no, usamos 256 (escala 0.5 exacta)
            boolean fits512 = (this.width - 40) >= 512 && (this.height - 100) >= 512;
            int target = fits512 ? 512 : 256;

            int x = (this.width - target) / 2;
            int y = (this.height - target) / 2;

            ResourceLocation tex = ResourceLocation.fromNamespaceAndPath("tfgmod", data.texturePath());

            // Desactivar BLUR y MIPMAPS en esta textura
            var tm = net.minecraft.client.Minecraft.getInstance().getTextureManager();
            var texObj = tm.getTexture(tex);
            if (texObj != null) texObj.setFilter(false, false);

            com.mojang.blaze3d.systems.RenderSystem.enableBlend();

            if (fits512) {
                // Dibujo 1:1 → sin escalado = nítido
                gg.blit(tex, x, y, 0, 0, 512, 512, texW, texH);
            } else {
                // Escala EXACTA 0.5 con la matriz (nítido también)
                var pose = gg.pose();
                pose.pushPose();
                pose.translate(x, y, 0);
                pose.scale(0.5f, 0.5f, 1f);  // 512 → 256
                gg.blit(tex, 0, 0, 0, 0, 512, 512, texW, texH);
                pose.popPose();
            }

            com.mojang.blaze3d.systems.RenderSystem.disableBlend();

            gg.drawCenteredString(this.font, data.title(), this.width/2, y - 18, 0xFFFFFF);
            gg.drawCenteredString(this.font, data.subtitle(), this.width/2, y + target + 8, 0xCCCCCC);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    // --- MVP: datos embebidos. Luego lo pasamos a JSON data-driven.
    public record CardData(String title, String subtitle, String texturePath) {}

    public static class DataMvp {
        public static CardData get(String id) {
            if ("card_japan_fushimi_inari".equals(id)) {
                return new CardData(
                        "Fushimi Inari Taisha",
                        "Santuario sintoísta famoso por sus miles de torii rojos.",
                        "textures/gui/cards/japan/fushimi_inari_full.png"
                );
            }
            return null;
        }
    }
}