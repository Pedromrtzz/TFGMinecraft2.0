package com.pedromrtz.tfgmod.chapter2;

import com.pedromrtz.tfgmod.network.CompleteChapter2CookingC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class CookingGameScreen extends Screen {

    private final List<String> selectedOrder = new ArrayList<>();
    private final List<String> correctOrder = List.of("caldo", "fideos", "carne", "cebolla", "alga");

    private final List<ButtonArea> ingredientButtons = new ArrayList<>();
    private final List<ButtonArea> actionButtons = new ArrayList<>();

    private String feedbackMessage = "";
    private int feedbackColor = 0xFFFFFF;

    private record ButtonArea(int x, int y, int w, int h, String id, String label) {}

    public CookingGameScreen() {
        super(Component.literal("Cocinar Toshikoshi Soba"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 420;
        int boxH = 300;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font,
                "Minijuego: Cocina el Toshikoshi Soba",
                this.width / 2,
                y + 12,
                0xFFFFFF);

        gg.drawString(this.font,
                "Selecciona los ingredientes en el orden correcto:",
                x + 14,
                y + 34,
                0xEEEEEE);

        ingredientButtons.clear();

        int btnW = 120;
        int btnH = 20;
        int gap = 8;

        int row1Y = y + 60;
        int row2Y = y + 88;

        addIngredientButton(ingredientButtons, x + 14, row1Y, btnW, btnH, "caldo", "Caldo");
        addIngredientButton(ingredientButtons, x + 14 + btnW + gap, row1Y, btnW, btnH, "fideos", "Fideos");
        addIngredientButton(ingredientButtons, x + 14 + (btnW + gap) * 2, row1Y, btnW, btnH, "carne", "Carne");

        addIngredientButton(ingredientButtons, x + 14 + 60, row2Y, btnW, btnH, "cebolla", "Cebolla");
        addIngredientButton(ingredientButtons, x + 14 + 60 + btnW + gap, row2Y, btnW, btnH, "alga", "Alga");

        for (ButtonArea btn : ingredientButtons) {
            int color = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, color);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 6, 0xFFFFFF);
        }

        gg.drawString(this.font,
                "Tu orden:",
                x + 14,
                y + 125,
                0xFFFFFF);

        int orderBoxX = x + 14;
        int orderBoxY = y + 140;
        int orderBoxW = boxW - 28;
        int orderBoxH = 70;

        gg.fill(orderBoxX, orderBoxY, orderBoxX + orderBoxW, orderBoxY + orderBoxH, 0xAA111111);

        int lineY = orderBoxY + 10;
        for (int i = 0; i < selectedOrder.size(); i++) {
            String ingredient = formatIngredient(selectedOrder.get(i));
            gg.drawString(this.font, (i + 1) + ". " + ingredient, orderBoxX + 10, lineY, 0xEEEEEE);
            lineY += 12;
        }

        if (!feedbackMessage.isEmpty()) {
            gg.drawCenteredString(this.font,
                    feedbackMessage,
                    this.width / 2,
                    y + 220,
                    feedbackColor);
        }

        actionButtons.clear();

        int actionY = y + boxH - 34;

        addActionButton(actionButtons, x + 14, actionY, 120, 20, "clear", "Limpiar");
        addActionButton(actionButtons, x + (boxW - 120) / 2, actionY, 120, 20, "confirm", "Confirmar");
        addActionButton(actionButtons, x + boxW - 14 - 120, actionY, 120, 20, "exit", "Salir");

        for (ButtonArea btn : actionButtons) {
            int color = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, color);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 6, 0xFFFFFF);
        }

        super.render(gg, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonArea btn : ingredientButtons) {
            if (isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)) {
                handleIngredientClick(btn.id);
                return true;
            }
        }

        for (ButtonArea btn : actionButtons) {
            if (isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)) {
                handleActionClick(btn.id);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleIngredientClick(String ingredientId) {
        playClickSound();

        if (selectedOrder.size() >= correctOrder.size()) {
            feedbackMessage = "Ya has seleccionado todos los ingredientes.";
            feedbackColor = 0xFFFFAA00;
            return;
        }

        selectedOrder.add(ingredientId);
        feedbackMessage = "";
    }

    private void handleActionClick(String actionId) {
        playClickSound();

        switch (actionId) {
            case "clear" -> {
                selectedOrder.clear();
                feedbackMessage = "Selección reiniciada.";
                feedbackColor = 0xFFCCCCCC;
            }
            case "confirm" -> {
                if (selectedOrder.size() != correctOrder.size()) {
                    feedbackMessage = "Aún no has añadido todos los ingredientes.";
                    feedbackColor = 0xFFFF5555;
                    return;
                }

                if (selectedOrder.equals(correctOrder)) {
                    feedbackMessage = "¡Perfecto! Has cocinado correctamente el soba.";
                    feedbackColor = 0xFF55FF55;

                    ModNetwork.CHANNEL.send(
                            new CompleteChapter2CookingC2SPacket(new ArrayList<>(selectedOrder)),
                            PacketDistributor.SERVER.noArg()
                    );

                    onClose();
                } else {
                    feedbackMessage = "El orden no es correcto. Inténtalo de nuevo.";
                    feedbackColor = 0xFFFF5555;
                }
            }
            case "exit" -> onClose();
        }
    }

    private void addIngredientButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label) {
        list.add(new ButtonArea(x, y, w, h, id, label));
    }

    private void addActionButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label) {
        list.add(new ButtonArea(x, y, w, h, id, label));
    }

    private String formatIngredient(String id) {
        return switch (id) {
            case "caldo" -> "Caldo";
            case "fideos" -> "Fideos";
            case "carne" -> "Carne";
            case "cebolla" -> "Cebolla";
            case "alga" -> "Alga";
            default -> id;
        };
    }

    private void playClickSound() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
    }

    private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}