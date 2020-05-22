package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.BicycleListAdapter;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Bicycle;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;

import java.util.List;

public class StationActivity extends AppCompatActivity {

    private ListView bicycleListView;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        bicycleListView = findViewById(R.id.stationBicycleList);
        messageText = findViewById(R.id.messageText);
        List<Bicycle> bicycleList = getStationBicycles();
        if (bicycleList != null) {
            populateBicycleList(bicycleList);
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