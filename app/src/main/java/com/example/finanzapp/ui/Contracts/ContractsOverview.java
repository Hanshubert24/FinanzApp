package com.example.finanzapp.ui.Contracts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

public class ContractsOverview extends AppCompatActivity {

    private static final String LOG_TAG = ContractsOverview.class.getSimpleName();

    DBDataAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_overview);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        String[] columns = {DBMyHelper.COLUMNContracts_Type,
                DBMyHelper.COLUMNContracts_Name,
                DBMyHelper.COLUMNContracts_MonthlyCosts};

        ListView itemList = (ListView) findViewById(R.id.listViewContractsOverview);
        int[] viewColumns = new int[]{
                R.id.itemContractsType,
                R.id.itemContractsName,
                R.id.itemContractsMonthlyCosts};

        Cursor cursor = db.viewAllInTable(DBMyHelper.TABLEContracts_NAME);

        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "Fehler beim Auslesen der Datenbank.", Toast.LENGTH_LONG).show();
        }

        try{
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    ContractsOverview.this,
                    R.layout.contract_item_layout,
                    cursor,
                    columns,
                    viewColumns,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            itemList.setAdapter(adapter);
            Log.d(LOG_TAG, "Daten für ListView wurden abgerufen.");

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler beim darstellen der Daten für den ListView.");
        }

        //Aktion wenn ein Eintrag in der Liste angeklickt wird.
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "onItemClick: Position -> " + position + ", id -> " + id);


                //Speichert die "ID" die angeklickt wurde
                SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0); //Shared Prefs Datei öffnen
                SharedPreferences.Editor editor = sharedPreferences.edit(); //Editorklasse initialisieren (um zu schreiben)
                editor.putLong("ContractID_ActivityChangeInfo", id); //Inhalt übergeben (Key, Value)
                editor.commit(); //Speichern
                Log.d(LOG_TAG, "ID: " + id + " wurde in Share Prefference SPContractsDetails gespeichert.");

                //Weiterleitung
                Intent i = new Intent(ContractsOverview.this, ContractsDetails.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBackContractToHome(View view){
        Intent i = new Intent(ContractsOverview.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void NavContractsAddNew(View view){
        Intent i = new Intent(ContractsOverview.this, ContractsAddNew.class);
        startActivity(i);
    }
}