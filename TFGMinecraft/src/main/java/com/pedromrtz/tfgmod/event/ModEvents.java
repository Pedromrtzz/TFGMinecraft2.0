package com.pedromrtz.tfgmod.event;

import com.pedromrtz.tfgmod.Item.ModItems;
import com.pedromrtz.tfgmod.TFGMod;
import com.pedromrtz.tfgmod.entity.ModEntities;
import com.pedromrtz.tfgmod.entity.custom.ChefEntity;
import com.pedromrtz.tfgmod.villager.ModVillagers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = TFGMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {


    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){

        if(event.getType() == ModVillagers.RAMEN.get()) {
            var trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 5),
                    new ItemStack(ModItems.RAMEN.get(), 1), 6, 4, 0.05f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.RAMEN_CARNE.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.RAMEN_PESCADO.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.RAMEN_POLLO.get(), 1), 6, 4, 0.05f
            ));

        }

        if(event.getType() == ModVillagers.ARROZ.get()) {
            var trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 5),
                    new ItemStack(ModItems.ARROZ_POLLO.get(), 1), 6, 4, 0.05f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.ARROZ_FRITO.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.ARROZ_VERDURAS.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.TERIYAKI.get(), 1), 6, 4, 0.05f
            ));

        }

        if(event.getType() == ModVillagers.OKONOMIYAKI.get()) {
            var trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 5),
                    new ItemStack(ModItems.OKONOMIYAKI_VERDURAS.get(), 1), 6, 4, 0.05f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.OKONOMIYAKI_CARNE.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.OKONOMIYAKI_POLLO.get(), 1), 6, 4, 0.05f
            ));

        }

        if(event.getType() == ModVillagers.SUSHI.get()) {
            var trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 5),
                    new ItemStack(ModItems.SUSHI.get(), 1), 6, 4, 0.05f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.GALLETA_SUERTE.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.DUMPLING.get(), 1), 6, 4, 0.05f
            ));

        }


        if(event.getType() == ModVillagers.MOCHI.get()) {
            var trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 5),
                    new ItemStack(ModItems.MOCHI_CHOCOLATE.get(), 1), 6, 4, 0.05f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 10),
                    new ItemStack(ModItems.MOCHI_FRESA.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 8),
                    new ItemStack(ModItems.MOCHI_PLATANO.get(), 1), 6, 4, 0.05f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 9),
                    new ItemStack(ModItems.MOCHI_VERDE.get(), 1), 6, 4, 0.05f
            ));

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.YEN.get(), 7),
                    new ItemStack(ModItems.TE_MATCHA.get(), 1), 6, 4, 0.05f
            ));

        }



    }

}
