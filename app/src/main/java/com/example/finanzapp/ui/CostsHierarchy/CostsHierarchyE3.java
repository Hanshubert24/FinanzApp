package com.example.finanzapp.ui.CostsHierarchy;

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

public class CostsHierarchyE3 extends AppCompatActivity {

    private static final String LOG_TAG = CostsHierarchyE3.class.getSimpleName();

    DBDataAccess db;
    int sharepreferencesIdE3;


    TextView textViewE1;
    TextView textViewE2;
    TextView textViewE3;
    String E1value;
    String E2value;
    String E3value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_hierarchy_e3);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewE1 = (TextView) findViewById(R.id.textViewCostsHierarchyE3E1);
        textViewE2 = (TextView) findViewById(R.id.textViewCostsHierarchyE3E2);
        textViewE3 = (TextView) findViewById(R.id.textViewCostsHierarchyE3E3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0);
        //Entnimmt werde aus der SharePreference-Datei
        sharepreferencesIdE3 = (int) sharedPreferences.getLong("CostsHierarchyE3_ActivityPassingInfo", 0);

        // Log.d(LOG_TAG, "Die ID: " + sharePreferencesIdE1 + " wurde für E1 aus dem Shared Preferences ausgelesen.");
        Log.d(LOG_TAG, "Die ID: " + sharepreferencesIdE3 + " wurde für E3 aus dem Shared Preferences ausgelesen.");


        //Auslesen des Eintrags aus der Datenbank für E2
        Cursor cursorViewE3 = db.viewOneEntryInTable(DBMyHelper.TABLECostsHierarchy_Name, sharepreferencesIdE3);

        if (cursorViewE3 == null) {
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            if (cursorViewE3.moveToFirst()) {
                int E1Index = cursorViewE3.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);
                int E2Index = cursorViewE3.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E2);
                int E3Index = cursorViewE3.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E3);
                E1value = cursorViewE3.getString(E1Index);
                E2value = cursorViewE3.getString(E2Index);
                E3value = cursorViewE3.getString(E3Index);
                Log.d(LOG_TAG, "Es wurden für E1, E2, E3: " + cursorViewE3.getString(E1Index) + " -> " + cursorViewE3.getString(E2Index) + " -> " + cursorViewE3.getString(E3Index) + " ausgegeben");

                textViewE1.setText(cursorViewE3.getString(E1Index));
                textViewE2.setText(cursorViewE3.getString(E2Index));
                textViewE3.setText(cursorViewE3.getString(E3Index));
            }
        } catch (Exception e) {
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
        finishAndRemoveTask ();
    }

    public void deleteE3(View view){
        showDialogDeleteE3();
    }

    public void showDialogDeleteE3(){
        AlertDialog.Builder dialogPopUp = new AlertDialog.Builder(CostsHierarchyE3.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_delete_popup = inflater.inflate(R.layout.dialog_delete_popup, null);

        dialogPopUp.setView(dialog_delete_popup);
        dialogPopUp.show();
    }

    public void dialogCancelButton(View view){
        Toast.makeText(this, "Vorgang abgebrochen", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CostsHierarchyE3.this, CostsHierarchyE3.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    public void dialogDeleteButton(View view){

        //Löscht den Eintrag mit Bezug zur ID von E3
        boolean success = db.deleteOneEntryInTable(DBMyHelper.TABLECostsHierarchy_Name, sharepreferencesIdE3);

        if(success) {
            //prüfe wie viele Einträge der Wert von E2 hat (übergeordneter Wert)
            int entryInE2 = db.getNumberOfEntrysInTable(DBMyHelper.TABLECostsHierarchy_Name, DBMyHelper.COLUMNCostsHierarchy_E2, E2value);

            if (entryInE2 == 0) {
                //prüfe wie viele EInträge vom Wert E1 existieren
                int entryInE1 = db.getNumberOfEntrysInTable(DBMyHelper.TABLECostsHierarchy_Name, DBMyHelper.COLUMNCostsHierarchy_E1, E1value);

                if(entryInE1 == 0){
                    Intent i = new Intent(CostsHierarchyE3.this, CostsHierarchyOverview.class);
                    Toast.makeText(this, "Alles gelöscht", Toast.LENGTH_SHORT).show();
                    finishAndRemoveTask();
                    startActivity(i);

                } else if(entryInE1 >= 1){
                    Intent i = new Intent(CostsHierarchyE3.this, CostsHierarchyE1.class);
                    Toast.makeText(this, "Unterkategorie gelöscht", Toast.LENGTH_SHORT).show();
                    finishAndRemoveTask();
                    startActivity(i);
                }
            } else if(entryInE2 >= 1){
                Toast.makeText(this, "Subunterkategorie gelöscht", Toast.LENGTH_SHORT).show();

                finishAndRemoveTask();

            }
        } else {
            Toast.makeText(this, "Datenbankfehler", Toast.LENGTH_SHORT).show();
        }
    }
}