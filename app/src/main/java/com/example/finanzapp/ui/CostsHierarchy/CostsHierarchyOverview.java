package com.example.finanzapp.ui.CostsHierarchy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBInformationObject;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.financebook.FinanceBookOverview;

import java.util.ArrayList;

public class CostsHierarchyOverview extends AppCompatActivity {

    private static final String LOG_TAG = CostsHierarchyOverview.class.getSimpleName();

    DBDataAccess db;
    DBInformationObject dbInfo;

    ListView listView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;

    View dialog_addnewentry_pupup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_hierarchy_overview);

        db = new DBDataAccess(getApplicationContext());
        dbInfo = new DBInformationObject();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        String[] columns = {DBMyHelper.COLUMNCostsHierarchy_E1};

        ListView itemList = (ListView) findViewById(R.id.listViewCostsHierarchyOverview);
        int[] viewColumns = new int[]{R.id.itemCostsHierarchy};

        Cursor cursor = db.viewColumnsFromCostsHierarchyOverviewForListview(DBMyHelper.COLUMNCostsHierarchy_E1);

        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "Fehler beim Auslesen der Datenbank.", Toast.LENGTH_LONG).show();
        }

        try{
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CostsHierarchyOverview.this,
                    R.layout.costshierarchy_item_layout,
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


                //Speichert die "ID" die angeklickt wurde -> data/data/com.example.haushaltsbuch/shared_prefs
                SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0); //Shared Prefs Datei öffnen
                SharedPreferences.Editor editor = sharedPreferences.edit(); //Editorklasse initialisieren (um zu schreiben)
                editor.putLong("CostsHierarchyE1_ActivityPassingInfo", id); //Inhalt übergeben (Key, Value)
                editor.commit(); //Speichern
                Log.d(LOG_TAG, "ID: " + id + " wurde in Share Prefference SPContractsDetails gespeichert.");

                //Weiterleitung
                Intent i = new Intent(CostsHierarchyOverview.this, CostsHierarchyE1.class);
                finishAndRemoveTask();
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

    public void addNewE1(View view) {showDialogAddNewHierarchy();}

    public void showDialogAddNewHierarchy(){
        AlertDialog.Builder dialogPopUP = new AlertDialog.Builder(CostsHierarchyOverview.this);

        LayoutInflater inflater = this.getLayoutInflater();
        dialog_addnewentry_pupup = inflater.inflate(R.layout.dialog_addnewentry_overview_popup, null);

        dialogPopUP.setView(dialog_addnewentry_pupup);
        dialogPopUP.show();
        dialogPopUP.setCancelable(true);
    }

    public void dialogSaveButton(View view){

        try {
            //Muss den Wert (findViewById()) über den View des PopUp-Layouts übernehmen
                //Bei direktem Aufruf = NULL
            EditText editTextEntryE1 = (EditText) dialog_addnewentry_pupup.findViewById(R.id.editTextCostsHierarchyAddEntryDialog);

            Log.d(LOG_TAG, editTextEntryE1.getText().toString() + " -> wurde für E1 eingegeben.");

            if (editTextEntryE1 != null) {
                if(isEditTextEmpty(editTextEntryE1)) {
                    Toast.makeText(this, "Geben Sie einen Wert ein.", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, dbInfo.getMassage());

                } else {

                    dbInfo = db.CostsHierarchyInDB(editTextEntryE1.getText().toString(), null, null);

                    Toast.makeText(this, dbInfo.getMassage(), Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Rückmeldung DB: " + dbInfo.getMassage());

                    if(dbInfo.isSuccess()) {
                        Intent i = new Intent(CostsHierarchyOverview.this, CostsHierarchyOverview.class);
                        finishAndRemoveTask();
                        startActivity(i);
                    }
                }
            } else {
                Log.d(LOG_TAG, "Es wurde ein NULL-Wert als Eingabe übergeben.");
                Toast.makeText(this, "Geben Sie einen Wert ein.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dialogChancelButton(View view){
        Toast.makeText(this, "Vorgang abgebrochen", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CostsHierarchyOverview.this, CostsHierarchyOverview.class);
        finishAndRemoveTask();
        startActivity(i);
    }
    public void costhierachyOverviewButton(View view){
     finish();
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else {return true;}
    }

    public void NavBackCostHierarchieOV(View view){
        Intent i = new Intent(CostsHierarchyOverview.this, FinanceBookOverview.class);
        finishAndRemoveTask();
        startActivity(i);

    }

}