package com.example.jonnd.fuelfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.example.jonnd.fuelfinder.database.dao.UserDAO;
import com.example.jonnd.fuelfinder.entities.User;

import java.util.List;
import java.util.concurrent.Executors;

public class UserRepository implements UserDAO {

    private static UserRepository INSTANCE;

    private final FillUpDatabase mDatabase;

    private MediatorLiveData<List<User>> mUsers;

    private UserDAO mDao;

    public UserRepository(Context ctx) {
        mDatabase = FillUpDatabase.getDatabase(ctx);
        mUsers = new MediatorLiveData<>();
        mDao = mDatabase.userDao();
        mUsers.addSource(mDao.loadUsers(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                // We use postValue because this could be called off of the main thread, and that will result in
                // an exception.
                mUsers.postValue(users);
            }
        });
    }

    public static UserRepository getUserRepo(Context ctx) {
        if (INSTANCE == null) {
            synchronized (UserRepository.class) {
                INSTANCE = new UserRepository(ctx);
            }
        }
        return INSTANCE;
    }

    public LiveData<List<User>> getUsers() {
        return mUsers;
    }

    @Override
    public void insert(final User user) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(user);
            }
        });
    }

    @Override
    public void update(User user) {
        mDao.update(user);
    }

    @Override
    public void delete(User user) {
        mDao.delete(user);
    }

    @Override
    public LiveData<List<User>> loadUsers() {
        return mUsers;
    }

    @Override
    public LiveData<User> loadUserWithId(long id) {
        return mDao.loadUserWithId(id);
    }

    @Override
    public LiveData<User> loadUser(String username, String password) {
        return mDao.loadUser(username, password);
    }
}
