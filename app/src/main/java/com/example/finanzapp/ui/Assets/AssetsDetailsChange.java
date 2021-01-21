package com.example.finanzapp.ui.Assets;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

public class AssetsDetailsChange extends AppCompatActivity {

    private static final String LOG_TAG = AssetsDetailsChange.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    double monthlyCostsOld;
    double monthlyEarningsOld;
    double finanzialAssetsOld;
    double creditOld;

    TextView textViewCategory;
    TextView textViewName;
    TextView textViewMonthlyCosts;
    TextView textViewMonthlyEarnings;
    TextView textViewFinancialAsset;
    TextView textViewCredit;
    TextView textViewNote;

    EditText editTextCategory;
    EditText editTextName;
    EditText editTextMonthlyCosts;
    EditText editTextMonthlyEarnings;
    EditText editTextFinancialAsset;
    EditText editTextCredit;
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
        textViewFinancialAsset = (TextView) findViewById(R.id.textViewAssetsChangeFinancialAsset);
        textViewCredit = (TextView) findViewById(R.id.textViewAssetsChangeCredit);
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
            int financialAssetIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_FinancialAsset);
            int creditIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Credit);
            int imagePathindex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_ImagePath);
            int noteIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Note);

            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(categoryIndex) + " " +
                            cursor.getString(nameIndex) + " " +
                            cursor.getString(monthlyCostsIndex) + " " +
                            cursor.getString(monthlyEarningsIndex) + " " +
                            cursor.getString(financialAssetIndex) + " " +
                            cursor.getString(creditIndex) + " " +
                            cursor.getString(imagePathindex) + " " +
                            cursor.getString(noteIndex));

                    //Format für die Ausgabe anpassen
                    double monthlyCosts = cursor.getDouble(monthlyCostsIndex);
                    String monthlyCostsStringPrepare = DBService.doubleInStringToView(monthlyCosts);

                    double monthlyEarnings = cursor.getDouble(monthlyEarningsIndex);
                    String monthlyEarningsStringPrepare = DBService.doubleInStringToView(monthlyEarnings);

                    double financialAsset = cursor.getDouble(financialAssetIndex);
                    String financialAssetStringPrepare = DBService.doubleInStringToView(financialAsset);

                    double credit = cursor.getDouble(creditIndex);
                    String creditStringPrepare = DBService.doubleInStringToView(credit);

                    textViewCategory.setText(cursor.getString(categoryIndex));
                    textViewName.setText(cursor.getString(nameIndex));
                    textViewMonthlyCosts.setText(monthlyCostsStringPrepare);
                    textViewMonthlyEarnings.setText(monthlyEarningsStringPrepare);
                    textViewFinancialAsset.setText(financialAssetStringPrepare);
                    textViewCredit.setText(creditStringPrepare);
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
        boolean isFinancialAssetNew = false;
        double financialAssetNew = finanzialAssetsOld;
        boolean isCreditNew = false;
        double creditNew = creditOld;
        boolean isNoteNew = false;
        String noteNew;
        boolean success = false;


        try {
            editTextCategory = (EditText) findViewById(R.id.editTextAssetsChangeCategory);
            editTextName = (EditText) findViewById(R.id.editTextAssetsChangeName);
            editTextMonthlyCosts = (EditText)findViewById(R.id.editTextAssetsChangeMonthlyCosts);
            editTextMonthlyEarnings = (EditText)findViewById(R.id.editTextAssetsChangeMonthlyEarnings);
            editTextFinancialAsset = (EditText) findViewById(R.id.editTextAssetsChangeFinancialAsset);
            editTextCredit = (EditText) findViewById(R.id.editTextAssetsChangeCredit);
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
                double monthlyCosts = Double.parseDouble(editTextMonthlyCosts.getText().toString());
                monthlyCostsNew = DBService.doubleValueForDB(monthlyCosts);
            }
            if(isEditTextEmpty(editTextMonthlyEarnings)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das MonthlyEarnings-Feld eingegeben.");
            } else {
                isMonthlyEarningsNew = true;
                double monthlyEarnings= Double.parseDouble(editTextMonthlyEarnings.getText().toString());
                monthlyEarningsNew = DBService.doubleValueForDB(monthlyEarnings);
            }
            if(isEditTextEmpty(editTextFinancialAsset)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das FinancialAsset-Feld eingegeben.");
            } else {
                isFinancialAssetNew = true;
                double financialAsset = Double.parseDouble(editTextFinancialAsset.getText().toString());
                financialAssetNew = DBService.doubleValueForDB(financialAsset);
            }
            if(isEditTextEmpty(editTextCredit)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Credit-Feld eingegeben.");
            } else {
                isCreditNew = true;
                double credit = Double.parseDouble(editTextCredit.getText().toString());
                creditNew = DBService.doubleValueForDB(credit);
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
            Log.d(LOG_TAG, "isFinancialAsset = " + isFinancialAssetNew+ " -> Der neue Verkehrwert des Asset = " + String.valueOf(financialAssetNew)); //TEST
            Log.d(LOG_TAG, "isCredit = " + isCreditNew + " -> Die Kredit-/Schulenhöhe des Asset = " + String.valueOf(creditNew)); //TEST

            Log.d(LOG_TAG, "isNoteNew = " + isNoteNew + " -> Die neue Note des Assets = \" + noteNew"); //TEST



            //Wenn alle Eingaben geänderd werden -> Auffolderung zum Löschen und neu anlegen!!!
            //Eine Änderung von allen Daten ist keine Änderung sonder ein neuer Eintrag

            if(isCategoryNew || isNameNew || isMonthlyCostsNew || isMonthlyEarningsNew || isNoteNew || isFinancialAssetNew || isCreditNew) {
                //Werte an DBDataAccess übergeben
                success = db.changeAssetOneEntryInDB(
                        sharePreferncesId,
                        categoryNew,
                        nameNew,
                        monthlyCostsNew,
                        monthlyEarningsNew,
                        financialAssetNew,
                        creditNew,
                        null,           //Funktion muss noch implementiert werden (ImagePath)
                        noteNew);

                if(success){
                    Toast.makeText(this, "Datensatz geändert.", Toast.LENGTH_SHORT).show();

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
    public void NavBackDetailsChangeToAssetsOV(View view){

        finish();
    }
}