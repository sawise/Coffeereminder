package com.sam.coffeereminder;

import android.app.Activity;

import java.util.ArrayList;
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

/**
 * Created by sam on 2/17/14.
 */
public class WidgetSettings extends Activity {
    private Button closeButton;
    private Button saveButton;


    private List regionList;
    private List cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_widget_settings_layout);

        closeButton = (Button) findViewById(R.id.close_button);
        saveButton = (Button) findViewById(R.id.save_button);

//        closeButton.setOnClickListener(this);
  //      saveButton.setOnClickListener(this);

        regionList = new ArrayList();
        cityList = new ArrayList();

        // adding all regions to regionList
        //addRegionsToList();

    }

}
