package com.example.finanzapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;

public class EnterPinActivity extends AppCompatActivity {

    EditText editTextNumberPin;
    Button buttonPinEnter;
    String pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        // load the PIN
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        pin = settings.getString("pin","");


        editTextNumberPin = (EditText) findViewById(R.id.editTextNumberPin);
        buttonPinEnter = (Button) findViewById(R.id.buttonPinEnter);

        buttonPinEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextNumberPin.getText().toString();

                if(text.equals(pin)) {

                    //enter to the Financial App, because successful
                    Toast.makeText(EnterPinActivity.this, "Einloggen erfolgreich",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else { // happens when there is no match
                    Toast.makeText(EnterPinActivity.this, "Falscher PIN",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}