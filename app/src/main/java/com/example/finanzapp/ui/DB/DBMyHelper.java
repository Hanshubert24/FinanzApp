package com.example.finanzapp.ui.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBMyHelper extends SQLiteOpenHelper {

    //https://docs.microsoft.com/de-de/dotnet/standard/data/sqlite/types#:~:text=Ein%20h%C3%A4ufig%20auftretendes%20Problem%20besteht%20darin%2C%20dass%20bei,SQLite-Typnamen%20zu%20verwenden%3A%20INTEGER%2C%20REAL%2C%20TEXT%20und%20BLOB.
    //https://www.sqlite.org/datatype3.html

    public static final String DB_NAME = "FinanzApp.db";
    public static final int DB_VERSION = 70;
    private static final String LOG_TAG = DBMyHelper.class.getSimpleName();

    public static boolean initializeWithExampleData = false; //Funktion für ExampleData in MainActivity

    public DBMyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
        Log.d(LOG_TAG, "DBMyHelper wurde aufgerufen.");
    }
    public DBMyHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBMyHelper wurde aufgerufen.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "DBMyHelper hat die Datenbank: " + getDatabaseName() + "mit der Versionsnummer: " + DB_VERSION + " erzeugt.");
        try{
            db.execSQL(SQL_CREATE_TableContracts);
            Log.d(LOG_TAG, "DBMyHelper hat die Tabelle: " + TABLEContracts_NAME + " erzeugt.");
        } catch (Exception e){
            Log.i("DB-Fehler", "Fehler beim Anlegen der Tabellen: " + TABLEContracts_NAME);
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabellen: " + TABLEContracts_NAME);
        }
        try{
            db.execSQL(SQL_CREATE_TableAssets);
            Log.d(LOG_TAG, "DBMyHelper hat die Tabelle: " + TABLEAssets_NAME + " erzeugt.");
        } catch (Exception e){
            Log.i("DB-Fehler", "Fehler beim Anlegen der Tabellen: " + TABLEAssets_NAME);
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabellen: " + TABLEAssets_NAME);
        }
        try{
            db.execSQL(SQL_CREATE_TableIncome);
            Log.d(LOG_TAG, "DBMyHelper hat die Tabelle: " + TABLEIncome_NAME+ " erzeugt.");
        } catch (Exception e){
            Log.i("DB-Fehler", "Fehler beim Anlegen der Tabelle: " + TABLEIncome_NAME);
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + TABLEIncome_NAME);
        }
        try{
            db.execSQL(SQL_CREATE_TableCostsHierarchy);
            Log.d(LOG_TAG, "DBMyHelper hat die Tabelle: " + TABLECostsHierarchy_Name+ " erzeugt.");
        } catch (Exception e){
            Log.i("DB-Fehler", "Fehler beim Anlegen der Tabelle: " + TABLECostsHierarchy_Name);
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + TABLECostsHierarchy_Name);
        }
        try{
            db.execSQL(SQL_CREATE_TableCashFlow);
            Log.d(LOG_TAG, "DBMyHelper hat die Tabelle: " + TABLECashFlow_Name+ " erzeugt.");
        } catch (Exception e){
            Log.i("DB-Fehler", "Fehler beim Anlegen der Tabelle: " + TABLECashFlow_Name);
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + TABLECashFlow_Name);
        }

        initializeWithExampleData = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Die Tabellen mit der Versionsnummer " + oldVersion + " werden gelöscht.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLEContracts_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLEAssets_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLEIncome_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLECostsHierarchy_Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLECashFlow_Name);
        onCreate(db);
    }

    public static ArrayList getTableListForCashFlowAddNew(){
        ArrayList<String> tableListArray = new ArrayList<String>();
        tableListArray.clear();
        tableListArray.add(TABLEContracts_NAME);
        tableListArray.add(TABLEAssets_NAME);
        tableListArray.add(TABLEIncome_NAME);

        return tableListArray;
    }


    //Contracts
    public static final String TABLEContracts_NAME = "Contracts";
    public static final int TABLEContracts_TableID = 0;
    public static final String COLUMNContracts_ID = "_id";
    public static final String COLUMNContracts_Type = "Type";
    public static final String COLUMNContracts_Name = "Name";
    public static final String COLUMNContracts_MonthlyCosts = "MonthlyCosts";
    public static final String COLUMNContracts_Note = "Note";
    public static final String SQL_CREATE_TableContracts =
            "CREATE TABLE " + TABLEContracts_NAME + " (" +
                    COLUMNContracts_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNContracts_Type + " TEXT NOT NULL, " +
                    COLUMNContracts_Name + " TEXT NOT NULL, " +
                    COLUMNContracts_MonthlyCosts + " REAL, " +
                    COLUMNContracts_Note + " TEXT);";

    public static final String[] columnsContracts = {
            COLUMNContracts_ID,
            COLUMNContracts_Type,
            COLUMNContracts_Name,
            COLUMNContracts_MonthlyCosts,
            COLUMNContracts_Note};


    //Assets (Vermögen)
    public static final String TABLEAssets_NAME = "Assets";
    public static final int TABLEAssets_TableID = 1;
    public static final String COLUMNAssets_ID = "_id";
    public static final String COLUMNAssets_Category = "Category";
    public static final String COLUMNAssets_Name = "Name";
    public static final String COLUMNAssets_MonthlyCosts = "MonthlyCosts";
    public static final String COLUMNAssets_MonthlyEarnings = "MonthlyEarnings";
    public static final String COLUMNAssets_FinancialAsset = "FinancialAsset";
    public static final String COLUMNAssets_Credit = "Credit";
    public static final String COLUMNAssets_ImagePath = "ImagePath";
    public static final String COLUMNAssets_Note = "Note";
    public static final String SQL_CREATE_TableAssets =
            "CREATE TABLE " + TABLEAssets_NAME + " (" +
                    COLUMNAssets_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNAssets_Category + " TEXT NOT NULL, " +
                    COLUMNAssets_Name + " TEXT NOT NULL, " +
                    COLUMNAssets_MonthlyCosts + " REAL, " +
                    COLUMNAssets_MonthlyEarnings + " REAL, " +
                    COLUMNAssets_FinancialAsset + " REAL, " +
                    COLUMNAssets_Credit + " REAL, " +
                    COLUMNAssets_ImagePath + " TEXT, " +
                    COLUMNAssets_Note + " TEXT);";

    public static final String[] columnsAssets = {
            COLUMNAssets_ID,
            COLUMNAssets_Category,
            COLUMNAssets_Name,
            COLUMNAssets_MonthlyCosts,
            COLUMNAssets_MonthlyEarnings,
            COLUMNAssets_FinancialAsset,
            COLUMNAssets_Credit,
            COLUMNAssets_ImagePath,
            COLUMNAssets_Note};


    //Income
    public static final String TABLEIncome_NAME = "Income";
    public static final int TABLEIncome_TableID = 2;
    public static final String COLUMNIncome_ID = "_id";
    public static final String COLUMNIncome_Category = "Category";
    public static final String COLUMNIncome_Company = "Company";
    public static final String COLUMNIncome_Brutto = "Brutto";
    public static final String COLUMNIncome_Netto = "Netto";
    public static final String COLUMNIncome_isMonthly = "iyMonthly";
    public static final String COLUMNIncome_Note = "Note";
    public static final String SQL_CREATE_TableIncome =
            "CREATE TABLE " + TABLEIncome_NAME + " (" +
                    COLUMNIncome_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNIncome_Category + " TEXT NOT NULL, " +
                    COLUMNIncome_Company + " TEXT NOT NULL, " +
                    COLUMNIncome_Brutto + " REAL, " +
                    COLUMNIncome_Netto + " REAL, " +
                    COLUMNIncome_isMonthly + " INTEGER NOT NULL, " +
                    COLUMNIncome_Note + " TEXT);";

    public static final String[] columnsIncome = {
            COLUMNIncome_ID,
            COLUMNIncome_Category,
            COLUMNIncome_Company,
            COLUMNIncome_Brutto,
            COLUMNIncome_Netto,
            COLUMNIncome_isMonthly,
            COLUMNIncome_Note};


    //Hierarchiy Variable Costs
    public static final String TABLECostsHierarchy_Name = "CostsHierarchy";
    public static final int TABLECostsHierarchy_TableID = 3;
    public static final String COLUMNCostsHierarchy_ID = "_id";
    public static final String COLUMNCostsHierarchy_E1 = "E1";
    public static final String COLUMNCostsHierarchy_E2 = "E2";
    public static final String COLUMNCostsHierarchy_E3 = "E3";
    public static final String SQL_CREATE_TableCostsHierarchy =
            "CREATE TABLE " + TABLECostsHierarchy_Name + " (" +
                    COLUMNCostsHierarchy_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNCostsHierarchy_E1 + " TEXT NOT NULL, " +
                    COLUMNCostsHierarchy_E2 + " TEXT, " +
                    COLUMNCostsHierarchy_E3 + " TEXT);";



    //CashFlow -> Tabelle Speichern alle Bewegungsdaten
    public static final String TABLECashFlow_Name = "CashFlow";
    public static final int TABLECashFlow_TableID = 4;
    public static final String COLUMNCashFlow_ID = "_id";
    public static final String COLUMNCashFlow_Date = "Date";
    public static final String COLUMNCashFlow_Type = "TypeID"; //1 = Einzahlung, 2 = Auszahlung
    public static final String COLUMNCashFlow_Tablename = "TablenID"; //Tabelle die den Cashflow verorsacht hat
    public static final String COLUMNCashFlow_TableEntryID = "TableEntrayID";
    public static final String COLUMNCashFlow_Value = "Value"; //Geldwert (Double)
    public static final String SQL_CREATE_TableCashFlow =
            "CREATE TABLE " + TABLECashFlow_Name + " (" +
                    COLUMNCashFlow_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNCashFlow_Date + " DATE NOT NULL, " +
                    COLUMNCashFlow_Type + " INTEGER NOT NULL, " +
                    COLUMNCashFlow_Tablename + " INTEGER NOT NULL, " +
                    COLUMNCashFlow_TableEntryID + " INTEGER NOT NULL, " +
                    COLUMNCashFlow_Value + " REAL NOT NULL);";
}
