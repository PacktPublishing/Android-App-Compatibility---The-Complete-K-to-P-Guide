package com.example.jonnd.fuelfinder.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.database.FillUpRepository;
import com.example.jonnd.fuelfinder.entities.FillUp;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FillupWorker extends Worker {

    public static final String INTERVAL = "INTERVAL";
    private NotificationManager mNotificationMngr;
    private final static String CHANNEL_ID = "default";
    private final static String CHANNEL_NAME = "Primary Channel";

    public FillupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        createNotificationChannel();
    }

    /**
     * Helper method for setting up a notification channel. This is required for Android 8 (Oreo) and above.
     */
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationMngr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Gas Purchase Reminder");
            channel.setLightColor(Color.RED);
            mNotificationMngr.createNotificationChannel(channel);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        // Grab the input data for the worker, if for some reason we don't have a value, use 7 days as the default.
        int daysBack = inputData.getInt(INTERVAL, 7);
        // Create a calendar instance initialize to today.
        Calendar calendar = Calendar.getInstance();
        // Today, will be our 'to' value in the Room db query.
        Date to = calendar.getTime();
        // We wanna go back, on the calendar instance to get the from date, to complete our Room db query.
        calendar.add(Calendar.DAY_OF_MONTH, -daysBack);
        Date from = calendar.getTime();
        // Get the fill up Repo.
        FillUpRepository fillRepo = ((FuelFinderApp) getApplicationContext()).getFillRepo();
        // Return a list of all fill ups between the two days.
        List<FillUp> fillUps = fillRepo.loadFillUpsBetweenDate(from, to);
        double total = 0.0;
        // Iterate over the fillups and calculate the total cash spent on each one, and add it to total.
        for (FillUp fillUp : fillUps) {
            total += fillUp.getNumberOfGallons() * fillUp.getPricePerGallon();
        }
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        // Get a currency string for the total, then pass it to createNotification.
        String cashStr = currencyInstance.format(total);
        createNotification(cashStr, daysBack);
        return Result.SUCCESS;
    }

    /**
     * Helper method for generating a notification for message, and days arguments.
     * @param cash  Cash string.
     * @param days  The number of days back we calculate fuel expenditures for.
     */
    private void createNotification(String cash, int days) {
        StringBuilder builder = new StringBuilder("During the last ");
        builder.append(days)
                .append(" day(s) you spent ")
                .append(cash)
                .append(" on fuel");
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(),
                CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Fuel Purchases")
                .setContentText(builder.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mNotificationMngr.notify(100 , nBuilder.build());
    }
}
