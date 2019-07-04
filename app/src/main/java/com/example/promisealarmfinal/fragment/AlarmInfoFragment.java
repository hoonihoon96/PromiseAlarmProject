package com.example.promisealarmfinal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.helper.MySQLiteHelper;
import com.example.promisealarmfinal.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AlarmInfoFragment extends BaseFragment {
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

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        View view = inflater.inflate(R.layout.fragment_alarm_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        Alarm alarm = MySQLiteHelper.getInstance(getContext()).selectAlarmById(id);

        titleInputView.setText(alarm.getTitle());
        descriptionInputView.setText(alarm.getDescription());
        dateInputView.setText(alarm.getAlarmDateInString());
        timeInputView.setText(alarm.getAlarmTimeInString());
        endMinuteInputView.setText(alarm.getEndMinuteInString());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
