package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.jonnd.fuelfinder.database.dao.FillUpDAO;
import com.example.jonnd.fuelfinder.database.dao.StationDAO;
import com.example.jonnd.fuelfinder.database.dao.UserDAO;
import com.example.jonnd.fuelfinder.database.util.Converter;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.entities.User;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Station.class, FillUp.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class FillUpDatabase extends RoomDatabase {

    private static FillUpDatabase INSTANCE;
    private static String DN_NAME = "FillUpDatabase";


    public static FillUpDatabase getDatabase(final Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(ctx, FillUpDatabase.class, DN_NAME)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            Executors.newSingleThreadExecutor()
                                    .execute(new Runnable() {
                                @Override
                                public void run() {
                                    sleepFor(2500);
                                    final User user = new User();
                                    user.setFirstName("John");
                                    user.setLastName("Doe");
                                    user.setUserName("johnDoe");
                                    user.setPassword("pa55word");
                                    DataGenerator generator = new DataGenerator();
                                    final List<Station> stations = generator.generateStations();
                                    final FillUpDatabase database = getDatabase(ctx);
                                    insertData(user, stations, database);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ctx, "Data has been added to " + DN_NAME + " db", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    private static void sleepFor(long howLong) {
        try {
            Thread.currentThread().sleep(howLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(final User user, final List<Station> stations, final FillUpDatabase database) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.userDao().insert(user);
                database.statioDao().insertAll(stations);
            }
        });
    }

    public abstract UserDAO userDao();
    public abstract StationDAO statioDao();
    public abstract FillUpDAO fillUpDao();
}
