package com.example.finanzapp.ui.financebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.CashFlow.QuickPay;
import com.example.finanzapp.ui.CostsHierarchy.CostsHierarchyOverview;

import java.util.ArrayList;
import java.util.List;


public class FinanceBookOverview extends AppCompatActivity {

    AnyChartView anyChartView;
    // set the settings for the chart
    String[] months = {"Jan", "Feb", "Mar"};
    int[] earnings = {500, 800, 2000};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_book_overview);
        anyChartView = findViewById(R.id.any_chart_view_fb);
        setupPieChart();

    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < months.length; i++) {
            dataEntries.add(new ValueDataEntry(months[i], earnings[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
    public void NavTemplatesCostHierarchy(View view){
        Intent i = new Intent(FinanceBookOverview.this, CostsHierarchyOverview.class);
        startActivity(i);

    }
    public void ShowFBChars(View view){
        Intent i = new Intent(FinanceBookOverview.this, FinanceBookCharts.class);
        startActivity(i);

    }

    public void NavBacktoHomeFB(View view){
        Intent i = new Intent(FinanceBookOverview.this, MainActivity.class);
        startActivity(i);
        finishAndRemoveTask();

    }
     //TEST-Activity
    public void NavToQuickPay(View view){
        Intent i = new Intent(FinanceBookOverview.this, QuickPay.class);
        startActivity(i);
    }
}

