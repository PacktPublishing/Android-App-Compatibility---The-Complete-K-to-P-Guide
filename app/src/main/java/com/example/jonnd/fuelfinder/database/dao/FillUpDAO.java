package com.example.jonnd.fuelfinder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;

import java.util.List;

@Dao
public interface FillUpDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insert(FillUp fillUp);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(FillUp fillUp);
    @Delete
    public void delete(FillUp fillUp);
    @Query("SELECT * FROM FillUp")
    public LiveData<List<FillUp>> loadFillUp();
    @Query("SELECT * FROM FillUp WHERE FillUp.Id == :id")
    public LiveData<FillUp> loadFillWithId(int id);
}
