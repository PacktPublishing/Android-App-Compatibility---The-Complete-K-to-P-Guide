package com.example.jonnd.fuelfinder.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jonnd.fuelfinder.fragments.FillUpFragment;
import com.example.jonnd.fuelfinder.fragments.SettingFragment;
import com.example.jonnd.fuelfinder.fragments.StationFragment;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return StationFragment.newInstance();
            case 2:
                return SettingFragment.newInstance();
            default:
                return FillUpFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
