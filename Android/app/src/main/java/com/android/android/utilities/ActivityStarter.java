package com.android.android.utilities;

import android.content.Context;
import android.content.Intent;

import com.android.android.activities.AccountActivity;
import com.android.android.activities.DiscountActivity;
import com.android.android.activities.MapActivity;
import com.android.android.activities.MessageActivity;
import com.android.android.activities.PaymentActivity;
import com.android.android.activities.ReportActivity;
import com.android.android.activities.ReportHistoryActivity;
import com.android.android.activities.StationActivity;
import com.android.android.activities.TransactionCreateActivity;
import com.android.android.activities.TransactionFinishActivity;
import com.android.android.activities.TransactionHistoryActivity;

public class ActivityStarter {

    public static void openAccountActivity(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    public static void openMessageActivity(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    public static void openPaymentActivity(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        context.startActivity(intent);
    }

    public static void openReportHistoryActivity(Context context) {
        Intent intent = new Intent(context, ReportHistoryActivity.class);
        context.startActivity(intent);
    }

    public static void openTransactionHistoryActivity(Context context) {
        Intent intent = new Intent(context, TransactionHistoryActivity.class);
        context.startActivity(intent);
    }

    public static void openMapActivity(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }

    public static void openStationActivity(Context context) {
        Intent intent = new Intent(context, StationActivity.class);
        context.startActivity(intent);
    }

    public static void openReportActivity(Context context) {
        Intent intent = new Intent(context, ReportActivity.class);
        context.startActivity(intent);
    }

    public static void openTransactionCreateActivity(Context context) {
        Intent intent = new Intent(context, TransactionCreateActivity.class);
        context.startActivity(intent);
    }

    public static void openTransactionFinishActivity(Context context) {
        Intent intent = new Intent(context, TransactionFinishActivity.class);
        context.startActivity(intent);
    }

    public static void openDiscountActivity(Context context) {
        Intent intent = new Intent(context, DiscountActivity.class);
        context.startActivity(intent);
    }

}
