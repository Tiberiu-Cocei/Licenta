package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.Report;

import java.util.List;

public class ReportHistoryListAdapter extends ArrayAdapter<Report> {

    private Context context;

    private int resource;

    public ReportHistoryListAdapter(Context context, int resource, List<Report> reportList) {
        super(context, resource, reportList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String reportDescription = getItem(position).getDescription();
        String reportSeverity = String.valueOf(getItem(position).getSeverity());
        String inspectionDescription = null;
        String inspectionReview = null;

        if(getItem(position).getInspection() != null) {
            inspectionDescription = getItem(position).getInspection().getDescription();
            if(!getItem(position).getInspection().isFake()) {
                inspectionReview = "Accepted";
            }
            else {
                inspectionReview = "Rejected";
            }
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvReportDescription = convertView.findViewById(R.id.report_hlv_report_description);
        TextView tvReportSeverity = convertView.findViewById(R.id.report_hlv_report_severity);

        tvReportDescription.setText(reportDescription);
        tvReportSeverity.setText(reportSeverity);

        if(inspectionDescription != null) {
            TextView tvInspectionDescription = convertView.findViewById(R.id.report_hlv_inspection_description);
            TextView tvInspectionReview = convertView.findViewById(R.id.report_hlv_inspection_review);

            tvInspectionDescription.setText(inspectionDescription);
            tvInspectionReview.setText(inspectionReview);
        }

        return convertView;
    }
}
