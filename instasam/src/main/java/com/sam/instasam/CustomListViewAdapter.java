package com.sam.instasam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.ArrayList;

/**
 * Created by sam on 2/19/14.
 */
public class CustomListViewAdapter extends ArrayAdapter<Integer> {

    Context context;
    int layoutResourceId;
    ArrayList<Integer> data = new ArrayList<Integer>();

    public CustomListViewAdapter(Context context, int layoutResourceId, ArrayList<Integer> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.timePicker = (TimePicker) row.findViewById(R.id.timepickerinlist);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        return row;

    }

    static class RecordHolder {
        TimePicker timePicker;
    }


}
