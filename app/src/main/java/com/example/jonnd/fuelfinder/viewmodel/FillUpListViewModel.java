package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.database.FillUpRepository;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.User;

import java.util.List;

public class FillUpListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<FillUp>> mFillUps;

    public FillUpListViewModel(@NonNull Application application) {
        super(application);
        mFillUps = new MediatorLiveData<>();
        mFillUps.setValue(null);

        FillUpRepository repo = ((FuelFinderApp) getApplication()).getFillRepo();
        User user = ((FuelFinderApp) getApplication()).getUser();

        mFillUps.addSource(repo.loadFillupsWithUserId(user.getId()), new Observer<List<FillUp>>() {
            @Override
            public void onChanged(@Nullable List<FillUp> fillUps) {
                mFillUps.setValue(fillUps);
            }
        });
    }

    public LiveData<List<FillUp>> getUserFillups() {
        return mFillUps;
    }
}
