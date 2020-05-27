package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.android.R;
import com.android.android.entities.AppDetails;
import com.android.android.utilities.ActivityStarter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TransactionCreateActivity extends AppCompatActivity {

    private TextView plannedStationNameText;

    private TextView discountValueText;

    private TextView messageText;

    private EditText cardPinText;

    private Context context;

    private Date plannedArrivalTime;

    private AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);
        this.context = this;
        this.appDetails = AppDetails.getAppDetails();

        this.plannedStationNameText = findViewById(R.id.createPlannedStationName);
        this.discountValueText = findViewById(R.id.createDiscountInfo);
        this.messageText = findViewById(R.id.messageText);
        this.cardPinText = findViewById(R.id.createCardPin);

        setArrivalTimeListener();
        setArrivalStationButtonAction();

        if(appDetails.getPlannedStationName() != null) {
            this.plannedStationNameText.setText(appDetails.getPlannedStationName());
        }
    }

    private void setArrivalTimeListener() {
        final EditText plannedArrivalTimeText = findViewById(R.id.createPlannedTime);
        plannedArrivalTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        plannedArrivalTimeText.setText(selectedHour + ":" + selectedMinute);
                        calendar.set(Calendar.HOUR, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        plannedArrivalTime = calendar.getTime();
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select planned arrival time");
                mTimePicker.show();
            }
        });
    }

    private void setArrivalStationButtonAction() {
        Button arrivalStationButton = this.findViewById(R.id.createChoosePlannedStation);
        arrivalStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDetails.setChoosingPlannedStation(true);
                ActivityStarter.openMapActivity(context);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //appDetails.setChoosingPlannedStation(false); DO THIS AFTER TRANSACTION FINISH
        //TODO : set stuff from app details to null
    }
}
