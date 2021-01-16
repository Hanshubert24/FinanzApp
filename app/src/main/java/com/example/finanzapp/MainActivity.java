package com.example.finanzapp;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finanzapp.ui.DB.DBDataAccess;
import com.google.android.material.navigation.NavigationView;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
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
    public void createExampleData(View view) {
        db.open();
        db.addNewContractInDB("Krankenversicherung", "HansaMerkur", 120.95, "Ab 07/21 Wechelbar.");
        db.addNewContractInDB("KFZ-Versicherung", "Alianz", 35.20, "");
        db.addNewContractInDB("Fitnesstudio", "McFit", 19.95, "Kündigen.");
        db.addNewContractInDB("Miete Wohnung", "A-Hausverwaltung", 825, "");
        db.addNewContractInDB("Dahrlehen", "Max Mustermann", 50, "Läuft 09/21 aus");

        db.addNewAssetInDB("Immobilie", "Berliner Str.13 15230 Ffo", 820, 1230, 400000 , 360000, "", "Bald mal wieder Renoveren!");
        db.addNewAssetInDB("Auto", "Mazda", 349.95, 0, 60000, 45000, "", "Bald abgezahlt ;-)");
        db.addNewAssetInDB("Gemälde", "Mona Lisa", 0, 0, 120000,0, "", "");

        db.addNewIncomeInDB("Lohn", "BMW", 0, 0, false, "Aktuelle Projekte in Berlin.");
        db.addNewIncomeInDB("Sold", "Bundeswehr", 3050.60, 2800.90, true, "Übergangsgebührnisse mit Zulagen.");
        db.addNewIncomeInDB("Gehalt", "SystemhausXNZ", 2800, 1900, true, "Regulärer Joy");
        db.addNewIncomeInDB("Nebentätigkeit", "Huttenschule Hoppegarten", 0, 0, false, "Unterstützung Digitalisierungsprojekte.");
        db.addNewIncomeInDB("Nebentätigkeit", "Lessingschule Ffo", 0, 0, false, "Unterstützung Digitalisierungsprojekte.");
    }

}





