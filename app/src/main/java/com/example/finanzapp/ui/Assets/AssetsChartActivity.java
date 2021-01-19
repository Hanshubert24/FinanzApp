package com.example.finanzapp.ui.Assets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.finanzapp.R;

import java.util.ArrayList;
import java.util.List;

public class AssetsChartActivity extends AppCompatActivity {

    TextView textViewAssetsValue;
    TextView textViewCredit;
    TextView textViewAsset;

    AnyChartView anyChartView;
    // set the settings for the chart
    String[] type = {"Vermögen", "Kredite"};
    int[] earnings = {20000, 80000};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_chart);
        anyChartView = findViewById(R.id.any_chart_assets_Chart);
        setupPieChart();


        textViewAssetsValue = (TextView) findViewById(R.id.textviewAssetsCahrtAssetsvalue);
        textViewCredit = (TextView) findViewById(R.id.textviewAssetsCahrtCredit);
        textViewAsset = (TextView) findViewById(R.id.textviewAssetsCahrtAsset);

        textViewAssetsValue.setText("100000€");
        textViewCredit.setText("80000€");
        textViewAsset.setText("20000€");
    }


    public void setupPieChart() {
        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(AssetsChartActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        pie.title("Vermögensübersicht");

        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < type.length; i++) {
            dataEntries.add(new ValueDataEntry(type[i], earnings[i]));
        }

        pie.data(dataEntries);

        pie.labels().position("outside");

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }

    public void NavBackAssetsChartsToAssetsOV(View view){
        Intent i = new Intent(AssetsChartActivity.this, AssetsOverview.class);
        startActivity(i);
        finish();
    }



}

