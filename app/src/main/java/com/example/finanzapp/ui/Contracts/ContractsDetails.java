package com.example.finanzapp.ui.Contracts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

public class ContractsDetails extends AppCompatActivity {

    private static final String LOG_TAG = ContractsDetails.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    TextView textViewType;
    TextView textViewName;
    TextView textViewMonthlyCosts;
    TextView textViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_details);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewType = (TextView) findViewById(R.id.textViewContractDetailsType);
        textViewName = (TextView) findViewById(R.id.textViewContractDetailsName);
        textViewMonthlyCosts = (TextView) findViewById(R.id.textViewContractDetailsMonthlyCosts);
        textViewNote = (TextView) findViewById(R.id.textViewContractDetailsNote);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0);
        sharePreferncesId = (int) sharedPreferences.getLong("ContractID_ActivityChangeInfo", 0);

        Log.d(LOG_TAG, "Die ID: " + sharePreferncesId + " wurde aus dem Shared Preferences ausgelesen.");

        //Auslesen des Eintrags aus der Datenbank
        Cursor cursor = db.viewOneEntryInTable(DBMyHelper.TABLEContracts_NAME, sharePreferncesId);

        if(cursor == null){
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            int typeIndex = cursor.getColumnIndex(DBMyHelper.COLUMNContracts_Type);
            int nameIndex = cursor.getColumnIndex(DBMyHelper.COLUMNContracts_Name);
            int costsIndex = cursor.getColumnIndex(DBMyHelper.COLUMNContracts_MonthlyCosts);
            int noteIndex = cursor.getColumnIndex((DBMyHelper.COLUMNContracts_Note));

            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(typeIndex) + " " +
                            cursor.getString(nameIndex) + " " +
                            cursor.getString(costsIndex) + " " +
                            cursor.getString(noteIndex));

                    //Monatliche Kosten rausnehmen, als String ausgeben und ein "€" dranbasteln
                    double monthlyCosts = cursor.getDouble(costsIndex);
                    String monthlyCostsString = String.valueOf(monthlyCosts);
                    String monthlyCostsStringPrepare = monthlyCostsString + "€";

                    textViewType.setText(cursor.getString(typeIndex));
                    textViewName.setText(cursor.getString(nameIndex));
                    textViewMonthlyCosts.setText(monthlyCostsStringPrepare);
                    textViewNote.setText(cursor.getString(noteIndex));

                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }


    public void NavBackContractsdetails(View view){
        Intent i = new Intent(ContractsDetails.this, ContractsOverview.class);
        startActivity(i);
        finish();
    }

    public void changeContract(View view){
        Intent i = new Intent(ContractsDetails.this, ContractsDetailsChange.class);
        startActivity(i);
        finish();
    }

    public void deleteContract(View view){
        showDialogDeleteContract();
    }

    public void showDialogDeleteContract(){
        AlertDialog.Builder dialogPopUp = new AlertDialog.Builder(ContractsDetails.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_delete_popup = inflater.inflate(R.layout.dialog_delete_popup, null);

        dialogPopUp.setView(dialog_delete_popup);
        dialogPopUp.show();
    }

    public void dialogCancelButton(View view){
        //Weiterleitung
        Intent i = new Intent(ContractsDetails.this, ContractsOverview.class);
        startActivity(i);
        finish();
    }

    public void dialogDeleteButton(View view){

        boolean success = db.deleteOneEntryInTable(DBMyHelper.TABLEContracts_NAME, sharePreferncesId);
        if(success){
            Log.d(LOG_TAG, "Datensatz mit der ID: " + sharePreferncesId + " gelöscht.");
            Toast.makeText(this, "Datensatz gelöscht", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG_TAG, "Fehler beim löschen des Datensatz mit der ID: " + sharePreferncesId + ".");
            Toast.makeText(this, "Fehler beim löschen", Toast.LENGTH_SHORT).show();
        }

        //Weiterleitung
        Intent i = new Intent(ContractsDetails.this, ContractsOverview.class);
        startActivity(i);
        finish();

    }
}