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
    public final ObservableField<Station> station = new ObservableField<>();
    StationRepository mStationRepo;
    public StationViewModel(@NonNull Application application, long stationId) {
        super(application);
        station.set(new Station());
        mStationRepo = ((FuelFinderApp) application).getStationRepo();
        mLoadedStation = mStationRepo.loadStationWithId(stationId);
    }

    public LiveData<Station> getLoadedStation() {
        return mLoadedStation;
    }

    public void setObservableStation(Station station) {
        this.station.set(station);
    }

    public void saveStation(OnFinishedListener listener) {
        Station station = this.station.get();
        if(0 < station.getId()) {
            mStationRepo.update(station,listener);
        }
        else {
            mStationRepo.insert(station, listener);
        }
    }

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
