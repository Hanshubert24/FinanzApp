package com.example.finanzapp.ui.Income;

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
import com.example.finanzapp.ui.Contracts.ContractsDetailsChange;
import com.example.finanzapp.ui.Contracts.ContractsOverview;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

public class IncomeDetailsChange extends AppCompatActivity {

    private static final String LOG_TAG = IncomeDetailsChange.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    double bruttoOld;
    double nettoOld;

    TextView textViewCategory;
    TextView textViewCompany;
    TextView textViewBrutto;
    TextView textViewNetto;
    TextView textViewNote;

    EditText editTextCategory;
    EditText editTextCompany;
    EditText editTextBrutto;
    EditText editTextNetto;
    EditText editTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_details_change);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewCategory = (TextView) findViewById(R.id.textViewIncomeDetailsChangeCategory);
        textViewCompany = (TextView) findViewById(R.id.textViewIncomeDetailsChangeCompany);
        textViewBrutto = (TextView) findViewById(R.id.textViewIncomeDetailsChangeBrutto);
        textViewNetto = (TextView) findViewById(R.id.textViewIncomeDetailsChangeNetto);
        textViewNote = (TextView) findViewById(R.id.textViewIncomeDetailsChangeNote);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SPTransferData", 0);
        sharePreferncesId = (int) sharedPreferences.getLong("IncomeID_ActivityChangeInfo", 0);

        Log.d(LOG_TAG, "Die ID: " + sharePreferncesId + " wurde aus dem Shared Preferences ausgelesen.");


        //Auslesen des Eintrags aus der Datenbank
        Cursor cursor = db.viewOneEntryInTable(DBMyHelper.TABLEIncome_NAME, sharePreferncesId);

        if(cursor == null){
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            int categoryIndex = cursor.getColumnIndex(DBMyHelper.COLUMNIncome_Category);
            int companyIndex = cursor.getColumnIndex(DBMyHelper.COLUMNIncome_Company);
            int bruttoIndex = cursor.getColumnIndex(DBMyHelper.COLUMNIncome_Brutto);
            int nettoIndex = cursor.getColumnIndex(DBMyHelper.COLUMNIncome_Netto);
            int noteIndex = cursor.getColumnIndex((DBMyHelper.COLUMNIncome_Note));

            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(categoryIndex) + " " +
                            cursor.getString(companyIndex) + " " +
                            cursor.getString(bruttoIndex) + " " +
                            cursor.getString(nettoIndex) + " " +
                            cursor.getString(noteIndex));

                    //anpassen des Double-Formates bei der Ausgabe
                    bruttoOld = cursor.getDouble(bruttoIndex);
                    String bruttoStringPrepare = DBService.doubleInStringToView(bruttoOld);

                    nettoOld = cursor.getDouble(nettoIndex);
                    String nettoStringPrepare = DBService.doubleInStringToView(nettoOld);

                    textViewCategory.setText(cursor.getString(categoryIndex));
                    textViewCompany.setText(cursor.getString(companyIndex));
                    textViewBrutto.setText(bruttoStringPrepare);
                    textViewNetto.setText(nettoStringPrepare);
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
        Intent i = new Intent(IncomeDetailsChange.this, IncomeOverview.class);
        startActivity(i);
    }

    public void changeIncome(View view){
        //Welche Informationen sollten nicht änderbar sein???

        boolean isCategoryNew = false;
        String categoryNew;
        boolean isCompanyNew = false;
        String companyNew;
        boolean isBruttoNew = false;
        double bruttoNew = bruttoOld;
        boolean isNettoNew = false;
        double nettoNew = nettoOld;
        boolean isNoteNew = false;
        String noteNew;
        boolean success = false;


        try {
            editTextCategory = (EditText) findViewById(R.id.editTextIncomeDetailsChangeCategory);
            editTextCompany = (EditText) findViewById(R.id.editTextIncomeDetailsChangeCompany);
            editTextBrutto = (EditText)findViewById(R.id.editTextIncomeDetailsChangeBrutto);
            editTextNetto = (EditText)findViewById(R.id.editTextIncomeDetailsChangeNetto);
            editTextNote = (EditText) findViewById(R.id.editTextIncomeDetailsChangeNote);

            //Prüfung, welche Felder beim betätigen des Ändern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if (isEditTextEmpty(editTextCategory)) {
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Type-Feld eingegeben.");
                categoryNew = null;
            } else {
                isCategoryNew = true;
                categoryNew = editTextCategory.getText().toString();
            }
            if(isEditTextEmpty(editTextCompany)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Name-Feld eingegeben.");
                companyNew = null;
            } else {
                isCompanyNew = true;
                companyNew = editTextCompany.getText().toString();
            }
            if(isEditTextEmpty(editTextBrutto)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das MonthlyCosts-Feld eingegeben.");
            } else {
                isBruttoNew = true;
                double brutto = Double.parseDouble(editTextBrutto.getText().toString());
                bruttoNew = DBService.doubleValueForDB(brutto);
            }
            if(isEditTextEmpty(editTextNetto)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das MonthlyCosts-Feld eingegeben.");
            } else {
                isNettoNew = true;
                double netto = Double.parseDouble(editTextNetto.getText().toString());
                nettoNew = DBService.doubleValueForDB(netto);
            }
            if(isEditTextEmpty(editTextNote)){
                Log.d(LOG_TAG, "Es wurde keine Änderung für das Note-Feld eingegeben.");
                noteNew = null;
            } else {
                isNoteNew = true;
                noteNew = editTextNote.getText().toString();
            }

            //Wenn alle Eingaben geänderd werden -> Auffolderung zum Löschen und neu anlegen!!!
            //Eine Änderung von allen Daten ist keine Änderung sonder ein neuer Eintrag

            if(isCategoryNew || isCompanyNew || isBruttoNew || isNettoNew || isNoteNew) {
                //Werte an DBDataAccess übergeben
                success = db.changeIncomeOneEntryInDB(
                        sharePreferncesId,
                        categoryNew,
                        companyNew,
                        bruttoNew,
                        nettoNew,
                        true,
                        noteNew);

                if(success){
                    Toast.makeText(this, "Datensatz geändert.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(IncomeDetailsChange.this, IncomeOverview.class);
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