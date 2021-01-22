package com.example.finanzapp.ui.CostsHierarchy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBInformationObject;
import com.example.finanzapp.ui.DB.DBMyHelper;

public class CostsHierarchyE1 extends AppCompatActivity {

    private static final String LOG_TAG = CostsHierarchyE1.class.getSimpleName();

    DBDataAccess db;
    DBInformationObject dbInfo;
    int sharePreferencesIdE1;
    View dialog_addnewentry_pupup;

    TextView textViewE1;
    String E1value;
    AlertDialog.Builder dialogPopUPTest;

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
            if (cursorViewE1.moveToFirst()) {
                do {
                    int E1Index = cursorViewE1.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);
                    E1value = cursorViewE1.getString(E1Index);
                    Log.d(LOG_TAG, "Für E1 wurde '" + cursorViewE1.getString(E1Index) + "' übernommen.");

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
        Cursor cursorViewList = db.viewColumnsFromCostsHierarchyE1ForListview(E1value, DBMyHelper.COLUMNCostsHierarchy_E2);

        if (DBMyHelper.COLUMNCostsHierarchy_E2 == "r23r"){
            itemList.setBackgroundColor(0xffffbb33);
            itemList.setClickable(false);
        }
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
            adapter.getItem(0);
            adapter.getCursorToStringConverter();


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


    public void NavBack(View view){
        Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyOverview.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    public void deleteE1(View view){
        showDialogDeleteE1();
    }

    public void showDialogDeleteE1(){
        AlertDialog.Builder dialogPopUp = new AlertDialog.Builder(CostsHierarchyE1.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_delete_popup = inflater.inflate(R.layout.dialog_delete_popup, null);

        dialogPopUp.setView(dialog_delete_popup);
        dialogPopUp.show();

    }

    public void dialogCancelButton(View view){
        Toast.makeText(this, "Vorgang abgebrochen", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyE1.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    public void dialogDeleteButton(View view){
        //Löscht alle Einträge in E1 mit der übergebenen Bezeichnung
        boolean success = db.deleteMultipleEntriesInTable(DBMyHelper.TABLECostsHierarchy_Name, DBMyHelper.COLUMNCostsHierarchy_E1, E1value);

        if(success) {
            Toast.makeText(this, "Hauptkategorie gelöscht", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyOverview.class);
            finishAndRemoveTask();
            startActivity(i);

        } else {
            Toast.makeText(this, "Datenbankfehler", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewE2(View view) {showDialogAddNewHierarchy();}

    public void showDialogAddNewHierarchy(){
        dialogPopUPTest = new AlertDialog.Builder(CostsHierarchyE1.this);

        LayoutInflater inflater = this.getLayoutInflater();
        dialog_addnewentry_pupup = inflater.inflate(R.layout.dialog_addnewentry_e1_popup, null);
        dialogPopUPTest.setCancelable(false);
        dialogPopUPTest.setView(dialog_addnewentry_pupup);


        dialogPopUPTest.show();
        }



        public void dialogSaveButton(View view) {

        try {
            //Muss den Wert (findViewById()) über den View des PopUp-Layouts übernehmen
            //Bei direktem Aufruf = NULL
            EditText editTextEntryE2 = (EditText) dialog_addnewentry_pupup.findViewById(R.id.editTextCostsHierarchyAddEntryDialog);

            Log.d(LOG_TAG, editTextEntryE2.getText().toString() + " -> wurde für E2 eingegeben.");

            if (editTextEntryE2 != null) {
                if (isEditTextEmpty(editTextEntryE2)) {
                    Toast.makeText(this, "Geben Sie einen Wert ein.", Toast.LENGTH_SHORT).show();

                } else {
                    dbInfo = db.CostsHierarchyInDB(
                            E1value,
                            editTextEntryE2.getText().toString()
                            , null);

                    Toast.makeText(this, dbInfo.getMassage(), Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Rückmeldung DB: " + dbInfo.getMassage());

                    if (dbInfo.isSuccess()) {
                        Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyE1.class);
                        finishAndRemoveTask();
                        startActivity(i);
                    }
                }
            } else {
                Log.d(LOG_TAG, "Es wurde ein NULL-Wert als Eingabe übergeben.");
                Toast.makeText(this, "Geben Sie einen Wert ein.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dialogCancelButton2(View view){
        Intent i = new Intent(CostsHierarchyE1.this, CostsHierarchyE1.class);
        finishAndRemoveTask();
        startActivity(i);
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else {return true;}
    }
}