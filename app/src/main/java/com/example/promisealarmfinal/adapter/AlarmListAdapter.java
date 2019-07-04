package com.example.promisealarmfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.activity.AlarmViewActivity;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Alarm> alarms;

    public AlarmListAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_alarm, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        TextView dateTimeTextView = convertView.findViewById(R.id.date_time_text_view);

        Alarm alarm = alarms.get(position);

        titleTextView.setText(alarm.getTitle());
        dateTimeTextView.setText(alarm.getAlarmDateTimeInString());

        convertView.setOnClickListener(v -> {
            int id = alarm.getId();
            Intent intent = new Intent(context, AlarmViewActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        });

        return convertView;
    }
}
