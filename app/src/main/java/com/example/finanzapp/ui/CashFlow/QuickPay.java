package com.example.finanzapp.ui.CashFlow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.Contracts.ContractsOverview;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.Income.IncomeOverview;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuickPay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = QuickPay.class.getSimpleName();

    DBDataAccess db;

    Spinner spinnerE1;
    Spinner spinnerE2;
    Spinner spinnerE3;

    ArrayList<String> arrayList;
    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_pay);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        spinnerE1 = (Spinner) findViewById(R.id.spinnerQuickPayE1);
        spinnerE2 = (Spinner) findViewById(R.id.spinnerQuickPayE2);
        spinnerE3 = (Spinner) findViewById(R.id.spinnerQuickPayE3);

        arrayList = new ArrayList<String>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        Cursor cursor = db.viewColumnsFromCostsHierarchyOverviewForListview(DBMyHelper.COLUMNCostsHierarchy_E1);
        //Cursor cursor = db.viewAllInTable(DBMyHelper.TABLECostsHierarchy_Name);

        //E1 aus CostsHierarchy auslesen für SpinnerE1
        if(cursor != null){
            if(cursor.moveToFirst()){
                int E1Index = cursor.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);
                do{
                    //Auslesen der Datenbankeinträge und speichern in der ArrayList<String>
                    arrayList.add(cursor.getString(E1Index));
                } while (cursor.moveToNext());
            }

            //arrayAdapter = ArrayAdapter.createFromResource(this, arrayList, android.R.layout.simple_spinner_item);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayList);

            spinnerE1.setAdapter(arrayAdapter);

            //spinnerE1.setOnItemClickListener(this::onItemSelected);
            //Context mContext = this;
            spinnerE1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("Spinner-Test", "ID: " + id + " Pos: " + position);
                    //Toast.makeText(mContext, "ID: " + id + " Pos: " + position, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage");
            Toast.makeText(this, "Datenbank-Abfragefehler", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBack(View view){
        Intent i = new Intent(QuickPay.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "ID: " + id + " Pos: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}