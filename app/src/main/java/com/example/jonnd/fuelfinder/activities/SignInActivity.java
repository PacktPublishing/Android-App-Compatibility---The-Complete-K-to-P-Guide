package com.example.jonnd.fuelfinder.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.jonnd.fuelfinder.Constants;
import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.databinding.ActivitySignInBinding;
import com.example.jonnd.fuelfinder.entities.User;
import com.example.jonnd.fuelfinder.viewmodel.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    SignInViewModel mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the sigin-in activities layout binding files, and then set the bindings root view as the activities content view.
        ActivitySignInBinding binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get a instance of SignInViewModel, that is "registered" to the SignInActivity. (This can return a new instance, or a
        // existing instance)
        mModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        // Pass the SignInViewModel to sign-in binding object to allow the UI to binding to it.
        binding.setViewModel(mModel);
        // Pass the sign-in click listener to the sing-in binding object to allow the sign-in button to bind to it.
        binding.setClickListener(clickListener);

        long userId = getUserId(this);
        if(userId != -1) {
            mModel.getUser(userId).observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    ((FuelFinderApp) getApplication()).setUser(user);
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                }
            });
        }
    }

    /**
     * Sign-in Click listener.
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Attempts to login using the currently entered username and password... This method returns a User LiveData, so we
            // need to observe on it to cause the underlying SQL query to be executed and a result returned.
            mModel.login().observe(SignInActivity.this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    // We need to perform a null check, because even if the username and password entered doesns't match an
                    // existing user, this onChanged callback will still be fired, passing in a null user arg... If none null
                    // that means the entered credentials match an existing user.
                    if (user != null) {
                        // We've found the user item in the FillUpDatabase that matches the entered credentials, so store the user
                        // in the FuelFinder application object so the entire app has access to it, then start the MainActivity.
                        ((FuelFinderApp) getApplication()).setUser(user);
                        persistUser(SignInActivity.this, user);
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        // Indicator to notify the user that the entered user credentials don't match a user inside of the FIllup
                        // db.
                        Toast.makeText(SignInActivity.this, "Did not find user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    /**
     * Stores a user(id) into the {@link SharedPreferences}.
     *
     * @param context
     * @param user  The user to be stored into the shared preferences.
     */
    public static void persistUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Constants.USER_ID, user.getId());
        editor.putString(Constants.USERNAME, user.getUserName());
        editor.apply();
    }

    /**
     * Retrieves the user (Id) from the {@link SharedPreferences}.
     * @param context
     * @return The user Id of the user currently stored in the {@link SharedPreferences}, if no user saved, return '-1'.
     */
    public static long getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return preferences.getLong(Constants.USER_ID, -1);
    }
}
