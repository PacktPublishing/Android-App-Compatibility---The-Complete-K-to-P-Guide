package com.example.jonnd.fuelfinder.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.jonnd.fuelfinder.database.OnFinishedListener;
import com.example.jonnd.fuelfinder.databinding.ActivityStationEditBinding;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.services.AddressLookupService;
import com.example.jonnd.fuelfinder.viewmodel.StationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class StationEditActivity extends AppCompatActivity {

    StationViewModel mViewModel;
    private FusedLocationProviderClient mClient;
    private LocationRequest mRequest;
    private AddressReceiver mReceiver;
    private LocationCallback mCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {
                startService(location);
                return;
            }
        }
    };

    private void startService(Location location) {
        Intent intent = new Intent(this, AddressLookupService.class);
        intent.setAction(AddressLookupService.ACTION_ADDRESS_LOOKUP);
        intent.putExtra(AddressLookupService.EXTRA_LOCATION, location);
        intent.putExtra(AddressLookupService.EXTRA_RESULT_RECEIVER, mReceiver);
        startService(intent);
    }

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

        binding.setLocationClickListener(locationClickListener);
        // Subscribe to the StationViewModel, so can observe upond the Station LiveData.
        subscribe(mViewModel);

        mRequest = new LocationRequest();
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mRequest.setNumUpdates(1);

        mReceiver = new AddressReceiver(new Handler(Looper.getMainLooper()));

        mClient = LocationServices.getFusedLocationProviderClient(this);
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




    private boolean handleLocationPermissions() {
        // In order to get a accurate location, we need the ACCESS_FINE_LOCATION permission,
        // so check to see if the application was granted the permission... The permission ACCESS_COARSE_LOCATION
        // would work but according to the API, the location is only accurate to a block.
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Request")
                    .setMessage("We need the Location Permission to get the address of the current location.")
                    .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(StationEditActivity.this,
                                    new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 100 );
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return false;
        }

        ActivityCompat.requestPermissions(StationEditActivity.this,
                new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 100 );

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission Granted");
            }
            else {
                showToast("Permission Granted");
            }
        }
    }

    View.OnClickListener locationClickListener = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            // If handle location Permission returns true, we are all good and are read yo
            // attempt to read the current location.
            if(handleLocationPermissions()) {
                mClient.requestLocationUpdates(mRequest, mCallback, null);
            }
        }
    };

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public class AddressReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public AddressReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            String msg = resultData.getString(AddressLookupService.RESULT_DATA);
            if(resultCode == AddressLookupService.SUCCESS_RESULT) {
                mViewModel.address.set(msg);
            }
            else {
                showToast(msg);
            }
        }
    }
}
