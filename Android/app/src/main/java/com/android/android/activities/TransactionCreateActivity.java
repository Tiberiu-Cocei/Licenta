package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.android.R;
import com.android.android.dtos.TransactionCreateDto;
import com.android.android.dtos.TransactionPreviewDto;
import com.android.android.entities.AppDetails;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;
import com.android.android.utilities.JsonConverter;
import com.android.android.utilities.NotificationWorker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransactionCreateActivity extends AppCompatActivity {

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

        TextView plannedStationNameText = findViewById(R.id.createPlannedStationName);
        TextView discountValueText = findViewById(R.id.createDiscountInfo);
        this.messageText = findViewById(R.id.messageText);
        this.cardPinText = findViewById(R.id.createCardPin);

        setArrivalTimeListener();
        setArrivalStationButtonAction();
        setDiscountButtonAction();
        setPreviewButtonAction();
        setCreateButtonAction();

        if(appDetails.getPlannedStationName() != null) {
            plannedStationNameText.setText(appDetails.getPlannedStationName());
        }
        if(appDetails.getDiscount() != null) {
            discountValueText.setText(String.format(appDetails.getDiscount().getDiscountValue().toString(), Locale.US));
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
                        calendar.set(Calendar.HOUR, selectedHour + 12); // +12 din cauza ca ora 16 devine 4
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1); // -1 din cauza ca incepe de la 0 ziua in calendar
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

    private void setDiscountButtonAction() {
        Button discountButton = this.findViewById(R.id.createChooseDiscount);
        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStarter.openDiscountActivity(context);
            }
        });
    }

    private void setPreviewButtonAction() {
        Button previewButton = this.findViewById(R.id.createPreviewTransaction);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewTransaction();
            }
        });
    }

    private void previewTransaction() {
        AppDetails appDetails = AppDetails.getAppDetails();
        if(appDetails.getPlannedStationId() == null) {
            messageText.setText(getResources().getString(R.string.transaction_create_invalid_arrival_station));
            return;
        }
        if(plannedArrivalTime == null || plannedArrivalTime.compareTo(new Date()) < 0) {
            messageText.setText(getResources().getString(R.string.transaction_create_invalid_arrival_time));
            return;
        }
        TransactionPreviewDto transactionPreviewDto = new TransactionPreviewDto(plannedArrivalTime);
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/transactions/preview";
        String jsonString = JsonConverter.objectToJson(transactionPreviewDto);
        try {
            apiCaller.execute("POST", url, User.getUser().getAuthenticationToken().toString(), jsonString);
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                Double discountVal = Double.valueOf(apiResponse.getJson());
                messageText.setText(getResources().getString(R.string.transaction_obtained_preview_price, discountVal.toString()));
            }
            else {
                messageText.setText(getResources().getString(R.string.transaction_failed_preview_price));
            }
        } catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
    }

    private void setCreateButtonAction() {
        Button createTransactionButton = this.findViewById(R.id.createTransaction);
        createTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTransaction();
            }
        });
    }

    private void createTransaction() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHour < 7 || currentHour > 21) {
            messageText.setText(getResources().getString(R.string.transaction_create_invalid_time));
            return;
        }
        AppDetails appDetails = AppDetails.getAppDetails();
        boolean closeToStation = DistanceCalculator.isCloseToStation(appDetails.getStartStation().getCoordinates());
        if(closeToStation) {
            if(plannedArrivalTime == null || plannedArrivalTime.compareTo(new Date()) < 0) {
                messageText.setText(getResources().getString(R.string.transaction_create_invalid_arrival_time));
                return;
            }
            if(cardPinText.getText().toString().length() == 0) {
                messageText.setText(getResources().getString(R.string.transaction_create_invalid_pin));
                return;
            }
            if(appDetails.getPlannedStationId() == null) {
                messageText.setText(getResources().getString(R.string.transaction_create_invalid_arrival_station));
                return;
            }
            if(User.getUser().getPaymentMethodId() == null) {
                messageText.setText(getResources().getString(R.string.transaction_create_invalid_payment_method));
                return;
            }
            TransactionCreateDto transactionCreateDto = new TransactionCreateDto(
                    plannedArrivalTime, cardPinText.getText().toString());
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix) + "/transactions/create";
            String jsonString = JsonConverter.objectToJson(transactionCreateDto);
            try {
                apiCaller.execute("POST", url, User.getUser().getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                messageText.setText(apiResponse.getJson());

                findViewById(R.id.createTransaction).setClickable(false);
                findViewById(R.id.createTransaction).setAlpha(.5f);
                findViewById(R.id.createPreviewTransaction).setClickable(false);
                findViewById(R.id.createPreviewTransaction).setAlpha(.5f);
                findViewById(R.id.createChooseDiscount).setClickable(false);
                findViewById(R.id.createChooseDiscount).setAlpha(.5f);
                findViewById(R.id.createChoosePlannedStation).setClickable(false);
                findViewById(R.id.createChoosePlannedStation).setAlpha(.5f);

                AppDetails.resetTransactionValues();

                OneTimeWorkRequest locationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
                WorkManager.getInstance(this).enqueue(locationWorkRequest);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityStarter.openMapActivity(context);
                    }
                }, 3500);
            } catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
        else {
            messageText.setText(getResources().getString(R.string.far_from_station));
        }
    }

}
