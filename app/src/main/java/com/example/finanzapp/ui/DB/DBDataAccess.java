package com.example.finanzapp.ui.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


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
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLEAssets_NAME + " fehlgeschlagen in changeAssetOneEntryInDB().");
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

    public int viewIDFromTableEntry(int tableID, String columnName, String columnEntry){
        String tableName;
        String tableIDName;
        int entryID;


        if(tableID == DBMyHelper.TABLEContracts_TableID){ //int = 0
            tableName = DBMyHelper.TABLEContracts_NAME;
            tableIDName = DBMyHelper.COLUMNContracts_ID;

        } else if(tableID == DBMyHelper.TABLEAssets_TableID){ //int = 1
            tableName = DBMyHelper.TABLEAssets_NAME;
            tableIDName = DBMyHelper.COLUMNAssets_ID;

        } else if(tableID == DBMyHelper.TABLEIncome_TableID){ //int = 2
            tableName = DBMyHelper.TABLEIncome_NAME;
            tableIDName = DBMyHelper.COLUMNIncome_ID;
        } else {
            tableName = null;
            tableIDName = null;
        }

        Log.i("DB-TEST", "SELECT *" +
                " FROM " + tableName +
                " WHERE " + columnName + " = '" + columnEntry + "';");

        try{
            Cursor cursor = database.rawQuery(
                    "SELECT *" +
                            " FROM " + tableName +
                            " WHERE " + columnName + " = '" + columnEntry + "';",
                    null);

            //TEST
            int count = cursor.getCount();
            Log.i("DB-TEST", "CursorCount = " + count);

            if(cursor != null) {
                cursor.moveToFirst();
                int IDindex = cursor.getColumnIndex(tableIDName);
                entryID = cursor.getInt(IDindex);

                return entryID;
            } else {
                Log.d(LOG_TAG, "Cursor für viewIDFromCostsHierarchyEntry() = null");
                return -1;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Auslesen der Methode: 'viewIDFromTableEntry' fehlgeschlagen.");

            return -1;
        }
    }

    public DBInformationObject CostsHierarchyInDB(String varE1, String varE2, String varE3){
        DBInformationObject dbInfo = new DBInformationObject();

        String emptyString = "Kei Eintrag vorhanden";
        String messageSuccessful = "Eingabe gespeichert";
        String messageEntryAlreadyExists = "Eintrag bereits vorhanden.";
        String messageDBError = "Datenbankfehler.";


        if(varE1 != null && varE2 == null && varE3 == null) { //wenn nur E1 übergeben wird

            try {
                //Abfrage ob der Eintrag shon exitiert
                Log.d(LOG_TAG, "SELECT *"+
                        " FROM " + dbHelper.TABLECostsHierarchy_Name +
                        " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "';");

                Cursor cursor = database.rawQuery(
                        "SELECT *"+
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "';", null);


                if (cursor.moveToFirst()) { //Wenn True -> da Eintrag schon vorhanden
                    Log.d(LOG_TAG, varE1 + " -> ist bereits in der DB vorhanden.");
                    dbInfo.setSuccess(false);
                    dbInfo.setMassage(messageEntryAlreadyExists);
                    return dbInfo;
                } else {

                    ContentValues value = new ContentValues();
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E1, varE1);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E2, emptyString);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E3, emptyString);
                    database.insert(DBMyHelper.TABLECostsHierarchy_Name, null, value);
                    Log.d(LOG_TAG, "Eintrag: " + varE1 + " wird in die Tabelle " + dbHelper.TABLECostsHierarchy_Name + " eingetragen.");

                    dbInfo.setSuccess(true);
                    dbInfo.setMassage(messageSuccessful);
                    return dbInfo;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage.");
                dbInfo.setSuccess(false);
                dbInfo.setMassage(messageDBError);
                return dbInfo;
            }


        } else if(varE1 != null && varE2 != null && varE3 == null){ //wenn E1 und E2 übergeben wird

            try {
                //Abfrage ob die Kombination aus E1 und E2 schon existiert
                Log.d(LOG_TAG, "SELECT *" +
                        " FROM " + dbHelper.TABLECostsHierarchy_Name +
                        " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                        " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                        ";");

                Cursor cursorEqual = database.rawQuery(
                        "SELECT *" +
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                                ";", null);

                if (cursorEqual.moveToFirst()) {
                    Log.d(LOG_TAG, "Der Eintrag E1 = " + varE1 + "und E2 = " + varE2 + " sind bereits in der DB vorhanden.");
                    dbInfo.setSuccess(false);
                    dbInfo.setMassage(messageEntryAlreadyExists);
                    return dbInfo;
                }

                //Abfrage ob ein E1 angelegt ist bei dem E2 den aktuellen Wert "empty" hat
                Log.d(LOG_TAG, "SELECT *" +
                        " FROM " + dbHelper.TABLECostsHierarchy_Name +
                        " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                        " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + emptyString + "'" +
                        ";");

                Cursor cursorEmpty = database.rawQuery(
                        "SELECT *" +
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + emptyString + "'" +
                                ";", null);

                //Wenn das E1 mit einem E2 vom Wert "empty" enthalten ist ...
                if (cursorEmpty.moveToFirst()) { //Wenn True -> E1 = vorhanden mit E2 = "empty" (String)
                    int IDIntex = cursorEmpty.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_ID); //Gibt den Index der Spalte "ID" des Datensatzes aus
                    int id = cursorEmpty.getInt(IDIntex); //Übernimmt den Eintrag von ID aus dem Datensatz

                    ContentValues value = new ContentValues();
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E2, varE2);
                    //ersetze E2 = "empty" durch varE2
                    database.update(
                            DBMyHelper.TABLECostsHierarchy_Name,
                            value,
                            DBMyHelper.COLUMNCostsHierarchy_ID + "=" + id, //Update des entsprechenden Datensatzes
                            null);

                    Log.d(LOG_TAG, "Für E1 (" + varE1 + ") wurde in E2 'empty' durch " + varE2 + " ersetzt.");
                    dbInfo.setSuccess(true);
                    dbInfo.setMassage(messageSuccessful);
                    return dbInfo;

                } else { //Wenn es kein E1 mit dazugehörigen E2 als "empty" gibt, dann lege einen neuen Datensatz mit E1 und E2 an.

                    ContentValues value = new ContentValues();
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E1, varE1);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E2, varE2);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E3, emptyString);

                    database.insert(DBMyHelper.TABLECostsHierarchy_Name, null, value);


                    Log.d(LOG_TAG, "Datensatz mit E1 = " + varE1 + ", E2 = " + varE2 + " wurde angelegt.");
                    dbInfo.setSuccess(true);
                    dbInfo.setMassage(messageSuccessful);
                    return dbInfo;
                }

            } catch (Exception e){
                e.printStackTrace();
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage.");
                dbInfo.setSuccess(false);
                dbInfo.setMassage("Datenbankfehler.");
                return dbInfo;
            }


        } else if(varE1 != null && varE2 != null && varE3 != null) { //wenn E1, E2 und E3 werden übergeben)

            try {
                //Abfrage ob die Kombination aus E1, E2 und E3 schon existiert
                Log.d(LOG_TAG, "SELECT *" +
                        " FROM " + dbHelper.TABLECostsHierarchy_Name +
                        " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                        " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                        " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + varE3 + "'" +
                        ";");

                Cursor cursorEqual = database.rawQuery(
                        "SELECT *" +
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + varE3 + "'" +
                                ";", null);

                if (cursorEqual.moveToFirst()) {
                    Log.d(LOG_TAG, "Der Eintrag E1 = " + varE1 + ", E2 = " + varE2 + " und E3 = " + varE3 + " sind bereits in der DB vorhanden.");
                    dbInfo.setSuccess(false);
                    dbInfo.setMassage(messageEntryAlreadyExists);
                    return dbInfo;
                }

                //Abfrage ob ein E1 und E2 angelegt, und es ein E3 gibt, bei dem der aktuellen Wert "empty" ist
                Log.d(LOG_TAG, "SELECT *" +
                        " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + emptyString + "'" +
                                ";");

                Cursor cursorEmpty = database.rawQuery(
                        "SELECT *" +
                                " FROM " + dbHelper.TABLECostsHierarchy_Name +
                                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                                " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + emptyString + "'" +
                                ";", null);

                //Wenn Wert E1, E2 zusammen mit einem E3 = "empty" exitieren dann ...
                if (cursorEmpty.moveToFirst()) {
                    int IDIntex = cursorEmpty.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_ID); //Gibt den Index der Spalte "ID" des Datensatzes aus
                    int id = cursorEmpty.getInt(IDIntex); //Übernimmt den Eintrag von ID aus dem Datensatz

                    ContentValues value = new ContentValues();
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E3, varE3);
                    //ersetzt E3 = "empty" durch varE3
                    database.update(
                            DBMyHelper.TABLECostsHierarchy_Name,
                            value,
                            DBMyHelper.COLUMNCostsHierarchy_ID + "=" + id, //Update des entsprechenden Datensatzes
                            null);

                    Log.d(LOG_TAG, "Für E1 (" + varE1 + ") und E2 (" + varE2 + ") wurde in E3 'empty' durch " + varE3 + " ersetzt.");
                    dbInfo.setSuccess(true);
                    dbInfo.setMassage(messageSuccessful);
                    return dbInfo;

                } else { //Wenn es kein E1 + E2 und E3 = "empty" gibt dann ...

                    ContentValues value = new ContentValues();
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E1, varE1);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E2, varE2);
                    value.put(DBMyHelper.COLUMNCostsHierarchy_E3, varE3);
                    //Füge neunen Datensatz ein
                    database.insert(DBMyHelper.TABLECostsHierarchy_Name, null, value);


                    Log.d(LOG_TAG, "Datensatz mit E1 = " + varE1 + ", E2 = " + varE2 + ", E3 = " + varE3 + " wurde angelegt.");
                    dbInfo.setSuccess(true);
                    dbInfo.setMassage(messageSuccessful);
                    return dbInfo;
                }

            } catch (Exception e){
                e.printStackTrace();
                Log.d(LOG_TAG, "Fehler bei der Datenbankabfrage.");
                dbInfo.setSuccess(false);
                dbInfo.setMassage("Datenbankfehler.");
                return dbInfo;
            }
        }
        dbInfo.setSuccess(false);
        dbInfo.setMassage("Kein Fall vorhanden.");
        return dbInfo;
    }

    public Cursor viewColumnsFromCostsHierarchyOverviewForListview(String column){
        //führ alle doppelten Datenbankeitäge zusammen
            //-> gibt für die übergebene Spalte keine Doppelten Werte aus
        try{
            Cursor cursor = database.query(
                    DBMyHelper.TABLECostsHierarchy_Name,
                    null,
                    null,
                    null,
                    column,
                    null, null);

            return cursor;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Cursor viewColumnsFromCostsHierarchyE1ForListview(String varE1, String column){
        //führ alle doppelten Datenbankeitäge zusammen
        //-> gibt für die übergebene Spalte keine Doppelten Werte aus
        try{
            Cursor cursor = database.query(
                    DBMyHelper.TABLECostsHierarchy_Name,
                    null,
                    DBMyHelper.COLUMNCostsHierarchy_E1 + "= '" + varE1 + "'",
                    null,
                    column,
                    null, null);

            return cursor;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Cursor viewColumnsFromCostsHierarchyE2ForListview(String varE1, String varE2){
        //führ alle doppelten Datenbankeitäge zusammen
        //-> gibt für die übergebene Spalte keine Doppelten Werte aus
        try{
             Cursor cursor = database.rawQuery("SELECT * FROM " + DBMyHelper.TABLECostsHierarchy_Name +
                    " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                    " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                    " GROUP BY " + DBMyHelper.COLUMNCostsHierarchy_E3 + ";", null);

            return cursor;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //TEST -> QuickPay-Activity
    public Cursor viewAllEntryFromCostsHierarchy(){
        //Gibt den gesamten Inhalt von CostsHierarchy zurück

        try {
            Cursor cursor = database.query(
                    "CostsHierarchy",
                    null,null, null, null, null, null);
            Log.i("DB-TESTSTESTTEST", "TESTABFRAGE WURDE DURCHGEFÜHRT!");
            return cursor;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int viewIDFromCostsHierarchyEntry(String varE1, String varE2, String varE3){
        int entryID;

        Log.i("DB-TEST", "SELECT *" +
                " FROM " + DBMyHelper.TABLECostsHierarchy_Name +
                " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + varE3 + "';");

        try{
            Cursor cursor = database.rawQuery(
                    "SELECT *" +
                            " FROM " + DBMyHelper.TABLECostsHierarchy_Name +
                            " WHERE " + DBMyHelper.COLUMNCostsHierarchy_E1 + " = '" + varE1 + "'" +
                            " AND " + DBMyHelper.COLUMNCostsHierarchy_E2 + " = '" + varE2 + "'" +
                            " AND " + DBMyHelper.COLUMNCostsHierarchy_E3 + " = '" + varE3 + "';",
                    null);

            //TEST
            int count = cursor.getCount();
            Log.i("DB-TEST", "CursorCount = " + count);

            if(cursor != null) {
                cursor.moveToFirst();
                int IDindex = cursor.getColumnIndex(DBMyHelper.COLUMNCostsHierarchy_ID);
                entryID = cursor.getInt(IDindex);

                return entryID;
            } else {
                Log.d(LOG_TAG, "Cursor für viewIDFromCostsHierarchyEntry() = null");
                return -1;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Auslesen der Methode: 'viewIDFromCostsHierarchyEntry' fehlgeschlagen.");

            return -1;
        }
    }

    public boolean addNewCashFlowInDB(String date, int typeID, int tableID, int tableEntryID, double doubleValue){
        //typeID => 1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe

        ContentValues cv = new ContentValues();
        cv.put(DBMyHelper.COLUMNCashFlow_Date, date);
        cv.put(DBMyHelper.COLUMNCashFlow_Type, typeID);
        cv.put(DBMyHelper.COLUMNCashFlow_Tablename, tableID);
        cv.put(DBMyHelper.COLUMNCashFlow_TableEntryID, tableEntryID);
        cv.put(DBMyHelper.COLUMNCashFlow_Value, doubleValue);

        try {
            //schreiben die Werte in die Datenbank.
            database.insert(DBMyHelper.TABLECashFlow_Name, null, cv);
            Log.d(LOG_TAG, "Datensatz in die Tabelle " + DBMyHelper.TABLECashFlow_Name + " eingetragen;");
            return true;

        } catch (Exception e) {
            Log.d(LOG_TAG, "Datenbankeintrag für " + DBMyHelper.TABLECashFlow_Name + " fehlgeschlagen.");
            e.printStackTrace();
            return false;
        }
    }

    public int getNumberOfEntrysInTable(String tablename, String selectionColumn, String selectionString){

        try{
            Cursor cursor = database.query(
                    tablename,
                    null,
                    selectionColumn + "= '" + selectionString + "'",
                    null, null, null, null);
            int numberOfEntrysinDB = cursor.getCount(); //Returns the numbers of rows in the cursor.

            return numberOfEntrysinDB;
        } catch (Exception e){
            e.printStackTrace();

            return -1;
        }
    }

    public Cursor viewAllInTable(String tablename) {
        try {
            Cursor cursor = database.query(tablename, null, null, null, null, null, null);
            Log.d(LOG_TAG, "Tabelle " + tablename + ": gesamter Inhalt wurde abgefragt.");

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
            Log.d(LOG_TAG, "Fehler in Methode viewOneEntryInTable().");
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

    public boolean deleteMultipleEntriesInTable(String tablename, String whereColumn, String whereCondition){
        try{
            database.delete(
                    tablename,
                    whereColumn + "= '" + whereCondition + "'",
                    null);

            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public double viewFinanceBookOverview(int typeID, String externalMonth, String externalYear){
        double sumValueDouble;

        Log.d(LOG_TAG,
                "SELECT " + "sum(Value) as sumValue, strftime('%m', date) as month, strftime('%Y', date) as year" +
                        " FROM " + DBMyHelper.TABLECashFlow_Name +
                        " WHERE " + DBMyHelper.COLUMNCashFlow_Type + " = " + typeID +
                        " AND " + "month" + " = '" + externalMonth + "'" +
                        " AND " + "year" + " = '" + externalYear + "';");

        try{
            Cursor cursor = database.rawQuery(
                    "SELECT " + "sum(Value) as sumValue, strftime('%m', date) as month, strftime('%Y', date) as year" +
                            " FROM " + DBMyHelper.TABLECashFlow_Name +
                            " WHERE " + DBMyHelper.COLUMNCashFlow_Type + " = " + typeID +
                            " AND " + "month" + " = '" + externalMonth + "'" +
                            " AND " + "year" + " = '" + externalYear + "';",
                    null);

            if(cursor != null) {
                cursor.moveToFirst();
                int valueIndex = cursor.getColumnIndex("sumValue");
                sumValueDouble = cursor.getDouble(valueIndex);

                return sumValueDouble;
            } else {
                Log.d(LOG_TAG, "Cursor für viewFinanceBookOverview() = null");
                return -1;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Auslesen aus Datenbank: 'viewFinanceBookOverview()' fehlgeschlagen.");

            return -1;
        }
    }

    public Cursor viewFinanceBookCharts(int tableID, String externalMonth, String externalYear){

        Log.d(LOG_TAG,
                "SELECT CostsHierarchy.E1, sum(CashFlow.Value) as sumValue, strftime('%m', date) as month, strftime('%Y', date) as year" +
                        " FROM CashFlow, CostsHierarchy" +
                        " WHERE CashFlow.TablenID = '" + tableID + "'" +
                        " AND CashFlow.TableEntrayID = CostsHierarchy._id " +
                        " AND " + "month" + " = '" + externalMonth + "'" +
                        " AND " + "year" + " = '" + externalYear + "'" +
                        " Group BY CostsHierarchy.E1;");

        try{
            Cursor cursor = database.rawQuery(
                    "SELECT CostsHierarchy.E1, sum(CashFlow.Value) as sumValue, strftime('%m', date) as month, strftime('%Y', date) as year" +
                            " FROM CashFlow, CostsHierarchy" +
                            " WHERE CashFlow.TablenID = '" + tableID + "'" +
                            " AND CashFlow.TableEntrayID = CostsHierarchy._id " +
                            " AND " + "month" + " = '" + externalMonth + "'" +
                            " AND " + "year" + " = '" + externalYear + "'" +
                            " Group BY CostsHierarchy.E1;",
                    null);
    /*

            Cursor cursor = database.rawQuery(
                    "SELECT " + DBMyHelper.TABLECostsHierarchy_Name + "." + DBMyHelper.COLUMNCostsHierarchy_E1 + "," + "sum(" + DBMyHelper.TABLECashFlow_Name + "." + DBMyHelper.COLUMNCashFlow_Value + ") as sumValue, strftime('%m', date) as month, strftime('%Y', date) as year" +
                            " FROM " + DBMyHelper.TABLECashFlow_Name + "," + DBMyHelper.TABLECostsHierarchy_Name +
                            " WHERE " + DBMyHelper.TABLECashFlow_Name + "." + DBMyHelper.COLUMNCashFlow_Tablename + "= '" + tableID + "'" +
                            " AND " + DBMyHelper.TABLECashFlow_Name + "." + DBMyHelper.COLUMNCashFlow_TableEntryID + "=" + DBMyHelper.TABLECostsHierarchy_Name + "." + DBMyHelper.COLUMNCostsHierarchy_ID +
                            " AND " + "month" + " = '" + externalMonth + "'" +
                            " AND " + "year" + " = '" + externalYear + "'" +
                            " GROUP BY " + DBMyHelper.TABLECostsHierarchy_Name + "." + DBMyHelper.COLUMNCostsHierarchy_E1 + "';",
                    null);
 */
           return cursor;

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Auslesen aus Datenbank: 'viewFinanceBookCharts()' fehlgeschlagen.");

            return null;
        }
    }

    public double viewAssetsChartActicity(String columnName){
        Cursor cursor;

        double sumValueOutput = -1;

        Log.d(LOG_TAG,
                "SELECT _id, sum(FinancialAsset) as sumFA, sum(Credit) as sumC" +
                        " FROM " + DBMyHelper.TABLEAssets_NAME);

        try{
            cursor = database.rawQuery(
                    "SELECT _id, sum("+columnName+") as sumValue" +
                            " FROM " + DBMyHelper.TABLEAssets_NAME,
                    null);

            if(cursor != null){
                if(cursor.moveToFirst()){
                    int valueID = cursor.getColumnIndex("sumValue");

                    sumValueOutput = cursor.getDouble(valueID);
                }
            } else {
                Log.d(LOG_TAG, "Cursor == NULL.");
            }

            return sumValueOutput;

        }catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Auslesen aus Datenbank: 'viewAssetsChartActicity()' fehlgeschlagen.");

            return sumValueOutput;
        }

    }


}