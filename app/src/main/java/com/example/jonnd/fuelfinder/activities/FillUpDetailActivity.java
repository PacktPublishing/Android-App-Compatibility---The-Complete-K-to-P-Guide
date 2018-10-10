package com.example.jonnd.fuelfinder.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jonnd.fuelfinder.databinding.ActivityFillUpDetailBinding;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.viewmodel.FillUpViewModel;

public class FillUpDetailActivity extends AppCompatActivity {

    private FillUpViewModel mViewModel;

    private long mStationId;
    private long mFuelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retreive the fill-up and station Id, from our starting intent.
        mFuelId = getIntent().getLongExtra("FILL_UP_ID", 0);
        mStationId = getIntent().getLongExtra("STATION_ID", 0);
        // Create a FillUpViewModel factory and pass in the station & fill-up Id.
        FillUpViewModel.Factory factory = new FillUpViewModel.Factory(getApplication(), mFuelId, mStationId);
        // Retrieve an instance of the FillUpViewModel registered with this Activity
        mViewModel = ViewModelProviders.of(this, factory).get(FillUpViewModel.class);
        // Inflate Station details binding object.
        ActivityFillUpDetailBinding binding = ActivityFillUpDetailBinding.inflate(getLayoutInflater());
        // Set the binding object's root view as this activity content view (this will make sure the layout is displayed on the screen).
        setContentView(binding.getRoot());
        // Pass in the FillUpViewModel to the binding object so that it's data can be bound to the layout.
        binding.setViewModel(mViewModel);
        // Pass in the edit click listener to the binding object so that it can be bound to the Edit FAB in the layout.
        binding.setEditClickListener(editClickListener);
        // Subscribe to the FillUpViewModel, so can observe upond the Station LiveData.
        subscribe(mViewModel);
    }

    /**
     * Helper method to subscribe on the view model's live data.
     * @param viewModel
     */
    private void subscribe(FillUpViewModel viewModel) {
        // Observe on the fill-up LiveData so that the SQL statement to load the station with id == "mStationId"
        // can be executed.
        viewModel.getLoadedFillUp().observe(this, new Observer<FillUp>() {
            @Override
            public void onChanged(@Nullable FillUp fillUp) {
                mViewModel.setFillUp(fillUp);
            }
        });
        // Observe on the loaded station LiveData so that the SQL statement to load the station with id == "mStationId"
        // can be executed.
        viewModel.getLoadedStation().observe(this, new Observer<Station>() {
            @Override
            public void onChanged(@Nullable Station station) {
                mViewModel.setStation(station);
            }
        });
    }

    /**
     * Click listener that will get fired when the edit FAB is clicked.
     */
    View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FillUpDetailActivity.this, FillUpEditActivity.class);
            // Pass in the fill-up Id to the FillUpEditActivity intent.
            intent.putExtra("FILL_UP_ID", mFuelId);
            // Pass in the station Id to the FillUpEditActivity intent.
            intent.putExtra("STATION_ID", mStationId);
            startActivity(intent);
        }
    };
}
