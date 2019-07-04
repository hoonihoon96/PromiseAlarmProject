package com.example.promisealarmfinal.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.promisealarmfinal.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.id_text_view)
    EditText idText;

    @BindView(R.id.pw_text_view)
    EditText pwText;

    @BindView(R.id.name_text_view)
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.submit_button, R.id.cancel_button})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();
                String name = nameText.getText().toString();
                String token = getSharedPreferences("token",MODE_PRIVATE).getString("token", "none");

                new SignUpAsync(this).execute(id, pw, name, token);
                break;
            case R.id.cancel_button:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class SignUpAsync extends AsyncTask<String, Void, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public SignUpAsync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("회원가입 중입니다.");
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
                String name = params[2];
                String token = params[3];

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("id", id)
                        .add("password", pw)
                        .add("name", name)
                        .add("token", token)
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.180.169.157:8080/PromiseAlarm/SignUp")
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
                    Toast.makeText(context, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                } else if (resultJson.getInt("code") == 400) {
                    Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
