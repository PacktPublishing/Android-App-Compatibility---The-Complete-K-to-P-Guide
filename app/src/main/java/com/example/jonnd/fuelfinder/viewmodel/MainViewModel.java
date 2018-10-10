package com.example.jonnd.fuelfinder.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.jonnd.fuelfinder.R;

public class MainViewModel extends ViewModel implements BottomNavigationView.OnNavigationItemSelectedListener{

    public final ObservableBoolean fabEnabled = new ObservableBoolean(true);
    public final ObservableInt currentItem = new ObservableInt(0);

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fillups:
                fabEnabled.set(true);
                currentItem.set(0);
                break;
            case R.id.stations:
                fabEnabled.set(true);
                currentItem.set(1);
                break;
            case R.id.settings:
                fabEnabled.set(false);
                currentItem.set(2);
                break;
        }
        return true;
    }
}
