package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.example.jonnd.fuelfinder.Constants;
import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.worker.FillupWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class SettingsViewModel extends AndroidViewModel {
    public final ObservableBoolean enabled = new ObservableBoolean(false);
    public final ObservableInt interval = new ObservableInt(0);

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        // Load the enabled value from shared preferences.
        enabled.set(isEnabled(getApplication()));
        // Load the reminder interval from shared preferences.
        interval.set(getInterval(getApplication()));
    }

    /**
     * Saves the reminder task's configurations as well as scheduling and unscheduling the reminder
     * with {@link WorkManager}.
     */
    public void saveSettings() {
        if(enabled.get()) {
            scheduleReminder();
        }
        else {
            unscheduleReminder();
        }
        persist(enabled.get(), interval.get());
    }

    private void scheduleReminder() {
        String [] intervals =  getApplication().getResources().getStringArray(R.array.intervals);
        int days = Integer.parseInt(intervals[interval.get()]);
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(FillupWorker.class,
                days, TimeUnit.DAYS);
        builder.setInputData(new Data.Builder().putInt(FillupWorker.INTERVAL, days).build());
        builder.setConstraints(new Constraints.Builder()
        .setRequiresCharging(true)
        .build());
        builder.addTag(Constants.WORKER_TAG);
        unscheduleReminder();
        WorkManager.getInstance().enqueue(builder.build());
    }

    /**
     * Helper method for cancelling all queued workers with the tag {@link Constants#WORKER_TAG}.
     */
    private void unscheduleReminder() {
        // Cancel any work request in the WorkManager.
        WorkManager.getInstance().cancelAllWorkByTag(Constants.WORKER_TAG);
    }

    /**
     * Stores the reminder enabled state and reminder interval value inside of FuelFinders {@link SharedPreferences}.
     *
     * @param enabled   The enable state of the reminder reoccurring task.
     * @param interval  The interval of the reminder reoccurring task.
     */
    private void persist(boolean enabled, int interval) {
        SharedPreferences preferences = getApplication().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Grab the shared preferences editor, so we can insert the enabled, and interval values.
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.REMINDER_ENABLED, enabled);
        editor.putInt(Constants.REMINDER_INTERVAL , interval);
        // This will store the inserted values into the shared preferences.
        editor.apply();
    }

    /**
     * Helper method for retrieving the enabled state of the reminder task.
     * @param context
     *
     * @return
     */
    public static boolean isEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Return the reminder enabled value if available, otherwise return false as the default value.
        return preferences.getBoolean(Constants.REMINDER_ENABLED, false);
    }

    /**
     * Helper method for retrieving the reminder interval of the reminder task.
     * @param context
     * @return
     */
    public static int getInterval(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Return the reminder interval value if available, otherwise return '0' as the default value
        // which cooresponds with the first value in the 'intervals' array.
        return preferences.getInt(Constants.REMINDER_INTERVAL, 0);
    }
}
