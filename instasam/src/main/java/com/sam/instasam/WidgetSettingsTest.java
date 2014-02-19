package com.sam.instasam;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sam on 2/17/14.
 */
public class WidgetSettingsTest extends Activity implements OnClickListener {
    private Button closeButton,saveButton, addTimepicker, removeTimepicker;
    private TimePicker alarm1, alarm2, alarm3;
    private ArrayList<Integer> timepickers = new ArrayList<Integer>();

    private int timepickerLastID = 100;
    private SharedPreferences prefs;
    private int widgetId;
    private LayoutInflater inflater;
    private LinearLayout ll;
    private DateFormat dateFormat;// = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_widget_settings_layout);


        closeButton = (Button) findViewById(R.id.close_buttontest);
        saveButton = (Button) findViewById(R.id.save_buttontest);
        addTimepicker = (Button) findViewById(R.id.addtimepicker);
        removeTimepicker = (Button) findViewById(R.id.removetimepicker);
        alarm1 = (TimePicker) findViewById(R.id.alarm1);
        alarm2 = (TimePicker) findViewById(R.id.alarm2);
        alarm3 = (TimePicker) findViewById(R.id.alarm3);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        addTimepicker.setOnClickListener(this);
        removeTimepicker.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);


        Intent intent = getIntent();
        widgetId = Session.getWidgetId();
        Log.i("widget ID settings", widgetId+"");
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ll = (LinearLayout) this.findViewById(R.id.settingslayout);


        //alarm1.setIs24HourView(true);alarm2.setIs24HourView(true);alarm3.setIs24HourView(true);
        prefs = this.getSharedPreferences("com.sam.coffeereminder", Context.MODE_PRIVATE);
        Log.i("alarm prefs", prefs.getString("alarms", ""));
        String alarmsfromprefss = prefs.getString("alarms", "");

        if(!TextUtils.isEmpty(alarmsfromprefss)){
            String[] alarmsFromprefs = alarmsfromprefss.split("->");
            restoreTimepicker(alarmsFromprefs);
        }
        for(PendingIntent pind : Session.getPendingIntents()){
            Log.i("pending ind", pind+"");
        }




        /*Long alarm1_prefs = prefs.getLong("alarm1", 0);
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
        saveButton.setOnClickListener(this);*/

    }

    public void createTimepicker(){
        View item = inflater.inflate(R.layout.list_layout, null);
        TimePicker x = (TimePicker) item.findViewById(R.id.timepickerinlist);
        x.setId(timepickerLastID);
        x.setIs24HourView(true);
        timepickers.add(timepickerLastID);
        Log.i("timepickerr", ""+x.getId());
        timepickerLastID++;
        ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void restoreTimepicker(String[] prefs){
        for(int i = 0; i < prefs.length; i++){
            String[] alarm = prefs[i].split(";");
            int alarmID = Integer.parseInt(alarm[0]);
            Long alarmTime = Long.valueOf(alarm[1]);
            Calendar alarmSession = Calendar.getInstance();
            alarmSession.setTimeInMillis(alarmTime);
            Log.i("alarm prefs id",""+alarmID);
            Log.i("alarm prefs id",""+alarmTime);
            View item = inflater.inflate(R.layout.list_layout, null);
            TimePicker x = (TimePicker) item.findViewById(R.id.timepickerinlist);
            x.setIs24HourView(true);
            x.setId(alarmID);
            x.setCurrentMinute(alarmSession.get(Calendar.MINUTE));
            x.setCurrentHour(alarmSession.get(Calendar.HOUR));
            timepickers.add(alarmID);
            timepickerLastID = alarmID;
            ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == closeButton){
            finish();
        } else if(v == addTimepicker){
            createTimepicker();
        }else if(v == removeTimepicker){
            if(timepickers.size() != 0){
                int lastitem = timepickers.size()-1;
                TimePicker x = (TimePicker) findViewById(timepickers.get(lastitem));
                x.setVisibility(View.GONE);
                if(!(Session.getPendingIntents().size() < timepickers.size())){
                    Session.getPendingIntents().get(lastitem).cancel();
                    Session.getPendingIntents().remove(lastitem);
                }
                timepickers.remove(lastitem);
                timepickerLastID--;
                Log.i("timepickerr remove", ""+timepickerLastID);
            } else {
                Toast.makeText(this.getApplicationContext(), "You dont have any alarm to remove", Toast.LENGTH_LONG).show();
            }

        }else if(v == saveButton)  {
            String alarms = "";
            String toastmsg = "";
            if(timepickers.size() > 0){
                toastmsg += "The coffeealarm will ring at ";
                Session.getPendingIntents().clear();
            }
            Intent myIntent = new Intent(this.getApplicationContext(), MyAlarmService.class);

            for(int i = 0; i < timepickers.size(); i++){
                TimePicker x = (TimePicker) findViewById(timepickers.get(i));

                PendingIntent pendingIntent = PendingIntent.getService(this.getApplicationContext(), timepickers.get(i), myIntent, 0);
                Calendar currentdate = Calendar.getInstance();
                Calendar alarmset = Calendar.getInstance();
                alarmset.set(currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH), x.getCurrentHour(), x.getCurrentMinute(),0);

                if(currentdate.getTimeInMillis() > alarmset.getTimeInMillis()){
                    int d = currentdate.get(Calendar.DAY_OF_MONTH);
                    alarmset.set(Calendar.DAY_OF_MONTH,d+1);
                    Log.i("calendar tomorrow"+timepickers.get(i), ""+dateFormat.format(alarmset.getTimeInMillis()));
                } else {
                    Log.i("calendar"+timepickers.get(i), dateFormat.format(alarmset.getTimeInMillis()));
                }

                AlarmManager alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,  alarmset.getTimeInMillis(), pendingIntent);
                if( i+1 != timepickers.size()){
                    alarms += timepickers.get(i)+";"+alarmset.getTimeInMillis()+"->";
                    toastmsg += dateFormat.format(alarmset.getTimeInMillis())+" And at ";
                } else {
                    alarms += timepickers.get(i)+";"+alarmset.getTimeInMillis();
                    toastmsg += dateFormat.format(alarmset.getTimeInMillis());
                }
                Session.addtoPendingIntents(pendingIntent);

            }

            prefs.edit().putString("alarms", alarms).commit();
            Log.i("timepickerr", alarms);
            Toast.makeText(this.getApplicationContext(), toastmsg, Toast.LENGTH_LONG).show();


        }
    }
}
