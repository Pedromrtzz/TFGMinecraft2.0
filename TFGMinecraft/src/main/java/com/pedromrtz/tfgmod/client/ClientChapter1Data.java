package com.pedromrtz.tfgmod.client;

public class ClientChapter1Data {
    public static boolean hasAlbum = false;
    public static boolean mission1Active = false;
    public static boolean hasFamilyCard = false;
    public static boolean mission1Completed = false;

    public static void apply(boolean a, boolean m1, boolean c, boolean done) {
        hasAlbum = a;
        mission1Active = m1;
        hasFamilyCard = c;
        mission1Completed = done;
    }
}