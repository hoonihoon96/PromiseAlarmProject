package com.example.promisealarmfinal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.dto.AlarmUserDTO;
import com.example.promisealarmfinal.helper.MySQLiteHelper;
import com.example.promisealarmfinal.service.AlarmReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmStartActivity extends BaseActivity {

    TextView titleView;
    TextView countView;

    private int count;
    private int tempCount;

    ArrayList<AlarmUserDTO> alarmusers;

    AlarmManager offManager;
    PendingIntent offPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_start);

        titleView = findViewById(R.id.alarm_title);
        countView = findViewById(R.id.alarm_member);

        alarmusers = new ArrayList<>();

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", -1);

        log(id + "");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference alarmUserRef = rootRef.child("alarm_user").child(id +"");

        alarmUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                count += 1;
                tempCount += 1;
                AlarmUserDTO userDTO =dataSnapshot.getValue(AlarmUserDTO.class);
                alarmusers.add(userDTO);
                countView.setText(String.format("%d명 남았습니다.", tempCount));
                check();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AlarmUserDTO user = dataSnapshot.getValue(AlarmUserDTO.class);

                for (AlarmUserDTO exist : alarmusers) {
                    if (exist.getName().equals(user.getName())) {
                        int index = alarmusers.indexOf(exist);
                        alarmusers.remove(index);
                        alarmusers.add(user);
                        check();
                        if (user.isOff()) {
                            tempCount--;
                            countView.setText(String.format("%d명 남았습니다.", tempCount));
                        }
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Alarm alarm = MySQLiteHelper.getInstance(this).selectAlarmById(id);

        titleView.setText(alarm.getTitle());

        log(alarm.getTitle());

        Calendar off = alarm.getAlarmDateTime();
        off.add(Calendar.MINUTE, alarm.getEndMinute());

        Intent offIntent = new Intent("com.example.promisealarmfinal.ALARM");
        offIntent.putExtra("status", false);

        offPendingIntent = PendingIntent.getBroadcast(
                this,
                alarm.getId(),
                offIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        offManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        offManager.set(
                AlarmManager.RTC_WAKEUP,
                off.getTimeInMillis(),
                offPendingIntent
        );

        Button button = findViewById(R.id.alarm_close);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getSharedPreferences("authentication", MODE_PRIVATE).getString("id", "none");
                String name = getSharedPreferences("authentication", MODE_PRIVATE).getString("name", "none");
                AlarmUserDTO userDTO = new AlarmUserDTO(id, name);
                userDTO.setOff(true);

                button.setText("대기 중");
                button.setEnabled(false);


                alarmUserRef.child(id).setValue(userDTO);
            }
        });
    }

    public void check() {
        if (alarmusers.size() == count) {
            boolean off = true;

            for (int i = 0; i < count; i++) {
                if (!alarmusers.get(i).isOff()) {
                    off = false;
                } else {

                }
            }

            countView.setText(String.format("%s명 남았습니다.", tempCount));

            if (!off) {
                Log.d("status", "다 안 끔");
            } else {
                Log.d("status", "다 끔");
                countView.setText("알람을 모두 종료했습니다.");
                offManager.cancel(offPendingIntent);

                Intent offIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                offIntent.putExtra("status", false);
                sendBroadcast(offIntent);

                Toast.makeText(this, "알람을 모두 종료했습니다.", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }
}
