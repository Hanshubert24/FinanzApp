package com.example.finanzapp.ui.CashFlow;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

import java.util.ArrayList;

public class QuickPay extends AppCompatActivity {

    //Info-Date -> https://www.sqlite.org/lang_datefunc.html

    private static final String LOG_TAG = QuickPay.class.getSimpleName();

    DBDataAccess db;
    Cursor cursorE1;
    Cursor cursorE2;
    Cursor cursorE3;

    String valueE1;
    String valueE2;
    String valueE3;
    double doubleValuePrepared;

    Spinner spinnerE1;
    Spinner spinnerE2;
    Spinner spinnerE3;

    ArrayList<String> arrayListE1;
    ArrayList<String> arrayListE2;
    ArrayList<String> arrayListE3;
    ArrayAdapter<CharSequence> arrayAdapterE1;
    ArrayAdapter<CharSequence> arrayAdapterE2;
    ArrayAdapter<CharSequence> arrayAdapterE3;

    EditText editTextDoubleValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_pay);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        spinnerE1 = (Spinner) findViewById(R.id.spinnerQuickPayE1);
        spinnerE2 = (Spinner) findViewById(R.id.spinnerQuickPayE2);
        spinnerE3 = (Spinner) findViewById(R.id.spinnerQuickPayE3);

        arrayListE1 = new ArrayList<String>();
        arrayListE2 = new ArrayList<String>();
        arrayListE3 = new ArrayList<String>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        fillSpinnerE1();
        //reagieren auf SpinnerE1
        spinnerE1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(QuickPay.this, "ID: " + id + " Pos: " + position, Toast.LENGTH_SHORT).show();
                valueE1 = arrayListE1.get(position);
                Log.d(LOG_TAG, "SpinnerE1 -> ID: " + id + " Pos: " + position + " String: " + valueE1);

                fillSpinnerE2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //reagieren auf SpinnerE2
        spinnerE2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueE2 = arrayListE2.get(position);
                Log.d(LOG_TAG, "SpinnerE2 -> ID: " + id + " Pos: " + position + " String: " + valueE2);

                fillSpinnerE3();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //reagieren auf SpinnerE3
        spinnerE3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueE3 = arrayListE3.get(position);
                Log.d(LOG_TAG, "SpinnerE3 -> ID: " + id + " Pos: " + position + " String: " + valueE3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fillSpinnerE1(){
        try{
            cursorE1 = db.viewColumnsFromCostsHierarchyOverviewForListview(DBMyHelper.COLUMNCostsHierarchy_E1);

            //E1 aus CostsHierarchy auslesen für SpinnerE1
            if(cursorE1 != null){
                if(cursorE1.moveToFirst()){
                    int E1Index = cursorE1.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);
                    do{
                        //Auslesen der Datenbankeinträge und speichern in der ArrayList<String>
                        arrayListE1.add(cursorE1.getString(E1Index));
                    } while (cursorE1.moveToNext());
                }

                arrayAdapterE1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListE1);

                spinnerE1.setAdapter(arrayAdapterE1);

            } else {
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE1()");
                Toast.makeText(this, "Datenbank-Abfragefehler", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE1()");
        }
    }

    public void fillSpinnerE2(){
        try{
            arrayListE2.clear(); //leert die aktuelle Liste -> auch bei Änderung von SpinnerE1
            //cursor = Inhalt Unterkategorie von gewählten E1 und GroupBy nach Werte in E2 (Unterkategorie)
            cursorE2 = db.viewColumnsFromCostsHierarchyE1ForListview(valueE1, DBMyHelper.COLUMNCostsHierarchy_E2);

            //E2 auslesen für Spinner 2
            if(cursorE2 != null){
                if(cursorE2.moveToFirst()){
                    int E2Index = cursorE2.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E2);
                    do{
                        //Auslesen des Eintrags und apeichern in die ArrayList
                        arrayListE2.add(cursorE2.getString(E2Index));
                    } while(cursorE2.moveToNext());
                }

                arrayAdapterE2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListE2);

                spinnerE2.setAdapter(arrayAdapterE2);

            } else {
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE2()");
                Toast.makeText(this, "Datenbank-Abfragefehler", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE2()");
        }
    }

    public void fillSpinnerE3(){
        try{
            arrayListE3.clear(); //leert die aktuelle Liste -> auch bei Änderung von SpinnerE2
            //cursor = Inhalt Subkategorie von gewählten E1 und E2 und GroupBy nach Werte in E3
            cursorE3 = db.viewColumnsFromCostsHierarchyE2ForListview(valueE1, valueE2);

            //E3 auslesen für SpinnerE3
            if(cursorE3 != null){
                if(cursorE3.moveToFirst()){
                    int E3Index = cursorE3.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E3);
                    do{
                        //Auslesen des Eintrags und apeichern in die ArrayList
                        arrayListE3.add(cursorE3.getString(E3Index));
                    } while(cursorE3.moveToNext());
                }

                arrayAdapterE3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListE3);

                spinnerE3.setAdapter(arrayAdapterE3);

            } else {
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE3()");
                Toast.makeText(this, "Datenbank-Abfragefehler", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE3()");
        }
    }

    public void SaveEntry(View view){
        double doubleValuePrepare;

        try{
            editTextDoubleValue = (EditText) findViewById(R.id.editTextQuickPayDoubleValue);

            //Prüfen ob der Betrag eingegeben wurde
            if(!isEditTextEmpty(editTextDoubleValue)){ //Wenn Betrag gesetzt wurde
                double doubleValue = Double.parseDouble(editTextDoubleValue.getText().toString());
                doubleValuePrepare = DBService.doubleValueForDB(doubleValue);

                //Abfrage der ID zu den Werten E1, E2 und E3 in CostsHierarchy
                int tableEntryID = db.viewIDFromCostsHierarchyEntry(valueE1, valueE2, valueE3);

                if(tableEntryID >= 0) {
                    //Speichern der Werte in die Datenbank
                    boolean success = db.addNewCashFlowInDB(
                            DBService.timeFormatForDB(),
                            2, //Auszahlung
                            DBMyHelper.TABLECostsHierarchy_TableID,
                            tableEntryID,
                            doubleValuePrepare);

                    Log.d(LOG_TAG, "Inhalt -> Date: "+DBService.timeFormatForDB()+", TypeID: "+2+", TableID: "+DBMyHelper.TABLECashFlow_TableID+", TableEntryID: "+tableEntryID+", DoubleValue: "+doubleValuePrepare);

                    if(success){
                        Log.d(LOG_TAG, "Datensatz wurden in Datenbank übernommen.");
                        Toast.makeText(this, "Eintrag gespeichert", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.d(LOG_TAG, "Fehler beim Eintragen des Datensatzes in die Datenbank (SaveEntry()).");
                        Toast.makeText(this, "Fehler beim Speichern.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(LOG_TAG, "Abfragefehler in der Datenbank -> db.viewIDFromCostsHierarchyEntry().");
                }
            } else { //Wenn Betrag leer (nicht gesetzt) wurde
                Toast.makeText(this, "Betrag eingeben.", Toast.LENGTH_SHORT).show();
                editTextDoubleValue.setHint("Bsp.: 19.95");
            }

        } catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE3()");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBack(View view){

        finish();
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }

}