package com.example.finanzapp.ui.Contracts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

public class ContractsDetailsChange extends AppCompatActivity {

    private static final String LOG_TAG = ContractsDetailsChange.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    double monthlyCostsOld;

    TextView textViewType;
    TextView textViewName;
    TextView textViewMonthlyCosts;
    TextView textViewNote;

    EditText editTextType;
    EditText editTextName;
    EditText editTextMonthlyCosts;
    EditText editTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_details_change);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewType = (TextView) findViewById(R.id.textViewContractChangeType);
        textViewName = (TextView) findViewById(R.id.textViewContractChangeName);
        textViewMonthlyCosts = (TextView) findViewById(R.id.textViewContractChangeMonthlyCosts);
        textViewNote = (TextView) findViewById(R.id.textViewContractChangeNote);
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
                    monthlyCostsOld = cursor.getDouble(costsIndex);
                    String monthlyCostsString = String.valueOf(monthlyCostsOld);
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

    public void NavBack(View view){
        Intent i = new Intent(ContractsDetailsChange.this, ContractsOverview.class);
        startActivity(i);
    }

    public void changeContract(View view){
        //Welche Informationen sollten nicht änderbar sein???

        boolean isTypeNew = false;
        String typeNew;
        boolean isNameNew = false;
        String nameNew;
        boolean isMonthlyCostsNew = false;
        double monthlyCostsNew = monthlyCostsOld;
        boolean isNoteNew = false;
        String noteNew;
        boolean success = false;


        try {
            editTextType = (EditText) findViewById(R.id.editTextContractsChangeType);
            editTextName = (EditText) findViewById(R.id.editTextContractsChangeName);
            editTextMonthlyCosts = (EditText)findViewById(R.id.editTextContractsChangeMonthlyCosts);
            editTextNote = (EditText) findViewById(R.id.editTextContractsChangeNote);

            //Prüfung, welche Felder beim betätigen des Ändern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if (isEditTextEmpty(editTextType)) {
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Type-Feld eingegeben.");
                typeNew = null;
            } else {
                isTypeNew = true;
                typeNew = editTextType.getText().toString();
            }
            if(isEditTextEmpty(editTextName)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Name-Feld eingegeben.");
                nameNew = null;
            } else {
                isNameNew = true;
                nameNew = editTextName.getText().toString();
            }
            if(isEditTextEmpty(editTextMonthlyCosts)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das MonthlyCosts-Feld eingegeben.");
            } else {
                isMonthlyCostsNew = true;
                monthlyCostsNew = Double.parseDouble(editTextMonthlyCosts.getText().toString());
            }
            if(isEditTextEmpty(editTextNote)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Note-Feld eingegeben.");
                noteNew = null;
            } else {
                isNoteNew = true;
                noteNew = editTextNote.getText().toString();
            }

            //TEST für die Entwicklung (Ausgabe der Zustände)
            Log.d(LOG_TAG, "isTypeNew = " + isTypeNew + " -> Der neue Typ des Contracts = " + typeNew); //TEST
            Log.d(LOG_TAG, "isNameNew = " + isNameNew + " -> Der neue Name des Contracts = " + nameNew); //TEST
            Log.d(LOG_TAG, "isMonthlyCosts = " + isMonthlyCostsNew + " -> Der neue monatliche Betrag des Contracts = " + String.valueOf(monthlyCostsNew)); //TEST
            Log.d(LOG_TAG, "isNoteNew = " + isNoteNew + " -> Die neue Note des Contracts = \" + noteNew"); //TEST



            //Wenn alle Eingaben geänderd werden -> Auffolderung zum Löschen und neu anlegen!!!
            //Eine Änderung von allen Daten ist keine Änderung sonder ein neuer Eintrag

            if(isTypeNew || isNameNew || isMonthlyCostsNew || isNoteNew) {
                //Werte an DBDataAccess übergeben
                success = db.changeContractOneEntryInDB(
                        sharePreferncesId,
                        typeNew,
                        nameNew,
                        monthlyCostsNew,
                        noteNew);

                if(success){
                    Toast.makeText(this, "Datensatz geändert.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ContractsDetailsChange.this, ContractsOverview.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Datensatzänderung fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Ändern Sie einen Eintrag.", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }
}