package com.example.finanzapp.ui.DB;

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

    public static String timeFormat(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

        return dateFormat.format(date);
    }

    public static String timeFormat(Date date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

        return dateFormat.format(date);
    }

    //Servie zum automatischne betanken der Kostentabelle!
}
