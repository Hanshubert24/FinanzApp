package com.example.finanzapp.ui.financebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.CashFlow.CashFlowAddNew;
import com.example.finanzapp.ui.CostsHierarchy.CostsHierarchyOverview;

import java.util.ArrayList;
import java.util.List;


public class FinanceBookOverview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_book_overview);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view_fb_current_month);
        anyChartView.setProgressBar(findViewById(R.id.ProgressBarFBOverview));

        Cartesian3d bar3d = AnyChart.bar3d();

        bar3d.animation(true);

        bar3d.padding(10d, 40d, 5d, 15d);

        bar3d.yScale().minimum(0d);

        bar3d.xAxis(0).labels()
                .rotation(-90d)
                .padding(0d, 0d, 20d, 0d);

        bar3d.yAxis(0).labels().format("€{%Value}{groupsSeparator: }");



        List<DataEntry> data = new ArrayList<>();
        data.add(new FinanceBookOverview.CustomDataEntry("Januar", 4376, 890));   // current month


        Set set = Set.instantiate();
        set.data(data);
        Mapping bar1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping bar2Data = set.mapAs("{ x: 'x', value: 'value2' }");


        bar3d.bar(bar1Data)
                .name("Einnahmen").color("#1fa900");


        bar3d.bar(bar2Data)
                .name("Ausgaben").color("#bb0000");



        bar3d.legend().enabled(true);
        bar3d.legend().fontSize(13d);
        bar3d.legend().padding(0d, 0d, 20d, 0d);

        bar3d.interactivity().hoverMode(HoverMode.SINGLE);

        bar3d.tooltip()
                .positionMode(TooltipPositionMode.POINT)
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(0d)
                .format("€{%Value}");

        bar3d.zAspect("10%")
                .zPadding(10d)
                .zAngle(45d)
                .zDistribution(true);

        anyChartView.setChart(bar3d);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);

        }


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
     finishAndRemoveTask();
    }
     //TEST-Activity
    public void NavCashFlowAddNew(View view){
        Intent i = new Intent(FinanceBookOverview.this, CashFlowAddNew.class);
        startActivity(i);
    }
}

