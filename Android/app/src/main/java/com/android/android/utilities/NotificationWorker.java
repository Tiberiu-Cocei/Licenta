package com.android.android.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.android.R;
import com.android.android.enums.NotificationMode;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationWorker extends Worker {

    private Context context;

    private Date arrivalTime;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.arrivalTime = ApiCaller.getUnfinishedTransactionDetails(context).get(0).getPlannedTime();
    }

    @Override
    public @NonNull Result doWork() {
        if(arrivalTime == null) {
            return Result.failure();
        }

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        Calendar arrivalTime = Calendar.getInstance();
        arrivalTime.setTime(this.arrivalTime);
        int arrivalHour = arrivalTime.get(Calendar.HOUR_OF_DAY);
        int arrivalMinute = arrivalTime.get(Calendar.MINUTE);

        int hourDelta = arrivalHour - currentHour;
        int minuteDelta = arrivalMinute - currentMinute;
        int minutesToArrival = hourDelta * 60 + minuteDelta;

        int sleepTime = (minutesToArrival - 5) * 60 * 1000;
        if(sleepTime < 0) {
            sleepTime = 0;
        }

        try {
            Thread.sleep(sleepTime); //sleep until 5 minutes before planned time
            if(verifyUserTransactionAndCreateNotification(NotificationMode.FIVE_MINUTES_BEFORE_ARRIVAL)) {
                return Result.success();
            }

            if(sleepTime == 0) {
                sleepTime = (minutesToArrival - 1) * 60 * 1000;
            }
            else {
                sleepTime = 4 * 60 * 1000;
            }

            Thread.sleep(sleepTime); //sleep until 1 minute before planned time
            if(verifyUserTransactionAndCreateNotification(NotificationMode.ONE_MINUTE_BEFORE_ARRIVAL)) {
                return Result.success();
            }
            Thread.sleep(60 * 1000); //sleep until planned time
            if(verifyUserTransactionAndCreateNotification(NotificationMode.AFTER_PLANNED_ARRIVAL)) {
                return Result.success();
            }
            Thread.sleep(5 * 60 * 1000); //sleep until 5 minutes after planned time
            if(verifyUserTransactionAndCreateNotification(NotificationMode.FIVE_MINUTES_AFTER_PLANNED_ARRIVAL)) {
                return Result.success();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }

    private boolean verifyUserTransactionAndCreateNotification(NotificationMode notificationMode) {
        if(ApiCaller.getUnfinishedTransactionDetails(context).size() != 0) {
            createNotification(notificationMode);
            return false;
        }
        else {
            return true;
        }
    }

    private void createNotification(NotificationMode notificationMode) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Bicycle App")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        int mode = notificationMode.getValue();
        String description = "";
        switch(mode) {
            case 0:
                description = "There are 5 or less minutes left until your planned arrival time.";
                break;
            case 1:
                description = "There is 1 minute left until your planned arrival time.";
                break;
            case 2:
                description = "Your planned time has passed. You have lost your reserved spot in the station.";
                break;
            case 3:
                description = "Your arrival is late by 5 minutes or more. From now on you will be penalised.";
                break;
        }

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        builder.setContentText(description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="Bicycle App";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Integer.toString(mode), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            else {
                Log.d("NOTIFICATION", "Null notification manager from getSystemService(NotificationManager.class)");
            }
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(mode, builder.build());
        }
        else {
            Log.d("NOTIFICATION", "Null notification manager from getSystemService(NOTIFICATION_SERVICE)");
        }
    }
}
