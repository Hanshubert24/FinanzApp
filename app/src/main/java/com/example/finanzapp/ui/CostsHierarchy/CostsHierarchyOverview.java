package com.example.finanzapp.ui.CostsHierarchy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finanzapp.MainActivity;
import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;

import java.util.ArrayList;

public class CostsHierarchyOverview extends AppCompatActivity {

    private static final String LOG_TAG = CostsHierarchyOverview.class.getSimpleName();

    DBDataAccess db;

    ListView listView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_hierarchy_overview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Übernehmen der E1-Werte aus der DB und befüllt den Listview
        arrayList = db.getE1FromCostsHierarchy();
        arrayAdapter = new ArrayAdapter(CostsHierarchyOverview.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }


    public void NavBack(View view){
        Intent i = new Intent(CostsHierarchyOverview.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void addNewE1(View view) {showDialogAddNewHierarchy();}

    public void showDialogAddNewHierarchy(){
        AlertDialog.Builder dialogPopUP = new AlertDialog.Builder(CostsHierarchyOverview.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_addnewentry_pupup = inflater.inflate(R.layout.dialog_addnewentry_popup, null);

        dialogPopUP.setView(dialog_addnewentry_pupup);
        dialogPopUP.show();
    }

    public void dialogSaveButton(View view){

        EditText editTextEntryE1 = (EditText) findViewById(R.id.editTextCOstsHierarchyAddNewE1);

        boolean success = db.CostsHierarchyInDB(editTextEntryE1.getText().toString(), null, null);

        if(success){
            Toast.makeText(this, "Eintrag erstellt.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, editTextEntryE1.getText().toString() + " ist bereits vorhanden.", Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(CostsHierarchyOverview.this, CostsHierarchyOverview.class);
        startActivity(i);
    }

    public void dialogChancelButton(View view){
        Intent i = new Intent(CostsHierarchyOverview.this, CostsHierarchyOverview.class);
        startActivity(i);
        //Versucher Versuchge
    }
}