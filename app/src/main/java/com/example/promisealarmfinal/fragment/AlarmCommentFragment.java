package com.example.promisealarmfinal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.promisealarmfinal.adapter.CommentListAdapter;
import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.dto.CommentDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmCommentFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.comment_list_view)
    ListView commentListView;

    @BindView(R.id.commentInput)
    EditText commentInput;

    private ArrayList<CommentDTO> comments;
    private CommentListAdapter adapter;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference commentRef;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        commentRef = rootRef.child("comment").child(id + "");
        View view = inflater.inflate(R.layout.fragment_alarm_comment, container, false);
        unbinder = ButterKnife.bind(this, view);

        comments = new ArrayList<>();
        adapter = new CommentListAdapter(getContext());

        adapter.setList(comments);
        commentListView.setAdapter(adapter);

        commentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "삐삑");
                CommentDTO comment = dataSnapshot.getValue(CommentDTO.class);
                comments.add(comment);
                adapter.setList(comments);
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

    @OnClick(R.id.send_button)
    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "none");
        String name = sharedPreferences.getString("name", "none");
        String comment = commentInput.getText().toString();
        String uploadDate = Calendar.getInstance().getTime().toString();

        CommentDTO commentDTO = new CommentDTO(id, name, comment, uploadDate);

        commentRef.push().setValue(commentDTO);
        commentInput.setText("");
    }
}
