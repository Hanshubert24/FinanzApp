package com.example.finanzapp.ui.financebook;

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
import com.example.finanzapp.ui.Contracts.ContractsChartActivity;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

import java.util.ArrayList;
import java.util.List;

public class FinanceBookCharts extends AppCompatActivity {

    private static final String LOG_TAG = FinanceBookCharts.class.getSimpleName();

    DBDataAccess db;

    Cursor cursor;


    AnyChartView anyChartView;
    // set the settings for the chart
    ArrayList<String> mainCategory;//{"Auto", "Haus", "Essen", "Entertaiment", "Konsum"}; -> E1
    ArrayList<Double> costs; //{250, 200, 1000, 75, 200};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financebook_charts);
        anyChartView = findViewById(R.id.any_chart_view_charts_fb_sum_category);

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

    public void NavBackFBChartsToFB(View view){
        finish();
    }

    private void databaseQuery(){

        try {
            db.open();

            cursor = db.viewFinanceBookCharts(
                    DBMyHelper.TABLECostsHierarchy_TableID,
                    DBService.getCurrentMonthString(),
                    DBService.getCurrentYearString());

            if(cursor != null){
                if(cursor.moveToFirst()){
                    int E1ID = cursor.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1);
                    int sumValue = cursor.getColumnIndex("sumValue");
                    do{
                        mainCategory.add(cursor.getString(E1ID));
                        costs.add(cursor.getDouble(sumValue));
                    }while(cursor.moveToNext());
                }
            } else {
                Log.d(LOG_TAG, "Cursor == NULL.");
            }

            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Fehler in databaseQuery().");
            db.close();
        }
    }
}

