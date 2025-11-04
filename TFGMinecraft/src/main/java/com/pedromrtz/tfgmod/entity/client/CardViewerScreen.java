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
            // Tamaño real de la textura (ancho x alto) definido en CardData
            final int texW = data.texW();
            final int texH = data.texH();

            // Márgenes para que no pegue a los bordes
            final int maxW = this.width - 40;
            final int maxH = this.height - 100;

            // Elegimos una escala "nítida" (1.0, 0.5, 0.25) que quepa
            float scale = pickPixelPerfectScale(texW, texH, maxW, maxH);

            int targetW = Math.round(texW * scale);
            int targetH = Math.round(texH * scale);

            int x = (this.width - targetW) / 2;
            int y = (this.height - targetH) / 2;

            ResourceLocation tex = ResourceLocation.fromNamespaceAndPath("tfgmod", data.texturePath());

            // Desactivar blur y mipmaps para nitidez
            var tm = net.minecraft.client.Minecraft.getInstance().getTextureManager();
            var texObj = tm.getTexture(tex);
            if (texObj != null) texObj.setFilter(false, false);

            RenderSystem.enableBlend();

            var pose = gg.pose();
            pose.pushPose();
            pose.translate(x, y, 0);
            pose.scale(scale, scale, 1f);
            // Dibujo 1:1 en espacio escalado → mantiene proporción y nitidez
            gg.blit(tex, 0, 0, 0, 0, texW, texH, texW, texH);
            pose.popPose();

            RenderSystem.disableBlend();

            // Título arriba y subtítulo abajo del rectángulo renderizado
            gg.drawCenteredString(this.font, data.title(), this.width / 2, y - 18, 0xFFFFFF);
            gg.drawCenteredString(this.font, data.subtitle(), this.width / 2, y + targetH + 8, 0xCCCCCC);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    /**
     * Escalas discretas para evitar blur: 1.0, 0.5, 0.25 (puedes añadir 0.125 si quieres).
     * Elige la mayor que quepa dentro de maxW x maxH.
     */
    private static float pickPixelPerfectScale(int texW, int texH, int maxW, int maxH) {
        float[] candidates = new float[]{1.0f, 0.5f, 0.25f};
        for (float s : candidates) {
            if (texW * s <= maxW && texH * s <= maxH) {
                return s;
            }
        }
        // Si ni 0.25 cabe, caemos a un escalado continuo (puede perder algo de nitidez pero evita desbordes)
        float sx = maxW / (float) texW;
        float sy = maxH / (float) texH;
        return Math.max(0.01f, Math.min(sx, sy));
    }

    // --- Data embebida (MVP). Ahora incluye tamaño real (texW, texH).
    public record CardData(String title, String subtitle, String texturePath, int texW, int texH) {}

    public static class DataMvp {
        public static CardData get(String id) {
            if ("card_japan_fushimi_inari".equals(id)) {
                return new CardData(
                        "Fushimi Inari Taisha",
                        "Santuario sintoísta famoso por sus miles de torii rojos.",
                        "textures/gui/cards/japan/fushimi_inari_full.png",
                        512, 768
                );
            }
            if ("card_japan_kinkakuji".equals(id)) {
                return new CardData(
                        "Kinkaku-ji (Pabellón Dorado)",
                        "Templo zen en Kioto, famoso por su pabellón recubierto de pan de oro.",
                        "textures/gui/cards/japan/kinkakuji_full.png",
                        512, 768
                );
            }
            return null;
        }
    }
}