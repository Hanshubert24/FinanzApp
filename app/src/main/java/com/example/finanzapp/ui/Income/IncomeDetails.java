package com.example.finanzapp.ui.Income;

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
import com.example.finanzapp.ui.DB.DBService;

public class IncomeDetails extends AppCompatActivity {

    private static final String LOG_TAG = IncomeDetails.class.getSimpleName();

    DBDataAccess db;
    int sharePreferncesId;

    TextView textViewCategory;
    TextView textViewCompany;
    TextView textViewBrutto;
    TextView textViewNetto;
    TextView textViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_details);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewCategory = (TextView) findViewById(R.id.textViewIncomeDetailsCategory);
        textViewCompany = (TextView) findViewById(R.id.textViewIncomeDetailsCompany);
        textViewBrutto = (TextView) findViewById(R.id.textViewIncomeDetailsBrutto);
        textViewNetto = (TextView) findViewById(R.id.textViewIncomeDetailsNetto);
        textViewNote = (TextView) findViewById(R.id.textViewIncomeDetailsNote);
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
                    double bruttoDouble = cursor.getDouble(bruttoIndex);
                    String bruttoStringPrepare = DBService.doubleInStringToView(bruttoDouble);

                    double nettoDouble = cursor.getDouble(nettoIndex);
                    String nettoStringPrepare = DBService.doubleInStringToView(nettoDouble);

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
        Intent i = new Intent(IncomeDetails.this, IncomeOverview.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    public void changeIncome(View view){
        Intent i = new Intent(IncomeDetails.this, IncomeDetailsChange.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    public void deleteIncome(View view){
        showDialogDeleteIncome();
    }

    public void showDialogDeleteIncome(){
        AlertDialog.Builder dialogPopUp = new AlertDialog.Builder(IncomeDetails.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_delete_popup = inflater.inflate(R.layout.dialog_delete_popup, null);

        dialogPopUp.setView(dialog_delete_popup);
        dialogPopUp.show();
    }

    public void dialogCancelButton(View view){
        //Weiterleitung
        Intent i = new Intent(IncomeDetails.this, IncomeDetails.class);
        Toast.makeText(this, "Vorgang abgebrochen", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
        startActivity(i);

    }

    public void dialogDeleteButton(View view){

        boolean success = db.deleteOneEntryInTable(DBMyHelper.TABLEIncome_NAME, sharePreferncesId);
        if(success){
            Log.d(LOG_TAG, "Datensatz mit der ID: " + sharePreferncesId + " gelöscht.");
            Toast.makeText(this, "Datensatz gelöscht", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG_TAG, "Fehler beim löschen des Datensatz mit der ID: " + sharePreferncesId + ".");
            Toast.makeText(this, "Fehler beim löschen", Toast.LENGTH_SHORT).show();
        }

        //Weiterleitung
        Intent i = new Intent(IncomeDetails.this, IncomeOverview.class);
        Toast.makeText(this, "Verdienstvertrag gelöscht", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
        startActivity(i);

    }
}