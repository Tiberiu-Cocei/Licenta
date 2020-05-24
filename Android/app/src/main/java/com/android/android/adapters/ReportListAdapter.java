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

public class ReportListAdapter extends ArrayAdapter<Report> {

    private Context context;

    private int resource;

    public ReportListAdapter(Context context, int resource, List<Report> reportList) {
        super(context, resource, reportList);
        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String reportDescription = getItem(position).getDescription();
        String reportSeverity = String.valueOf(getItem(position).getSeverity());

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvReportDescription = convertView.findViewById(R.id.report_lv_description);
        TextView tvReportSeverity = convertView.findViewById(R.id.report_lv_severity);

        tvReportDescription.setText(reportDescription);
        tvReportSeverity.setText(reportSeverity);

        return convertView;
    }
}
