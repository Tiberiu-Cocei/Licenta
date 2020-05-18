package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageListAdapter extends ArrayAdapter<Message> {

    private Context context;

    private int resource;

    public MessageListAdapter(Context context, int resource, List<Message> messageList) {
        super(context, resource, messageList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String description = getItem(position).getDescription();
        Date date = getItem(position).getDate();

        String datePattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);
        String dateToString = dateFormat.format(date);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvDescription = convertView.findViewById(R.id.message_list_view_description);
        TextView tvDate = convertView.findViewById(R.id.message_list_view_date);

        tvDescription.setText(description);
        tvDate.setText(dateToString);

        return convertView;
    }
}
