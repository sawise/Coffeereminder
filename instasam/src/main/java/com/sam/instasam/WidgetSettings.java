package com.sam.instasam;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by sam on 2/17/14.
 */
public class WidgetSettings extends Activity implements OnClickListener {
    private Button closeButton,saveButton;
    private TimePicker alarm1, alarm2, alarm3;
    private SharedPreferences prefs;
    private int widgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_widget_settings_layout_org);


        closeButton = (Button) findViewById(R.id.close_button);
        saveButton = (Button) findViewById(R.id.save_button);
        alarm1 = (TimePicker) findViewById(R.id.alarm1);
        alarm2 = (TimePicker) findViewById(R.id.alarm2);
        alarm3 = (TimePicker) findViewById(R.id.alarm3);




        alarm1.setIs24HourView(true);alarm2.setIs24HourView(true);alarm3.setIs24HourView(true);
        prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        widgetId = Session.getWidgetId();
        Log.i("widget ID settings", widgetId+"");


        Long alarm1_prefs = prefs.getLong("alarm1", 0);
        int hours1   = (int) ((alarm1_prefs/ (1000*60*60)) % 24);
        int minutes1 = (int) ((alarm1_prefs/ (1000*60)) % 60);
        alarm1.setCurrentHour(hours1+1);
        alarm1.setCurrentMinute(minutes1);

        Long alarm2_prefs = prefs.getLong("alarm2", 0);
        int hours2   = (int) ((alarm2_prefs/ (1000*60*60)) % 24);
        int minutes2 = (int) ((alarm2_prefs/ (1000*60)) % 60);
        alarm2.setCurrentHour(hours2+1);
        alarm2.setCurrentMinute(minutes2);

        Long alarm3_prefs = prefs.getLong("alarm3", 0);
        int hours3   = (int) ((alarm3_prefs/ (1000*60*60)) % 24);
        int minutes3 = (int) ((alarm3_prefs/ (1000*60)) % 60);
        alarm3.setCurrentHour(hours3+1);
        alarm3.setCurrentMinute(minutes3);
        closeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == closeButton){
            // close activity
            finish();
        } else if(v == saveButton){
            prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);

            Intent myIntent = new Intent(this.getApplicationContext(), MyAlarmService.class);
            myIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent1 = PendingIntent.getService(this.getApplicationContext(), 1, myIntent, 0);
            PendingIntent pendingIntent2 = PendingIntent.getService(this.getApplicationContext(), 2, myIntent, 0);
            PendingIntent pendingIntent3 = PendingIntent.getService(this.getApplicationContext(), 3, myIntent, 0);


            Calendar currentdate = Calendar.getInstance();
            Calendar al1 = Calendar.getInstance();
            Calendar al2 = Calendar.getInstance();
            Calendar al3 = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



            al1.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH), alarm1.getCurrentHour(), alarm1.getCurrentMinute(),0);
            if(currentdate.getTimeInMillis() > al1.getTimeInMillis()){
                int d = currentdate.get(Calendar.DAY_OF_MONTH);
                al1.set(Calendar.DAY_OF_MONTH,d+1);
                prefs.edit().putLong("alarm1", al1.getTimeInMillis()).commit();
                Log.i("calendar111 tomorrow", ""+dateFormat.format(al1.getTimeInMillis()));
            } else {
                prefs.edit().putLong("alarm1", al1.getTimeInMillis()).commit();
                Log.i("calendar111", dateFormat.format(al1.getTimeInMillis()));
            }



            al2.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),currentdate.get(Calendar.DAY_OF_MONTH), alarm2.getCurrentHour(), alarm2.getCurrentMinute(),0);
            if(currentdate.getTimeInMillis() > al2.getTimeInMillis()){
                int d = currentdate.get(Calendar.DAY_OF_MONTH);
                al2.set(Calendar.DAY_OF_MONTH,d+1);
                prefs.edit().putLong("alarm2", al2.getTimeInMillis()).commit();
                Log.i("calendar112 tomorrow", dateFormat.format(al2.getTimeInMillis()));
            } else {
                prefs.edit().putLong("alarm2", al2.getTimeInMillis()).commit();
                Log.i("calendar112", dateFormat.format(al2.getTimeInMillis()));
            }

            al3.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),currentdate.get(Calendar.DAY_OF_MONTH), alarm3.getCurrentHour(), alarm3.getCurrentMinute(),0);
            Log.i("calendar113 date", dateFormat.format(al3.getTimeInMillis())+"<->"+dateFormat.format(currentdate.getTimeInMillis()));
            Log.i("calendar113 millis", al3.getTimeInMillis()+"<->"+currentdate.getTimeInMillis());
            if(currentdate.getTimeInMillis() > al3.getTimeInMillis()){
                int d = currentdate.get(Calendar.DAY_OF_MONTH);
                al3.set(Calendar.DAY_OF_MONTH,d+1);
                prefs.edit().putLong("alarm3", al3.getTimeInMillis()).commit();
                Log.i("calendar113 tomorrow", dateFormat.format(al3.getTimeInMillis()));
            } else {
                prefs.edit().putLong("alarm3", al3.getTimeInMillis()).commit();
                Log.i("calendar113", dateFormat.format(al3.getTimeInMillis()));
                Log.i("calendar113", dateFormat.format(currentdate.getTimeInMillis()));
            }
            AlarmManager alarmManager1 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
            alarmManager1.set(AlarmManager.RTC_WAKEUP,  al1.getTimeInMillis(), pendingIntent1);


            AlarmManager alarmManager2 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
            alarmManager2.set(AlarmManager.RTC_WAKEUP,  al2.getTimeInMillis(), pendingIntent2);

            AlarmManager alarmManager3 = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
            alarmManager3.set(AlarmManager.RTC_WAKEUP,  al3.getTimeInMillis(), pendingIntent3);

            String date1 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (al1.getTimeInMillis()));
            String date2 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (al2.getTimeInMillis()));
            String date3 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (al3.getTimeInMillis()));

            String toastMessage = "";

            Toast.makeText(this.getApplicationContext(), "Alarm enabled at " + date1 + " and at " + date2 + " and at " + date3, Toast.LENGTH_LONG).show();



            Log.i("WidgetID", Integer.toString(widgetId));

            //finish();
        }
    }
}
