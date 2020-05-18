package com.android.android.utilities;

import android.content.Context;
import android.content.Intent;

import com.android.android.activities.MapActivity;
import com.android.android.activities.MessageActivity;
import com.android.android.activities.PaymentActivity;

public class ActivityStarter {

    public static void openAccountActivity(Context context) {
        //Intent intent = new Intent(context, AccountActivity.class);
        //context.startActivity(intent);
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
        //Intent intent = new Intent(context, ReportHistoryActivity.class);
        //context.startActivity(intent);
    }

    public static void openTransactionHistoryActivity(Context context) {
        //Intent intent = new Intent(context, TransactionHistoryActivity.class);
        //context.startActivity(intent);
    }

    public static void openMapActivity(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }

}
