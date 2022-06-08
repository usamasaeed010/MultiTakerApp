package com.example.multitakerapp.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.multitakerapp.Utlis.Constants;
import com.example.multitakerapp.Utlis.MysharedPreferences;
import com.example.multitakerapp.databinding.ActivitySelectLoginBinding;

public class SelectLoginActivity extends AppCompatActivity {


    ActivitySelectLoginBinding binding;
    Activity activity = SelectLoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.loginChief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                MysharedPreferences.getInstance(activity).PutSelectRoleInfo(Constants.SELECT_USER, Constants.CHIEF);
                /* intent.putExtra(Constants.SELECT_USER, Constants.CHIEF);*/
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        binding.loginOwnHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                MysharedPreferences.getInstance(activity).PutSelectRoleInfo(Constants.SELECT_USER, Constants.OWNER_HOSTEL);

                /*  intent.putExtra(Constants.SELECT_USER, Constants.OWNER_HOSTEL);*/
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        binding.loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                MysharedPreferences.getInstance(activity).PutSelectRoleInfo(Constants.SELECT_USER, Constants.USER);

                /*  intent.putExtra(Constants.SELECT_USER, Constants.USER);*/
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}