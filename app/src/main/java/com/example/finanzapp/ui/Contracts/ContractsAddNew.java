package com.example.finanzapp.ui.Contracts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBService;

public class ContractsAddNew extends AppCompatActivity {

    private static final String LOG_TAG = ContractsAddNew.class.getSimpleName();

    DBDataAccess db;

    EditText inputContractType;
    EditText inputContractName;
    EditText inputContractMonthlyCosts;
    EditText inputContractNote;

    String inputContractTypeString;
    String inputContractNameString;
    double inputContractMonthlyCostsDouble = 0;
    String inputContractNoteString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_add_new);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        db = new DBDataAccess(getApplicationContext());
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }


    public void SaveContract(View view){

        boolean isType = false;
        boolean isName = false;
        boolean isMonthlyCosts = false;
        double monthlyCostsPrepare;

        try {
            inputContractType = findViewById(R.id.editTextContractType);
            inputContractName = findViewById(R.id.editTextContractName);
            inputContractMonthlyCosts = findViewById(R.id.editTextContractMonthlyCosts);
            inputContractNote = findViewById(R.id.editTextContractNote);

            //Prüfung, welche Felder beim betätigen des Ändern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if(isEditTextEmpty(inputContractType)){
                Toast.makeText(this, "Typ angeben.", Toast.LENGTH_SHORT).show();
                inputContractType.setHint("Bsp.: Autoversicherung");
            } else {
                isType = true;
                inputContractTypeString = inputContractType.getText().toString();
            }
            if(isEditTextEmpty(inputContractName)){
                Toast.makeText(this, "Name angeben.", Toast.LENGTH_SHORT).show();
                inputContractName.setHint("Bsp.: Verti, HUK");
            } else {
                isName = true;
                inputContractNameString = inputContractName.getText().toString();
            }
            if(isEditTextEmpty(inputContractMonthlyCosts)){
                Toast.makeText(this, "Monatliche Kosten angeben.", Toast.LENGTH_SHORT).show();
                inputContractMonthlyCosts.setHint("Bsp.: 149.98");
                monthlyCostsPrepare = 0.00;
            } else {
                isMonthlyCosts = true;
                inputContractMonthlyCostsDouble = Double.parseDouble(inputContractMonthlyCosts.getText().toString());
                monthlyCostsPrepare = DBService.doubleValueForDB(inputContractMonthlyCostsDouble);
            }

            inputContractNoteString = inputContractNote.getText().toString();


            //Übergabe an die Datenbank
            if(isType && isName && isMonthlyCosts) {
                db.addNewContractInDB(
                        inputContractTypeString,
                        inputContractNameString,
                        monthlyCostsPrepare,
                        inputContractNoteString);

                //Weiterleitung zurück auf die ContractsOverview Seite
                Intent i = new Intent(ContractsAddNew.this, ContractsOverview.class);
                startActivity(i);
                finish();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }
    public void NavBack(View view){
        Intent i = new Intent(ContractsAddNew.this, ContractsOverview.class);
        finishAndRemoveTask();
        startActivity(i);
    }

}