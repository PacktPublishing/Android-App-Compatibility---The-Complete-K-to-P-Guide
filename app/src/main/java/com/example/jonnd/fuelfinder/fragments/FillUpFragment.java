package com.example.jonnd.fuelfinder.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.activities.FillUpDetailActivity;
import com.example.jonnd.fuelfinder.adapters.FillUpRVAdapter;
import com.example.jonnd.fuelfinder.databinding.FragmentListBinding;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.listeners.FillUpClickListener;
import com.example.jonnd.fuelfinder.viewmodel.FillUpListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillUpFragment extends Fragment {

    private FillUpRVAdapter mAdapter;
    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    public FillUpFragment() {
        // Required empty public constructor
    }

    public static FillUpFragment newInstance() {
        return new FillUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the FragmentListBinding class.
        FragmentListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mAdapter = new FillUpRVAdapter(mClickListener);
        // Pass in the Station RecyclerView adapter to the binding class so it can be bound
        // to the list inside of the fragment_list.
        binding.setAdapter(mAdapter);
        binding.setIsLoading(mIsLoading);
        // Return the bindings Root view.
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create the FillUpListViewModel, and subscribe on the LiveData list.
        FillUpListViewModel model = ViewModelProviders.of(this).get(FillUpListViewModel.class);
        subscribe(model);
    }

    /**
     * Helper method for observing on view model live data.
     * @param model
     */
    public void subscribe(FillUpListViewModel model) {
        // Observe of the fill-up list of LiveData, so the contained SQL statment is executed
        // and the station rows in the SQLITE db are returned.
        model.getUserFillups().observe(this, new Observer<List<FillUp>>() {
            @Override
            public void onChanged(@Nullable List<FillUp> fillUps) {
                // If the fill-up list, is not null, we wanna set it on the RV adapter
                // otherwise set the isLoading flag to true.
                if (fillUps != null) {
                    mAdapter.setFillUps(fillUps);
                    mIsLoading.set(false);
                }
                else {
                    mIsLoading.set(true);
                }
            }
        });
    }

    /**
     * Click listener that will get fired when a fill-up item has been seleted from the RecyclerView.
     */
    public FillUpClickListener mClickListener = new FillUpClickListener() {

        @Override
        public void onFillUpClicked(FillUp fillUp) {
            // Store the station id and fill-up Id inside of FillUpDetailActivity intent.
            Intent intent = new Intent(getContext(), FillUpDetailActivity.class);
            intent.putExtra("FILL_UP_ID", fillUp.getId());
            intent.putExtra("STATION_ID", fillUp.getStationId());
            startActivity(intent);
        }
    };
}
