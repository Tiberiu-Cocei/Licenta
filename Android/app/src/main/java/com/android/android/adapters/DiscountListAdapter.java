package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.android.R;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Discount;
import com.android.android.entities.Station;
import com.android.android.utilities.ActivityStarter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DiscountListAdapter extends ArrayAdapter<Discount> {

    private Context context;

    private int resource;

    private AppDetails appDetails;

    public DiscountListAdapter(Context context, int resource, List<Discount> discountList) {
        super(context, resource, discountList);
        this.context = context;
        this.resource = resource;
        this.appDetails = AppDetails.getAppDetails();
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        UUID toStationId = getItem(position).getToStationId();
        String toStationName = getStationName(toStationId);

        Integer numberLeft = getItem(position).getDiscountsLeft();
        Double discountValue = getItem(position).getDiscountValue();
        Date endTime = getItem(position).getEndTime();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        String datePattern = "HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.US);

        TextView tvToStationName = convertView.findViewById(R.id.discountToStation);
        TextView tvNumberLeft = convertView.findViewById(R.id.discountNumberLeft);
        TextView tvDiscountValue = convertView.findViewById(R.id.discountValue);
        TextView tvEndTime = convertView.findViewById(R.id.discountEndTime);

        tvNumberLeft.setText(context.getResources().getString(R.string.discount_lv_number_left_text, numberLeft.toString()));
        tvDiscountValue.setText(context.getResources().getString(R.string.discount_lv_value_text, discountValue.toString()));
        tvEndTime.setText(context.getResources().getString(R.string.discount_lv_end_date_text, dateFormat.format(endTime)));
        if(toStationName != null) {
            tvToStationName.setText(context.getResources().getString(R.string.discount_lv_station_name_text, toStationName));
        }

        Button button = convertView.findViewById(R.id.discountSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID stationId = getItem(position).getToStationId();
                if(appDetails.getPlannedStationId() != stationId) {
                    appDetails.setPlannedStationId(stationId);
                    appDetails.setPlannedStationName(getStationName(stationId));
                }

                appDetails.setDiscount(getItem(position));
                ActivityStarter.openTransactionCreateActivity(context);
            }
        });

        return convertView;
    }

    private String getStationName(UUID stationId) {
        for(Station station : appDetails.getStationList()) {
            if (station.getId().equals(stationId)) {
                return station.getName();
            }
        }
        return null;
    }

}
