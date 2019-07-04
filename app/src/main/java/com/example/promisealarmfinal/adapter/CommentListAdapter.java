package com.example.promisealarmfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.promisealarmfinal.R;
import com.example.promisealarmfinal.dto.CommentDTO;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommentDTO> comments;

    public CommentListAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<CommentDTO> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)) .inflate(R.layout.item_comment, parent, false);
        }

        TextView idNameText = convertView.findViewById(R.id.id_name_text);
        TextView messageText = convertView.findViewById(R.id.message_text);
        TextView uploadDateText = convertView.findViewById(R.id.upload_date_text);

        CommentDTO commentDTO = comments.get(position);

        String id = commentDTO.getId();
        String name = commentDTO.getName();

        idNameText.setText(name + "(" + id + ")");
        messageText.setText(commentDTO.getComment());
        uploadDateText.setText(commentDTO.getUploadDate());

        return convertView;
    }
}
