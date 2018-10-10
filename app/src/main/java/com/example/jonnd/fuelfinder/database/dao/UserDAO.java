package com.example.jonnd.fuelfinder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jonnd.fuelfinder.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insert(User user);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(User user);
    @Delete
    public void delete(User user);
    @Query("SELECT * FROM User")
    public LiveData<List<User>> loadUsers();
    @Query("SELECT * FROM User WHERE User.Id == :id")
    public LiveData<User> loadUserWithId(int id);
    @Query("SELECT * FROM User WHERE User.userName == :username AND User.password == :password")
    public LiveData<User> loadUser(String username, String password);
}
