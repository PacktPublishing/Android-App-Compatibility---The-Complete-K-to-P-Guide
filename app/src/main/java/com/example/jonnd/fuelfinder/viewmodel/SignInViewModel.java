package com.example.jonnd.fuelfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.adapters.TextViewBindingAdapter;
import android.support.annotation.NonNull;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.database.UserRepository;
import com.example.jonnd.fuelfinder.entities.User;

public class SignInViewModel extends AndroidViewModel implements TextViewBindingAdapter.OnTextChanged {
    public ObservableField<String> username = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableBoolean enabled = new ObservableBoolean(false);

    UserRepository mRepo;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mRepo = ((FuelFinderApp) application).getUserRepo();
    }

    public LiveData<User> getUser(long id) {
        return mRepo.loadUserWithId(id);
    }

    public LiveData<User> login() {
        return mRepo.loadUser(username.get(), password.get());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        enabled.set(!username.get().isEmpty() && !password.get().isEmpty());
    }
}
