package com.pedromrtz.tfgmod.client;

public class Chapter2TaskTexts {

    public static String getTaskText(int task) {
        return switch (task) {
            case 1 -> "Go to the market outside the village and buy the ingredients";
            case 2 -> "Return to your mother to check the ingredients";
            case 3 -> "Cook Toshikoshi Soba";
            case 4 -> "Set the table for dinner";
            case 5 -> "Have dinner with your family";
            case 6 -> "Go to the temple outside the village for the 108 bell chimes";
            case 7 -> "Write your wish";
            case 8 -> "Hang your wish at the temple";
            default -> "No active task";
        };
    }
}