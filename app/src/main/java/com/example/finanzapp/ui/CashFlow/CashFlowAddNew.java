package com.example.finanzapp.ui.CashFlow;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

import java.util.ArrayList;

public class CashFlowAddNew extends AppCompatActivity {

    //Info-Date -> https://www.sqlite.org/lang_datefunc.html

    private static final String LOG_TAG = CashFlowAddNew.class.getSimpleName();

    DBDataAccess db;
    Cursor cursorTableContent;

    double doubleValuePrepared;

    Spinner spinnerTable;
    Spinner spinnerTableContent;

    ArrayList<String> arrayListTableContent;

    ArrayAdapter arrayAdapterTable;
    ArrayAdapter arrayAdapterTableContent;

    EditText editTextDate;
    EditText editTextDoubleValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow_add_new);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        spinnerTable = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTable);
        spinnerTableContent = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTableContent);
        editTextDate = (EditText) findViewById(R.id.editTextCashFlowAddNewDate);
        editTextDoubleValue = (EditText) findViewById(R.id.editTextCashFlowAddNewValue);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        fillSpinnerTable();
        //Reagieren auf Spinner-Auswahl
        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                fillSpinnerTableContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Reagieren auf Spinner-Auswahl
        spinnerTableContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillSpinnerTable(){
        arrayAdapterTable = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DBMyHelper.tableList);
        spinnerTable.setAdapter(arrayAdapterTable);
    }

    private void fillSpinnerTableContent(){



    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBackCashFlowAddNewToFinancBookOV(View view){
        Intent i = new Intent(CashFlowAddNew.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }
}