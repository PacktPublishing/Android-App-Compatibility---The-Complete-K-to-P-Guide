package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.database.OnFinishedListener;
import com.example.jonnd.fuelfinder.database.StationRepository;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;

public class StationViewModel extends AndroidViewModel {
    LiveData<Station> mLoadedStation;
    private long mStationId;
    StationRepository mStationRepo;

    public final ObservableField<String> stationName = new ObservableField<>();
    public final ObservableField<String> address = new ObservableField<>();
    public final ObservableField<String> thumbnailFilePath = new ObservableField<>();

    public StationViewModel(@NonNull Application application, long stationId) {
        super(application);
        mStationId = stationId;
        mStationRepo = ((FuelFinderApp) application).getStationRepo();
        mLoadedStation = mStationRepo.loadStationWithId(stationId);
    }

    public LiveData<Station> getLoadedStation() {
        return mLoadedStation;
    }

    public void setObservableStation(Station station) {
        // If fillup is null, that means there wasn't a fillup object that matched fuelId
        // that means we are more than likely adding a new FillUp.
        if(station == null) {
            return;
        }

        stationName.set(station.getStationName());
        address.set(station.getAddress());
        thumbnailFilePath.set(station.getThumbnailFilePath());
    }

    public void saveStation(OnFinishedListener listener) {
        Station station = getStation();
        if(0 < station.getId()) {
            mStationRepo.update(station,listener);
        }
        else {
            mStationRepo.insert(station, listener);
        }
    }

    /**
     * Helper method that creates a new FillUp object from this view model's observable fields.
     * @return  Returns anew FillUp instances that represents this view models fields.
     */
    private Station getStation() {
        Station station = new Station();
        station.setStationName(stationName.get());
        station.setAddress(address.get());
        station.setThumbnailFilePath(thumbnailFilePath.get());
        return station;
    }

    /**
     * Factory class that handles creating an instance of {@link StationViewModel} whose constructor
     * requires parameters that the ViewModelProvider API cannot provide.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private Application mApp;
        private long mStationId;

        public Factory(Application application, long stationId) {
            mStationId = stationId;
            mApp = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StationViewModel(mApp, mStationId);
        }
    }
}
