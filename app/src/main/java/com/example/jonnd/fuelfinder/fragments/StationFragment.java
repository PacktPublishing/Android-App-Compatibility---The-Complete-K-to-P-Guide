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
import com.example.jonnd.fuelfinder.activities.StationDetailActivity;
import com.example.jonnd.fuelfinder.adapters.StationRVAdapter;
import com.example.jonnd.fuelfinder.databinding.FragmentListBinding;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.listeners.StationClickListener;
import com.example.jonnd.fuelfinder.viewmodel.StationListViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment {

    private StationRVAdapter mAdapter;
    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    public StationFragment() {
        // Required empty public constructor
    }

    public static StationFragment newInstance() {
        return new StationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the FragmentListBinding class.
        FragmentListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mAdapter = new StationRVAdapter(mClickListener);
        // Pass in the Station RecyclerView adapter to the binding class so it can be bound
        // to the list inside of the fragment_list.
        binding.setAdapter(mAdapter);
        // Pass in the isLoading flag to control the visibility of list and textview.
        binding.setIsLoading(mIsLoading);
        // Return the bindings Root view.
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create the StationListViewModel, and subscribe on the LiveData list.
        StationListViewModel model = ViewModelProviders.of(this).get(StationListViewModel.class);
        subscribe(model);
    }

    /**
     * Helper method for observing on view model live data.
     * @param model
     */
    public void subscribe(StationListViewModel model) {
        // Observe of the station list of LiveData, so the contained SQL statment is executed
        // and the station rows in the SQLITE db are returned.
        model.getStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                // If the station list, is not null, we wanna set it on the RV adapter
                // otherwise set the isLoading flag to true.
                if (stations != null) {
                    mAdapter.setStations(stations);
                    mIsLoading.set(false);
                }
                else {
                    mIsLoading.set(true);
                }
            }
        });
    }

    /**
     * Click listener that will get fired when a station item has been seleted from the RecyclerView.
     */
    public StationClickListener mClickListener = new StationClickListener() {

        @Override
        public void onStationClicked(Station station) {
            // Store the station id inside of StationDetailActivity intent.
            Intent intent = new Intent(getContext(), StationDetailActivity.class);
            intent.putExtra("STATION_ID", station.getId());
            startActivity(intent);
        }
    };

}
