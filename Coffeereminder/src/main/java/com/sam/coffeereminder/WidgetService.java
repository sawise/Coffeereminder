package com.sam.coffeereminder;

/**
 * Created by sam on 2/17/14.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.security.Provider;
import java.util.Calendar;
import java.util.Random;

public class WidgetService extends Service {

    //private RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);
    private AppWidgetManager appWidgetMan;
    private int widgetId;
    public static int[] coffee = new int[]{R.drawable.coffeesmall, R.drawable.nocoffeesmall};
    public int coffestatus = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int coffeArray() {
        if(coffestatus+1 == coffee.length){
            coffestatus = 0;
            return coffee.length;
        } else{
            coffestatus += 1;
            return coffestatus;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //views.set
        Log.i("widgett settings", "yees");

        updateWidget(intent);

        stopSelf(startId);

        return START_STICKY;
    }


    // when report is getting fetched
    /*public void progressFeedback(){
        views.setTextViewText(R.id.update_text, "Loading...");
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    // if a network error occurs
    public void errorFeedback(){
        views.setTextViewText(R.id.update_text, "Error occured!");
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    // when the report is done loading
    public void completeFeedback(){
        //set texts to views
        views.setTextViewText(R.id.update_text, "");
        appWidgetMan.updateAppWidget(widgetId, views);
    }*/

    private void updateWidget(Intent intent){
        if (intent != null){
            String requestedAction = intent.getAction();
            if (requestedAction != null && requestedAction.equals("widgetupdate") || requestedAction != null && requestedAction.equals("widgetupdatefromservice")){
                RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);
                widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
                appWidgetMan = AppWidgetManager.getInstance(this);

                Random r = new Random();
                int i1 = r.nextInt(2);
                Log.i("widget onclick", ""+i1);
//                views.setImageViewResource(R.id.coffestatus, coffee[i1]);


                MyAlarmService alarmService = new MyAlarmService();

                Log.i("trying to stop alarm", "yees");

                    Intent myIntent = new Intent(this.getApplicationContext(), MyAlarmService.class);
                    myIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                    PendingIntent pendingIntent = PendingIntent.getService(this.getApplicationContext(), 0, myIntent, 0);

                    Calendar alarmtime = Calendar.getInstance();
                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime.getTimeInMillis()));
                    Log.i("alarm enabled", date+"|"+alarmtime.getTimeInMillis());
                    alarmtime.set(2014, 1, 18, 10, 0, 0);
                    AlarmManager alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,  alarmtime.getTimeInMillis(), pendingIntent);
                    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 1392714600, 1392717600, pendingIntent);
                    date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime.getTimeInMillis()));
                    Log.i("alarm enabled", date+"|"+alarmtime.getTimeInMillis());
                    Toast.makeText(this.getApplicationContext(), "Alarm enabled at "+date, Toast.LENGTH_LONG).show();




                // try fetching region and city from SharedPreferences
//                SharedPreferences prefs = this.getSharedPreferences("se.svempa.weatherapp", Context.MODE_PRIVATE);

                Log.i("WidgetID", Integer.toString(widgetId));
                appWidgetMan.updateAppWidget(widgetId, views);

            }
        }
    }


}
