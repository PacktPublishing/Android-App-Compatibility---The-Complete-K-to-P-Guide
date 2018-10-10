package com.example.jonnd.fuelfinder;

import android.app.Application;

import com.example.jonnd.fuelfinder.database.FillUpDatabase;
import com.example.jonnd.fuelfinder.database.FillUpRepository;
import com.example.jonnd.fuelfinder.database.StationRepository;
import com.example.jonnd.fuelfinder.database.UserRepository;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.entities.User;

public class FuelFinderApp extends Application {

    private User mUser;

    public FillUpRepository getFillRepo() {
        return FillUpRepository.getFillUpRepo(this);
    }
    public UserRepository getUserRepo() {
        return UserRepository.getUserRepo(this);
    }
    public StationRepository getStationRepo() {
        return StationRepository.getStationRepo(this);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
