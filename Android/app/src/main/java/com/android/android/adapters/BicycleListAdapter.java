package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.Bicycle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BicycleListAdapter extends ArrayAdapter<Bicycle> {

    private Context context;

    private int resource;

    public BicycleListAdapter(Context context, int resource, List<Bicycle> bicycleList) {
        super(context, resource, bicycleList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String status = getItem(position).getStatus();
        String model = getItem(position).getModel();
        Integer lockNumber = getItem(position).getLockNumber();
        Date arrivalTime = getItem(position).getArrivalTime();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        String datePattern = "HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);

        TextView tvStatus = convertView.findViewById(R.id.bicycle_status);
        TextView tvModel = convertView.findViewById(R.id.bicycle_model);
        TextView tvLockNumber = convertView.findViewById(R.id.bicycle_lock_number);
        TextView tvArrivalTime = convertView.findViewById(R.id.bicycle_arrival_time);

        tvStatus.setText(context.getResources().getString(R.string.bicycle_status, status));
        tvModel.setText(context.getResources().getString(R.string.bicycle_model, model));
        if(lockNumber != null) {
            tvLockNumber.setText(context.getResources().getString(R.string.bicycle_lock_number, lockNumber.toString()));
        }
        if(arrivalTime != null) {
            tvArrivalTime.setText(context.getResources().getString(R.string.bicycle_arrival_time, dateFormat.format(arrivalTime)));
            //TODO : de verificat daca merge
        }

        //TODO : functionalitatea la butoane, gray-ed out in anumite cazuri

        return convertView;
    }

}
