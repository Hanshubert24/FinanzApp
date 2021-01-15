package com.example.finanzapp.ui.DB;

import android.util.Log;

import java.text.DecimalFormat;

public class DBService {




    public static double doubleValueForDB(double d){
        //Genau 2 Nachkommastellen

        DecimalFormat format = new DecimalFormat("####################0.00");
        format.format(d);
        Log.i("DB-Service", "format von " + d + " = " + format); //noch nicht getestet!
        //Wir formatiert man das DezimalFormat Objekt in ein Double um?

        //Double newDouble;
       // newDouble.parseDouble(format);
/*
        System.out.println(myDouble);
        double d = Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", myDouble));
        System.out.println(d);```

 */
        return d;
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

    //Servie zum automatischne betanken der Kostentabelle!
}
