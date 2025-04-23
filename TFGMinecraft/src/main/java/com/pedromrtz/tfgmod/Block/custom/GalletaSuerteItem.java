package com.pedromrtz.tfgmod.Block.custom;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class GalletaSuerteItem extends Item {

    private static final String[] FRASES = {
            "La fortuna sonríe a quienes la buscan.",
            "Hoy es un buen día para empezar algo nuevo.",
            "La creatividad es la inteligencia divirtiéndose.",
            "Un amigo fiel es un tesoro invaluable.",
            "La felicidad es una dirección, no un lugar.",
            "La vida es lo que haces de ella.",
            "Confía en tus instintos; ellos te guiarán.",
            "Las mejores cosas de la vida son gratuitas.",
            "Un cambio de perspectiva puede cambiar tu vida.",
            "La perseverancia es la clave del éxito.",
            "Un viaje de mil millas comienza con un solo paso.",
            "El futuro pertenece a quienes creen en la belleza de sus sueños.",
            "Las oportunidades no son un accidente, son un resultado.",
            "Cada día es una nueva oportunidad para brillar.",
            "La sabiduría es saber cuándo hablar y cuándo escuchar.",
            "No te detengas hasta que estés orgulloso.",
            "La felicidad se encuentra en las pequeñas cosas.",
            "Tu sonrisa puede cambiar el mundo.",
            "Lo mejor está por venir.",
            "La paciencia es una virtud muy apreciada.",
            "La vida es un regalo, disfrútala al máximo.",
            "El amor y la risa son las mejores medicinas.",
            "Nunca es tarde para seguir tus sueños.",
            "Las estrellas no pueden brillar sin oscuridad.",
            "Los cambios son parte de la vida; acéptalos con gracia.",
            "La actitud es la clave del éxito.",
            "La gratitud convierte lo que tenemos en suficiente.",
            "Tu energía es contagiosa; irradia positividad.",
            "La fe mueve montañas.",
            "La vida te da lo que le das."
    };

    private final Random random = new Random();

    public GalletaSuerteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            // Elegir una frase aleatoria
            String fraseAleatoria = FRASES[random.nextInt(FRASES.length)];
            // Enviar el mensaje al jugador
            player.sendSystemMessage(Component.translatable(fraseAleatoria));

            // Quitar una unidad del ItemStack
            ItemStack itemStack = player.getItemInHand(hand);
            itemStack.shrink(1); // Reduce la cantidad en 1

            // Si la cantidad es 0, se eliminará automáticamente del inventario
            if (itemStack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY); // Remover el item del inventario
            }
        }

        return super.use(level, player, hand);
    }
}

