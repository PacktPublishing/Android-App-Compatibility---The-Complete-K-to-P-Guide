package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;


import com.example.jonnd.fuelfinder.database.dao.StationDAO;
import com.example.jonnd.fuelfinder.entities.Station;

import java.util.List;
import java.util.concurrent.Executors;

public class StationRepository {

    private static StationRepository INSTANCE;

    private final FillUpDatabase mDatabase;

    private MediatorLiveData<List<Station>> mStations;

    private StationDAO mDao;

    public StationRepository(Context ctx) {
        mDatabase = FillUpDatabase.getDatabase(ctx);
        mStations = new MediatorLiveData<>();
        mDao = mDatabase.statioDao();
        mStations.addSource(mDao.loadStations(), new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                mStations.setValue(stations);
            }
        });
    }

    public static StationRepository getStationRepo(Context ctx) {
        if (INSTANCE == null) {
            synchronized (StationRepository.class) {
                INSTANCE = new StationRepository(ctx);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Station>> getStations() {
        return mStations;
    }

    public void insertStation(final Station station) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(station);
            }
        });
    }
}
