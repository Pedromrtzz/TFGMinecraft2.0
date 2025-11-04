package com.pedromrtz.tfgmod.Item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.Minecraft;

public class CardItem extends Item {

    public static final String CARD_ID_KEY = "card_id";

    public CardItem(Properties props) {
        super(props.stacksTo(1));
    }


    public static void setCardId(ItemStack stack, String cardId) {
        // Creamos un nuevo CompoundTag
        CompoundTag tag = new CompoundTag();
        tag.putString(CARD_ID_KEY, cardId);

        // Lo metemos en el componente CustomData
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static String getCardId(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return "";
        CompoundTag tag = data.copyTag();
        return tag.contains(CARD_ID_KEY) ? tag.getString(CARD_ID_KEY) : "";
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            String cardId = getCardId(stack);
            Minecraft.getInstance().setScreen(
                    new com.pedromrtz.tfgmod.entity.client.CardViewerScreen(cardId)
            );
        }
        return InteractionResultHolder.success(stack);
    }

    @OnlyIn(Dist.CLIENT)
    private void openCardScreenClient(String cardId) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new com.pedromrtz.tfgmod.entity.client.CardViewerScreen(cardId));
    }
}