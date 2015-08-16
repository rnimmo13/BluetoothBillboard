package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

@SuppressWarnings("unused")
public class splash_screen extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        @SuppressWarnings("UnusedAssignment")
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        Dynamo_Interface.setApplication_context(getApplicationContext());
        Dynamo_Interface.setCurrent_board("000000");
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(splash_screen.this, view_post_list_screen.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}