package com.pedromrtz.tfgmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class SushiGameScreen extends Screen {

    // -------- TEXTURAS --------
    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame_bg.png");

    private static final ResourceLocation ARROZ_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/arroz.png");
    private static final ResourceLocation ALGA_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/alga.png");
    private static final ResourceLocation SALMON_ICON =
            ResourceLocation.fromNamespaceAndPath("tfgmod", "textures/gui/sushigame/salmon.png");

    // -------- CONSTANTES --------
    private static final int ICON_SIZE = 32;

    // Posición fija de los iconos laterales
    private static final int ARROZ_X = 20;
    private static final int ARROZ_Y = 40;

    private static final int ALGA_X  = 20;
    private static final int ALGA_Y  = 90;

    private static final int SALMON_X = 20;
    private static final int SALMON_Y = 140;

    // Botón LIMPIAR
    private static final int CLEAR_W = 60;
    private static final int CLEAR_H = 18;

    // Receta correcta: arroz -> alga -> salmón
    private static final List<String> RECETA_CORRECTA = List.of("arroz", "alga", "salmon");

    // Secuencia actual
    private final List<String> ingredientes = new ArrayList<>();

    // Mensaje HUD
    private String hudMensaje = "";
    private long hudMensajeExpire = 0L;   // ms

    public SushiGameScreen() {
        super(Component.literal("Sushi Maker"));
    }

    // ================== RENDER ==================
    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // --- Tatami centrado ---
        int matX = centerX - 128;
        int matY = centerY - 128;

        RenderSystem.enableBlend();
        gg.blit(BG, matX, matY, 0, 0, 256, 256, 256, 256);
        RenderSystem.disableBlend();

        // --- Iconos laterales clicables ---
        // Resaltado al pasar el ratón
        if (isInside(mouseX, mouseY, ARROZ_X, ARROZ_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(ARROZ_X - 2, ARROZ_Y - 2, ARROZ_X + ICON_SIZE + 2, ARROZ_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }
        if (isInside(mouseX, mouseY, ALGA_X, ALGA_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(ALGA_X - 2, ALGA_Y - 2, ALGA_X + ICON_SIZE + 2, ALGA_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }
        if (isInside(mouseX, mouseY, SALMON_X, SALMON_Y, ICON_SIZE, ICON_SIZE)) {
            gg.fill(SALMON_X - 2, SALMON_Y - 2, SALMON_X + ICON_SIZE + 2, SALMON_Y + ICON_SIZE + 2, 0x55FFFFFF);
        }

        drawIngredientIcon(gg, ARROZ_ICON, ARROZ_X, ARROZ_Y);
        drawIngredientIcon(gg, ALGA_ICON,  ALGA_X,  ALGA_Y);
        drawIngredientIcon(gg, SALMON_ICON, SALMON_X, SALMON_Y);

        // --- Barra de ingredientes centrada en el tatami ---
        int totalWidth = ingredientes.size() * (ICON_SIZE + 8) - 8;
        if (totalWidth < 0) totalWidth = 0;

        int barX = matX + (256 - totalWidth) / 2;
        int barY = matY + 256 / 2 - ICON_SIZE / 2;

        int offset = 0;
        for (String ing : ingredientes) {
            ResourceLocation tex = switch (ing) {
                case "arroz"  -> ARROZ_ICON;
                case "alga"   -> ALGA_ICON;
                case "salmon" -> SALMON_ICON;
                default       -> null;
            };
            if (tex != null) {
                drawIngredientIcon(gg, tex, barX + offset, barY);
                offset += ICON_SIZE + 8;
            }
        }

        // --- Botón LIMPIAR ---
        int clearX = matX + 256 - CLEAR_W - 8;
        int clearY = matY + 8;

        // Resaltado al pasar el ratón sobre LIMPIAR
        if (isInside(mouseX, mouseY, clearX, clearY, CLEAR_W, CLEAR_H)) {
            gg.fill(clearX, clearY, clearX + CLEAR_W, clearY + CLEAR_H, 0xFF666666);
        } else {
            gg.fill(clearX, clearY, clearX + CLEAR_W, clearY + CLEAR_H, 0xFF444444);
        }
        gg.drawCenteredString(this.font, "LIMPIAR",
                clearX + CLEAR_W / 2, clearY + 5, 0xFFFFFF);

        // --- Título general ---
        gg.drawCenteredString(this.font, "SUSHI MAKER", this.width / 2, 10, 0xFFFFFF);

        // --- Receta actual y paso ---
        String pasoTexto = "Paso " + ingredientes.size() + " / " + RECETA_CORRECTA.size();

        gg.drawCenteredString(this.font, pasoTexto, this.width / 2, 34, 0xCCCCCC);

        // --- Mensaje flotante del minijuego ---
        if (!hudMensaje.isEmpty() && System.currentTimeMillis() < hudMensajeExpire) {
            gg.drawCenteredString(this.font, hudMensaje, this.width / 2, 50, 0xFFFFFF);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    // ================== INPUT ==================
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int matX = centerX - 128;
        int matY = centerY - 128;

        int clearX = matX + 256 - CLEAR_W - 8;
        int clearY = matY + 8;

        // Botón LIMPIAR
        if (isInside(mouseX, mouseY, clearX, clearY, CLEAR_W, CLEAR_H)) {
            ingredientes.clear();
            playClick();
            showHudMensaje("Ingredientes borrados");
            return true;
        }

        // ARROZ
        if (isInside(mouseX, mouseY, ARROZ_X, ARROZ_Y, ICON_SIZE, ICON_SIZE)) {
            if (puedeAñadirIngrediente()) {
                ingredientes.add("arroz");
                playClick();
                checkReceta();
            }
            return true;
        }

        // ALGA
        if (isInside(mouseX, mouseY, ALGA_X, ALGA_Y, ICON_SIZE, ICON_SIZE)) {
            if (puedeAñadirIngrediente()) {
                ingredientes.add("alga");
                playClick();
                checkReceta();
            }
            return true;
        }

        // SALMÓN
        if (isInside(mouseX, mouseY, SALMON_X, SALMON_Y, ICON_SIZE, ICON_SIZE)) {
            if (puedeAñadirIngrediente()) {
                ingredientes.add("salmon");
                playClick();
                checkReceta();
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean puedeAñadirIngrediente() {
        // No dejamos añadir más si ya hay el tamaño de la receta
        return ingredientes.size() < RECETA_CORRECTA.size();
    }

    // ================== LÓGICA DEL MINIJUEGO ==================
    private void checkReceta() {
        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return;

        if (ingredientes.size() == RECETA_CORRECTA.size()) {
            boolean ok = true;
            for (int i = 0; i < RECETA_CORRECTA.size(); i++) {
                if (!RECETA_CORRECTA.get(i).equals(ingredientes.get(i))) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                showHudMensaje("¡Sushi perfecto! \uD83C\uDF63");
                // Sonido de éxito
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                // Aquí luego podrás dar recompensa (cromo, ítem, etc.)
            } else {
                showHudMensaje("La receta no es correcta...");
                // Sonido de fallo
                player.playSound(SoundEvents.VILLAGER_NO, 1.0f, 1.0f);
            }

            // Reseteamos para que pueda volver a intentar (manteniendo el feedback visual solo con el mensaje)
            ingredientes.clear();
        }
    }

    // ================== MENSAJE HUD ==================
    private void showHudMensaje(String txt) {
        this.hudMensaje = txt;
        this.hudMensajeExpire = System.currentTimeMillis() + 2500; // 2.5 s
    }

    // ================== HELPERS ==================
    private void playClick() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        player.playSound(
                SoundEvents.UI_BUTTON_CLICK.value(),
                1.0f,
                1.0f
        );
    }

    private void drawIngredientIcon(GuiGraphics gg, ResourceLocation tex, int x, int y) {
        RenderSystem.enableBlend();
        gg.blit(tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        RenderSystem.disableBlend();
    }

    private boolean isInside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}