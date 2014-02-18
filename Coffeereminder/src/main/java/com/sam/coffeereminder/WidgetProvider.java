package com.sam.coffeereminder;

/**
 * Created by sam on 2/17/14.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

public class WidgetProvider extends AppWidgetProvider {

    private static final String SHOW_SETTINGS_POPUP_ACTION = "com.sam.coffeereminder.WidgetSettings";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // used to launch settings activity
        if(intent.getAction().equals(SHOW_SETTINGS_POPUP_ACTION)){
            Intent popUpIntent = new Intent(context, WidgetSettings.class);
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            popUpIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            popUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(popUpIntent);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Perform this loop procedure for each App Widget
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            // intent to call service
            Intent intent = new Intent(context, WidgetService.class);
            intent.setAction("widgetupdate");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            PendingIntent pendingIntent = PendingIntent.getService(context, appWidgetId, intent, 0);
            Log.i("widgett", "tesssting");
            views.setOnClickPendingIntent(R.id.coffestatus, pendingIntent);


            // intent to call weather activity
            Intent activityIntent = new Intent(context, WidgetSettings.class);
            activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            activityIntent.setAction("launchweatheractivity");
            PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

            views.setOnClickPendingIntent(R.id.widgetsettings, activityPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
