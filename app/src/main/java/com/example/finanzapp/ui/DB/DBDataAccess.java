package com.example.finanzapp.ui.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues; //Schreiben von Daten in die DB
import android.database.Cursor; //Lesen von Daten aus der DB

import java.util.ArrayList;


public class DBDataAccess {

    private static final String LOG_TAG = DBDataAccess.class.getSimpleName();

    private SQLiteDatabase database;
    private DBMyHelper dbHelper;


    public DBDataAccess(Context context) {
        Log.d(LOG_TAG, "DBDataAccess ruft DBMyHelper zur erzeugung der Datanbank auf.");
        dbHelper = new DBMyHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        //getWritable der Klasse DBHelper (geerbt) startet den Erstellungsprozess der Datenbank (wenn nicht existiert)
        //Wenn Rückgabewert null ist -> DBMyHelper.onCreate()
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz enthält Pfad zur Datenbank: " + database.getPath());
        Log.d(LOG_TAG, "Verbindung zur Datenbank wurde hergestellt.");
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbankverbindung geschlossen.");
    }


    //Fügt einen Datensatz in die Tabelle Contract ein
    public boolean addNewContractInDB(String type, String name, double monthlyCosts, String note) {

        ContentValues values = new ContentValues();
        values.put(DBMyHelper.COLUMNContracts_Type, type);
        values.put(DBMyHelper.COLUMNContracts_Name, name);
        values.put(DBMyHelper.COLUMNContracts_MonthlyCosts, monthlyCosts);
        values.put(DBMyHelper.COLUMNContracts_Note, note);
        try {
            //schreiben die Werte in die Datenbank.
            database.insert(DBMyHelper.TABLEContracts_NAME, null, values);
            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEContracts_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEContracts_NAME + " eingetragen;");
            e.printStackTrace();
            return false;
        }
    }

    //Ändert einen Datensatz in die Tabelle Contract
    //Wenn der Eintrag nicht geändert werden soll dann MUSS "null" übergeben werden!
    public boolean changeContractOneEntryInDB(int id, String type, String name, Double monthlyCosts, String note) {
        Log.d(LOG_TAG, "Übernommene Daten für die Tabelle: " + DBMyHelper.TABLEContracts_NAME);
        Log.d(LOG_TAG, "ID: " + String.valueOf(id));
        Log.d(LOG_TAG, "Type: " + type);
        Log.d(LOG_TAG, "Name: " + name);
        Log.d(LOG_TAG, "MonthlyCosts: " + String.valueOf(monthlyCosts));
        Log.d(LOG_TAG, "Note: " + note);

        ContentValues values = new ContentValues();
        if(type != null){
            values.put(DBMyHelper.COLUMNContracts_Type, type);
            Log.d(LOG_TAG, "Type mit Wert: " + type + " wird zum Update der Datenbank übernommen.");
        }
        if(name != null){
            values.put(DBMyHelper.COLUMNContracts_Name, name);
            Log.d(LOG_TAG, "Name mit Wert: " + name + " wird zum Update der Datenbank übernommen.");
        }
        if(monthlyCosts != null){
            values.put(DBMyHelper.COLUMNContracts_MonthlyCosts, monthlyCosts);
            Log.d(LOG_TAG, "MonthlyCosts mit Wert: " + monthlyCosts + " wird zum Update der Datenbank übernommen.");
        }
        if(note != null){
            values.put(DBMyHelper.COLUMNContracts_Note, note);
            Log.d(LOG_TAG, "Note mit Wert: " + note + " wird zum Update der Datenbank übernommen.");
        }

        try {
            database.update(
                    DBMyHelper.TABLEContracts_NAME,
                    values,
                    DBMyHelper.COLUMNContracts_ID + "=" + id,
                    null);

            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEContracts_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEContracts_NAME + " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewAssetInDB(String category, String name, double monthlyCosts, double monthlyEarnings, double financialAsset, double credit, String imagePath, String note){
        ContentValues values = new ContentValues();
        values.put(DBMyHelper.COLUMNAssets_Category, category);
        values.put(DBMyHelper.COLUMNAssets_Name, name);
        values.put(DBMyHelper.COLUMNAssets_MonthlyCosts, monthlyCosts);
        values.put(DBMyHelper.COLUMNAssets_MonthlyEarnings, monthlyEarnings);
        values.put(DBMyHelper.COLUMNAssets_FinancialAsset, financialAsset);
        values.put(DBMyHelper.COLUMNAssets_Credit, credit);
        values.put(DBMyHelper.COLUMNAssets_ImagePath, imagePath);
        values.put(DBMyHelper.COLUMNAssets_Note, note);

        try {
            //schreiben die Werte in die Datenbank.
            database.insert(DBMyHelper.TABLEAssets_NAME, null, values);
            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEAssets_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEAssets_NAME + " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean changeAssetOneEntryInDB(int id, String category, String name, Double monthlyCosts, Double monthlyEarnings, Double financialAsset, Double credit, String imagePath, String note){
        Log.d(LOG_TAG, "Übernommene Daten für die Tabelle: " + DBMyHelper.TABLEAssets_NAME);
        Log.d(LOG_TAG, "ID: " + String.valueOf(id));
        Log.d(LOG_TAG, "Category: " + category);
        Log.d(LOG_TAG, "Name: " + name);
        Log.d(LOG_TAG, "MonthlyCosts: " + String.valueOf(monthlyCosts));
        Log.d(LOG_TAG, "MonthlyEarnings: " + String.valueOf(monthlyEarnings));
        Log.d(LOG_TAG, "FinancialAsset: " + String.valueOf(financialAsset));
        Log.d(LOG_TAG, "Credit: " + String.valueOf(credit));
        Log.d(LOG_TAG, "ImagePath: " + imagePath);
        Log.d(LOG_TAG, "Note: " + note);

        ContentValues values = new ContentValues();
        if(category != null){
            values.put(DBMyHelper.COLUMNAssets_Category, category);
            Log.d(LOG_TAG, "Category mit Wert: " + category + " wird zum Update der Datenbank übernommen.");
        }
        if(name != null){
            values.put(DBMyHelper.COLUMNAssets_Name, name);
            Log.d(LOG_TAG, "Name mit Wert: " + name + " wird zum Update der Datenbank übernommen.");
        }
        if(monthlyCosts != null){
            values.put(DBMyHelper.COLUMNAssets_MonthlyCosts, monthlyCosts);
            Log.d(LOG_TAG, "MonthlyCosts mit Wert: " + monthlyCosts + " wird zum Update der Datenbank übernommen.");
        }
        if(monthlyEarnings != null){
            values.put(DBMyHelper.COLUMNAssets_MonthlyEarnings, monthlyEarnings);
            Log.d(LOG_TAG, "MonthlyEarings mit Wert: " + monthlyEarnings + " wird zum Update der Datenbank übernommen.");
        }
        if(monthlyEarnings != null){
            values.put(DBMyHelper.COLUMNAssets_FinancialAsset, financialAsset);
            Log.d(LOG_TAG, "FinancialAsset mit Wert: " + financialAsset + " wird zum Update der Datenbank übernommen.");
        }
        if(monthlyEarnings != null){
            values.put(DBMyHelper.COLUMNAssets_Credit, credit);
            Log.d(LOG_TAG, "Credit mit Wert: " + credit + " wird zum Update der Datenbank übernommen.");
        }
        if(imagePath != null){
            values.put(DBMyHelper.COLUMNAssets_ImagePath, imagePath);
            Log.d(LOG_TAG, "ImagePath mit Wert: " + imagePath + " wird zum Update der Datenbank übernommen.");
        }
        if(note != null){
            values.put(DBMyHelper.COLUMNAssets_Note, note);
            Log.d(LOG_TAG, "Note mit Wert: " + note + " wird zum Update der Datenbank übernommen.");
        }

        try {
            database.update(
                    DBMyHelper.TABLEAssets_NAME,
                    values,
                    DBMyHelper.COLUMNAssets_ID + "=" + id,
                    null);

            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEAssets_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEAssets_NAME + " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewIncomeInDB(String category, String company, double brutto, double netto, boolean isMonthly, String note) {
        ContentValues values = new ContentValues();
        values.put(DBMyHelper.COLUMNIncome_Category, category);
        values.put(DBMyHelper.COLUMNIncome_Company, company);
        values.put(DBMyHelper.COLUMNIncome_Brutto, brutto);
        values.put(DBMyHelper.COLUMNIncome_Netto, netto);
        values.put(DBMyHelper.COLUMNIncome_isMonthly, isMonthly);
        values.put(DBMyHelper.COLUMNIncome_Note, note);

        try {
            //schreiben die Werte in die Datenbank.
            database.insert(DBMyHelper.TABLEIncome_NAME, null, values);
            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEIncome_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEIncome_NAME+ " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeIncomeOneEntryInDB(int id, String category, String company, Double brutto, Double netto, boolean isMonthly, String note){
        //Umwandeln des boolischen Wertes in ein Int (zum speichern in die DB)
        int isMonthlyInt;
        if(isMonthly){
            isMonthlyInt = 1;
        } else { isMonthlyInt = 0; }

        Log.d(LOG_TAG, "Übernommene Daten für die Tabelle: " + DBMyHelper.TABLEIncome_NAME);
        Log.d(LOG_TAG, "ID: " + String.valueOf(id));
        Log.d(LOG_TAG, "Category: " + category);
        Log.d(LOG_TAG, "Company: " + company);
        Log.d(LOG_TAG, "Brutto: " + String.valueOf(brutto));
        Log.d(LOG_TAG, "Netto: " + String.valueOf(netto));
        Log.d(LOG_TAG, "isMonthly: " + isMonthly + "    ImagePathInt: " + String.valueOf(isMonthlyInt));
        Log.d(LOG_TAG, "Note: " + note);

        ContentValues values = new ContentValues();
        if(category != null){
            values.put(DBMyHelper.COLUMNIncome_Category, category);
            Log.d(LOG_TAG, "Category mit Wert: " + category + " wird zum Update der Datenbank übernommen.");
        }
        if(company != null){
            values.put(DBMyHelper.COLUMNIncome_Company, company);
            Log.d(LOG_TAG, "Company mit Wert: " + company + " wird zum Update der Datenbank übernommen.");
        }
        if(brutto != null){
            values.put(DBMyHelper.COLUMNIncome_Brutto, brutto);
            Log.d(LOG_TAG, "Brutto mit Wert: " + brutto + " wird zum Update der Datenbank übernommen.");
        }
        if(netto != null){
            values.put(DBMyHelper.COLUMNIncome_Netto, netto);
            Log.d(LOG_TAG, "Netto mit Wert: " + netto + " wird zum Update der Datenbank übernommen.");
        }

        values.put(DBMyHelper.COLUMNIncome_isMonthly, isMonthlyInt);
        Log.d(LOG_TAG, "isMonthly mit Wert: " + isMonthlyInt + " wird zum Update der Datenbank übernommen.");

        if(note != null){
            values.put(DBMyHelper.COLUMNAssets_Note, note);
            Log.d(LOG_TAG, "Note mit Wert: " + note + " wird zum Update der Datenbank übernommen.");
        }

        try {
            database.update(
                    DBMyHelper.TABLEIncome_NAME,
                    values,
                    DBMyHelper.COLUMNIncome_ID + "=" + id,
                    null);

            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLEIncome_NAME + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEIncome_NAME+ " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }

    public String CostsHierarchyInDB(String E1, String E2, String E3){
        ContentValues value = new ContentValues();

        if(E1 != null && E2 == null && E3 == null) {
/*
            //Prüfung ob ein Eintrag mit der Bezeichnung schon existiert.
            // Überarbeiten, scheißt fehler!!!!!
            Cursor cursor = database.query(
                    dbHelper.TABLECostsHierarchy_Name,
                    null,
                    dbHelper.COLUMNCostsHierarchy_E1 + "=" + E1, //Ausbessern
                    null,
                    null,
                    null,
                    null);

            if(cursor == null) {

 */
            try {

                Log.d(LOG_TAG, "SELECT * "+
                        "FROM " + dbHelper.TABLECostsHierarchy_Name + " " +
                        DBMyHelper.COLUMNCostsHierarchy_E1 + " = " + E1 + ";");

/*
                Cursor cursor = database.rawQuery(
                        "SELECT " + DBMyHelper.COLUMNCostsHierarchy_E1 +
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE type = " + DBMyHelper.TABLECostsHierarchy_Name + " UND " +
                                DBMyHelper.COLUMNCostsHierarchy_E1 + " = " + E1 + ";", null);

 */
                Cursor cursor = database.rawQuery(
                        "SELECT * "+
                                "FROM " + dbHelper.TABLECostsHierarchy_Name + " " +
                                "E1" + " = " + "Auto" + ";", null);

               //type = 'table' UND tbl_name = 'Unternehmen'
                //FROM DAS UNTERNEHMEN GEHALT> 50000;


                //Abfrage ob der Eintag schon in der DB existiert.
                if (cursor.moveToFirst()) {
                    Log.d(LOG_TAG, E1 + " -> ist bereits in der DB vorhanden.");
                    return "Eintrag bereits vorhanden.";
                }
                Log.d(LOG_TAG, E1 + " -> wird in die DB überommen.");

                value.put(DBMyHelper.COLUMNCostsHierarchy_E1, E1);
                value.put(DBMyHelper.COLUMNCostsHierarchy_E2, "empty");
                value.put(DBMyHelper.COLUMNCostsHierarchy_E3, "empty");
                database.insert(DBMyHelper.TABLECostsHierarchy_Name, null, value);
                Log.d(LOG_TAG, "Eintrag: " + E1 + " wird in die Tabelle " + dbHelper.TABLECostsHierarchy_Name + " eingetragen.");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage.");
                return "Fehler bei der DB-Abfrage.";
            }


            //  } else {
            //   Log.d(LOG_TAG, "Eintrag: " + E1 + " ist bereits in der Tabelle " + dbHelper.TABLECostsHierarchy_Name + " vorhanden.");

            //   return false;
            // }

        }
        return "Fall nicht vorhanden.";
    }


    public ArrayList getE1FromCostsHierarchy(){
        ArrayList arrayList = new ArrayList();
        try{
            // ArrayList arrayList = new ArrayList();

            Cursor cursor = database.rawQuery("SELECT " + DBMyHelper.COLUMNCostsHierarchy_E1 + " FROM " + DBMyHelper.TABLECostsHierarchy_Name, null);

            //befüllen der ArrayList
            if(cursor.moveToFirst()){
                do{
                    arrayList.add(cursor.getString((cursor.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E1))));

                } while (cursor.moveToNext());
            }
            return arrayList;

        } catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }
    public ArrayList getE2FromCostsHierarchy(){
        ArrayList arrayList = new ArrayList();
        try{
            // ArrayList arrayList = new ArrayList();

            Cursor cursor = database.rawQuery("SELECT " + DBMyHelper.COLUMNCostsHierarchy_E2 + " FROM " + DBMyHelper.TABLECostsHierarchy_Name, null);

            //befüllen der ArrayList
            if(cursor.moveToFirst()){
                do{
                    arrayList.add(cursor.getString((cursor.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_E2))));

                } while (cursor.moveToNext());
            }
            return arrayList;

        } catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }


    public Cursor viewAllInTable(String tablename) {
        try {
            Cursor cursor = database.query(tablename, null, null, null, null, null, null);
            Log.d(LOG_TAG, "Tabelle " + tablename + " tabe: gesamter Inhalt wurde abgefragt.");

            return cursor;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Cursor viewOneEntryInTable(String tablename, int id){
        try{
            Cursor cursor = database.query(
                    tablename,
                    null,
                    DBMyHelper.COLUMNContracts_ID + "=" + id,
                    null, null, null, null);
            return cursor;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteOneEntryInTable(String tablename, int id){
        try{
            database.delete(
                    tablename,
                    DBMyHelper.COLUMNContracts_ID + "=" + id,
                    null);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}


