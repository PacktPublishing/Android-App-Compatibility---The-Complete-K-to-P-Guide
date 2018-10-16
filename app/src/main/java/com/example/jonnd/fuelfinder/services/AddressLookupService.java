package com.example.jonnd.fuelfinder.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressLookupService extends IntentService {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = -1;
    private final static String TAG = "LOG_TAG";
    private final static String SERVICE_NAME = "AddressLookupService";
    private final static String PACKAGE_NAME = "com.example.jonnd.fuelfinder.services";
    private final static int MAX_RESULTS = 1;
    public final static String ACTION_ADDRESS_LOOKUP = PACKAGE_NAME + ".ACTION_ADDRESS_LOOKUP";
    public final static String EXTRA_LOCATION = PACKAGE_NAME + ".EXTRA_LOCATION";
    public final static String EXTRA_RESULT_RECEIVER = PACKAGE_NAME + ".EXTRA_RESULT_RECEIVER";
    public final static String RESULT_DATA = PACKAGE_NAME + ".RESULT_DATA";
    private Geocoder mGeocoder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public AddressLookupService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_ADDRESS_LOOKUP)) {
            // If not already created, create a Geocoder instances so we can lookup the address
            // for the passed in location.
            if (mGeocoder == null) {
                mGeocoder = new Geocoder(this, Locale.getDefault());
            }
            // Get the location and parcelled ResultReceiver instance from intent.
            Location currLoc = intent.getParcelableExtra(EXTRA_LOCATION);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
            // Pass the location and ResultReceiver to the getAddress helper method.
            getAddress(currLoc, receiver);
        }
    }

    private void getAddress(Location location, ResultReceiver receiver) {
        if (location == null) {
            sendResult(receiver, FAILURE_RESULT, "Location is null.");
        }
        List<Address> addresses = null;
        String errorMsg = null;
        try {
            addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), MAX_RESULTS);
        } catch (IOException e) {
            e.printStackTrace();
            errorMsg = "Network error or I/O error";
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            errorMsg = "Location is no good, try another";
        }

        if (addresses == null || addresses.isEmpty()) {
            if (errorMsg != null) {
                sendResult(receiver, FAILURE_RESULT, errorMsg);
                return;
            }
            sendResult(receiver, FAILURE_RESULT, "Couldn't determine address for location");
            return;
        }
        Address address = addresses.get(0);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            builder.append(address.getAddressLine(i));
        }

        sendResult(receiver, SUCCESS_RESULT, builder.toString());
    }

    private void sendResult(ResultReceiver receiver, int resultCOde, String message) {
        if (receiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(RESULT_DATA, message);
            receiver.send(resultCOde, bundle);
        }
    }
}
