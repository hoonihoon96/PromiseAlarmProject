package com.example.promisealarmfinal.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.promisealarmfinal.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.id_text_view)
    EditText idText;

    @BindView(R.id.pw_text_view)
    EditText pwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);


        if(sharedPreferences != null) {
            String currentId = sharedPreferences.getString("id", "none");

            if (!currentId.equals("none")) {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    @OnClick({R.id.sign_in_button, R.id.sign_up_button})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();
                String token = getSharedPreferences("token",MODE_PRIVATE).getString("token", "none");

                new SignInAsync(this).execute(id, pw, token);
                break;
            case R.id.sign_up_button:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }

    public class SignInAsync extends AsyncTask<String, Void, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public SignInAsync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("로그인 중입니다.");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String id = params[0];
                String pw = params[1];
                String token = params[2];

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("id", id)
                        .add("password", pw)
                        .add("token", token)
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.180.169.157:8080/PromiseAlarm/SignIn")
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

            try {
                JSONObject resultJson = new JSONObject(result);

                if (resultJson.getInt("code") == 200) {
                    Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("authentication", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", idText.getText().toString());
                    editor.putString("name", resultJson.getString("name"));
                    editor.apply();

                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                } else if (resultJson.getInt("code") == 400) {
                    Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
