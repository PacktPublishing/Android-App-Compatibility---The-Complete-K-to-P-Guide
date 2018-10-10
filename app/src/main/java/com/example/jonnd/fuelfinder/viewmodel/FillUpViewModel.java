package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.database.FillUpRepository;
import com.example.jonnd.fuelfinder.database.OnFinishedListener;
import com.example.jonnd.fuelfinder.database.StationRepository;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FillUpViewModel extends AndroidViewModel {

    LiveData<Station> mLoadedStation;
    LiveData<FillUp> mLoadedFillUp;
    LiveData<List<Station>> mLoadedStations;

    private long mFillUpId;
    private long mStationId;
    private long mUserId;

    public final ObservableInt numberOfGallons = new ObservableInt(0);
    public final ObservableDouble pricePerGallon = new ObservableDouble(0.00f);
    public final ObservableField<String> fuelType = new ObservableField<>("");
    public final ObservableField<Date> date = new ObservableField<>(new Date());
    public final ObservableLong time = new ObservableLong();
    public final ObservableField<String> stationName = new ObservableField<>("");
    public final ObservableField<String> address = new ObservableField<>("");

    private FillUpRepository mFillUpRepo;
    public FillUpViewModel(@NonNull Application application, long fuelId, long stationId) {
        super(application);
        FuelFinderApp fuelFinderApp = (FuelFinderApp) application;
        mUserId = fuelFinderApp.getUser().getId();
        mFillUpId = fuelId;
        mStationId = stationId;
        StationRepository stationRepo = fuelFinderApp.getStationRepo();
        mFillUpRepo = fuelFinderApp.getFillRepo();
        // Get a LiveData FillUp for the passed in fuelId... This will needs to be observed upon
        // by a lifecycle owner inorder for the SQL to be executed and a FillUp returned.
        mLoadedFillUp = mFillUpRepo.loadFillupWithId(mFillUpId);
        // Get a LiveData Station for the passed in stationId... This will needs to be observed upon
        // by a lifecycle owner inorder for the SQL to be executed and a Station returned.
        mLoadedStation = stationRepo.loadStationWithId(mStationId);
        // Get a LiveData List of station objects. We will use this for displaying a list of stations.
        mLoadedStations = stationRepo.loadStations();
    }

    public LiveData<FillUp> getLoadedFillUp() {
        return mLoadedFillUp;
    }

    public LiveData<Station> getLoadedStation() {
        return mLoadedStation;
    }

    public LiveData<List<Station>> getLoadedStations() {
        return mLoadedStations;
    }

    public void setStation(Station station) {
        // If station is null, that means there wasn't a Station object that matched stationId
        // that means we are more than likely adding a new FillUp.
        if(station == null) {
            return;
        }
        if (mStationId == 0) {
            // Make sure that we store the id of ethe set station
            // so we have a valid station id when adding a new fillup.
            mStationId = station.getId();
        }
        // Copy over the Station's fields.
        stationName.set(station.getStationName());
        address.set(station.getAddress());
    }

    public void setFillUp(FillUp fillUp) {
        // If fillup is null, that means there wasn't a fillup object that matched fuelId
        // that means we are more than likely adding a new FillUp.
        if(fillUp == null) {
            return;
        }
        // Copy over the FillUp's fields.
        numberOfGallons.set(fillUp.getNumberOfGallons());
        pricePerGallon.set(fillUp.getPricePerGallon());
        fuelType.set(fillUp.getFuelType());
        date.set(fillUp.getDate());
    }

    /**
     * Set's this view model's observable date field.
     * @param newDate
     */
    public void setDate(Date newDate) {
        date.set(newDate);
    }

    /**
     * Set's this view model's observable date field.
     * @param newTime
     */
    public void setTime(Date newTime) {
        time.set(newTime.getTime());
    }

    /**
     *
     * @param listener
     */
    public void saveFillUp(OnFinishedListener listener) {
        if(0 < mFillUpId) {
            mFillUpRepo.update(getFillUp(), listener);
        }
        else {
            mFillUpRepo.insert(getFillUp(), listener);
        }
    }

    /**
     * Helper method that creates a new FillUp object from this view model's observable fields.
     * @return  Returns anew FillUp instances that represents this view models fields.
     */
    private FillUp getFillUp() {
        FillUp fillUp = new FillUp();
        fillUp.setId(mFillUpId);
        fillUp.setUserId(mUserId);
        fillUp.setStationId(mStationId);
        fillUp.setNumberOfGallons(numberOfGallons.get());
        fillUp.setPricePerGallon(pricePerGallon.get());
        fillUp.setFuelType(fuelType.get());
        fillUp.setDate(date.get());
        return fillUp;
    }

    /**
     * Update the view model's date object with the current year, month, and dayOfMonth arguments.
     *
     * @param year          The year that you'd like to update currentDate with.
     * @param month         The month that you'd like to update currentDate with.
     * @param dayOfMonth    The dayOfMonth that you'd like to update currentDate with.
     */
    public void setDate(int year, int month, int dayOfMonth) {
        // Grab the date instance from the date ObservableField...
        Date fillDate = date.get();
        // If null we wanna create a new Date instance.
        if(fillDate == null) {
            fillDate = new Date();
        }
        // Update the fillDate instance with the new date information, then set the returned date on the
        // the observable field, so whomever is observing on this field can be notified of a change.
        date.set(updateDateWithDateInfo(fillDate, year, month, dayOfMonth));
    }

    /**
     * Update the view model's date object with the current hourOfDay, minute, and second arguments.
     *
     * @param hourOfDay     The hour of day that you'd like to update currentDate with.
     * @param minute        The minute in the hour that you'd like to update currentDate with.
     * @param second        The second in the minute that you'd like to update currentDate with.
     */
    public void setTime(int hourOfDay, int minute, int second) {
        // Grab the date instance from the date ObservableField...
        Date fillDate = date.get();
        // If null we wanna create a new Date instance.
        if(fillDate == null) {
            fillDate = new Date();
        }
        // Update the fillDate instance with the new time information, then set the returned date on the
        // the observable field, so whomever is observing on this field can be notified of a change.
        date.set(updateDateWithTimeInfo(fillDate, hourOfDay, minute, second));
    }


    /**
     * Helper method for updating currentDate with the specified year, month, and dayOfMonth arguments.
     *
     * @param currentDate   The current {@link Date} instance that you would like to update with the provided date information.
     * @param year          The year that you'd like to update currentDate with.
     * @param month         The month that you'd like to update currentDate with.
     * @param dayOfMonth    The dayOfMonth that you'd like to update currentDate with.
     *
     * @return  A new {@link Date} instance that contains the mutated date information. The time information is unmodified.
     */
    private static Date updateDateWithDateInfo(Date currentDate, int year, int month, int dayOfMonth) {
        // We need grab a Calendar object to modified the Date's date information... This is necessary
        // because the api to directly modify date information has been deprecated, in favor
        // of using Calendar.
        Calendar cal = Calendar.getInstance();
        // Set the Calendars date to match current Date.
        cal.setTime(currentDate);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // Return a new Date instance that represents the Calendar date information.
        return cal.getTime();
    }

    /**
     * Helper method for updating currentDate with the specified hourOfDay, minute, and second arguments.
     *
     * @param currentDate   The current {@link Date} instance that you would like to update with the provided time information.
     * @param hourOfDay     The hour of day that you'd like to update currentDate with.
     * @param minute        The minute in the hour that you'd like to update currentDate with.
     * @param second        The second in the minute that you'd like to update currentDate with.
     *
     * @return  A new {@link Date} instance that contains the mutated time information. The date information is unmodified.
     */
    private static Date updateDateWithTimeInfo(Date currentDate, int hourOfDay, int minute, int second) {
        // We need grab a Calendar object to modified the Date's time information... This is necessary
        // because the api to directly modify time information has been deprecated, in favor
        // of using Calendar.
        Calendar cal = Calendar.getInstance();
        // Set the Calendars date to match current Date.
        cal.setTime(currentDate);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        // Return a new Date instance that represents the Calendar date information.
        return cal.getTime();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Application mApp;
        private long mFuelId;
        private long mStationId;

        public Factory(Application application, long fuelId, long stationId) {
            mFuelId = fuelId;
            mStationId = stationId;
            mApp = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FillUpViewModel(mApp, mFuelId, mStationId);
        }
    }
}
