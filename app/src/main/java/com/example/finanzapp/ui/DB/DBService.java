package com.example.finanzapp.ui.DB;

import android.content.SharedPreferences;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DBService {

    public static double doubleValueForDB(double d){
        //Genau 2 Nachkommastellen und Datenbank nicht mit zu viel unnötigen Daten zu belasten

        //Formatierungen:
        //0 = steht für eine auf jeden Fall anzuzeigende Ziffer innerhalb der darzustellenden Zahl; ist die Stelle leer, wird eine '0' dargestellt
        //# = steht für eine Ziffer innerhalb der darzustellenden Zahl; ist die Stelle leer, wird nichts dargestellt
        //. = steht für den Dezimaltrenner; sein Format richtet sich in der Standardeinstellung nach den lokalen Systemeinstellungen
        //, = Tausendertrenner, genauer: gruppiert die Ziffern zwischen ',' und '.'

        //funktioniert nicht auf deutschen Geräten ("." -> ",") Locale.XX anpassen: siehe Link
        //https://stackoverflow.com/questions/36418901/change-decimalformat-locale

        String newDoubleString = new DecimalFormat("0.00").format(d);
        Double newDouble = Double.parseDouble(newDoubleString);

        return newDouble;
    }


    public static String doubleInStringToView(double d) {
        String outputValue = null;

        //Formatierungen:
        //0 = steht für eine auf jeden Fall anzuzeigende Ziffer innerhalb der darzustellenden Zahl; ist die Stelle leer, wird eine '0' dargestellt
        //# = steht für eine Ziffer innerhalb der darzustellenden Zahl; ist die Stelle leer, wird nichts dargestellt
        //. = steht für den Dezimaltrenner; sein Format richtet sich in der Standardeinstellung nach den lokalen Systemeinstellungen
        //, = Tausendertrenner, genauer: gruppiert die Ziffern zwischen ',' und '.'
        if(d >= 1000000000){
            double newDouble = d/1000000000;
            String formatDouble = new DecimalFormat("###,##0.000").format(newDouble);

            String formatDoubleChange = formatDouble.replaceAll("[,]", "a");
            formatDoubleChange = formatDoubleChange.replaceAll("[.]", ",");
            outputValue = formatDoubleChange.replaceAll("[a]", ".");

            return outputValue+"Mrd €";
        } else if(d >= 1000000){
            double newDouble = d/1000000;
            String formatDouble = new DecimalFormat("###,##0.000").format(newDouble);

            String formatDoubleChange = formatDouble.replaceAll("[,]", "a");
            formatDoubleChange = formatDoubleChange.replaceAll("[.]", ",");
            outputValue = formatDoubleChange.replaceAll("[a]", ".");

            Log.i("DB-Service", "format von TESTTSETTSET " + d + " = " + outputValue); //noch nicht getestet!

            return outputValue+"Mio €";

        } else {
            //setzt den Wert auf genau 2 Nachkommastellen
            //DecimalFormat newFormat = new DecimalFormat("###0.00");
            String formatDouble = new DecimalFormat("###,##0.00").format(d); //legt das Format fest und übergibt den double-Wert

            String formatDoubleChange = formatDouble.replaceAll("[,]", "a");
            formatDoubleChange = formatDoubleChange.replaceAll("[.]", ",");
            outputValue = formatDoubleChange.replaceAll("[a]", ".");
            Log.i("DB-Service", "format von TEST < 1Mio" + d + " = " + outputValue); //noch nicht getestet!

            return outputValue+" €";
        }
    }

    public static String timeFormatForDB(){
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer currentMonth = month + 1;
        Integer currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String currentMonthString;
        String currentDayString;

        if(currentMonth >= 1 && currentMonth <= 9) {
            currentMonthString = "0" + currentMonth.toString();
        } else {
            currentMonthString = currentMonth.toString();
        }

        if(currentDay >= 1 && currentDay <= 9) {
            currentDayString = "0" + currentDay.toString();
        } else {
            currentDayString = currentDay.toString();
        }
        String dbDate = currentYear.toString() + "-" + currentMonthString + "-" + currentDayString;

        return dbDate;
    }

    //NOCH IN BEARBEITUNG!
    public static String timeFormatForDB(Date date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

        return dateFormat.format(date);
    }

    public static String timeFormatToView(){
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer currentMonth = month + 1;
        Integer currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String currentMonthString;
        String currentDayString;

        if(currentMonth >= 1 && currentMonth <= 9) {
            currentMonthString = "0" + currentMonth.toString();
        } else {
            currentMonthString = currentMonth.toString();
        }

        if(currentDay >= 1 && currentDay <= 9) {
            currentDayString = "0" + currentDay.toString();
        } else {
            currentDayString = currentDay.toString();
        }
        String dbDate = currentDayString + "." + currentMonthString + "." + currentYear.toString();

        return dbDate;
    }

    public static int getCurrentMonthInteger(){
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        return currentMonth+1; //Januar = 0
    }

    public static String getCurrentMonthString(){
        String currentMonthString = "";

        Calendar calendar = Calendar.getInstance();
        Integer currentMonthInt = calendar.get(Calendar.MONTH);
        currentMonthInt = currentMonthInt + 1; //Januar wird als "0" zurück gegeben

        if(currentMonthInt >= 1 && currentMonthInt <= 9) {
            currentMonthString = "0" + currentMonthInt.toString();
        } else if(currentMonthInt >= 10 && currentMonthInt <= 12){
            currentMonthString = currentMonthInt.toString();
        }

        return currentMonthString;
    }

    public static String getLastMonthString(int monthStep){
        String currentMonthString = "";

        Calendar calendar = Calendar.getInstance();
        Integer currentMonth = calendar.get(Calendar.MONTH);
        Integer lastMonth = currentMonth + 1 - monthStep;

        if(lastMonth >= 1 && lastMonth <= 9) {
            currentMonthString = "0" + lastMonth.toString();
        } else if(lastMonth >= 10 && lastMonth <= 12){
            currentMonthString = lastMonth.toString();
        }

        return currentMonthString.toString();
    }

    public static String getLastMonthNumberString(int monthStep){
        Calendar calendar = Calendar.getInstance();
        Integer currentMonth = calendar.get(Calendar.MONTH);
        Integer lastMonth = currentMonth + 1 - monthStep;

        return lastMonth.toString();
    }

    public static int getCurrentYearInteger(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        return currentYear;
    }
    public static String getCurrentYearString(){
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);

        return currentYear.toString();
    }

    public static String getLastYearString(int yearStep){
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer lastYear = currentYear - yearStep;

        return lastYear.toString();
    }

}
