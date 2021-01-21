package com.example.finanzapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.finanzapp.ui.CashFlow.QuickPay;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBService;
import com.example.finanzapp.ui.Service.DateService;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    DBDataAccess db;

    double costsCurrent, incomeCurrent, costsMinusOne, incomeMinusOne, costsMinusTwo, incomeMinusTwo;

    String currentMonthString, currentMonthMinusOneString, currentMonthMinusTwoString;
    String currentMonthNumberString, currentYearNumberString;
    String currentMonthMinusOneNumberString, currentYearMinusOneNumberString;
    String currentMonthMinusTwoNumberString, currentYearMinusTwoNumberString;

    TextView textViewCashFlowCurrentMonth;
    TextView textViewCashFlowCurrentMonthMinusOne;
    TextView textViewCashFlowCurrentMonthMinusTwo;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Initialisieren der Datenabank
        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getActivity());

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Intent intent = new Intent(getActivity(), QuickPay.class);
        final Button button = (Button) root.findViewById(R.id.AddButtonHome);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        AnyChartView anyChartView = root.findViewById(R.id.any_chart_assets_Chart);
        anyChartView.setProgressBar(root.findViewById(R.id.progressBarHome));


        //Abfrage Datumsangaben und Umwandlung der Monatsintegers in die Bezeichnungen für Grafik-Achse
        currentMonthNumberString = DBService.getCurrentMonthString();
        currentYearNumberString = DBService.getCurrentYearString();

        currentMonthString = DateService.getMonthName(DBService.getCurrentMonthInteger());

        if (DBService.getCurrentMonthInteger() <= 1) { //Januar -> Dezember(Year-1) -> November(Year-1)
            currentMonthMinusOneNumberString = "12";
            currentYearMinusOneNumberString = DBService.getLastYearString(1);
            currentMonthMinusOneString = DateService.getMonthName(12);

            currentMonthMinusTwoNumberString = "11";
            currentYearMinusTwoNumberString = DBService.getLastYearString(1);
            currentMonthMinusTwoString = DateService.getMonthName(11);

        } else if (DBService.getCurrentMonthInteger() <= 2) { //Februar -> Januar -> Dezember(Year-1)
            currentMonthMinusOneNumberString = "01";
            currentYearMinusOneNumberString = DBService.getLastYearString(0);
            currentMonthMinusOneString = DateService.getMonthName(1);

            currentMonthMinusTwoNumberString = "12";
            currentYearMinusTwoNumberString = DBService.getLastYearString(1);
            currentMonthMinusTwoString = DateService.getMonthName(12);

        } else {  //März - Dezember
            currentMonthMinusOneNumberString = DBService.getLastMonthString(1);
            currentYearMinusOneNumberString = DBService.getLastYearString(0);
            currentMonthMinusOneString = DateService.getMonthName(DBService.getCurrentMonthInteger() - 1);

            currentMonthMinusTwoNumberString = DBService.getLastMonthString(2);
            currentYearMinusTwoNumberString = DBService.getLastYearString(0);
            currentMonthMinusTwoString = DateService.getMonthName(DBService.getCurrentMonthInteger() - 2);
        }

        //Abfrage der Datenbank zum befüllen des Diagramms
        databaseQuery();


        // set cash flow numbers for textview

        textViewCashFlowCurrentMonth = (TextView) root.findViewById(R.id.textViewFragmentHomeCashFlow1);
        textViewCashFlowCurrentMonthMinusOne = (TextView) root.findViewById(R.id.textViewFragmentHomeCashFlow2);
        textViewCashFlowCurrentMonthMinusTwo = (TextView) root.findViewById(R.id.textViewFragmentHomeCashFlow3);

        try {
            // implement current value
            double cashFlowCurrentMonth;
            double cashFlowCurrentMonthMinusOne;
            double cashFlowCurrentMonthMinusTwo;

            cashFlowCurrentMonth = incomeCurrent - costsCurrent;
            cashFlowCurrentMonthMinusOne = incomeMinusOne - costsMinusOne;
            cashFlowCurrentMonthMinusTwo = incomeMinusTwo - costsMinusTwo;

            // current
            textViewCashFlowCurrentMonth.setText("Cashflow: "+Double.toString(cashFlowCurrentMonth)+"€");
            if (cashFlowCurrentMonth >= 0){
                textViewCashFlowCurrentMonth.setTextColor(0xff99cc00);
            }
            else{
                textViewCashFlowCurrentMonth.setTextColor(0xffffbb33);
            }
            // current minus one
            textViewCashFlowCurrentMonthMinusOne.setText("Cashflow: "+Double.toString(cashFlowCurrentMonthMinusOne)+"€");
            if (cashFlowCurrentMonthMinusOne >= 0){
                textViewCashFlowCurrentMonthMinusOne.setTextColor(0xff99cc00);
            }
            else{
                textViewCashFlowCurrentMonthMinusOne.setTextColor(0xffffbb33);
            }
            // current minus two
            textViewCashFlowCurrentMonthMinusTwo.setText("Cashflow: "+Double.toString(cashFlowCurrentMonthMinusTwo)+"€");
            if (cashFlowCurrentMonthMinusTwo >= 0){
                textViewCashFlowCurrentMonthMinusTwo.setTextColor(0xff99cc00);
            }
            else{
                textViewCashFlowCurrentMonthMinusTwo.setTextColor(0xffffbb33);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        Cartesian3d bar3d = AnyChart.bar3d();

        bar3d.animation(true);

        bar3d.padding(10d, 40d, 5d, 20d);

        //bar3d.title("Einnahmen und Ausgaben");

        bar3d.yScale().minimum(0d);

        bar3d.xAxis(0).labels()
                .rotation(-90d)
                .padding(0d, 0d, 20d, 0d);

        bar3d.yAxis(0).labels().format("€{%Value}{groupsSeparator: }");

        bar3d.yAxis(0).title(" ");

        List<DataEntry> data = new ArrayList<>();
        data.add(new HomeFragment.CustomDataEntry(currentMonthMinusTwoString, incomeMinusTwo, costsMinusTwo));   // 2 Monate zurück
        data.add(new HomeFragment.CustomDataEntry(currentMonthMinusOneString, incomeMinusOne, costsMinusOne)); // 1 Monat zurück
        data.add(new HomeFragment.CustomDataEntry(currentMonthString, incomeCurrent, costsCurrent)); // Aktueller Monat

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


        return root;


    }

    class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);

        }
    }

    private void databaseQuery(){
        //Abfrage der Datenbank -> Summe für alle Einnahmen im aktuellen Monat
        Log.d(LOG_TAG, "Die Datenquelle wird geöffent.");
        db.open();

        incomeCurrent = db.viewFinanceBookOverview(1, currentMonthNumberString, currentYearNumberString);
        //TEST
        Log.d(LOG_TAG, "übergebener Monat incomeCurrent: "+currentMonthNumberString+ ", Jahr: "+currentYearNumberString);
        if(incomeCurrent == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für icomeCurrent.");
        }

        incomeMinusOne = db.viewFinanceBookOverview(1, currentMonthMinusOneNumberString, currentYearMinusOneNumberString);
        //TEST
        Log.d(LOG_TAG, "übergebener Monat incomeMinusOne: "+currentMonthMinusOneNumberString+ ", Jahr: "+currentYearMinusOneNumberString);
        if(incomeMinusOne == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für icomeMinusOne.");
        }

        incomeMinusTwo = db.viewFinanceBookOverview(1, currentMonthMinusTwoNumberString, currentYearMinusTwoNumberString);
        //TEST
        Log.d(LOG_TAG, "übergebener Monat incomeMinusTwo: "+currentMonthMinusTwoNumberString+ ", Jahr: "+currentYearMinusTwoNumberString);
        if(incomeMinusTwo == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für icomeMinusTwo.");
        }


        //Abfrage der Datenbank -> Summe für alle Ausgaben im aktuellen Monat
        costsCurrent = db.viewFinanceBookOverview(2, currentMonthNumberString, currentYearNumberString);
        if(costsCurrent == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für costsCurrent.");
        }

        costsMinusOne = db.viewFinanceBookOverview(2, currentMonthMinusOneNumberString, currentYearMinusOneNumberString);
        if(costsMinusOne == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für costsMinusOne.");
        }

        costsMinusTwo = db.viewFinanceBookOverview(2, currentMonthMinusTwoNumberString, currentYearMinusTwoNumberString);
        if(costsMinusTwo == -1){
            Log.d(LOG_TAG, "Fehler in der Datenbankabfrage für costsMinusTwo.");
        }

        db.close();
        Log.d(LOG_TAG, "Die Datenquelle wurde geschlossen.");
    }
}

