package com.sam.coffeereminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by sam on 9/6/13.
 */
public class MyAlarmService extends Service {
    public static MediaPlayer mp;
    private NotificationManager mManager;
    //private RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);

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
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        Notification notification = new Notification(R.drawable.ic_launcher, "Time for school =)", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(getApplicationContext(), "Alarm", "Alarm is reached!", pendingNotificationIntent);

        mManager.notify(0, notification);

        mp = MediaPlayer.create(this, R.raw.slurp);
        mp.start();

        //mp.setLooping(true);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget);
        views.setImageViewResource(R.id.coffestatus, R.drawable.sooncoffeesmall);
        AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

}
