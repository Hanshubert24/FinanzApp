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

public class CreatePinActivity extends AppCompatActivity {
    EditText editTextNumberPasswordChangePin1, editTextNumberPasswordChangePin2;
    Button buttonPinChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        editTextNumberPasswordChangePin1 = (EditText) findViewById(R.id.editTextNumberPasswordChangePin1);
        editTextNumberPasswordChangePin2 = (EditText) findViewById(R.id.editTextNumberPasswordChangePin2);
        buttonPinChange =(Button) findViewById(R.id.buttonPinChange);

        buttonPinChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String pin1 = editTextNumberPasswordChangePin1.getText().toString();
                String pin2 = editTextNumberPasswordChangePin2.getText().toString();

                if(pin1.equals("") || pin2.equals("")) {  // no Pin set
                    Toast.makeText(CreatePinActivity.this, "Keinen PIN eingegeben", Toast.LENGTH_SHORT).show();
                } else { // match and save PIN
                     if (pin1.equals(pin2)) {
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pin", pin1);
                        editor.apply();

                        //enter to the Financial App
                         Toast.makeText(CreatePinActivity.this, "PIN erfolgreich festgelegt", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                         startActivity(intent);
                         finish();
                    } else { // no match
                        Toast.makeText(CreatePinActivity.this, "Der eingegebene PIN stimmt nicht überein",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}