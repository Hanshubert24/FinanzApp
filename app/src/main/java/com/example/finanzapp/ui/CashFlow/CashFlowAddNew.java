package com.example.finanzapp.ui.CashFlow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    String tableContentValue;
    String contractViewColumn; //Spalte die ausgegeben und gefoltert werden soll
    String assetViewColumn; //Spalte die ausgegeben und gefoltert werden soll
    String incomeViewColumn; //Spalte die ausgegeben und gefoltert werden soll
    String currentColumn; //Aktuell gewählter Spaltenwert
    int tableID;
    double doubleValuePrepared;
    String currentDate;
    int cashFlowType;
    int day;
    int month;
    int year;
    boolean setAssetButtons;

    Spinner spinnerTable;
    Spinner spinnerTableContent;

    ArrayList<String> arrayListTableContent;

    ArrayAdapter arrayAdapterTable;
    ArrayAdapter arrayAdapterTableContent;

    TextView textViewDate;

    EditText editTextDoubleValue;

    Button buttonAssetIncome; //Einzahlung/Einnahme -> trifft nur auf Assets zu
    Button buttonAssetOutput; //Auszahlung/Ausgabe  -> trifft nur auf Assets zu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow_add_new);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        cashFlowType = 0;
        setAssetButtons = false;

        spinnerTable = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTable);
        spinnerTableContent = (Spinner) findViewById(R.id.spinnerCashFlowAddNewTableContent);
        textViewDate = (TextView) findViewById(R.id.textViewCashFlowAddNewDateEntry);
        arrayListTableContent = new ArrayList<String>();
        buttonAssetIncome = (Button) findViewById(R.id.buttonAssetIncomeCashFlowAddNew);
        buttonAssetOutput = (Button) findViewById(R.id.buttonAssetOutputCashFlowAddNew);

        //AssetButtons initial ausblenden (android:visibility)
        assetButtonsVisibility(false);

        //Datum abfragen und als Hint setzen
        textViewDate.setText(DBService.timeFormatToView());
        Toast.makeText(this, "CT: "+DBService.timeFormatToView(), Toast.LENGTH_LONG).show();
        currentDate = DBService.timeFormatForDB(); //Speichert den aktuellen Wert zwischen
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
                Log.d(LOG_TAG, "SpinnerTable -> ID: " + id + ", Pos.: " + position + ", Eintrag: " + DBMyHelper.getTableListForCashFlowAddNew());
                //Reagieren auf Auswahl des Nutzers und Übergabe des entsprechenende Tabellennamen
                if(position == 0){
                    cashFlowType = 2; //Auszahlung / Ausgabe
                    tableID = DBMyHelper.TABLEContracts_TableID; //int = 0
                    contractViewColumn = DBMyHelper.COLUMNContracts_Type;
                    fillSpinnerTableContent(DBMyHelper.TABLEContracts_NAME, contractViewColumn); //füllt Spinner 2
                    currentColumn = contractViewColumn;
                    assetButtonsVisibility(false);
                    buttonAssetNeutral();

                } else if(position == 1){
                    cashFlowType = 0; //Weder noch (Zustand undediniert)
                    tableID = DBMyHelper.TABLEAssets_TableID; //int = 1
                    assetViewColumn = DBMyHelper.COLUMNAssets_Name;
                    fillSpinnerTableContent(DBMyHelper.TABLEAssets_NAME, assetViewColumn); //füllt Spinner 2
                    currentColumn = assetViewColumn;
                    assetButtonsVisibility(true);

                } else if(position == 2){
                    cashFlowType = 1; //Einzahlung / Einnahme
                    tableID = DBMyHelper.TABLEIncome_TableID; //int = 2
                    incomeViewColumn = DBMyHelper.COLUMNIncome_Company;
                    fillSpinnerTableContent(DBMyHelper.TABLEIncome_NAME, incomeViewColumn); //füllt Spinner 2
                    currentColumn = incomeViewColumn;
                    assetButtonsVisibility(false);
                    buttonAssetNeutral();

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
                //Übernimmt String aus ArrayList je nach Auswahl
                tableContentValue = arrayListTableContent.get(position);
                Log.d(LOG_TAG, "SpinnerTableContent = " + tableContentValue);

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

                //CashFlowType -> Abfangen wenn kein Button gedrückt wurde!
                if(cashFlowType != 0) {

                    //Abfrage der TableEntryID zur ausgewählten Table und dessen Entry
                    int tableEntryID = db.viewIDFromTableEntry(tableID, currentColumn, tableContentValue);

                    db.addNewCashFlowInDB(
                            currentDate, //Datum
                            cashFlowType, //1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe
                            tableID, //TableID
                            tableEntryID, //TableEntryID
                            doubleValuePrepared); //DoubleValue -> Geldwert
                    Log.d(LOG_TAG, "Eintragung in DB: Date: "+currentDate+", CashFlowType: "+cashFlowType+", TableID: "+tableID+", TableEnrtryID: "+ tableEntryID +", DoubleValue: "+doubleValuePrepared);

                } else {
                    Toast.makeText(this, "Wählen Sie 'Eingabe' oder 'Ausgabe'.", Toast.LENGTH_LONG).show();
                }
            } else { //Wenn Betrag leer (nicht gesetzt) wurde
            Toast.makeText(this, "Betrag eingeben.", Toast.LENGTH_LONG).show();
            editTextDoubleValue.setHint("Bsp.: 19.95");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage bei fillSpinnerE3()");
        }
    }

    private void assetButtonsVisibility(boolean isAssetButtonVisible){

        if(isAssetButtonVisible) {
            buttonAssetIncome.setVisibility(View.VISIBLE);
            buttonAssetOutput.setVisibility(View.VISIBLE);
        } else {
            buttonAssetIncome.setVisibility(View.INVISIBLE);
            buttonAssetOutput.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickButtonAssetIncome(View view){
        buttonAssetIncome.setTextColor(0xff99cc00); //Green
        buttonAssetOutput.setTextColor(Color.GRAY);

        cashFlowType = 1; //Einzahlung/Einkommen
    }

    public void onClickButtonAssetOutcome(View view){
        buttonAssetIncome.setTextColor(Color.GRAY);
        buttonAssetOutput.setTextColor(0xffffbb33); //Orange

        cashFlowType = 2; //Auszahlung/Ausgabe
    }

    private void buttonAssetNeutral(){
        buttonAssetIncome.setTextColor(Color.GRAY);
        buttonAssetOutput.setTextColor(Color.GRAY);
    }

    public void buttonDatePicker(View view) {
        try {
            Calendar calendar;
            DatePickerDialog dateDialog;

            calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);

            //NEED API-LEVEL-24 -> Anzeige: wir haben API-Level-22 -> wir haben aber API-Level-30 ??
            dateDialog = new DatePickerDialog(CashFlowAddNew.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int chosenYear, int chosenMonth, int chosenDay) {
                    Toast.makeText(CashFlowAddNew.this, chosenDay+"."+(chosenMonth+1)+"."+chosenYear, Toast.LENGTH_LONG).show();
                    textViewDate.setText(chosenDay + "." + (chosenMonth+1) + "." + chosenYear);
                    currentDate = chosenYear+"-"+(chosenMonth+1)+"-"+chosenDay;
                    Log.d(LOG_TAG, "Datum für Datenbank wurde auf " + currentDate + " gesetzt.");
                }
            }, day, month, year); //Setzt das Datum in dem der Kalender beim Aufrugen gesetzt wird.
            dateDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler in buttonDatePicker(View view)");
        }
    }


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