package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.DiscountListAdapter;
import com.android.android.adapters.ReportHistoryListAdapter;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Discount;
import com.android.android.entities.Report;
import com.android.android.entities.User;
import com.android.android.utilities.ApiCaller;
import com.android.android.utilities.ApiResponse;

import java.util.List;
import java.util.UUID;

public class DiscountActivity extends AppCompatActivity {

    private ListView discountListView;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        discountListView = findViewById(R.id.discountList);
        messageText = findViewById(R.id.messageText);
        List<Discount> discountList = getDiscounts();
        if(discountList != null) {
            populateDiscountList(discountList);
        }
    }

    private List<Discount> getDiscounts() {
        List<Discount> discountList = null;
        ApiCaller apiCaller = new ApiCaller();
        String url = getResources().getString(R.string.api_secure_prefix) + "/discounts/station";
        try {
            apiCaller.execute("GET", url, User.getUser().getAuthenticationToken().toString(),
                    AppDetails.getAppDetails().getStartStationId().toString());
            ApiResponse apiResponse = apiCaller.get();
            if(apiResponse != null) {
                if(apiResponse.getCode() == 200) {
                    try {
                        discountList = Discount.createDiscountListFromJson(apiResponse.getJson());
                        messageText.setText(getResources().getString(R.string.api_success_to_get_discounts));
                    } catch(Exception e) {
                        messageText.setText(getResources().getString(R.string.api_failed_to_get_discounts));
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            messageText.setText(getResources().getString(R.string.api_generic_call_failure));
        }
        return discountList;
    }

    private void populateDiscountList(List<Discount> discountList) {
        DiscountListAdapter discountListAdapter = new DiscountListAdapter(
                this, R.layout.discount_list_view, discountList);
        discountListView.setAdapter(discountListAdapter);
    }
}
