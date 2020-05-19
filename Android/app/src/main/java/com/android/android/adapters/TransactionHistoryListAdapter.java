package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryListAdapter extends ArrayAdapter<Transaction> {

    private Context context;

    private int resource;

    public TransactionHistoryListAdapter(Context context, int resource, List<Transaction> transactionList) {
        super(context, resource, transactionList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String startStation = getItem(position).getStartStation();
        String plannedStation = getItem(position).getPlannedStation();
        String finishStation = getItem(position).getFinishStation();
        Date startTime = getItem(position).getStartTime();
        Date plannedTime = getItem(position).getPlannedTime();
        Date finishTime = getItem(position).getFinishTime();
        Double initialCost = getItem(position).getInitialCost();
        Double discountValue = getItem(position).getDiscountValue();
        Double penalty = getItem(position).getPenalty();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        String datePattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);

        TextView tvStartStation = convertView.findViewById(R.id.transaction_lv_start_station);
        TextView tvPlannedStation = convertView.findViewById(R.id.transaction_lv_planned_station);
        TextView tvFinishStation = convertView.findViewById(R.id.transaction_lv_finish_station);
        TextView tvStartTime = convertView.findViewById(R.id.transaction_lv_start_time);
        TextView tvPlannedTime = convertView.findViewById(R.id.transaction_lv_planned_time);
        TextView tvFinishTime = convertView.findViewById(R.id.transaction_lv_finish_time);
        TextView tvInitialCost = convertView.findViewById(R.id.transaction_lv_initial_cost);
        TextView tvDiscountValue = convertView.findViewById(R.id.transaction_lv_discount_value);
        TextView tvPenalty = convertView.findViewById(R.id.transaction_lv_penalty);

        tvStartStation.setText(context.getResources().getString(R.string.transaction_start_station, startStation));
        tvStartTime.setText(context.getResources().getString(R.string.transaction_start_time, dateFormat.format(startTime)));
        tvPlannedTime.setText(context.getResources().getString(R.string.transaction_planned_time, dateFormat.format(plannedTime)));
        tvInitialCost.setText(context.getResources().getString(R.string.transaction_initial_cost, String.format(initialCost.toString(), Locale.US)));
        if(plannedStation != null) {
            tvPlannedStation.setText(context.getResources().getString(R.string.transaction_planned_station, plannedStation));
        }
        if(finishStation != null) {
            tvFinishStation.setText(context.getResources().getString(R.string.transaction_finish_station, finishStation));
        }
        if(finishTime != null) {
            tvFinishTime.setText(context.getResources().getString(R.string.transaction_finish_time, dateFormat.format(finishTime)));
        }
        if(discountValue != null) {
            tvDiscountValue.setText(context.getResources().getString(R.string.transaction_discount, String.format(discountValue.toString(), Locale.US)));
        }
        if(penalty != null) {
            tvPenalty.setText(context.getResources().getString(R.string.transaction_penalty, String.format(penalty.toString(), Locale.US)));
        }

        return convertView;
    }

}
