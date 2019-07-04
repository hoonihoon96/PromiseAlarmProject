package com.example.promisealarmfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.dto.AlarmUserDTO;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AlarmUserDTO> users;

    public UserListAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<AlarmUserDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_user, parent, false);
        }

        TextView userIdNameText = convertView.findViewById(R.id.user_id_name_text);

        AlarmUserDTO alarmUserDTO = users.get(position);

        String id = alarmUserDTO.getId();
        String name = alarmUserDTO.getName();

        userIdNameText.setText(id + "(" + name + ")");

        return convertView;
    }
}
