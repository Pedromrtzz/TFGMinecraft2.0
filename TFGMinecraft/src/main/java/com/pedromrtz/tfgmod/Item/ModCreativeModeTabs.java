package com.pedromrtz.tfgmod.Item;

import com.pedromrtz.tfgmod.Block.ModBlocks;
import com.pedromrtz.tfgmod.TFGMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFGMod.MOD_ID);

    public static void register (IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> MOD_ITEMS_TAB = CREATIVE_MODE_TABS.register("alexanderite_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ARROZ.get()))
                    .title(Component.translatable("creativetab.tfgmod.ejemplo"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.SILLA.get());
                        output.accept(ModItems.RAMEN.get());
                        output.accept(ModBlocks.VITROCERAMICA.get());
                        output.accept(ModBlocks.OLLA.get());
                        output.accept(ModBlocks.SARTEN.get());
                        output.accept(ModBlocks.ARMARIO.get());
                        output.accept(ModBlocks.ESPECIAS.get());
                        output.accept(ModBlocks.BALDA.get());
                        output.accept(ModBlocks.BASURA.get());
                        output.accept(ModBlocks.ENCIMERA.get());
                        output.accept(ModBlocks.FREGADERO.get());
                        output.accept(ModBlocks.NEVERA.get());
                        output.accept(ModBlocks.EXTRACTOR.get());
                        output.accept(ModBlocks.MICROONDAS.get());
                        output.accept(ModBlocks.MESA.get());
                        output.accept(ModBlocks.BOTE.get());
                        output.accept(ModBlocks.PANADERIAMEDIO.get());

                        output.accept(ModBlocks.PARED_TATAMI.get());
                        output.accept(ModBlocks.SUELO_TATAMIARRIBA.get());
                        output.accept(ModBlocks.PARED_TATAMICEREZO.get());

                        output.accept(ModItems.MORTERO.get());
                        output.accept(ModItems.HARINA.get());
                        output.accept(ModItems.SAL.get());
                        output.accept(ModItems.PASTA.get());
                        output.accept(ModItems.FIDEOS.get());
                        output.accept(ModItems.CALDO.get());

                        output.accept(ModItems.CALDO_POLLO.get());
                        output.accept(ModItems.RAMEN_POLLO.get());

                        output.accept(ModItems.CALDO_CARNE.get());
                        output.accept(ModItems.RAMEN_CARNE.get());

                        output.accept(ModItems.CALDO_PESCADO.get());
                        output.accept(ModItems.RAMEN_PESCADO.get());

                        output.accept(ModItems.OKONOMIYAKI_POLLO.get());
                        output.accept(ModItems.OKONOMIYAKI_CARNE.get());
                        output.accept(ModItems.OKONOMIYAKI_VERDURAS.get());

                        output.accept(ModItems.MASA.get());
                        output.accept(ModItems.DUMPLING.get());

                        output.accept(ModItems.ARROZ.get());
                        output.accept(ModItems.SUSHI.get());

                        output.accept(ModItems.TAZA.get());
                        output.accept(ModItems.TE.get());
                        output.accept(ModItems.TE_MATCHA.get());

                        output.accept(ModItems.PASTA_ARROZ.get());
                        output.accept(ModItems.MOCHI_VERDE.get());
                        output.accept(ModItems.FRESA.get());
                        output.accept(ModItems.MOCHI_FRESA.get());
                        output.accept(ModItems.CHOCOLATE.get());
                        output.accept(ModItems.MOCHI_CHOCOLATE.get());
                        output.accept(ModItems.PLATANO.get());
                        output.accept(ModItems.MOCHI_PLATANO.get());

                        output.accept(ModItems.TERIYAKI.get());
                        output.accept(ModItems.CEBOLLA.get());
                        output.accept(ModItems.BROCHETA_POLLO.get());

                        output.accept(ModItems.ARROZ_POLLO.get());
                        output.accept(ModItems.ARROZ_VERDURAS.get());
                        output.accept(ModItems.ARROZ_FRITO.get());
                        output.accept(ModItems.ONIGIRI.get());

                        output.accept(ModItems.CEBOLLA_SEMILLA.get());
                        output.accept(ModItems.FRESA_SEMILLA.get());
                        output.accept(ModItems.ARROZ_SEMILLA.get());
                        output.accept(ModItems.TEVERDE_SEMILLA.get());
                        output.accept(ModItems.CHOCOLATE_POLVO.get());

                        output.accept(ModItems.GALLETA_SUERTE.get());
                        output.accept(ModItems.YEN.get());
                        output.accept(ModItems.CARD.get());

                        output.accept(ModItems.EMA.get());
                        output.accept(ModBlocks.PANADERIADCH.get());
                        output.accept(ModBlocks.PANADERIAIZQ.get());
                        output.accept(ModBlocks.PANADERIAMEDIO.get());

                        output.accept(ModBlocks.FRUTERIADCH.get());
                        output.accept(ModBlocks.FRUTERIAMEDIO.get());
                        output.accept(ModBlocks.FRUTERIAIZQ.get());

                        output.accept(ModBlocks.CARNICERIADCH.get());
                        output.accept(ModBlocks.CARNICERIAIZQ.get());
                        output.accept(ModBlocks.CARNICERIAMEDIO.get());

                        output.accept(ModBlocks.PESCADERIADCH.get());
                        output.accept(ModBlocks.PESCADERIAIZQ.get());
                        output.accept(ModBlocks.PESCADERIAMEDIO.get());

                        output.accept(ModBlocks.MINERALESDCH.get());
                        output.accept(ModBlocks.MINERALESMEDIO.get());
                        output.accept(ModBlocks.MINERALESIZQ.get());

                    }).build());



}
