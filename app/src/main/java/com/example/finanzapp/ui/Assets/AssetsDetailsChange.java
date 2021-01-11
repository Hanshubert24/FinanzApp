package com.example.finanzapp.ui.Assets;

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

import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.R;

public class AssetsDetailsChange extends AppCompatActivity {

    private static final String LOG_TAG = AssetsDetailsChange.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    double monthlyCostsOld;
    double monthlyEarningsOld;

    TextView textViewCategory;
    TextView textViewName;
    TextView textViewMonthlyCosts;
    TextView textViewMonthlyEarnings;
    TextView textViewNote;

    EditText editTextCategory;
    EditText editTextName;
    EditText editTextMonthlyCosts;
    EditText editTextMonthlyEarnings;
    EditText editTextNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details_change);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewCategory = (TextView) findViewById(R.id.textViewAssetsChangeCategory);
        textViewName = (TextView) findViewById(R.id.textViewAssetsChangeName);
        textViewMonthlyCosts = (TextView) findViewById(R.id.textViewAssetsChangeMonthlyCosts);
        textViewMonthlyEarnings = (TextView) findViewById(R.id.textViewAssetsChangeMonthlyEarnings);
        textViewNote = (TextView) findViewById(R.id.textViewAssetsChangeNote);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0);
        sharePreferncesId = (int) sharedPreferences.getLong("AssetID_ActivityChangeInfo", 0);

        Log.d(LOG_TAG, "Die ID: " + sharePreferncesId + " wurde aus dem Shared Preferences ausgelesen.");


        //Auslesen des Eintrags aus der Datenbank
        Cursor cursor = db.viewOneEntryInTable(DBMyHelper.TABLEAssets_NAME, sharePreferncesId);

        if(cursor == null){
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            int categoryIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Category);
            int nameIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Name);
            int monthlyCostsIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyCosts);
            int monthlyEarningsIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyEarnings);
            int imagePathindex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_ImagePath);
            int noteIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Note);

            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(categoryIndex) + " " +
                            cursor.getString(nameIndex) + " " +
                            cursor.getString(monthlyCostsIndex) + " " +
                            cursor.getString(monthlyEarningsIndex) + " " +
                            cursor.getString(imagePathindex) + " " +
                            cursor.getString(noteIndex));

                    //Monatliche Kosten rausnehmen, als String ausgeben und ein "€" dranbasteln
                    double monthlyCosts = cursor.getDouble(monthlyCostsIndex);
                    String monthlyCostsString = String.valueOf(monthlyCosts);
                    String monthlyCostsStringPrepare = monthlyCostsString + "€";

                    double monthlyEarnings = cursor.getDouble(monthlyEarningsIndex);
                    String monthlyEarningsString = String.valueOf(monthlyEarnings);
                    String monthlyEarningsStringPrepare = monthlyEarningsString + "€";


                    textViewCategory.setText(cursor.getString(categoryIndex));
                    textViewName.setText(cursor.getString(nameIndex));
                    textViewMonthlyCosts.setText(monthlyCostsStringPrepare);
                    textViewMonthlyEarnings.setText(monthlyEarningsStringPrepare);
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
        Intent i = new Intent(AssetsDetailsChange.this, AssetsOverview.class);
        startActivity(i);
    }

    public void changeContract(View view){
        //Welche Informationen sollten nicht änderbar sein???

        boolean isCategoryNew = false;
        String categoryNew;
        boolean isNameNew = false;
        String nameNew;
        boolean isMonthlyCostsNew = false;
        double monthlyCostsNew = monthlyCostsOld;
        boolean isMonthlyEarningsNew = false;
        double monthlyEarningsNew = monthlyCostsOld;
        boolean isNoteNew = false;
        String noteNew;
        boolean success = false;


        try {
            editTextCategory = (EditText) findViewById(R.id.editTextAssetsChangeCategory);
            editTextName = (EditText) findViewById(R.id.editTextAssetsChangeName);
            editTextMonthlyCosts = (EditText)findViewById(R.id.editTextAssetsChangeMonthlyCosts);
            editTextMonthlyEarnings = (EditText)findViewById(R.id.editTextAssetsChangeMonthlyEarnings);
            editTextNote = (EditText) findViewById(R.id.editTextAssetsChangeNote);

            //Prüfung, welche Felder beim betätigen des Ändern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if (isEditTextEmpty(editTextCategory)) {
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Category-Feld eingegeben.");
                categoryNew = null;
            } else {
                isCategoryNew = true;
                categoryNew = editTextCategory.getText().toString();
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
            if(isEditTextEmpty(editTextMonthlyEarnings)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das MonthlyEarnings-Feld eingegeben.");
            } else {
                isMonthlyEarningsNew = true;
                monthlyEarningsNew= Double.parseDouble(editTextMonthlyEarnings.getText().toString());
            }
            if(isEditTextEmpty(editTextNote)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Note-Feld eingegeben.");
                noteNew = null;
            } else {
                isNoteNew = true;
                noteNew = editTextNote.getText().toString();
            }

            //TEST für die Entwicklung (Ausgabe der Zustände)
            Log.d(LOG_TAG, "isCategoryNew = " + isCategoryNew + " -> Der neue Typ des Assets = " + categoryNew); //TEST
            Log.d(LOG_TAG, "isNameNew = " + isNameNew + " -> Der neue Name des Assets = " + nameNew); //TEST
            Log.d(LOG_TAG, "isMonthlyCosts = " + isMonthlyCostsNew + " -> Die neuen monatlichen Kosten des Asset = " + String.valueOf(monthlyCostsNew)); //TEST
            Log.d(LOG_TAG, "isMonthlyEarnings = " + isMonthlyEarningsNew + " -> Die neuen monatlichen Einnahmen durch des Asset = " + String.valueOf(monthlyEarningsNew)); //TEST
            Log.d(LOG_TAG, "isNoteNew = " + isNoteNew + " -> Die neue Note des Assets = \" + noteNew"); //TEST



            //Wenn alle Eingaben geänderd werden -> Auffolderung zum Löschen und neu anlegen!!!
            //Eine Änderung von allen Daten ist keine Änderung sonder ein neuer Eintrag

            if(isCategoryNew || isNameNew || isMonthlyCostsNew || isMonthlyEarningsNew || isNoteNew) {
                //Werte an DBDataAccess übergeben
                success = db.changeAssetOneEntryInDB(
                        sharePreferncesId,
                        categoryNew,
                        nameNew,
                        monthlyCostsNew,
                        monthlyEarningsNew,
                        null,           //Funktion muss noch implementiert werden (ImagePath)
                        noteNew);

                if(success){
                    Toast.makeText(this, "Datensatz geändert.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AssetsDetailsChange.this, AssetsOverview.class);
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