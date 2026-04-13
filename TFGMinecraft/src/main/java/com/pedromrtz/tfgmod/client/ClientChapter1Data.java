package com.pedromrtz.tfgmod.client;

public class ClientChapter1Data {
    public static boolean hasAlbum = false;
    public static boolean mission1Active = false;
    public static boolean hasFamilyCard = false;
    public static boolean mission1Completed = false;

    // ===== CAPÍTULO 2 =====
    public static boolean chapter2Active = false;
    public static boolean chapter2Completed = false;
    public static int chapter2Task = 0;

    public static void apply(
            boolean a,
            boolean m1,
            boolean c,
            boolean done,
            boolean c2Active,
            boolean c2Completed,
            int c2Task
    ) {
        hasAlbum = a;
        mission1Active = m1;
        hasFamilyCard = c;
        mission1Completed = done;

        chapter2Active = c2Active;
        chapter2Completed = c2Completed;
        chapter2Task = c2Task;
    }
}