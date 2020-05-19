package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.ReportHistoryListAdapter;
import com.android.android.entities.Report;
import com.android.android.entities.User;
import com.android.android.utilities.ActivityStarter;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;

import java.util.List;
import java.util.UUID;

public class ReportHistoryActivity extends AppCompatActivity {

    private User user;

    private ListView reportHistoryListView;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);
        user = User.getUser();
        reportHistoryListView = findViewById(R.id.reportHistoryList);
        messageText = findViewById(R.id.messageText);
        List<Report> reportList = getUserReports();
        if(reportList != null) {
            populateMessageList(reportList);
        }
    }

    private List<Report> getUserReports() {
        UUID userId = user.getId();
        List<Report> reportList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/reports/user";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    userId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        reportList = Report.createReportListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_user_reports));
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_user_reports));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return reportList;
    }

    private void populateMessageList(List<Report> reportList) {
        ReportHistoryListAdapter reportHistoryListAdapter = new ReportHistoryListAdapter(
                this, R.layout.report_history_list_view, reportList);
        reportHistoryListView.setAdapter(reportHistoryListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.report_history_menu, menu);
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
            case R.id.menu_payment:
                ActivityStarter.openPaymentActivity(this);
                return true;
            case R.id.menu_message:
                ActivityStarter.openMessageActivity(this);
                return true;
            case R.id.menu_transaction_history:
                ActivityStarter.openTransactionHistoryActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
