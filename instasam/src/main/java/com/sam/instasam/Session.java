package com.sam.instasam;

import android.app.PendingIntent;

import java.util.ArrayList;

/**
 * Created by sam on 2/19/14.
 */
public class Session {
    public static int widgetId;
    public static boolean coffeebreak;
    public static ArrayList<PendingIntent> pendingIntents = new ArrayList<PendingIntent>();

    public static ArrayList<PendingIntent> getPendingIntents() {
        return pendingIntents;
    }
    public static void addtoPendingIntents(PendingIntent pendingIntents) {
        Session.pendingIntents.add(pendingIntents);
    }

    public static void setPendingIntents(ArrayList<PendingIntent> pendingIntents) {
        Session.pendingIntents = pendingIntents;
    }

    public static boolean isCoffeebreak() {
        return coffeebreak;
    }

    public static void setCoffeebreak(boolean coffeebreak) {
        Session.coffeebreak = coffeebreak;
    }

    public static int getWidgetId() {
        return widgetId;
    }

    public static void setWidgetId(int widgetId) {
        Session.widgetId = widgetId;
    }
}
