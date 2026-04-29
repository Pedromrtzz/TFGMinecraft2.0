package com.pedromrtz.tfgmod.capitulo2;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.network.CompleteChapter2CookingC2SPacket;
import com.pedromrtz.tfgmod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class CookingGameScreen extends Screen {

    private final List<String> selectedOrder = new ArrayList<>();
    private final List<String> correctOrder = List.of("caldo", "fideos", "carne", "cebolla", "alga");

    private final List<ButtonArea> ingredientButtons = new ArrayList<>();
    private final List<ButtonArea> actionButtons = new ArrayList<>();

    private String feedbackMessage = "Prepara el soba siguiendo el orden correcto.";
    private int feedbackColor = 0xEEEEEE;
    private int errorCount = 0;

    private record ButtonArea(int x, int y, int w, int h, String id, String label, ItemStack icon) {}

    public CookingGameScreen() {
        super(Component.literal("Cocinar Toshikoshi Soba"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 470;
        int boxH = 330;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(
                this.font,
                "Minijuego: Toshikoshi Soba",
                this.width / 2,
                y + 12,
                0xFFFFFF
        );

        gg.drawCenteredString(
                this.font,
                "Selecciona los ingredientes en el orden correcto.",
                this.width / 2,
                y + 32,
                0xDDDDDD
        );

        ingredientButtons.clear();

        int btnW = 135;
        int btnH = 34;
        int gap = 10;

        int row1Y = y + 58;
        int row2Y = y + 102;

        addIngredientButton(ingredientButtons, x + 22, row1Y, btnW, btnH, "caldo", "Caldo", new ItemStack(ModItems.CALDO.get()));
        addIngredientButton(ingredientButtons, x + 22 + btnW + gap, row1Y, btnW, btnH, "fideos", "Fideos", new ItemStack(ModItems.FIDEOS.get()));
        addIngredientButton(ingredientButtons, x + 22 + (btnW + gap) * 2, row1Y, btnW, btnH, "carne", "Carne", new ItemStack(Items.BEEF));

        addIngredientButton(ingredientButtons, x + 92, row2Y, btnW, btnH, "cebolla", "Cebolla", new ItemStack(ModItems.CEBOLLA.get()));
        addIngredientButton(ingredientButtons, x + 92 + btnW + gap, row2Y, btnW, btnH, "alga", "Alga", new ItemStack(Items.KELP));

        for (ButtonArea btn : ingredientButtons) {
            int color = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, color);

            gg.renderItem(btn.icon, btn.x + 8, btn.y + 9);

            gg.drawString(
                    this.font,
                    btn.label,
                    btn.x + 32,
                    btn.y + 13,
                    0xFFFFFF
            );
        }

        gg.drawString(this.font, "Tu receta:", x + 22, y + 150, 0xFFFFFF);

        int orderBoxX = x + 22;
        int orderBoxY = y + 165;
        int orderBoxW = boxW - 44;
        int orderBoxH = 78;

        gg.fill(orderBoxX, orderBoxY, orderBoxX + orderBoxW, orderBoxY + orderBoxH, 0xAA111111);

        int slotSize = 42;
        int slotGap = 9;
        int startX = orderBoxX + 18;
        int slotY = orderBoxY + 18;

        for (int i = 0; i < correctOrder.size(); i++) {
            int sx = startX + i * (slotSize + slotGap);

            gg.fill(sx, slotY, sx + slotSize, slotY + slotSize, 0xFF222222);
            gg.drawCenteredString(this.font, String.valueOf(i + 1), sx + slotSize / 2, slotY - 12, 0xAAAAAA);

            if (i < selectedOrder.size()) {
                ItemStack icon = getIcon(selectedOrder.get(i));
                gg.renderItem(icon, sx + 13, slotY + 8);

                gg.drawCenteredString(
                        this.font,
                        formatIngredient(selectedOrder.get(i)),
                        sx + slotSize / 2,
                        slotY + slotSize + 4,
                        0xEEEEEE
                );
            }
        }

        if (!feedbackMessage.isEmpty()) {
            gg.drawCenteredString(
                    this.font,
                    feedbackMessage,
                    this.width / 2,
                    y + 255,
                    feedbackColor
            );
        }

        actionButtons.clear();

        int actionY = y + boxH - 36;
        addActionButton(actionButtons, x + 22, actionY, 120, 22, "clear", "Limpiar", ItemStack.EMPTY);
        addActionButton(actionButtons, x + (boxW - 120) / 2, actionY, 120, 22, "confirm", "Confirmar", ItemStack.EMPTY);
        addActionButton(actionButtons, x + boxW - 22 - 120, actionY, 120, 22, "exit", "Salir", ItemStack.EMPTY);

        for (ButtonArea btn : actionButtons) {
            int color = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, color);
            gg.drawCenteredString(this.font, btn.label, btn.x + btn.w / 2, btn.y + 7, 0xFFFFFF);
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
        feedbackMessage = "Ingrediente añadido: " + formatIngredient(ingredientId);
        feedbackColor = 0xEEEEEE;
    }

    private void handleActionClick(String actionId) {
        playClickSound();

        switch (actionId) {
            case "clear" -> {
                selectedOrder.clear();
                feedbackMessage = "Has limpiado la receta. Inténtalo de nuevo.";
                feedbackColor = 0xFFCCCCCC;
            }

            case "confirm" -> {
                if (selectedOrder.size() != correctOrder.size()) {
                    feedbackMessage = "Te faltan ingredientes. Debes colocar 5 en total.";
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
                    errorCount++;
                    selectedOrder.clear();

                    if (errorCount % 2 == 0) {
                        feedbackMessage = getHint();
                        feedbackColor = 0xFFFFAA00;
                    } else {
                        feedbackMessage = "El orden no es correcto. La receta se ha reiniciado.";
                        feedbackColor = 0xFFFF5555;
                    }
                }
            }

            case "exit" -> onClose();
        }
    }

    private String getHint() {
        return switch (errorCount) {
            case 2 -> "Pista: primero se prepara la base líquida.";
            case 4 -> "Pista: después del caldo van los fideos.";
            case 6 -> "Pista: el alga se coloca al final como acompañamiento.";
            default -> "Pista: piensa en base, fideos, proteína y toppings.";
        };
    }

    private ItemStack getIcon(String id) {
        return switch (id) {
            case "caldo" -> new ItemStack(ModItems.CALDO.get());
            case "fideos" -> new ItemStack(ModItems.FIDEOS.get());
            case "carne" -> new ItemStack(Items.BEEF);
            case "cebolla" -> new ItemStack(ModItems.CEBOLLA.get());
            case "alga" -> new ItemStack(Items.KELP);
            default -> ItemStack.EMPTY;
        };
    }

    private void addIngredientButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label, ItemStack icon) {
        list.add(new ButtonArea(x, y, w, h, id, label, icon));
    }

    private void addActionButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label, ItemStack icon) {
        list.add(new ButtonArea(x, y, w, h, id, label, icon));
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