package com.pedromrtz.tfgmod.entity.client;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.network.CompleteChapter3SushiAssemblyC2SPacket;
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

public class SushiAssemblyGameScreen extends Screen {

    private final List<String> selectedOrder = new ArrayList<>();
    private final List<String> correctOrder = List.of("arroz", "alga", "salmon", "forma");

    private final List<ButtonArea> optionButtons = new ArrayList<>();
    private final List<ButtonArea> actionButtons = new ArrayList<>();

    private String message = "Hint: base, wrapping, fish, and final shape.";
    private int messageColor = 0xEEEEEE;
    private int errors = 0;

    private record ButtonArea(int x, int y, int w, int h, String id, String label, ItemStack icon) {}

    public SushiAssemblyGameScreen() {
        super(Component.literal("Assemble Sushi"));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float pt) {
        this.renderBackground(gg, mouseX, mouseY, pt);

        int boxW = 500;
        int boxH = 330;
        int x = (this.width - boxW) / 2;
        int y = (this.height - boxH) / 2;

        gg.fill(x, y, x + boxW, y + boxH, 0xDD000000);

        gg.drawCenteredString(this.font, "Minigame: Assemble Sushi", this.width / 2, y + 14, 0xFFFFFF);

        gg.drawCenteredString(
                this.font,
                "Arrange the correct steps to prepare sushi.",
                this.width / 2,
                y + 36,
                0xDDDDDD
        );

        gg.drawCenteredString(
                this.font,
                "Riddle: white base, seaweed, cut from the sea, and final shape.",
                this.width / 2,
                y + 52,
                0xFFFFAA00
        );

        optionButtons.clear();

        int btnW = 110;
        int btnH = 36;
        int gap = 10;
        int btnY = y + 80;
        int startX = x + 18;

        addOptionButton(optionButtons, startX, btnY, btnW, btnH,
                "arroz", "Rice", new ItemStack(ModItems.ARROZ.get()));

        addOptionButton(optionButtons, startX + btnW + gap, btnY, btnW, btnH,
                "alga", "Seaweed", new ItemStack(Items.KELP));

        addOptionButton(optionButtons, startX + (btnW + gap) * 2, btnY, btnW, btnH,
                "salmon", "Salmon", new ItemStack(Items.SALMON));

        addOptionButton(optionButtons, startX + (btnW + gap) * 3, btnY, btnW, btnH,
                "forma", "Shape", new ItemStack(Items.BOWL));

        for (ButtonArea btn : optionButtons) {
            int color = isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)
                    ? 0xFF666666 : 0xFF333333;

            gg.fill(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, color);
            gg.renderItem(btn.icon, btn.x + 8, btn.y + 10);
            gg.drawString(this.font, btn.label, btn.x + 32, btn.y + 14, 0xFFFFFF);
        }

        int recipeX = x + 35;
        int recipeY = y + 145;
        int recipeW = boxW - 70;
        int recipeH = 78;

        gg.drawString(this.font, "Your assembly:", recipeX, recipeY - 16, 0xFFFFFF);
        gg.fill(recipeX, recipeY, recipeX + recipeW, recipeY + recipeH, 0xAA111111);

        int slotSize = 46;
        int slotGap = 24;
        int slotsStartX = recipeX + 45;
        int slotY = recipeY + 18;

        for (int i = 0; i < 4; i++) {
            int sx = slotsStartX + i * (slotSize + slotGap);

            gg.fill(sx, slotY, sx + slotSize, slotY + slotSize, 0xFF222222);
            gg.drawCenteredString(this.font, String.valueOf(i + 1), sx + slotSize / 2, slotY - 12, 0xAAAAAA);

            if (i < selectedOrder.size()) {
                String id = selectedOrder.get(i);
                gg.renderItem(getIcon(id), sx + 15, slotY + 10);

                gg.drawCenteredString(
                        this.font,
                        formatName(id),
                        sx + slotSize / 2,
                        slotY + slotSize + 4,
                        0xEEEEEE
                );
            }
        }

        gg.drawCenteredString(this.font, message, this.width / 2, y + 245, messageColor);

        actionButtons.clear();

        int actionY = y + boxH - 38;

        addActionButton(actionButtons, x + 25, actionY, 120, 22, "clear", "Clear", ItemStack.EMPTY);
        addActionButton(actionButtons, x + (boxW - 120) / 2, actionY, 120, 22, "confirm", "Confirm", ItemStack.EMPTY);
        addActionButton(actionButtons, x + boxW - 145, actionY, 120, 22, "exit", "Exit", ItemStack.EMPTY);

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
        for (ButtonArea btn : optionButtons) {
            if (isMouseOver(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)) {
                handleOptionClick(btn.id);
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

    private void handleOptionClick(String id) {
        playClickSound();

        if (selectedOrder.size() >= 4) {
            message = "You have already placed all four steps.";
            messageColor = 0xFFFFAA00;
            return;
        }

        selectedOrder.add(id);
        message = "Added: " + formatName(id);
        messageColor = 0xEEEEEE;
    }

    private void handleActionClick(String id) {
        playClickSound();

        switch (id) {
            case "clear" -> {
                selectedOrder.clear();
                message = "Assembly reset.";
                messageColor = 0xCCCCCC;
            }

            case "confirm" -> {
                if (selectedOrder.size() < 4) {
                    message = "Missing steps. You must choose 4.";
                    messageColor = 0xFFFF5555;
                    return;
                }

                if (selectedOrder.equals(correctOrder)) {

                    ModNetwork.CHANNEL.send(
                            new CompleteChapter3SushiAssemblyC2SPacket(),
                            PacketDistributor.SERVER.noArg()
                    );

                    Minecraft.getInstance().setScreen(new SushiResultScreen());
                } else {
                    errors++;
                    selectedOrder.clear();

                    if (errors % 2 == 0) {
                        message = "Hint: rice goes first and salmon does not come before seaweed.";
                        messageColor = 0xFFFFAA00;
                    } else {
                        message = "Incorrect order. Try again.";
                        messageColor = 0xFFFF5555;
                    }
                }
            }

            case "exit" -> onClose();
        }
    }

    private ItemStack getIcon(String id) {
        return switch (id) {
            case "arroz" -> new ItemStack(ModItems.ARROZ.get());
            case "alga" -> new ItemStack(Items.KELP);
            case "salmon" -> new ItemStack(Items.SALMON);
            case "forma" -> new ItemStack(Items.BOWL);
            default -> ItemStack.EMPTY;
        };
    }

    private String formatName(String id) {
        return switch (id) {
            case "arroz" -> "Rice";
            case "alga" -> "Seaweed";
            case "salmon" -> "Salmon";
            case "forma" -> "Shape";
            default -> id;
        };
    }

    private void addOptionButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label, ItemStack icon) {
        list.add(new ButtonArea(x, y, w, h, id, label, icon));
    }

    private void addActionButton(List<ButtonArea> list, int x, int y, int w, int h, String id, String label, ItemStack icon) {
        list.add(new ButtonArea(x, y, w, h, id, label, icon));
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