package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.dtos.UserModifyDto;
import com.android.android.entities.Transaction;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.JsonConverter;

import org.apache.commons.validator.routines.EmailValidator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AccountActivity extends AppCompatActivity {

    private EditText newEmail;

    private EditText currentPassword;

    private EditText newPassword;

    private TextView startStation;

    private TextView plannedStation;

    private TextView startTime;

    private TextView plannedTime;

    private TextView warningCount;

    private TextView detailsChangeMessage;

    private TextView currentTransactionMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        newEmail = findViewById(R.id.accountNewEmail);
        currentPassword = findViewById(R.id.accountOldPassword);
        newPassword = findViewById(R.id.accountNewPassword);
        startStation = findViewById(R.id.accountStartStationDetails);
        plannedStation = findViewById(R.id.accountPlannedStationDetails);
        startTime = findViewById(R.id.accountStartTimeDetails);
        plannedTime = findViewById(R.id.accountPlannedTimeDetails);
        warningCount = findViewById(R.id.accountWarningsDetails);
        detailsChangeMessage = findViewById(R.id.accountChangeMessage);
        currentTransactionMessage = findViewById(R.id.accountTransactionMessage);

        warningCount.setText(String.valueOf(User.getUser().getWarningCount()));

        getUnfinishedTransactionDetails();

        final Button changeEmail = findViewById(R.id.accountChangeEmailButton);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeEmail();
            }
        });

        final Button changePassword = findViewById(R.id.accountChangePasswordButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = this.currentPassword.getText().toString();
        String newPassword = this.newPassword.getText().toString();
        if(currentPassword.length() == 0) {
            detailsChangeMessage.setText(getResources().getString(R.string.account_empty_current_password));
        }
        else if(newPassword.length() == 0) {
            detailsChangeMessage.setText(getResources().getString(R.string.account_empty_new_password));
        }
        else {
            UserModifyDto userModifyDto = new UserModifyDto(null, User.getUser().getUsername(), currentPassword, newPassword);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix) + "/users/modify/";
            String jsonString = JsonConverter.objectToJson(userModifyDto);
            try {
                apiCaller.execute("PUT", url, User.getUser().getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                if(apiResponse != null) {
                    if(apiResponse.getCode() == 200) {
                        detailsChangeMessage.setText(getResources().getString(R.string.account_successfully_change_password));
                    }
                    else {
                        detailsChangeMessage.setText(getResources().getString(R.string.account_failure_change_password));
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                detailsChangeMessage.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }

    private void changeEmail() {
        String currentPassword = this.currentPassword.getText().toString();
        if(currentPassword.length() == 0) {
            detailsChangeMessage.setText(getResources().getString(R.string.account_empty_current_password));
            return;
        }
        String email = newEmail.getText().toString();
        boolean isEmailValid = EmailValidator.getInstance().isValid(email);

        if(isEmailValid) {
            UserModifyDto userModifyDto = new UserModifyDto(email, User.getUser().getUsername(), currentPassword, null);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix) + "/users/modify/";
            String jsonString = JsonConverter.objectToJson(userModifyDto);
            try {
                apiCaller.execute("PUT", url, User.getUser().getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                if(apiResponse != null) {
                    if(apiResponse.getCode() == 200) {
                        detailsChangeMessage.setText(getResources().getString(R.string.account_successfully_change_email));
                    }
                    else {
                        detailsChangeMessage.setText(getResources().getString(R.string.account_failure_change_email));
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                detailsChangeMessage.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
        else {
            detailsChangeMessage.setText(getResources().getString(R.string.account_invalid_new_email));
        }
    }

    private void getUnfinishedTransactionDetails() {
        UUID userId = User.getUser().getId();
        List<Transaction> transactionList;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(
                R.string.api_secure_prefix) + "/transactions/get-active-transaction";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    userId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        transactionList = Transaction.createTransactionListFromJson(apiResponse.getJson());
                        if(transactionList.size() == 0) {
                            currentTransactionMessage.setText(getResources().getString(R.string.account_no_active_transaction_found));
                        }
                        else if(transactionList.size() > 1) {
                            currentTransactionMessage.setText(getResources().getString(R.string.account_too_many_active_transactions));
                        }
                        else {
                            startStation.setText(transactionList.get(0).getStartStation());
                            plannedStation.setText(transactionList.get(0).getPlannedStation());

                            String datePattern = "MM/dd/yyyy HH:mm:ss";
                            DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);
                            String startTime = dateFormat.format(transactionList.get(0).getStartTime());
                            String plannedTime = dateFormat.format(transactionList.get(0).getPlannedTime());

                            this.startTime.setText(startTime);
                            this.plannedTime.setText(plannedTime);
                        }
                    } catch(Exception e) {
                        currentTransactionMessage.setText(getResources().getString(R.string.account_failed_to_get_active_transaction));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            currentTransactionMessage.setText(getResources().getString(R.string.api_generic_call_failure));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map:
                ActivityStarter.openMapActivity(this);
                return true;
            case R.id.menu_message:
                ActivityStarter.openMessageActivity(this);
                return true;
            case R.id.menu_payment:
                ActivityStarter.openPaymentActivity(this);
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
