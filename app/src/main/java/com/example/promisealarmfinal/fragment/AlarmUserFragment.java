package com.example.promisealarmfinal.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.adapter.UserListAdapter;
import com.example.promisealarmfinal.dto.AlarmUserDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlarmUserFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.user_list_view)
    ListView userListView;

    private UserListAdapter adapter;

    private Unbinder unbinder;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference alarmUserRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_alarm_user, container, false);
        unbinder = ButterKnife.bind(this, view);

        alarmUserRef = rootRef.child("alarm_user").child(id + "");

        ArrayList<AlarmUserDTO> users = new ArrayList<>();
        adapter = new UserListAdapter(getContext());

        adapter.setList(users);
        userListView.setAdapter(adapter);

        alarmUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AlarmUserDTO alarmUserDTO = dataSnapshot.getValue(AlarmUserDTO.class);
                users.add(alarmUserDTO);
                adapter.setList(users);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_user_button)
    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        EditText inputText = new EditText(getContext());

        dialog.setTitle("유저 초대");
        dialog.setMessage("초대할 유저의 아이디를 입력해주세요.");
        dialog.setView(inputText);
        dialog.setPositiveButton("확인", (d, w) -> {
            String sender = getContext().getSharedPreferences("authentication", Context.MODE_PRIVATE).getString("id", "none");
            String receiver = inputText.getText().toString();
            String id = String.valueOf(getArguments().getInt("id"));

            new AddUserAsync(getContext()).execute(sender, receiver, id);
        });
        dialog.setNegativeButton("취소", (d, w) -> {

        });
        dialog.show();
    }

    public class AddUserAsync extends AsyncTask<String, Void, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public AddUserAsync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("초대 메시지 전송 중입니다.");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String sender = params[0];
                String receiver = params[1];
                String id = params[2];

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("sender", sender)
                        .add("receiver", receiver)
                        .add("id", id)
                        .build();

                Request request = new Request.Builder()
                        .url("http://54.180.169.157:8080/PromiseAlarm/SendInvite")
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

                int code = resultJson.getInt("code");

                if (code == 200) {
                    Toast.makeText(context, "초대를 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    int subCode = resultJson.getInt("sub_code");

                    if (subCode == 0) {
                        Toast.makeText(context, "존재하지 않는 유저입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "서버에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
