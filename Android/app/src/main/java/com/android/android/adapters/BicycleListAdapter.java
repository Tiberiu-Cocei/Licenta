package com.android.android.adapters;

import android.content.Context;
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
import com.android.android.entities.Station;
import com.android.android.entities.Transaction;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BicycleListAdapter extends ArrayAdapter<Bicycle> {

    private Context context;

    private int resource;

    private int transactionCount;

    public BicycleListAdapter(Context context, int resource, List<Bicycle> bicycleList) {
        super(context, resource, bicycleList);
        this.context = context;
        this.resource = resource;
        this.transactionCount = getUnfinishedTransactionDetails().size();
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
        tvModel.setText(context.getResources().getString(R.string.bicycle_model, model));
        if(lockNumber != null) {
            tvLockNumber.setText(context.getResources().getString(R.string.bicycle_lock_number, lockNumber.toString()));
        }
        if(arrivalTime != null) {
            tvArrivalTime.setText(context.getResources().getString(R.string.bicycle_arrival_time, dateFormat.format(arrivalTime)));
            //TODO : de verificat daca merge
        }

        createButtonListeners(convertView, position);

        return convertView;
    }

    private void createButtonListeners(View convertView, final int position) {
        Button reportButton = convertView.findViewById(R.id.bicycleReports);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID bicycleId = getItem(position).getId();
                AppDetails.getAppDetails().setBicycleId(bicycleId);
                //TODO : intent pentru ReportActivity
            }
        });

        if(transactionCount == 0) {
            String stationCoordinates = null;
            for(Station station : AppDetails.getAppDetails().getStationList()) {
                if(station.getId() == AppDetails.getAppDetails().getStationId()) {
                    stationCoordinates = station.getCoordinates();
                    break;
                }
            }
            Double distanceDifference = DistanceCalculator.calculateDistance(stationCoordinates);
            //TODO : in functie de distanta, se face grey-out la butonul de jos sau nu, de pus in if-ul asta?
        }

        Button selectButton = convertView.findViewById(R.id.bicycleSelect);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID bicycleId = getItem(position).getId();
                AppDetails.getAppDetails().setBicycleId(bicycleId);
                //TODO: intent pentru Transaction Start Activity
            }
        });
    }

    private List<Transaction> getUnfinishedTransactionDetails() {
        UUID userId = User.getUser().getId();
        List<Transaction> transactionList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = context.getResources().getString(
                R.string.api_secure_prefix) + "/transactions/get-active-transaction";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    userId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        transactionList = Transaction.createTransactionListFromJson(apiResponse.getJson());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }

}
