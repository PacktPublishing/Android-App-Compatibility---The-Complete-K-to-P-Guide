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
    public long insert(FillUp fillUp);
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertAll(List<FillUp> fillUps);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public int update(FillUp fillUp);
    @Delete
    public void delete(FillUp fillUp);
    @Query("SELECT * FROM FillUp")
    public LiveData<List<FillUp>> loadFillUp();
    @Query("SELECT * FROM FillUp WHERE FillUp.Id == :id")
    public LiveData<FillUp> loadFillupWithId(long id);
    @Query("SELECT * FROM FillUp WHERE FillUp.UserId == :id")
    public LiveData<List<FillUp>> loadFillupsWithUserId(long id);
}
