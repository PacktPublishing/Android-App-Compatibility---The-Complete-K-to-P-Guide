package com.example.jonnd.fuelfinder.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonnd.fuelfinder.databinding.FragmentSettingBinding;
import com.example.jonnd.fuelfinder.viewmodel.SettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private SettingsViewModel mVModel;


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get a reference to our SettingsViewModel
        mVModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // Inflate the binding layout.
        FragmentSettingBinding binding = FragmentSettingBinding.inflate(getLayoutInflater());
        // Pass the view model binding class so it can be bound to the layout.]
        binding.setViewModel(mVModel);
        // Return the binding layout's root layout.
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Whenever this fragment is being destroyed, we want to call save settings to make sure the
        // reminder task is properly scheduled and it's configurations are persisted.
        mVModel.saveSettings();
    }
}
