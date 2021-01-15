package com.example.finanzapp.ui.CostsHierarchy;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

public class CostsHierarchyE1 extends AppCompatActivity {

    private static final String LOG_TAG = CostsHierarchyE1.class.getSimpleName();

    DBDataAccess db;
    int sharePreferencesIdE1;

    TextView textViewE1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_hierarchy_e1);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewE1 = (TextView) findViewById(R.id.textViewCostsHierarchyE1E1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0);
        sharePreferencesIdE1 = (int) sharedPreferences.getLong("CostsHierarchyE1_ActivityPassingInfo", 0);

        Log.d(LOG_TAG, "Die ID: " + sharePreferencesIdE1 + " wurde aus dem Shared Preferences ausgelesen.");

        //Auslesen des Eintrags aus der Datenbank
        Cursor cursorViewE1 = db.viewOneEntryInTable(DBMyHelper.TABLECostsHierarchy_Name, sharePreferencesIdE1);

        if(cursorViewE1 == null){
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            int E1Index = cursorViewE1.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);

            if (cursorViewE1.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursorViewE1.getString(E1Index));

                    textViewE1.setText(cursorViewE1.getString(E1Index));
                } while (cursorViewE1.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }



        //ListView ausgabe
        String[] columns = {DBMyHelper.COLUMNCostsHierarchy_E2};

        ListView itemList = (ListView) findViewById(R.id.listViewCostsHierarchyE1);
        int[] viewColumns = new int[]{R.id.itemCostsHierarchy};

        Cursor cursorViewList = db.viewColumnsFromCostsHierarchyForListview(DBMyHelper.COLUMNCostsHierarchy_E2);

        if (cursorViewList == null) {
            Toast.makeText(getApplicationContext(), "Fehler beim Auslesen der Datenbank.", Toast.LENGTH_LONG).show();
        }

        try{
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CostsHierarchyE1.this,
                    R.layout.costshierarchy_item_layout,
                    cursorViewList,
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


                //Speichert die "ID" die angeklickt wurde -> data/data/com.example.haushaltsbuch/shared_prefs
                SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0); //Shared Prefs Datei öffnen
                SharedPreferences.Editor editor = sharedPreferences.edit(); //Editorklasse initialisieren (um zu schreiben)
                editor.putLong("CostsHierarchyE2_ActivityPassingInfo", id); //Inhalt übergeben (Key, Value)
                editor.commit(); //Speichern
                Log.d(LOG_TAG, "ID: " + id + " wurde in Share Prefference SPContractsDetails gespeichert.");

                //Weiterleitung
                Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyE2.class);
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


    public void NavBack(View view){
        Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyOverview.class);
        startActivity(i);
        finish();
    }
}