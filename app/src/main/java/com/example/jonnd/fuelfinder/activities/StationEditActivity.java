package com.example.jonnd.fuelfinder.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.database.OnFinishedListener;
import com.example.jonnd.fuelfinder.databinding.ActivityStationEditBinding;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.viewmodel.StationViewModel;

public class StationEditActivity extends AppCompatActivity {

    StationViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long stationId =  getIntent().getLongExtra("STATION_ID", 0);
        // Create a StationViewModel factory and pass in the station Id.
        StationViewModel.Factory factory = new StationViewModel.Factory(getApplication(), stationId);
        // Retrieve an instance of the StationViewModel registered with this Activity
        mViewModel = ViewModelProviders.of(this, factory).get(StationViewModel.class);
        // Inflate Station details binding object.
        ActivityStationEditBinding binding = ActivityStationEditBinding.inflate(getLayoutInflater());
        // Set the binding object's root view as this activity content view (this will make sure the layout is displayed on the screen).
        setContentView(binding.getRoot());
        // Pass in the StationViewModel to the binding object so that it's data can be bound to the layout.
        binding.setViewModel(mViewModel);
        // Pass in the save click listener to the binding object so that it can be bound to the Edit FAB in the layout.
        binding.setSaveClickListener(saveClickListener);
        // Subscribe to the StationViewModel, so can observe upond the Station LiveData.
        subscribe(mViewModel);
    }

    /**
     * Helper method to subscribe on the view model's live data.
     * @param model
     */
    private void subscribe(StationViewModel model) {
        // Observe on the loaded station LiveData so that the SQL statement to load the station with id == "mStationId"
        // can be executed.
        model.getLoadedStation().observe(this, new Observer<Station>() {
            @Override
            public void onChanged(@Nullable Station station) {
                if (station != null) {
                    mViewModel.setObservableStation(station);
                }
            }
        });
    }

    /**
     * Click listener that will get fired when the save FAB is clicked.
     */
    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Call save view model to attempt to either update or insert the station item.
            mViewModel.saveStation(new OnFinishedListener() {
                @Override
                public void onFinished() {
                    Toast.makeText(StationEditActivity.this, "Updated Station db", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
