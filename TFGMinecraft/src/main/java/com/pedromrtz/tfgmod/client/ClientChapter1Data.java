package com.pedromrtz.tfgmod.client;

public class ClientChapter1Data {

    public static boolean hasAlbum = false;
    public static boolean mission1Active = false;
    public static boolean hasFamilyCard = false;
    public static boolean mission1Completed = false;

    public static boolean chapter2Active = false;
    public static boolean chapter2Completed = false;
    public static int chapter2Task = 0;
    public static int chapter2TableStage = 0;

    public static boolean chapter3Active = false;
    public static boolean chapter3Completed = false;
    public static int chapter3Task = 0;

    public static boolean chapter4Active = false;
    public static boolean chapter4Completed = false;
    public static int chapter4Task = 0;

    public static boolean chapter5Active = false;
    public static boolean chapter5Completed = false;
    public static int chapter5Task = 0;

    public static void apply(
            boolean a,
            boolean m1,
            boolean c,
            boolean done,
            boolean c2Active,
            boolean c2Completed,
            int c2Task,
            int c2TableStage,
            boolean c3Active,
            boolean c3Completed,
            int c3Task,
            boolean c4Active,
            boolean c4Completed,
            int c4Task,
            boolean c5Active,
            boolean c5Completed,
            int c5Task
    ) {
        hasAlbum = a;
        mission1Active = m1;
        hasFamilyCard = c;
        mission1Completed = done;

        chapter2Active = c2Active;
        chapter2Completed = c2Completed;
        chapter2Task = c2Task;
        chapter2TableStage = c2TableStage;

        chapter3Active = c3Active;
        chapter3Completed = c3Completed;
        chapter3Task = c3Task;

        chapter4Active = c4Active;
        chapter4Completed = c4Completed;
        chapter4Task = c4Task;

        chapter5Active = c5Active;
        chapter5Completed = c5Completed;
        chapter5Task = c5Task;
    }
}