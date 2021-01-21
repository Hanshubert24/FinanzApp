package com.example.finanzapp.ui.Contracts;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.CashFlow.CashFlowAddNew;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

import java.util.ArrayList;
import java.util.List;

public class ContractsChartActivity extends AppCompatActivity {

    private static final String LOG_TAG = ContractsChartActivity.class.getSimpleName();

    DBDataAccess db;

    Cursor cursor;

    AnyChartView anyChartView;
    // set the settings for the chart
    ArrayList<String> mainCategory; //{"Krankenversicherung", "KfzVersicherung", "Fitnessstudio", "Miete Wohnung"}; -> Type
    ArrayList<Double> costs; //{250, 200, 50, 750}; -> MonthlyCosts

    // 1. Abfrage alle namen der Kategorie 1 und den Wert , darauf ein goroup by ( theortisch dann kein Sum mehr notwendig.... ) Ergebnis sollte sein eine Spalte Nme und eine Wert
    // dann Name in mainCategory und Wert in Costs
    // einfügen und es sollte fertig sein




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_chart);
        anyChartView = findViewById(R.id.any_chart_view_contracts);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        mainCategory = new ArrayList<String>();
        costs = new ArrayList<Double>();

        //Abfrage der Datenbank
        databaseQuery();

        setupPieChart();
    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < mainCategory.size(); i++) {
            dataEntries.add(new ValueDataEntry(mainCategory.get(i), costs.get(i)));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public void NavBackContractChartsToContractOV(View view){
        finish();
    }

    private void databaseQuery(){

        try {
            db.open();

            cursor = db.viewAllInTable(DBMyHelper.TABLEContracts_NAME);

            if(cursor != null){
                if(cursor.moveToFirst()){
                    int typeID = cursor.getColumnIndex(DBMyHelper.COLUMNContracts_Type);
                    int monthlyCostsID = cursor.getColumnIndex(DBMyHelper.COLUMNContracts_MonthlyCosts);
                    do{
                        mainCategory.add(cursor.getString(typeID));
                        costs.add(cursor.getDouble(monthlyCostsID));
                    }while(cursor.moveToNext());
                }
            } else {
                Log.d(LOG_TAG, "Cursor == NULL.");
            }

            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler in databaseQuery().");
        }
    }
}

