package com.sam.coffeereminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sam on 9/6/13.
 */
public class MyAlarmService extends Service {
    public static MediaPlayer mp;
    private NotificationManager mManager;
    int coffeeimg = R.drawable.nocoffeesmall;
    private Handler handler = new Handler();
    private int repeat = 60;
    private int p = 0;
    private Timer timer = new Timer();

    @Override
    public void onCreate() {
        //Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "Coffeetime!!", Toast.LENGTH_LONG).show();

       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), StopAlarm.class);
        final int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        Notification notification = new Notification(R.drawable.coffeesmall, "Coffeetime!!!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.ledARGB = 0xff0000ff;          // Blue flash
        notification.ledOnMS = 100;                 // LED's on for 300 ms
        notification.ledOffMS = 100;               // LEDs off for 1 second
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.setLatestEventInfo(getApplicationContext(), "Alarm", "Alarm is reached!", pendingNotificationIntent);


        mManager.notify(0, notification);

        mp = MediaPlayer.create(this, R.raw.slurp);
        mp.start();
        final RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget);

        final AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
        //mp.setLooping(true);

        timer.schedule(new TimerTask() {

            public void run() {
                p++;
                if(p < repeat){
                    if(coffeeimg == R.drawable.nocoffeesmall){
                        coffeeimg = R.drawable.coffeesmall;
                        views.setImageViewResource(R.id.coffestatus, coffeeimg);
                        appWidgetMan.updateAppWidget(widgetId, views);
                    } else {
                        coffeeimg = R.drawable.nocoffeesmall;
                        views.setImageViewResource(R.id.coffestatus, coffeeimg);
                        appWidgetMan.updateAppWidget(widgetId, views);
                    }
                } else {
                    coffeeimg = R.drawable.nocoffeesmall;
                    views.setImageViewResource(R.id.coffestatus, coffeeimg);
                    appWidgetMan.updateAppWidget(widgetId, views);
                    timer.cancel();
                }
            }

        }, 1000,1000);


    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

}
