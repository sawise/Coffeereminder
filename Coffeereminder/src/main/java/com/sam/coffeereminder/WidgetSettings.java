package com.sam.coffeereminder;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * Created by sam on 2/17/14.
 */
public class WidgetSettings extends Activity implements OnClickListener {
    private Button closeButton;
    private Button saveButton;
    private TimePicker alarm1, alarm2, alarm3;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_widget_settings_layout);


        closeButton = (Button) findViewById(R.id.close_button);
        saveButton = (Button) findViewById(R.id.save_button);
        alarm1 = (TimePicker) findViewById(R.id.alarm1);
        alarm2 = (TimePicker) findViewById(R.id.alarm2);
        alarm3 = (TimePicker) findViewById(R.id.alarm3);
        alarm1.setIs24HourView(true);alarm2.setIs24HourView(true);alarm3.setIs24HourView(true);
        prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);


        Long alarm1_prefs = prefs.getLong("alarm1", 0);
        int hours1   = (int) ((alarm1_prefs/ (1000*60*60)) % 24);
        int minutes1 = (int) ((alarm1_prefs/ (1000*60)) % 60);
        alarm1.setCurrentHour(hours1);
        alarm1.setCurrentMinute(minutes1);

        Long alarm2_prefs = prefs.getLong("alarm2", 0);
        int hours2   = (int) ((alarm2_prefs/ (1000*60*60)) % 24);
        int minutes2 = (int) ((alarm2_prefs/ (1000*60)) % 60);
        alarm2.setCurrentHour(hours2);
        alarm2.setCurrentMinute(minutes2);

        Long alarm3_prefs = prefs.getLong("alarm3", 0);
        int hours3   = (int) ((alarm3_prefs/ (1000*60*60)) % 24);
        int minutes3 = (int) ((alarm3_prefs/ (1000*60)) % 60);
        alarm3.setCurrentHour(hours3);
        alarm3.setCurrentMinute(minutes3);
        closeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == closeButton){
            // close activity
            finish();
        }

        if(v == saveButton){

            prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);
            Calendar currentdate = Calendar.getInstance();
            Calendar al1 = Calendar.getInstance();
            Calendar al2 = Calendar.getInstance();
            Calendar al3 = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            al1.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH), alarm1.getCurrentHour(), alarm1.getCurrentMinute(),0);
            if(currentdate.getTimeInMillis() < al1.getTimeInMillis()){
                long a = al1.getTimeInMillis()+60000;
                prefs.edit().putLong("alarm1", a).commit();
                Log.i("calendar111", dateFormat.format(a));
            } else {
                prefs.edit().putLong("alarm1", al1.getTimeInMillis()).commit();
                Log.i("calendar111", dateFormat.format(al1.getTimeInMillis()));
            }



            al2.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),currentdate.get(Calendar.DAY_OF_MONTH), alarm2.getCurrentHour(), alarm2.getCurrentMinute(),0);
            if(currentdate.getTimeInMillis() < al2.getTimeInMillis()){
                long a = al2.getTimeInMillis()+60000;
                prefs.edit().putLong("alarm2", a).commit();
                Log.i("calendar112", dateFormat.format(a));
            } else {
                prefs.edit().putLong("alarm2", al2.getTimeInMillis()).commit();
                Log.i("calendar112", dateFormat.format(al2.getTimeInMillis()));
            }

            al3.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),currentdate.get(Calendar.DAY_OF_MONTH), alarm3.getCurrentHour(), alarm3.getCurrentMinute(),0);
            if(currentdate.getTimeInMillis() < al3.getTimeInMillis()){
                long a = al3.getTimeInMillis()+60000;
                prefs.edit().putLong("alarm3", a).commit();
                Log.i("calendar113", dateFormat.format(a));
            } else {
                prefs.edit().putLong("alarm3", al3.getTimeInMillis()).commit();
                Log.i("calendar113", dateFormat.format(al3.getTimeInMillis()));
            }
        }
    }
}
