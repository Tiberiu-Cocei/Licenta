package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.TransactionHistoryListAdapter;
import com.android.android.entities.Transaction;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;

import java.util.List;
import java.util.UUID;

public class TransactionHistoryActivity extends AppCompatActivity {

    private User user;

    private ListView transactionListView;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        user = User.getUser();
        transactionListView = findViewById(R.id.transactionList);
        messageText = findViewById(R.id.messageText);
        List<Transaction> transactionList = getUserTransactions();
        if(transactionList != null) {
            populateTransactionList(transactionList);
        }
    }

    private List<Transaction> getUserTransactions() {
        UUID userId = user.getId();
        List<Transaction> transactionList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/transactions/user";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    userId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        transactionList = Transaction.createTransactionListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_user_transactions));
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_user_transactions));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return transactionList;
    }

    private void populateTransactionList(List<Transaction> transactionList) {
        TransactionHistoryListAdapter transactionHistoryListAdapter = new TransactionHistoryListAdapter(
                this, R.layout.transaction_list_view, transactionList);
        transactionListView.setAdapter(transactionHistoryListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.transaction_history_menu, menu);
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
            case R.id.menu_payment:
                ActivityStarter.openPaymentActivity(this);
                return true;
            case R.id.menu_report_history:
                ActivityStarter.openReportHistoryActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
