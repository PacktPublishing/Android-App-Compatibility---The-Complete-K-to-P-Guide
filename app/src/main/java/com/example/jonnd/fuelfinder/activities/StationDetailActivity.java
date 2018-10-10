package com.example.jonnd.fuelfinder.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jonnd.fuelfinder.databinding.ActivityStationDetailBinding;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.viewmodel.StationViewModel;

public class StationDetailActivity extends AppCompatActivity {

    private long mStationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retreive the station Id, from our starting intent.
        mStationId = getIntent().getLongExtra("STATION_ID", 0);
        // Create a StationViewModel factory and pass in the station Id.
        StationViewModel.Factory factory = new StationViewModel.Factory(getApplication(), mStationId);
        // Retrieve an instance of the StationViewModel registered with this Activity
        StationViewModel model = ViewModelProviders.of(this, factory).get(StationViewModel.class);
        // Inflate Station details binding object.
        ActivityStationDetailBinding binding = ActivityStationDetailBinding.inflate(getLayoutInflater());
        // Set the binding object's root view as this activity content view (this will make sure the layout is displayed on the screen).
        setContentView(binding.getRoot());
        // Pass in the StationViewModel to the binding object so that it's data can be bound to the layout.
        binding.setViewModel(model);
        // Pass in the edit click listener to the binding object so that it can be bound to the Edit FAB in the layout.
        binding.setEditCLickListener(editCLickListener);
        // Subscribe to the StationViewModel, so can observe upond the Station LiveData.
        subscribe(model);
    }

    /**
     * Helper method to subscribe on the view model's live data.
     * @param model
     */
    private void subscribe(final StationViewModel model) {
        // Observe on the loaded station LiveData so that the SQL statement to load the station with id == "mStationId"
        // can be executed.
        model.getLoadedStation().observe(this, new Observer<Station>() {
            @Override
            public void onChanged(@Nullable Station station) {
                // set the station object on the view model.
                model.setObservableStation(station);
            }
        });
    }

    /**
     * Click listener that will get fired when the edit FAB is clicked.
     */
    View.OnClickListener editCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StationDetailActivity.this, StationEditActivity.class);
            // Pass in the station Id to the StationEditActivity intent.
            intent.putExtra("STATION_ID", mStationId);
            startActivity(intent);
        }
    };
}
