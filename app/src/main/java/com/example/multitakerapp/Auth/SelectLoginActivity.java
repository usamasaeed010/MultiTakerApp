package com.example.multitakerapp.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.multitakerapp.Utlis.Constants;
import com.example.multitakerapp.databinding.ActivitySelectLoginBinding;

public class SelectLoginActivity extends AppCompatActivity {


    ActivitySelectLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.loginChief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                intent.putExtra(Constants.SELECT_USER, Constants.CHIEF);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        binding.loginOwnHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                intent.putExtra(Constants.SELECT_USER, Constants.OWNER_HOSTEL);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        binding.loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLoginActivity.this, LoginMainActivity.class);
                intent.putExtra(Constants.SELECT_USER, Constants.USER);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }


}