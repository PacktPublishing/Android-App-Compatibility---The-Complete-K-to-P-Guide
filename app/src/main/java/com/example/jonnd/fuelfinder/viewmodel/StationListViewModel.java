package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.database.StationRepository;
import com.example.jonnd.fuelfinder.entities.Station;

import java.util.List;

public class StationListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Station>> mStations;
    public StationListViewModel(@NonNull Application application) {
        super(application);

        mStations = new MediatorLiveData<>();
        mStations.setValue(null);
        StationRepository repo = ((FuelFinderApp) getApplication()).getStationRepo();
        mStations.addSource(repo.loadStations(), new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                mStations.setValue(stations);
            }
        });
    }

    public LiveData<List<Station>> getStations() {
        return mStations;
    }
}
