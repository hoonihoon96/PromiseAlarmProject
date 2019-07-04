package com.example.promisealarmfinal.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.adapter.AlarmListAdapter;
import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.dto.AlarmUserDTO;
import com.example.promisealarmfinal.helper.MySQLiteHelper;
import com.example.promisealarmfinal.service.AlarmReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.alarm_list_view)
    ListView alarmListView;

    private ArrayList<Alarm> alarms;
    private AlarmListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        alarms = MySQLiteHelper.getInstance(this).selectAlarm();
        adapter = new AlarmListAdapter(this);

        adapter.setList(alarms);
        alarmListView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("notification", false)) {
            String message = intent.getStringExtra("sender") + "님의 초대를 수락하시겠습니까?";
            String id = String.valueOf(intent.getIntExtra("id", 0));

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("초대")
                    .setMessage(message)
                    .setPositiveButton("예", (d, w) -> {
                        new AcceptInviteAsync(this).execute(id);
                    })
                    .setNegativeButton("아니오", (d,w) -> {

                    });

            builder.show();

            alarms = MySQLiteHelper.getInstance(this).selectAlarm();
            adapter.setList(alarms);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        alarms = MySQLiteHelper.getInstance(this).selectAlarm();
        adapter.setList(alarms);
    }

    @OnClick({R.id.add_alarm_button})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_alarm_button:
                Intent intent = new Intent(this, AlarmActivity.class);
                intent.putExtra("mode", AlarmActivity.ADD_MODE);
                startActivity(intent);
                break;
        }
    }

    public class AcceptInviteAsync extends AsyncTask<String, Void, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public AcceptInviteAsync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("서버로부터 알람 정보를 받아오는 중입니다.");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String id = params[0];

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("id", id)
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.180.169.157:8080/PromiseAlarm/AcceptInvite")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            log(result);

            try {
                JSONObject resultJson = new JSONObject(result);

                if (resultJson.getInt("code") == 200) {
                    Toast.makeText(context, "알람 구독에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    Alarm alarm = new Alarm();
                    int id = resultJson.getInt("id");
                    String title = resultJson.getString("title");
                    String description = resultJson.getString("description");
                    String creator = resultJson.getString("creator");
                    String alarmDateTime = resultJson.getString("alarmDateTime");
                    int endMinute = resultJson.getInt("endMinute");
                    String createdDateTime = resultJson.getString("createdDateTime");
                    int modifiedCount = resultJson.getInt("modifiedCount");

                    alarm.setId(id);
                    alarm.setTitle(title);
                    alarm.setDescription(description);
                    alarm.setCreator(creator);
                    alarm.setAlarmDateTimeOfString(alarmDateTime);
                    alarm.setEndMinute(endMinute);
                    alarm.setCreatedDateTimeOfString(createdDateTime);
                    alarm.setModifiedCount(modifiedCount);

                    MySQLiteHelper.getInstance(context).insertAlarm(alarm);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference alarmUserRef = rootRef.child("alarm_user").child(id +"");

                    String userId = context.getSharedPreferences("authentication", MODE_PRIVATE).getString("id", "none");
                    String userName = context.getSharedPreferences("authentication", MODE_PRIVATE).getString("name", "none");
                    AlarmUserDTO alarmUserDTO = new AlarmUserDTO(userId, userName);

                    alarmUserRef.child(userId).setValue(alarmUserDTO);

                    alarms = MySQLiteHelper.getInstance(context).selectAlarm();
                    adapter.setList(alarms);

                    Intent onIntent = new Intent(context, AlarmReceiver.class);
                    onIntent.putExtra("on", true);
                    onIntent.putExtra("id", alarm.getId());

                    PendingIntent onPendingIntent = PendingIntent.getBroadcast(
                            context,
                            alarm.getId(),
                            onIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            alarm.getAlarmDateTime().getTimeInMillis(),
                            onPendingIntent
                    );


                } else {
                    Toast.makeText(context, "이건 안 나와야 정상입니다만?", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
