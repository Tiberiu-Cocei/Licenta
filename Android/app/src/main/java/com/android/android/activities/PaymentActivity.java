package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.PaymentMethodSaveDto;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    private EditText cardNumberText;

    private EditText expiryDateText;

    private EditText firstNameText;

    private EditText lastNameText;

    private TextView messageText;

    private Context context;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        context = this;
        cardNumberText = findViewById(R.id.paymentCardNumber);
        firstNameText = findViewById(R.id.paymentFirstName);
        lastNameText = findViewById(R.id.paymentLastName);
        messageText = findViewById(R.id.paymentMessage);
        createDateInputForEditText();

        if(User.getUser().getPaymentMethodId() == null) {
            messageText.setText(getResources().getString(R.string.payment_method_missing));
        }
        else {
            messageText.setText(getResources().getString(R.string.payment_method_found));
        }

        final Button savePayment = findViewById(R.id.paymentButton);
        savePayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptPaymentSave();
            }
        });
    }

    private void createDateInputForEditText() {
        calendar = Calendar.getInstance();
        expiryDateText = findViewById(R.id.paymentExpiryDate);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                expiryDateText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        expiryDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void attemptPaymentSave() {
        String cardNumber = cardNumberText.getText().toString();
        Date expiryDate = null;
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
            expiryDate = format.parse(expiryDateText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.payment_invalid_date));
        }

        if(cardNumber.length() == 0) {
            messageText.setText(getResources().getString(R.string.payment_empty_card_number));
        }
        else if(expiryDate == null) {
            messageText.setText(getResources().getString(R.string.payment_null_date));
        }
        else if(firstName.length() == 0) {
            messageText.setText(getResources().getString(R.string.payment_empty_first_name));
        }
        else if(lastName.length() == 0) {
            messageText.setText(getResources().getString(R.string.payment_empty_last_name));
        }
        else {
            PaymentMethodSaveDto paymentMethodSaveDto = new PaymentMethodSaveDto(cardNumber, expiryDate, firstName, lastName);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix)
                    + "/users/payment-methods/" + User.getUser().getId().toString();
            String jsonString = JsonConverter.objectToJson(paymentMethodSaveDto);
            try {
                apiCaller.execute("PUT", url, User.getUser().getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                if(apiResponse != null) {
                    if(apiResponse.getCode() == 200) {
                        String paymentMethodId = apiResponse.getJson().substring(1, apiResponse.getJson().length()-1);
                        User.getUser().setPaymentMethodId(UUID.fromString(paymentMethodId));
                        messageText.setText(getResources().getString(R.string.message_saved_payment_successfully));
                    }
                    else {
                        messageText.setText(getResources().getString(R.string.api_failed_payment_save));
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.payment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map:
                ActivityStarter.openMapActivity(this);
                return true;
            case R.id.menu_account:
                ActivityStarter.openAccountActivity(this);
                return true;
            case R.id.menu_message:
                ActivityStarter.openMessageActivity(this);
                return true;
            case R.id.menu_report_history:
                ActivityStarter.openReportHistoryActivity(this);
                return true;
            case R.id.menu_transaction_history:
                ActivityStarter.openTransactionHistoryActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
