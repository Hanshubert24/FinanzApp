package com.example.finanzapp.ui.Service;

public class DateService {

    static String[] str =
            {"Januar",
                    "Februar",
                    "März",
                    "April",
                    "Mai",
                    "Juni",
                    "Juli",
                    "August",
                    "September",
                    "October",
                    "November",
                    "Dezember"};

    public static String getMonthName(int monthIndex) {
        //since this is zero based, 11 = December
        if (monthIndex < 0 || monthIndex > 11 ) {
            throw new IllegalArgumentException(monthIndex + " Kein Gültiger Monat");
        }
        return str[monthIndex-1];
    }
}

