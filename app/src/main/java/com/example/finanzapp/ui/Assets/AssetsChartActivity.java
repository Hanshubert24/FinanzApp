package com.example.finanzapp.ui.Assets;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;

import java.util.ArrayList;
import java.util.List;

public class AssetsChartActivity extends AppCompatActivity {

    private static final String LOG_TAG = AssetsChartActivity.class.getSimpleName();

    DBDataAccess db;

    TextView textViewAssetsValue;
    TextView textViewCredit;
    TextView textViewAsset;

    AnyChartView anyChartView;

    double sumFAm, sumC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_chart);
        anyChartView = findViewById(R.id.any_chart_assets_Chart);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewAssetsValue = (TextView) findViewById(R.id.textviewAssetsCahrtAssetsvalue);
        textViewCredit = (TextView) findViewById(R.id.textviewAssetsCahrtCredit);
        textViewAsset = (TextView) findViewById(R.id.textviewAssetsCahrtAsset);

        double assetcalc;
        assetcalc = sumFAm - sumC;
        textViewAssetsValue.setText(sumFAm+"€");
        textViewCredit.setText(sumC+"€");
        textViewAsset.setText(Double.toString(assetcalc));

        //Abfrage der Datenbank
        databaseQuery();

        setupPieChart();
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
        dataEntries.add(new ValueDataEntry("Kredite",sumC ));
        dataEntries.add(new ValueDataEntry("Verkehrswert",sumFAm ));

        pie.data(dataEntries);

        pie.labels().position("outside");

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }

    public void NavBackAssetsChartsToAssetsOV(View view){

        finish();
    }

    private void databaseQuery(){
        try {
            db.open();

            Cursor cursor = db.viewAssetsChartActicity();

            if(cursor != null){
                if(cursor.moveToFirst()){
                    int financialAssetsID = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_FinancialAsset);
                    int credeitID = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Credit);

                        sumFAm = cursor.getDouble(financialAssetsID);
                        sumC = cursor.getDouble(credeitID);

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

