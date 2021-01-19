package com.example.finanzapp;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.google.android.material.navigation.NavigationView;
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public AppBarConfiguration mAppBarConfiguration;
    private DBDataAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBDataAccess(getApplicationContext());

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


        //Einspielen der Testdaten wenn die DB_Version eine 10er ist.
        int dbVersion = DBMyHelper.DB_VERSION % 10;
        if(dbVersion == 0 && DBMyHelper.initializeWithExampleData){
            Log.d(LOG_TAG, "Beispieldaten werden abgerufen.");
            createExampleData();
            Log.d(LOG_TAG, "Beispieldaten wurden in die Tabellen eingetragen.");
            DBMyHelper.initializeWithExampleData = false;
            Toast.makeText(this, "TestDaten wurden angelegt.", Toast.LENGTH_SHORT).show();
        }
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



    //Testdaten für die Datenbank
    public void createExampleData() {
        db.open();
        db.addNewContractInDB("Krankenversicherung", "HansaMerkur", 120.95, "Ab 07/21 wechselbar.");
        db.addNewContractInDB("Kfz-Versicherung", "Allianz", 35.20, "");
        db.addNewContractInDB("Fitnesstudio", "McFit", 19.95, "Kündigen.");
        db.addNewContractInDB("Miete Wohnung", "A-Hausverwaltung", 825, "3 Monte Kündigungsfrist ab jeweils dem 1. des Monats (Posteingang)");
        db.addNewContractInDB("Darlehen", "Max Mustermann", 50, "Läuft 09/21 aus");

        db.addNewAssetInDB("Immobilie", "Berliner Str.13 15230 Ffo", 820, 1230, 400000 , 360000, "", "Bald mal wieder Renovieren!");
        db.addNewAssetInDB("Auto", "Mazda", 349.95, 0, 60000, 45000, "", "Bald abgezahlt ;-)");
        db.addNewAssetInDB("Gemälde", "Mona Lisa", 0, 0, 120000,0, "", "");

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
        db.CostsHierarchyInDB("Einkauf", "Kaffee to Go", null);
        db.CostsHierarchyInDB("Einkauf", "FastFood", null);
        db.close();
    }

}





