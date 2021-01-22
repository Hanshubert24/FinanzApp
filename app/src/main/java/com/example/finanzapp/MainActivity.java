package com.example.finanzapp;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public AppBarConfiguration mAppBarConfiguration;
    private DBDataAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_assets
                , R.id.nav_logout, R.id.nav_financeBook, R.id.nav_contracts)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

            db = new DBDataAccess(getApplicationContext());
            db.open();
            //Wenn Datenbank neu erstellt wurde (onCreate())
            if (DBMyHelper.initializeFirstDatabaseAutomaticFunctionStart) {
                initialDatabaseAutomatikFunctionInformation();
                DBMyHelper.initializeFirstDatabaseAutomaticFunctionStart = false;
            }

            automaticDatabaseFunction();

            //Einspielen der Testdaten wenn die DB_Version eine 10er ist.
            int dbVersion = DBMyHelper.DB_VERSION % 10;
            if (dbVersion == 0 && DBMyHelper.initializeWithExampleData) {
                Log.d(LOG_TAG, "Beispieldaten werden abgerufen.");
                createExampleData();
                Log.d(LOG_TAG, "Beispieldaten wurden in die Tabellen eingetragen.");
                DBMyHelper.initializeWithExampleData = false;
                Toast.makeText(this, "TestDaten wurden angelegt.", Toast.LENGTH_SHORT).show();
            }
        }

        private void automaticDatabaseFunction () {
            Calendar calendar = Calendar.getInstance();
            Integer currentYear = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH); //Januar wird als "0" übergeben.
            Integer currentMonth = month + 1;

            String currentDateName = "AutoDBFunction" + currentYear.toString() + currentMonth.toString();

            //erstelle / öffne Speicherdatei
            SharedPreferences sharedPreferences = getSharedPreferences("SP_DBFunction", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit(); //Editorklasse initialisieren (um zu schreiben)


            if (sharedPreferences.getBoolean(currentDateName, false)) { //Ist die Datai mit dem aktuellen Monat mit Wert "true" hinterlegt dann ...

                //erzeuge neue Dateien für den kommenden Monat
                String nextDataName;
                Integer nextMonth = currentMonth + 1;

                if (currentMonth == 12) {
                    Integer nextYear = currentYear + 1;
                    nextDataName = "AutoDBFunction" + nextYear.toString() + nextMonth.toString();
                } else {
                    nextDataName = "AutoDBFunction" + currentYear.toString() + nextMonth.toString();
                }

                editor.putBoolean(nextDataName, true); //Inhalt übergeben (Key, Value)
                editor.commit(); //Speichern des Wertes


                String currentDate = DBService.timeFormatForDB(); //YYYY-MM-DD

                db.open();

                //Table Contracts in CashFlow (monthly Costs)
                try {
                    Cursor cursorContracts = db.viewAllInTable(DBMyHelper.TABLEContracts_NAME);

                    if (cursorContracts != null) {
                        if (cursorContracts.moveToFirst()) {
                            int idIndex = cursorContracts.getColumnIndex(DBMyHelper.COLUMNContracts_ID);
                            int monthlyCostsIndex = cursorContracts.getColumnIndex(DBMyHelper.COLUMNContracts_MonthlyCosts);

                            do {
                                if (cursorContracts.getDouble(monthlyCostsIndex) > 0.0) { //nimmt nur Einträge auf die auch einen Wert haben
                                    boolean successContracts = db.addNewCashFlowInDB(
                                            currentDate, //Datum
                                            2, //1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe
                                            DBMyHelper.TABLEContracts_TableID, // TableID
                                            cursorContracts.getInt(idIndex), //ID des Eintrags in Tabelle
                                            cursorContracts.getDouble(monthlyCostsIndex)); //Wert zum Eintrag
                                    if (successContracts) {
                                        Log.d(LOG_TAG, "Table CashFlow Eintrag: Contracts, Ausgabe, ID-" + cursorContracts.getInt(idIndex) + ", Wert(€)-" + cursorContracts.getDouble(monthlyCostsIndex));
                                    }
                                }
                            } while (cursorContracts.moveToNext());

                        } else {
                            Log.d(LOG_TAG, "CursorContracts kann Nicht zur erstellen Stelle springen.");
                        }
                    } else {
                        Log.d(LOG_TAG, "CursorContracts = NULL");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception: automaticDatabaseFunction() -> Table Contracts.");
                }


                //Table Assets in CashFlow (monthly Costs + monthlyErnings)
                try {
                    Cursor cursorAssets = db.viewAllInTable(DBMyHelper.TABLEAssets_NAME);

                    if (cursorAssets != null) {
                        if (cursorAssets.moveToFirst()) {
                            int idIndex = cursorAssets.getColumnIndex(DBMyHelper.COLUMNAssets_ID);
                            int monthlyCostsIndex = cursorAssets.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyCosts);
                            int monthlyEarningsIndex = cursorAssets.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyEarnings);

                            do {
                                //Werte von monthlyEarnings
                                if (cursorAssets.getDouble(monthlyEarningsIndex) > 0.0) {//nimmt nur Einträge auf die auch einen Wert haben
                                    boolean successAssetsEarning = db.addNewCashFlowInDB(
                                            currentDate, //Datum
                                            1, //1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe
                                            DBMyHelper.TABLEAssets_TableID, // TableID
                                            cursorAssets.getInt(idIndex), //ID des Eintrags in Tabelle
                                            cursorAssets.getDouble(monthlyEarningsIndex)); //Wert zum Eintrag
                                    if (successAssetsEarning) {
                                        Log.d(LOG_TAG, "Table CashFlow Eintrag: Assets, Einnahme, ID-" + cursorAssets.getInt(idIndex) + ", Wert(€)-" + cursorAssets.getDouble(monthlyEarningsIndex));
                                    }
                                }

                                //Werte von monthlyCosts
                                if (cursorAssets.getDouble(monthlyCostsIndex) > 0.0) { //nimmt nur Einträge auf die auch einen Wert haben
                                    boolean successAccessCosts = db.addNewCashFlowInDB(
                                            currentDate, //Datum
                                            2, //1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe
                                            DBMyHelper.TABLEAssets_TableID, // TableID
                                            cursorAssets.getInt(idIndex), //ID des Eintrags in Tabelle
                                            cursorAssets.getDouble(monthlyCostsIndex)); //Wert zum Eintrag
                                    if (successAccessCosts) {
                                        Log.d(LOG_TAG, "Table CashFlow Eintrag: Assets, Ausgabe, ID-" + cursorAssets.getInt(idIndex) + ", Wert(€)-" + cursorAssets.getDouble(monthlyCostsIndex));
                                    }
                                }
                            } while (cursorAssets.moveToNext());
                        } else {
                            Log.d(LOG_TAG, "CursorAssets kann Nicht zur erstellen Stelle springen.");
                        }
                    } else {
                        Log.d(LOG_TAG, "CursorAssets = NULL");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception: automaticDatabaseFunction() -> Table Assets.");
                }


                //Table Income in CashFlow (Netto)
                try {
                    Cursor cursorIncome = db.viewAllInTable(DBMyHelper.TABLEIncome_NAME);

                    if (cursorIncome != null) {
                        if (cursorIncome.moveToFirst()) {
                            int idIndex = cursorIncome.getColumnIndex(DBMyHelper.COLUMNIncome_ID);
                            int nettoIndex = cursorIncome.getColumnIndex(DBMyHelper.COLUMNIncome_Netto);

                            do {
                                if (cursorIncome.getDouble(nettoIndex) > 0.0) { //nimmt nur Einträge auf die auch einen Wert haben
                                    boolean successIncome = db.addNewCashFlowInDB(
                                            currentDate, //Datum
                                            1, //1 = Einzahlung/Einnahme, 2 = Auszahlung/Ausgabe
                                            DBMyHelper.TABLEIncome_TableID, // TableID
                                            cursorIncome.getInt(idIndex), //ID des Eintrags in Tabelle
                                            cursorIncome.getDouble(nettoIndex)); //Wert zum Eintrag
                                    if (successIncome) {
                                        Log.d(LOG_TAG, "Table CashFlow Eintrag: Income, Einnahme, ID-" + cursorIncome.getInt(idIndex) + ", Wert(€)-" + cursorIncome.getDouble(nettoIndex));
                                    }
                                }
                            } while (cursorIncome.moveToNext());

                        } else {
                            Log.d(LOG_TAG, "CursorIncome kann Nicht zur erstellen Stelle springen.");
                        }
                    } else {
                        Log.d(LOG_TAG, "CursorIncome = NULL");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception: automaticDatabaseFunction() -> Table Income.");
                }

                db.close();

                Log.d(LOG_TAG, "Einträge wurden automatisiert in die Tabelle Cashflow übernommen.");

                editor.putBoolean(currentDateName, false); //Inhalt übergeben (Key, Value)
                editor.commit();
            }
        }

        //Wird nur von DBMyHelper aufgerufen -> NUR wenn onCreate()! (Neuerstellung der DB)
        public void initialDatabaseAutomatikFunctionInformation () {
            Calendar calendar = Calendar.getInstance();
            Integer currentYear = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH); //Januar wird als "0" übergeben.
            Integer currentMonth = month + 1;

            String initialDateName = "AutoDBFunction" + currentYear.toString() + currentMonth.toString();

            //erstelle Speicherdatei und anlegen von Eintrag
            SharedPreferences sharedPreferences = getSharedPreferences("SP_DBFunction", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit(); //Editorklasse initialisieren (um zu schreiben)
            editor.putBoolean(initialDateName, true); //Inhalt übergeben (Key, Value)
            editor.commit();
        }

        //Testdaten für die Datenbank
        public void createExampleData () {
            db.open();
            db.addNewContractInDB("Krankenversicherung", "HansaMerkur", 120.95, "Ab 07/21 wechselbar.");
            db.addNewContractInDB("Kfz-Versicherung", "Allianz", 35.20, "");
            db.addNewContractInDB("Fitnesstudio", "McFit", 19.95, "Kündigen.");
            db.addNewContractInDB("Miete Wohnung", "A-Hausverwaltung", 825, "3 Monte Kündigungsfrist ab jeweils dem 1. des Monats (Posteingang)");
            db.addNewContractInDB("Darlehen", "Max Mustermann", 50, "Läuft 09/21 aus");

            db.addNewAssetInDB("Immobilie", "Berliner Str.13 15230 Ffo", 820, 1230, 400000, 360000, "", "Bald mal wieder Renovieren!");
            db.addNewAssetInDB("Auto", "Mazda", 349.95, 0, 60000, 45000, "", "Bald abgezahlt ;-)");
            db.addNewAssetInDB("Gemälde", "Mona Lisa", 0, 0, 120000, 0, "", "");

            db.addNewIncomeInDB("Lohn", "BMW", 0, 0, false, "Aktuelle Projekte in Berlin.");
            db.addNewIncomeInDB("Sold", "Bundeswehr", 3050.60, 2800.90, true, "Übergangsgebührnisse mit Zulagen.");
            db.addNewIncomeInDB("Gehalt", "SystemhausXNZ", 2800, 1900, true, "Regulärer Joy");
            db.addNewIncomeInDB("Nebentätigkeit", "Huttenschule Hoppegarten", 0, 0, false, "Unterstützung Digitalisierungsprojekte.");
            db.addNewIncomeInDB("Nebentätigkeit", "Lessingschule Ffo", 0, 0, false, "Unterstützung Digitalisierungsprojekte.");

            db.CostsHierarchyInDB("Auto", null, null);
            db.CostsHierarchyInDB("Auto", "Tanken", null);
            db.CostsHierarchyInDB("Auto", "Reparatur", null);
            db.CostsHierarchyInDB("Auto", "Reparatur", "Vertragswerkstatt");
            db.CostsHierarchyInDB("Auto", "Reparatur", "Freie Werkstatt");
            db.CostsHierarchyInDB("Auto", "Reparatur", "Selbst gebaut (Material)");
            db.CostsHierarchyInDB("Auto", "Bußgelder", null);
            db.CostsHierarchyInDB("Auto", "Bußgelder", "Zu schnell gefahren.");
            db.CostsHierarchyInDB("Auto", "Bußgelder", "Falsch geparkt.");
            db.CostsHierarchyInDB("Wohnung", null, null);
            db.CostsHierarchyInDB("Wohnung", "Einrichtung", null);
            db.CostsHierarchyInDB("Wohnung", "Einrichtung", "Möbel Boss");
            db.CostsHierarchyInDB("Wohnung", "Einrichtung", "XXXLutz");
            db.CostsHierarchyInDB("Wohnung", "Einrichtung", "Ikea");
            db.CostsHierarchyInDB("Wohnung", "Nebenkosten", "Strom");
            db.CostsHierarchyInDB("Wohnung", "Nebenkosten", "Wasser");
            db.CostsHierarchyInDB("Wohnung", "Nebenkosten", "Heizung");
            db.CostsHierarchyInDB("Wohnung", "Nebenkosten", "Sonstiges");
            db.CostsHierarchyInDB("Bildung", null, null);
            db.CostsHierarchyInDB("Bildung", "Technische Hochschule", null);
            db.CostsHierarchyInDB("Bildung", "Technische Hochschule", "Gebühren");
            db.CostsHierarchyInDB("Bildung", "Technische Hochschule", "Material");
            db.CostsHierarchyInDB("Bildung", "Technische Hochschule", "Sonstiges");
            db.CostsHierarchyInDB("Bildung", "Private Weiterbildung", "Online Kurse");
            db.CostsHierarchyInDB("Bildung", "Private Weiterbildung", "Bücher");
            db.CostsHierarchyInDB("Bildung", "Private Weiterbildung", "Material");
            db.CostsHierarchyInDB("Bildung", "Private Weiterbildung", "Sonstiges");
            db.CostsHierarchyInDB("Einkauf", null, null);
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", null);
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", "Obst und Gemüse");
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", "Backwaren");
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", "Fertigwaren");
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", "Süßigkeiten");
            db.CostsHierarchyInDB("Einkauf", "Lebensmittel", "Backwaren");
            db.CostsHierarchyInDB("Einkauf", "Kaffee to Go", null);
            db.CostsHierarchyInDB("Einkauf", "FastFood", null);

            //Date -> YYYY-MM-DD ; typeID -> 1=Einzahlung/Einnahme, 2=Auszahlung/Ausgabe
            //tableID -> 0=Contracts, 1=Assets, 2=Income, 3=CostsHierarchy
            db.addNewCashFlowInDB("2020-11-01", 1, 2, 1, 3100);
            db.addNewCashFlowInDB("2020-11-02", 2, 0, 0, 2600);

            db.addNewCashFlowInDB("2020-12-01", 1, 2, 1, 3100);
            db.addNewCashFlowInDB("2020-12-01", 2, 0, 0, 3500);

            db.addNewCashFlowInDB("2021-01-01", 1, 2, 1, 3100);

            Toast.makeText(this, "DB eingeladen", Toast.LENGTH_SHORT).show();
            db.close();
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void QuitApp(View view) {
        MainActivity.this.finishAndRemoveTask();
        System.exit(0);
    }

}





