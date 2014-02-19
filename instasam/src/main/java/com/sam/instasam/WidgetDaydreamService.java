package com.sam.instasam;

/**
 * Created by sam on 2/17/14.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.service.dreams.DreamService;

import java.util.Calendar;
import java.util.Random;

public class WidgetDaydreamService extends DreamService {

    //private RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);
    private AppWidgetManager appWidgetMan;
    private int widgetId;
    private ImageView daydreamimg;
    String date1, date2, date3;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Allow user touch
        setInteractive(true);
        // Hide system UI
        setFullscreen(true);
        // Set the dream layout
        setContentView(R.layout.daydream);
        RemoteViews viewss = new RemoteViews(this.getPackageName(),R.layout.daydream);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //views.set
        Log.i("daydream settings", "yees");

        updateWidget(intent);

        stopSelf(startId);
        return START_STICKY;
    }

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


                Log.i("trying to stop alarm", "yees");

                    Intent myIntent = new Intent(this.getApplicationContext(), MyAlarmService.class);
                    myIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                    PendingIntent pendingIntent1 = PendingIntent.getService(this.getApplicationContext(), 1, myIntent, 0);
                PendingIntent pendingIntent2 = PendingIntent.getService(this.getApplicationContext(), 2, myIntent, 0);
                PendingIntent pendingIntent3 = PendingIntent.getService(this.getApplicationContext(), 3, myIntent, 0);

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
                    alarmManager1.set(AlarmManager.RTC_WAKEUP,  alarmtime.getTimeInMillis(), pendingIntent1);


                AlarmManager alarmManager2 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP,  alarmtime2.getTimeInMillis(), pendingIntent2);

                AlarmManager alarmManager3 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                alarmManager3.set(AlarmManager.RTC_WAKEUP,  alarmtime3.getTimeInMillis(), pendingIntent3);

                    date1 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime.getTimeInMillis()));
                date2 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime2.getTimeInMillis()));
                date3 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (alarmtime3.getTimeInMillis()));

                String toastMessage = "";

                    Toast.makeText(this.getApplicationContext(), "Alarm enabled at "+date1+" and at "+date2+" and at "+date3, Toast.LENGTH_LONG).show();



                Log.i("WidgetID", Integer.toString(widgetId));
                appWidgetMan.updateAppWidget(widgetId, views);

            }
        }
    }


}
