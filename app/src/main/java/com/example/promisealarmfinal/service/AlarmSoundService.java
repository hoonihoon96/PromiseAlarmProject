package com.example.promisealarmfinal.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.promisealarmfinal.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmSoundService extends Service {
    public static final String TAG = "service";

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onCreate: ");
        boolean status = intent.getBooleanExtra("status", false);

        if (status) {
            Log.d(TAG, "on");
            alarmOn();
        } else {
            Log.d(TAG, "off");
            alarmOff();
        }

        return START_NOT_STICKY;
    }

    public void alarmOn() {
        Log.d(TAG, "on");
        player = MediaPlayer.create(this, R.raw.alarm);
        player.setLooping(true);
        player.start();
    }

    public void alarmOff() {
        Log.d(TAG, "off");
        player.stop();
        player.reset();
        player.release();
    }
}
