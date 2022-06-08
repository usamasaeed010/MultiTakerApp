package com.example.multitakerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.multitakerapp.Auth.SelectLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, SelectLoginActivity.class));
                }
                finish();
            }

        }, 1000);
    }
}