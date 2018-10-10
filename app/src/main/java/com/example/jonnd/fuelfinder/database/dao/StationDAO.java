package com.example.jonnd.fuelfinder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jonnd.fuelfinder.entities.Station;

import java.util.List;
@Dao
public interface StationDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insert(Station station);
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertAll(List<Station> stations);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(Station station);
    @Delete
    public void delete(Station station);
    @Query("SELECT * FROM Station")
    public LiveData<List<Station>> loadStations();
    @Query("SELECT * FROM Station WHERE Station.Id == :id")
    public LiveData<Station> loadStationWithId(long id);
}
