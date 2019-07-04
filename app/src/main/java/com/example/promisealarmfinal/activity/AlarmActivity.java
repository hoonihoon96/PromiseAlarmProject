package com.example.promisealarmfinal.activity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.dto.AlarmUserDTO;
import com.example.promisealarmfinal.helper.MySQLiteHelper;
import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.service.AlarmReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_input_view)
    TextView titleInputView;

    @BindView(R.id.description_input_view)
    TextView descriptionInputView;

    @BindView(R.id.date_input_view)
    TextView dateInputView;

    @BindView(R.id.time_input_view)
    TextView timeInputView;

    @BindView(R.id.end_minute_input_view)
    TextView endMinuteInputView;

    public static final int ADD_MODE = 0;
    public static final int EDIT_MODE = 1;

    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 0);

        if (mode == ADD_MODE) {
            editing = false;
        } else {
            editing = true;

            int id = intent.getIntExtra("id", 0);
            Alarm alarm = MySQLiteHelper.getInstance(this).selectAlarmById(id);

            titleInputView.setText(alarm.getTitle());
            descriptionInputView.setText(alarm.getDescription());
            dateInputView.setText(alarm.getAlarmDateInString());
            timeInputView.setText(alarm.getAlarmTimeInString());
            endMinuteInputView.setText(alarm.getEndMinuteInString());
        }
    }

    @OnClick({R.id.title_layout, R.id.description_layout, R.id.date_layout,
            R.id.time_layout, R.id.end_minute_layout, R.id.cancel_button, R.id.submit_button})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout:
                showInputDialog("제목 입력", "제목을 입력해주세요.", titleInputView);
                break;
            case R.id.description_layout:
                showInputDialog("설명 입력", "설명을 입력해주세요.", descriptionInputView);
                break;
            case R.id.date_layout:
                showDatePicker();
                break;
            case R.id.time_layout:
                showTimePicker();
                break;
            case R.id.end_minute_layout:
                showEndMinuteDialog();
                break;
            case R.id.submit_button:
                if (!editing) {
                    String title = titleInputView.getText().toString();
                    String description = descriptionInputView.getText().toString();
                    String creator = getSharedPreferences("authentication", MODE_PRIVATE).getString("id", "anonymous");
                    String alarmTime = dateInputView.getText().toString() + " " + timeInputView.getText().toString();
                    String endTime = endMinuteInputView.getText().toString().substring(0, 1);

                    Alarm alarm = new Alarm();
                    alarm.setTitle(title);
                    alarm.setDescription(description);
                    alarm.setCreator(creator);
                    alarm.setAlarmDateTimeOfString(dateInputView.getText().toString() + " " + timeInputView.getText().toString());
                    log(alarm.getAlarmDateTime().getTime().toString());
                    alarm.setEndMinute(Integer.parseInt(endTime));

                    new AlarmAsync(this, alarm).execute(title, description, creator, alarmTime, endTime);
                } else {
                    Toast.makeText(this, "!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_button:
                finish();
        }
    }

    public void showInputDialog(String title, String message, TextView inputView) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        EditText inputText = new EditText(this);

        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setView(inputText);
        dialog.setPositiveButton("확인", (d, w) -> {
            inputView.setText(inputText.getText().toString());
        });
        dialog.setNegativeButton("취소", (d, w) -> {

        });
        dialog.show();
    }

    public void showEndMinuteDialog() {
        ArrayList<String> minutes = new ArrayList<>();
        minutes.add("1분");
        minutes.add("3분");
        minutes.add("5분");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("알람 최대 울림 시간");
        dialog.setItems(minutes.toArray(new String[0]), (d, p) -> {
            String selectedItem = minutes.get(p);
            endMinuteInputView.setText(selectedItem);
        });
        dialog.show();
    }

    public void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                (v, y, m, d) -> {
                    String date = y + "-" + (m + 1) + "-" + d;
                    dateInputView.setText(date);
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show(getSupportFragmentManager(), "date");
    }

    public void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dialog = TimePickerDialog.newInstance(
                (v, h, m, s) -> {
                    String time = h + ":" + m;
                    timeInputView.setText(time);
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                0,
                true
        );

        dialog.show(getSupportFragmentManager(), "time");
    }

    public class AlarmAsync extends AsyncTask<String, Void, String> {
        private Context context;
        private Alarm alarm;
        private ProgressDialog progressDialog;

        public AlarmAsync(Context context, Alarm alarm) {
            this.context = context;
            this.alarm = alarm;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("알람 등록 중입니다.");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String title = params[0];
            String description = params[1];
            String creator = params[2];
            String alarmTime = params[3];
            String endTime = params[4];

            log(endTime);

            if (!editing) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("title", title)
                            .add("description", description)
                            .add("creator", creator)
                            .add("alarmTime", alarmTime)
                            .add("endTime", endTime)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://54.180.169.157:8080/PromiseAlarm/AddAlarm")
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();

                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                JSONObject resultJson = new JSONObject(result);
                log(resultJson.toString());

                if (!editing) {
                    if (resultJson.getInt("code") == 200) {
                        Toast.makeText(context, "알람 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        alarm.setId(resultJson.getInt("id"));
                        alarm.setModifiedCount(0);
                        alarm.setCreatedDateTime(Calendar.getInstance());
                        MySQLiteHelper.getInstance(context).insertAlarm(alarm);

                        String id = getSharedPreferences("authentication", MODE_PRIVATE).getString("id", "none");
                        String name = getSharedPreferences("authentication", MODE_PRIVATE).getString("name", "none");

                        //MySQLiteHelper.getInstance(context).insertAlarmUser(alarm.getId(), id, name);

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference alarmRef = rootRef.child("alarm");
                        alarmRef.child(alarm.getId() + "").setValue(alarm.getTitle());

                        AlarmUserDTO alarmUserDTO = new AlarmUserDTO(id, name);

                        DatabaseReference alarmUserRef = rootRef.child("alarm_user");
                        alarmUserRef.child(alarm.getId() + "").child(id).setValue(alarmUserDTO);

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

                        finish();
                    } else {

                    }
                } else {
                    if (resultJson.getInt("code") == 200) {

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
