package com.example.finanzapp.ui.CashFlow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

import java.util.ArrayList;
import java.util.Calendar;

public class CashFlowAddNew extends AppCompatActivity {

    //Info-Date -> https://www.sqlite.org/lang_datefunc.html

    private static final String LOG_TAG = CashFlowAddNew.class.getSimpleName();

    DBDataAccess db;
    Cursor cursorTableContent;

    String valueTable;
    double doubleValuePrepared;
    String currentDate;

    Spinner spinnerTable;
    Spinner spinnerTableContent;

    ArrayList<String> arrayListTableContent;

    ArrayAdapter arrayAdapterTable;
    ArrayAdapter arrayAdapterTableContent;

    TextView textViewDate;

    EditText editTextDoubleValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow_add_new);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        spinnerTable = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTable);
        spinnerTableContent = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTableContent);

        arrayListTableContent = new ArrayList<String>();

        //Datum abfragen und als Hint setzen
        //textViewDate.setText(DBService.timeFormatToView());
        Toast.makeText(this, "CT: "+DBService.timeFormatToView(), Toast.LENGTH_SHORT).show();
        currentDate = DBService.timeFormatToView(); //Speichert den aktuellen Wert zwischen
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        fillSpinnerTable();
        //Reagieren auf Spinner-Auswahl
        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "SpinnerTable -> ID: " + id + ", Pos.: " + position + "");
                //Reagieren auf Auswahl des Nutzers und Übergabe des entsprechenende Tabellennamen
                if(position == 0){
                    fillSpinnerTableContent(DBMyHelper.TABLEContracts_NAME, DBMyHelper.COLUMNContracts_Type);
                } else if(position == 1){
                    fillSpinnerTableContent(DBMyHelper.TABLEAssets_NAME, DBMyHelper.COLUMNAssets_Name);
                } else if(position == 2){
                    fillSpinnerTableContent(DBMyHelper.TABLEIncome_NAME, DBMyHelper.COLUMNIncome_Company);
                } else {
                    Log.d(LOG_TAG, "Fehler Auswahl spinnerTable.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fillSpinnerTableContent(DBMyHelper.TABLEContracts_NAME, DBMyHelper.COLUMNContracts_Type);
            }
        });

        //Reagieren auf Spinner-Auswahl
        spinnerTableContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillSpinnerTable(){
        arrayAdapterTable = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DBMyHelper.getTableListForCashFlowAddNew());
        spinnerTable.setAdapter(arrayAdapterTable);
    }

    private void fillSpinnerTableContent(String table, String columnName) {
        try {
            arrayListTableContent.clear();
            cursorTableContent = db.viewAllInTable(table);

            //Ausgeben der Informationen die für den Spinner benötigt werden.
            if (cursorTableContent != null) {
                if (cursorTableContent.moveToFirst()) {
                    int columnIndex = cursorTableContent.getColumnIndex(columnName);
                    do {
                        Log.d(LOG_TAG, "Eintrag in cursorTableContent -> ColumnIndex: " + columnIndex + ", Eintrag: " + cursorTableContent.getString(columnIndex) + " Rest (Count): " + cursorTableContent.getCount());
                        //Auslesen des Cursoreintrags und speichern in das Array
                        arrayListTableContent.add(cursorTableContent.getString(columnIndex));
                    } while (cursorTableContent.moveToNext());
                }
                arrayAdapterTableContent = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListTableContent);

                spinnerTableContent.setAdapter(arrayAdapterTableContent);

            } else {
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerTable()");
                Toast.makeText(this, "Datenbank-Abfragefehler", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Absturz in fillSpinnerTableContent().");
        }
    }

    public void SaveEntry(View view){
        try{

            editTextDoubleValue = (EditText) findViewById(R.id.editTextCashFlowAddNewValue);
            //Prüfen ob ein Eintrag für den Wert gemacht wurde
            if(!isEditTextEmpty(editTextDoubleValue)){
                double doubleValue = Double.parseDouble(editTextDoubleValue.getText().toString());
                doubleValuePrepared = DBService.doubleValueForDB(doubleValue);

/*
                db.addNewCashFlowInDB(
                        checkDate(),

                );

 */



            } else { //Wenn Betrag leer (nicht gesetzt) wurde
            Toast.makeText(this, "Betrag eingeben.", Toast.LENGTH_SHORT).show();
            editTextDoubleValue.setHint("Bsp.: 19.95");
        }







        } catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE3()");
        }
    }

    private String checkDate() {
        //textViewDate = (EditText) findViewById(R.id.textViewCashFlowAddNewDate);
/*
        //Prüfen ob Daten gesetzt wurde
        if(isEditTextEmpty(textViewDate)) { //Wenn kein Datum gesetzt wurde
            return DBService.timeFormatForDB();


        } else {
            Toast.makeText(this, "Aktuelle Daten wird für den Eintrag gesetzt", Toast.LENGTH_SHORT).show();


            return "EingabeDatum";
        }
 */
        return "";
    }

/*
    public void buttonDatePicker(View view){
        Calendar calendar;
        DatePickerDialog dateDialog;

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        //NEED API LEVEL 24!
        dateDialog = new DatePickerDialog(CashFlowAddNew.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textViewDate.setText(dayOfMonth + "." + month + "." + year);
            }
        }, day, month, year);
        dateDialog.show();


    }

 */



    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBackCashFlowAddNewToFinancBookOV(View view){
        Intent i = new Intent(CashFlowAddNew.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }
}