package com.example.finanzapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;


public class SplashActivity extends AppCompatActivity {

    String pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // load the PIN
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        pin = settings.getString("pin","");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(pin.equals("")) { // if nothing typed
                Intent intent = new Intent(getApplicationContext(), CreatePinActivity.class);
                startActivity(intent);
                finish();
                } else {    // something is wrote down

                    Intent intent = new Intent(getApplicationContext(), EnterPinActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }, 2000);
    }
}