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
    String date1, date2, date3;

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
                Calendar alarmtime2 = Calendar.getInstance();
                Calendar alarmtime3 = Calendar.getInstance();


                    SharedPreferences prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);
                    Long alarm1 = prefs.getLong("alarm1", 0);
                Long alarm2 = prefs.getLong("alarm2", 0);
                Long alarm3 = prefs.getLong("alarm3", 0);

                    if(alarm1 != null){
                        long millis = alarm1;
                        Log.i("calendarr1",alarm1+"<->"+millis);
                        alarmtime.setTimeInMillis(millis);
                    }
                if(alarm2 != null){
                    long millis = alarm2;
                    Log.i("calendarr2",alarm1+"<->"+millis);
                    alarmtime2.setTimeInMillis(millis);
                }
                if(alarm3 != null){
                    long millis = alarm3;
                    Log.i("calendarr3",alarm1+"<->"+millis);
                    alarmtime3.setTimeInMillis(millis);
                }

                    AlarmManager alarmManager1 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                    alarmManager1.set(AlarmManager.RTC_WAKEUP,  alarmtime.getTimeInMillis(), pendingIntent);


                AlarmManager alarmManager2 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP,  alarmtime2.getTimeInMillis(), pendingIntent);

                AlarmManager alarmManager3 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                alarmManager3.set(AlarmManager.RTC_WAKEUP,  alarmtime3.getTimeInMillis(), pendingIntent);

                    date1 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime.getTimeInMillis()));
                date2 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime2.getTimeInMillis()));
                date3 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime3.getTimeInMillis()));

                String toastMessage = "";

                    Toast.makeText(this.getApplicationContext(), "Alarm enabled at "+date1+" and at "+date2+" and at "+date3, Toast.LENGTH_LONG).show();




                // try fetching region and city from SharedPreferences
//                SharedPreferences prefs = this.getSharedPreferences("se.svempa.weatherapp", Context.MODE_PRIVATE);

                Log.i("WidgetID", Integer.toString(widgetId));
                appWidgetMan.updateAppWidget(widgetId, views);

            }
        }
    }


}
