package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.BicycleListAdapter;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Bicycle;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;

import java.util.List;

public class StationActivity extends AppCompatActivity {

    private ListView bicycleListView;

    private TextView messageText;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        bicycleListView = findViewById(R.id.stationBicycleList);
        messageText = findViewById(R.id.messageText);
        List<Bicycle> bicycleList = getStationBicycles();
        context = this;
        if (bicycleList != null) {
            populateBicycleList(bicycleList);
        }
        createButtonListener();
    }

    private void createButtonListener() {
        final Button finishTransactionButton = findViewById(R.id.transactionFinishButton);
        int transactionCount = ApiCaller.getUnfinishedTransactionDetails(this).size();
        if(transactionCount == 1) {
            String stationCoordinates = AppDetails.getAppDetails().getStationCoordinates();
            boolean isCloseToStation = DistanceCalculator.isCloseToStation(stationCoordinates);
            if(isCloseToStation) {
                finishTransactionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityStarter.openFinishTransactionActivity(context);
                    }
                });
            }
            else {
                finishTransactionButton.setAlpha(.5f);
                finishTransactionButton.setClickable(false);
                messageText.setText(getResources().getString(R.string.far_from_station));
            }
        }
        else if (transactionCount == 0) {
            finishTransactionButton.setAlpha(.5f);
            finishTransactionButton.setClickable(false);
            messageText.setText(getResources().getString(R.string.account_no_active_transaction_found));
        }
        else {
            finishTransactionButton.setAlpha(.5f);
            finishTransactionButton.setClickable(false);
            messageText.setText(getResources().getString(R.string.account_too_many_active_transactions));
        }
    }

    private List<Bicycle> getStationBicycles() {
        List<Bicycle> bicycleList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/bicycles/station";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    AppDetails.getAppDetails().getStationId().toString());
            ApiResponse apiResponse = apiCaller.get();
            if (apiResponse != null) {
                if (apiResponse.getCode() == 200) {
                    try {
                        bicycleList = Bicycle.createBicycleListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_station_bicycles));
                    } catch (Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_station_bicycles));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return bicycleList;
    }

    private void populateBicycleList(List<Bicycle> bicycleList) {
        BicycleListAdapter bicycleListAdapter = new BicycleListAdapter(
                this, R.layout.bicycle_list_view, bicycleList);
        bicycleListView.setAdapter(bicycleListAdapter);
    }
}