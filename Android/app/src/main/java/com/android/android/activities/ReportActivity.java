package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.ReportListAdapter;
import com.android.android.dtos.ReportCreateDto;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Report;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;
import com.android.android.utilities.DistanceCalculator;
import com.android.android.utilities.JsonConverter;

import java.util.List;
import java.util.UUID;

public class ReportActivity extends AppCompatActivity {

    private User user;

    private ListView reportListView;

    private TextView messageText;

    private EditText reportDescription;

    private EditText reportSeverity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        user = User.getUser();
        reportListView = findViewById(R.id.bicycleReportList);
        messageText = findViewById(R.id.messageText);
        reportDescription = findViewById(R.id.reportDescription);
        reportSeverity = findViewById(R.id.reportSeverity);
        List<Report> reportList = getBicycleReports();
        if(reportList != null) {
            populateReportList(reportList);
        }

        Button reportCreateButton = findViewById(R.id.reportCreate);
        reportCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DistanceCalculator.isCloseToStation(AppDetails.getAppDetails().getStartStation().getCoordinates())) {
                    createReport();
                }
                else {
                    messageText.setText(getResources().getString(R.string.far_from_station));
                }
            }
        });
    }

    private void createReport() {
        String reportDescriptionString = reportDescription.getText().toString();
        String reportSeverityString = reportSeverity.getText().toString();
        int reportSeverityInt = -1;
        if(reportSeverityString.length() == 0) {
            messageText.setText(getResources().getString(R.string.report_empty_severity));
        }
        else {
            reportSeverityInt = Integer.valueOf(reportSeverity.getText().toString());
        }
        if(reportDescriptionString.length() < 10 || reportDescriptionString.length() > 500) {
            messageText.setText(getResources().getString(R.string.report_wrong_description_size));
        }
        else if(reportSeverityInt < 1 || reportSeverityInt > 10) {
            messageText.setText(getResources().getString(R.string.report_wrong_severity));
        }
        else {
            UUID userId = User.getUser().getId();
            UUID bicycleId = AppDetails.getAppDetails().getBicycleId();
            ReportCreateDto  reportCreateDto = new ReportCreateDto(
                    userId, bicycleId, reportSeverityInt, reportDescriptionString);
            ApiCaller apiCaller = new ApiCaller();
            String url = getResources().getString(R.string.api_secure_prefix) + "/reports";
            String jsonString = JsonConverter.objectToJson(reportCreateDto);
            try {
                apiCaller.execute("POST", url, user.getAuthenticationToken().toString(), jsonString);
                ApiResponse apiResponse = apiCaller.get();
                messageText.setText(apiResponse.getJson());
                List<Report> reportList = getBicycleReports();
                if(reportList != null) {
                    populateReportList(reportList);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                messageText.setText(getResources().getString(R.string.api_generic_call_failure));
            }
        }
    }

    private List<Report> getBicycleReports() {
        UUID bicycleId = AppDetails.getAppDetails().getBicycleId();
        List<Report> reportList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/reports/bicycle";
        try {
            apiCaller.execute("GET", url, user.getAuthenticationToken().toString(), bicycleId.toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        reportList = Report.createReportListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_bicycle_reports));
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_bicycle_reports));
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

    private void populateReportList(List<Report> reportList) {
        ReportListAdapter reportListAdapter = new ReportListAdapter(
                this, R.layout.report_list_view, reportList);
        reportListView.setAdapter(reportListAdapter);
    }
}
