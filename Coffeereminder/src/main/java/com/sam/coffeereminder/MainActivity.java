package com.sam.coffeereminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity  implements View.OnClickListener, TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener   {
    private Button cancel_alarm;
    private Switch set_alarm;
    private Switch repeat_alarm;
    private TextView timeText;
    private Button alarmsetup;
    private TextView alarmiIsseton;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private RadioGroup alarmRadiogroup;
    public static int alarmChoice;
    private PendingIntent pendingIntent;
    private Time time;
    private int y,mo,d,h,mi;
    private Handler timer;
    private  String alarmtime_text;
    public final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public final SimpleDateFormat sdfH = new SimpleDateFormat("HH");
    public final SimpleDateFormat sdfM = new SimpleDateFormat("mm");
    public final SimpleDateFormat sdfMo = new SimpleDateFormat("MM");
    public final SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
    public final SimpleDateFormat sdfD = new SimpleDateFormat("dd");
    Intent myIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_main);
        

        myIntent = new Intent(this, MyAlarmService.class);
        alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);

        alarmsetup = (Button) findViewById(R.id.alarm_setup);
        alarmiIsseton = (TextView) findViewById(R.id.alarmiIsseton);
        timeText = (TextView) findViewById(R.id.timeText);
        cancel_alarm = (Button) findViewById(R.id.cancel_alarm);

        alarmsetup.setOnClickListener(this);
        cancel_alarm.setOnClickListener(this);

        timer = new Handler();
        timer.post(new Runnable() {
            @Override
            public void run() {
                timeText.setText(sdf.format(new Date()));
                timer.postDelayed(this, 1000);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == alarmsetup) {
            dialog();
        }
        if(v == cancel_alarm){
            alarmManager.cancel(pendingIntent);
            alarmiIsseton.setText("N/A");
            try {
                if (MyAlarmService.mp.isPlaying()) {
                    MyAlarmService.mp.stop();
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Calendar current = Calendar.getInstance();
        Toast.makeText(this, "You can set your alarm now!", Toast.LENGTH_LONG).show();
        String currentH = sdfH.format(current.getTime());
        String currentM = sdfM.format(current.getTime());
        int currentH_int = Integer.parseInt(currentH);
        int currentM_int = Integer.parseInt(currentM);

        if(view == timePicker && (hourOfDay >= currentH_int && minute > currentM_int || hourOfDay > currentH_int && minute <= currentM_int)) {
            set_alarm.setEnabled(true);

        } else {
            set_alarm.setEnabled(false);
        }
        //}

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Calendar alarmtime = Calendar.getInstance();
        Calendar currenttime = (Calendar)alarmtime.clone();
        y = datePicker.getYear();
        mo = datePicker.getMonth();
        d = datePicker.getDayOfMonth();
        h = timePicker.getCurrentHour();
        mi = timePicker.getCurrentMinute();
        Log.i("datepickerr", y+"-"+mo+"-"+d+" | "+h+":"+mi);

        alarmtime.set(y, mo, d, h, mi, 0);
        alarmtime_text = "The alarm will start at "+h+":"+mi;;


        if(buttonView == set_alarm && isChecked){
            if(repeat_alarm.isChecked()) {
                repeat_alarm.setEnabled(true);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmtime.getTimeInMillis(), 60000, pendingIntent);
                Toast.makeText(this, alarmtime_text+" every day!", Toast.LENGTH_LONG).show();
            } else if (!repeat_alarm.isChecked()) {
                repeat_alarm.setEnabled(true);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pendingIntent);
                alarmiIsseton.setText(y+"/"+mo+"/"+d+" "+h+":"+mi);
                Toast.makeText(this, alarmtime_text+" on "+d+"/"+mo, Toast.LENGTH_LONG).show();
            }
        } else if (buttonView == set_alarm && !isChecked) {
            alarmManager.cancel(pendingIntent);
            repeat_alarm.setEnabled(false);
            try {
                if (MyAlarmService.mp.isPlaying()) {
                    MyAlarmService.mp.stop();
                }
            } catch (Exception e){
            }
            Toast.makeText(this, "The alarm has stopped", Toast.LENGTH_LONG).show();

        } else if (buttonView == repeat_alarm && isChecked) {
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmtime.getTimeInMillis(), 60000, pendingIntent);
            Toast.makeText(this, "Repeat has been enabled. "+alarmtime_text+" every day!", Toast.LENGTH_LONG).show();
        } else if(buttonView == repeat_alarm && !isChecked){
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Repeat has been disabled", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group == alarmRadiogroup && checkedId == 1){
            alarmChoice = R.raw.alarm1;
            Toast.makeText(this, "The alarmtone has been changed..." + checkedId, Toast.LENGTH_LONG).show();
        } else if(group == alarmRadiogroup && checkedId == 2){
            alarmChoice = R.raw.alarm2;
            Toast.makeText(this, "The alarmtone has been changed..."+checkedId, Toast.LENGTH_LONG).show();
        }
    }

    public void dialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alarmdialog);
        dialog.setTitle("ALarm setup");
        dialog.setCancelable(true);
        dialog.show();

        set_alarm = (Switch) dialog.findViewById(R.id.alarm_switch);
        repeat_alarm = (Switch) dialog.findViewById(R.id.repeat_switch);
        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        alarmRadiogroup = (RadioGroup) dialog.findViewById((R.id.alarmchoice));

        set_alarm.setOnCheckedChangeListener(this);
        repeat_alarm.setOnCheckedChangeListener(this);
        alarmRadiogroup.setOnCheckedChangeListener(this);
        alarmRadiogroup.check(1);

        timePicker.setOnTimeChangedListener(this);
        timePicker.setIs24HourView(true);
    }


}
