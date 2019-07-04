package com.example.promisealarmfinal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.promisealarmfinal.activity.AlarmStartActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        boolean status = intent.getBooleanExtra("status", true);

        if (status) {
            Log.d(TAG, "on");


            Intent activityIntent = new Intent(context, AlarmStartActivity.class);
            int id = intent.getIntExtra("id", -1);

            Log.d(TAG, id + "");

            activityIntent.putExtra("id", id);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);

            Intent serviceIntent = new Intent(context, AlarmSoundService.class);
            serviceIntent.putExtra("status", true);

            context.startService(serviceIntent);
        } else {
            Log.d(TAG, "off");

            Intent serviceIntent = new Intent(context, AlarmSoundService.class);
            serviceIntent.putExtra("status", false);

            context.startService(serviceIntent);
        }
    }
}
