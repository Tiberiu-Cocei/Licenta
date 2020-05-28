package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.TransactionFinishDto;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Transaction;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;
import com.android.android.utilities.JsonConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionFinishActivity extends AppCompatActivity {

    private TextView messageText;

    private Button finishButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO : de testat
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_finish);

        context = this;
        messageText = findViewById(R.id.messageText);
        populateTextViews();

        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTransaction();
            }
        });
    }

    private void finishTransaction() {
        AppDetails appDetails = AppDetails.getAppDetails();
        boolean closeToStation = DistanceCalculator.isCloseToStation(appDetails.getStationCoordinates());
        if(closeToStation) {
            TransactionFinishDto transactionFinishDto = new TransactionFinishDto(appDetails.getStartStationId());
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix) + "/transactions/finalize";
            String jsonString = JsonConverter.objectToJson(transactionFinishDto);
            try {
                apiCaller.execute("PUT", url, User.getUser().getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                messageText.setText(apiResponse.getJson());
                finishButton.setClickable(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityStarter.openMapActivity(context);
                    }
                }, 7500);
            } catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
        else {
            messageText.setText(getResources().getString(R.string.far_from_station));
        }
    }

    private void populateTextViews() {
        AppDetails appDetails = AppDetails.getAppDetails();
        Transaction transaction = appDetails.getTransaction();
        TextView startStation = findViewById(R.id.finishStartStation);
        TextView plannedStation = findViewById(R.id.finishPlannedStation);
        TextView finishStation = findViewById(R.id.finishFinishStation);
        TextView startTime = findViewById(R.id.finishStartTime);
        TextView plannedTime = findViewById(R.id.finishPlannedTime);
        TextView currentTime = findViewById(R.id.finishCurrentTime);

        String datePattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);

        startStation.setText(getResources().getString(R.string.transaction_start_station, transaction.getStartStation()));
        if(transaction.getPlannedStation() != null) {
            plannedStation.setText(getResources().getString(R.string.transaction_planned_station, transaction.getPlannedStation()));
        }
        finishStation.setText(getResources().getString(R.string.transaction_finish_station, appDetails.getStartStationName()));
        startTime.setText(getResources().getString(R.string.transaction_start_time, dateFormat.format(transaction.getStartTime())));
        plannedTime.setText(getResources().getString(R.string.transaction_planned_time, dateFormat.format(transaction.getPlannedTime())));
        currentTime.setText(getResources().getString(R.string.transaction_finish_time, dateFormat.format(new Date())));
    }
}
