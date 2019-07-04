package com.example.promisealarmfinal.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;


public class FirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        Log.d("Token", s);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", s);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String type = remoteMessage.getData().get("type");

        if (type.equals("invite")) {
            String sender = remoteMessage.getData().get("sender");
            int id = Integer.parseInt(remoteMessage.getData().get("id"));

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("notification", true);
            intent.putExtra("sender", sender);
            intent.putExtra("id", id);



            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channel = "invite";
                String channel_nm = "invite";

                NotificationManager channelManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channelMessage = new NotificationChannel(channel,
                        channel_nm,
                        NotificationManager.IMPORTANCE_DEFAULT);

                channelMessage.setDescription("초대 메시지 관련 채널입니다.");
                channelMessage.enableLights(true);
                channelMessage.enableVibration(true);
                channelMessage.setShowBadge(true);
                channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
                channelManager.createNotificationChannel(channelMessage);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channel)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("알람 초대")
                                .setContentText(sender + "님이 당신을 알람에 초대했습니다.")
                                .setChannelId(channel)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                notificationManager.notify(id, notificationBuilder.build());
            } else {
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, "")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("알람 초대")
                                .setContentText(sender + "님이 당신을 알람에 초대했습니다.")
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(id, notificationBuilder.build());
            }
        }
    }
}
