package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.database.dao.StationDAO;
import com.example.jonnd.fuelfinder.entities.Station;

import java.util.List;
import java.util.concurrent.Executors;

public class StationRepository implements StationDAO {

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

    @Override
    public void insert(final Station station) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(station);
            }
        });
    }

    @Override
    public void insertAll(List<Station> stations) {
        mDao.insertAll(stations);
    }

    @Override
    public void update(Station station) {
        mDao.update(station);
    }

    @Override
    public void delete(Station station) {
        mDao.delete(station);
    }

    @Override
    public LiveData<List<Station>> loadStations() {
        return mStations;
    }

    @Override
    public LiveData<Station> loadStationWithId(long id) {
        return mDao.loadStationWithId(id);
    }

    public void update(final Station station, final OnFinishedListener listener) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Update station using the StationDao.
                mDao.update(station);
                // Make sure that the OnFinishedListener isn't null before going through the work
                // of creating a Handler instance.
                if (listener != null) {
                    // We wanna pass in the "Main" Looper into the constructor of Handler so that
                    // it will dispatch our callback on the UI thread... Doing this allows up to
                    // write code that manipulates the UI.
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFinished();
                                }
                            });
                }
            }
        });
    }

    public void insert(final Station station, final OnFinishedListener listener) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Insert the station into the FillUpDatabase using the StationDao.
                mDao.insert(station);
                // Make sure that the OnFinishedListener isn't null before going through the work
                // of creating a Handler instance.
                if (listener != null) {
                    // We wanna pass in the "Main" Looper into the constructor of Handler so that
                    // it will dispatch our callback on the UI thread... Doing this allows up to
                    // write code that manipulates the UI.
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFinished();
                                }
                            });
                }
            }
        });
    }
}
