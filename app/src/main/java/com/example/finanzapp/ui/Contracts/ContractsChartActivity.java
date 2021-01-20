package com.example.finanzapp.ui.Contracts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.finanzapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContractsChartActivity extends AppCompatActivity {



    AnyChartView anyChartView;
    // set the settings for the chart
    String[] mainCategory = {"Krankenversicherung", "KfzVersicherung", "Fitnessstudio", "Miete Wohnung"};
    int[] costs = {250, 200, 50, 750};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_chart);
        anyChartView = findViewById(R.id.any_chart_view_contracts);
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

    public void NavBackContractChartsToContractOV(View view){
        Intent i = new Intent(ContractsChartActivity.this, ContractsOverview.class);
        startActivity(i);
        finish();
    }
}
