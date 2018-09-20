package com.example.jonnd.fuelfinder;

import android.app.Application;

import com.example.jonnd.fuelfinder.database.FillUpRepository;
import com.example.jonnd.fuelfinder.database.StationRepository;
import com.example.jonnd.fuelfinder.database.UserRepository;
import com.example.jonnd.fuelfinder.entities.Station;

public class FuelFinderApp extends Application {

    public FillUpRepository getFillRepo() {
        return FillUpRepository.getFillUpRepo(this);
    }
    public UserRepository getUserRepo() {
        return UserRepository.getUserRepo(this);
    }
    public StationRepository getStationRepo() {
        return StationRepository.getStationRepo(this);
    }
}
