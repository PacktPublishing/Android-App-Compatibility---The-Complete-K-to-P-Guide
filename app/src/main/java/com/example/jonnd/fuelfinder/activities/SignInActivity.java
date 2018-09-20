package com.example.jonnd.fuelfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jonnd.fuelfinder.FuelFinderApp;
import com.example.jonnd.fuelfinder.R;
import com.example.jonnd.fuelfinder.entities.User;

import java.util.Date;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Jacob");
        user.setUserName("johnJacob");
        user.setPassword("1234");
        user.setDateCreated(new Date());
        ((FuelFinderApp) getApplication()).getUserRepo().insertUser(user);
    }
}
