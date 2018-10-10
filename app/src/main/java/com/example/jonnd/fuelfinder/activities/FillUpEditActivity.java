package com.example.jonnd.fuelfinder.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.database.OnFinishedListener;
import com.example.jonnd.fuelfinder.databinding.ActivityFillUpEditBinding;
import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.fragments.DatePickerFragment;
import com.example.jonnd.fuelfinder.fragments.TimePickerFragment;
import com.example.jonnd.fuelfinder.viewmodel.FillUpViewModel;

import java.util.ArrayList;
import java.util.List;

public class FillUpEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FillUpViewModel mViewModel;
    private ArrayAdapter<Station> mStationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retreive the fill-up and station Id, from our starting intent.
        long fuelId = getIntent().getLongExtra("FILL_UP_ID", 0);
        long stationId = getIntent().getLongExtra("STATION_ID", 0);
        // Create a FillUpViewModel factory and pass in the station & fill-up Id.
        FillUpViewModel.Factory factory = new FillUpViewModel.Factory(getApplication(), fuelId, stationId);
        // Retrieve an instance of the FillUpViewModel registered with this Activity
        mViewModel = ViewModelProviders.of(this, factory).get(FillUpViewModel.class);
        mStationAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, new ArrayList<Station>());
        // Inflate Station details binding object.
        ActivityFillUpEditBinding binding = ActivityFillUpEditBinding.inflate(getLayoutInflater());
        // Set the binding object's root view as this activity content view (this will make sure the layout is displayed on the screen).
        setContentView(binding.getRoot());
        // Pass in the FillUpViewModel to the binding object so that it's data can be bound to the layout.
        binding.setViewModel(mViewModel);
        binding.setAdapter(mStationAdapter);
        // Pass in the the dateTimeClick listener to the binding object so that it can be bound to the date & time EditTexts.
        binding.setDateTimeClickListener(dateClickListener);
        // Pass in the save click listener to the binding object so that it can be bound to the Edit FAB in the layout.
        binding.setSaveClickListener(saveClickListener);
        // Pass in the item click listenr to the binding object so that it can be bound to the stationName AutoCompleteTextView.
        binding.setClickListener(onItemClickListener);
        // Subscribe to the FillUpViewModel, so can observe upond the Station LiveData.
        subscribe(mViewModel);
    }

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
        // Observe on the Station List LiveData so that the SQL statement to load the station with id == "mStationId"
        // can be executed.
        viewModel.getLoadedStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable List<Station> stations) {
                mStationAdapter.clear();
                // Pass that list of stations to the ArrayAdapter.
                mStationAdapter.addAll(stations);
            }
        });
    }

    /**
     * Click listener that will get fired when either the date or time TextView's are seleted.
     */
    View.OnClickListener dateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // If data EditText is selected, launch the DataPicker Fragment, otherwise
            // select the TimePicker Fragment.
            if (v.getId() == R.id.date_txtVw) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), "DATE_PICKER");
            }
            else {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), "TIME_PICKER");
            }
        }
    };

    /**
     * Item click listener that will get fired when a station is selected inside of the {@link android.widget.AutoCompleteTextView}.
     */
    AdapterView.OnItemClickListener onItemClickListener = new  AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Station selectedStation = mStationAdapter.getItem(position);
            mViewModel.setStation(selectedStation);
        }
    };

    /**
     * Click listener that will get fired when the save FAB is clicked.
     */
    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Call save view model to attempt to either update or insert the fill-up item.
            mViewModel.saveFillUp(new OnFinishedListener() {
                @Override
                public void onFinished() { Toast.makeText(FillUpEditActivity.this, "Updated FillUp db", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mViewModel.setDate(year, month, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mViewModel.setTime(hourOfDay, minute, 0);
    }
}
