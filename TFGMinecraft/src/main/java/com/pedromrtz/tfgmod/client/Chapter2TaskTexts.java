package com.pedromrtz.tfgmod.client;

public class Chapter2TaskTexts {

    public static String getTaskText(int task) {
        return switch (task) {
            case 1 -> "Ve al mercado de las afueras y compra los ingredientes";
            case 2 -> "Vuelve con la madre para comprobar los ingredientes";
            case 3 -> "Cocina el toshikoshi soba";
            case 4 -> "Pon la mesa para la cena";
            case 5 -> "Cena con la familia";
            case 6 -> "Ve al templo de las afueras para las 108 campanadas";
            case 7 -> "Escribe tu deseo";
            case 8 -> "Cuelga el deseo en el templo";
            default -> "Sin tarea activa";
        };
    }
}