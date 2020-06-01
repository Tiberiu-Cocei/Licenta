package com.android.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Bicycle;
import com.android.android.entities.Transaction;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BicycleListAdapter extends ArrayAdapter<Bicycle> {

    private Context context;

    private int resource;

    private List<Transaction> transactionList;

    public BicycleListAdapter(Context context, int resource, List<Bicycle> bicycleList) {
        super(context, resource, bicycleList);
        this.context = context;
        this.resource = resource;
        this.transactionList = ApiCaller.getUnfinishedTransactionDetails(context);
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
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

        if(status.equals("Station")) {
            tvStatus.setTextColor(Color.GREEN);
        }
        else if(status.equals("Damaged") || status.equals("Stolen")) {
            tvStatus.setTextColor(Color.RED);
        }
        else {
            tvStatus.setTextColor(Color.YELLOW);
        }

        tvModel.setText(context.getResources().getString(R.string.bicycle_model, model));
        if(lockNumber != null) {
            tvLockNumber.setText(context.getResources().getString(R.string.bicycle_lock_number, lockNumber.toString()));
        }
        if(arrivalTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(arrivalTime);
            calendar.add(Calendar.HOUR, -2);
            arrivalTime = calendar.getTime();
            tvArrivalTime.setText(context.getResources().getString(R.string.bicycle_arrival_time, dateFormat.format(arrivalTime)));
        }

        createButtonListeners(convertView, position);

        return convertView;
    }

    private void createButtonListeners(View convertView, final int position) {
        final Button reportButton = convertView.findViewById(R.id.bicycleReports);
        UUID bicycleId = getItem(position).getId();
        Integer reportNumber = getReportNumberForBicycle(bicycleId);
        reportButton.setText(context.getResources().getString(
                R.string.report_button_text, reportNumber.toString()));

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID bicycleId = getItem(position).getId();
                AppDetails.getAppDetails().setBicycleId(bicycleId);
                ActivityStarter.openReportActivity(context);
            }
        });

        final Button selectButton = convertView.findViewById(R.id.bicycleSelect);
        if (transactionList.size() == 0 && getItem(position).getStatus().equals("Station")) {
            String stationCoordinates = AppDetails.getAppDetails().getStartStation().getCoordinates();
            boolean showSelectButton = DistanceCalculator.isCloseToStation(stationCoordinates);
            if (showSelectButton) {
                selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UUID bicycleId = getItem(position).getId();
                        AppDetails.getAppDetails().setBicycleId(bicycleId);
                        ActivityStarter.openTransactionCreateActivity(context);
                    }
                });
            } else {
                selectButton.setAlpha(.5f);
                selectButton.setClickable(false);
            }
        } else {
            selectButton.setAlpha(.5f);
            selectButton.setClickable(false);
        }
    }

    private Integer getReportNumberForBicycle(UUID bicycleId) {
        Integer reportNumber = 0;
        ApiCaller apiCaller = new ApiCaller();
        String url = context.getResources().getString(R.string.api_secure_prefix) + "/reports/count";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(), bicycleId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        String numberResponse = apiResponse.getJson();
                        reportNumber = Integer.valueOf(numberResponse);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return reportNumber;
    }
}
