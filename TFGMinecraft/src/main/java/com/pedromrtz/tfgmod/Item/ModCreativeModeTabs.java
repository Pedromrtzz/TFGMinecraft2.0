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

    public static final RegistryObject<CreativeModeTab> ALEXANDRITE_ITEMS_TAB = CREATIVE_MODE_TABS.register("alexanderite_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EJEMPLO.get()))
                    .title(Component.translatable("creativetab.tfgmod.ejemplo"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.EJEMPLO.get());
                        output.accept(ModBlocks.SILLA.get());
                        output.accept(ModItems.RAMEN.get());
                        output.accept(ModBlocks.VITROCERAMICA.get());

                        output.accept(ModItems.MORTERO.get());
                        output.accept(ModItems.HARINA.get());
                        output.accept(ModItems.SAL.get());
                        output.accept(ModItems.PASTA.get());
                        output.accept(ModItems.FIDEOS.get());

                    }).build());

}
