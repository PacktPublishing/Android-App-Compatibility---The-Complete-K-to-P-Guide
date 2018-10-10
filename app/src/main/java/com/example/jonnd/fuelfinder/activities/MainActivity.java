package com.example.jonnd.fuelfinder.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jonnd.fuelfinder.adapters.CustomPagerAdapter;
import com.example.jonnd.fuelfinder.databinding.ActivityMainBinding;
import com.example.jonnd.fuelfinder.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    MainViewModel mViewModel;
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setContentView(mBinding.getRoot());
        // Pass the viewmodel, pagerAdapter, FAB clicklistener, and pageChangeListener
        // to the binding class so they can be bound to the views.
        mBinding.setViewModel(mViewModel);
        mBinding.setPagerAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        mBinding.setFabClickListener(fabClickListener);
        mBinding.setPageChangeListener(pageChangeListener);
    }

    /**
     * FAB click listener that is responsible for opening either {@link FillUpEditActivity}
     * or {@link StationEditActivity}, depending on the current item in the ViewPager.
     */
    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mViewModel.currentItem.get() == 0) {
                startActivity(new Intent(MainActivity.this, FillUpEditActivity.class));
            }
            else {
                startActivity(new Intent(MainActivity.this, StationEditActivity.class));
            }
        }
    };

    /**
     * ViewPager Page change listener that's responsible for keeping the viewpager
     * in-sync with the BottomNavigationView when the fragment's are changed from
     * drag events.
     */
    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mBinding.navigation.getMenu().getItem(position).setChecked(true);
        }
    };
}
