package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.database.dao.FillUpDAO;
import com.example.jonnd.fuelfinder.entities.FillUp;

import java.util.List;
import java.util.concurrent.Executors;

public class FillUpRepository {

    private static FillUpRepository INSTANCE;

    private final FillUpDatabase mDatabase;

    private MediatorLiveData<List<FillUp>> mFillUps;

    private FillUpDAO mDao;

    public FillUpRepository(Context ctx) {
        mDatabase = FillUpDatabase.getDatabase(ctx);
        mFillUps = new MediatorLiveData<>();
        mDao = mDatabase.fillUpDao();
        mFillUps.addSource(mDao.loadFillUp(), new Observer<List<FillUp>>() {
            @Override
            public void onChanged(@Nullable List<FillUp> fillUps) {
                mFillUps.setValue(fillUps);
            }
        });
    }

    public static FillUpRepository getFillUpRepo(Context ctx) {
        if (INSTANCE == null) {
            synchronized (FillUpRepository.class) {
                INSTANCE = new FillUpRepository(ctx);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<FillUp>> getFillUps() {
        return mFillUps;
    }

    public void insertFillUp(final FillUp fillUp) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(fillUp);
            }
        });
    }
}
