package com.example.finanzapp.ui.financebook;

import android.database.Cursor;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class FinanceBookCharts extends AppCompatActivity {

    private static final String LOG_TAG = FinanceBookCharts.class.getSimpleName();

    DBDataAccess db;

    Cursor cursor;


    AnyChartView anyChartView;
    // set the settings for the chart
    String[] mainCategory;//{"Auto", "Haus", "Essen", "Entertaiment", "Konsum"}; -> E1
    int[] costs; //{250, 200, 1000, 75, 200}; ->


    // 1. Abfrage alle namen der Kategorie 1 und den Wert , darauf ein goroup by ( theortisch dann kein Sum mehr notwendig.... ) Ergebnis sollte sein eine Spalte Nme und eine Wert
    // dann Name in mainCategory und Wert in Costs
    // einfügen und es sollte fertig sein

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financebook_charts);
        anyChartView = findViewById(R.id.any_chart_view_charts_fb_sum_category);
        setupPieChart();

    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < mainCategory.length; i++) {
            dataEntries.add(new ValueDataEntry(mainCategory[i], costs[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public void NavBackFBChartsToFB(View view){
        finish();
    }
}

