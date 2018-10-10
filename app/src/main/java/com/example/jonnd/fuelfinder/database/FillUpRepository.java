package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.database.dao.FillUpDAO;
import com.example.jonnd.fuelfinder.entities.FillUp;

import java.util.List;
import java.util.concurrent.Executors;

public class FillUpRepository implements FillUpDAO {

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

    @Override
    public long insert(final FillUp fillUp) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(fillUp);
            }
        });
        return 0;
    }

    @Override
    public void insertAll(final List<FillUp> fillUps) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertAll(fillUps);
            }
        });
    }

    @Override
    public int update(FillUp fillUp) {
        return mDao.update(fillUp);
    }

    @Override
    public void delete(FillUp fillUp) {
        mDao.delete(fillUp);
    }

    @Override
    public LiveData<List<FillUp>> loadFillUp() {
        return mFillUps;
    }

    @Override
    public LiveData<List<FillUp>> loadFillupsWithUserId(long id) {
        return mDao.loadFillupsWithUserId(id);
    }

    @Override
    public LiveData<FillUp> loadFillupWithId(long id) {
        return mDao.loadFillupWithId(id);
    }

    public void update(final FillUp fillUp, final OnFinishedListener listener) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Update fillUp using the FillUpDao.
                int val = mDao.update(fillUp);
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

    public void insert(final FillUp fillUp, final OnFinishedListener listener) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Insert the fillUp into the FillUpDatabase using the FillUpDao.
                long val = mDao.insert(fillUp);
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
