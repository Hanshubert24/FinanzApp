package com.example.finanzapp.ui.logout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        // TODO Exit ggf raus oder neu
        finish();
        System.exit(0);
    }
}