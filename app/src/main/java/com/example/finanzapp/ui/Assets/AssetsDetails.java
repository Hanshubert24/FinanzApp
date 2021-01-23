package com.example.finanzapp.ui.Assets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBMyHelper;
import com.example.finanzapp.ui.DB.DBService;

public class AssetsDetails extends AppCompatActivity {

    private static final String LOG_TAG = AssetsDetails.class.getSimpleName();

    DBDataAccess db;
    int sharePreferencesId;

    TextView textViewCategory;
    TextView textViewName;
    TextView textViewMonthlyCosts;
    TextView textViewMonthlyEarnings;
    TextView textViewFinancialAssets;
    TextView textViewCredit;
    TextView textViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details);

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird aufgerufen.");
        db = new DBDataAccess(getApplicationContext());

        textViewCategory = (TextView) findViewById(R.id.textViewAssetsDetailsCategory);
        textViewName = (TextView) findViewById(R.id.textViewAssetsDetailsName);
        textViewMonthlyCosts = (TextView) findViewById(R.id.textViewAssetsDetailsMonthlyCosts);
        textViewMonthlyEarnings = (TextView) findViewById(R.id.textViewAssetsDetailsMonthlyEarnings);
        textViewFinancialAssets = (TextView) findViewById(R.id.textViewAssetsDetailsFinancialAsset);
        textViewCredit = (TextView) findViewById(R.id.textViewAssetsDetailsCredit);
        textViewNote = (TextView) findViewById(R.id.textViewAssetsDetailsNote);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();

        //Shared Prefs Datei öffnen und Daten auslesen
        SharedPreferences sharedPreferences = getSharedPreferences("SpTransferData", 0);
        sharePreferencesId = (int) sharedPreferences.getLong("AssetID_ActivityChangeInfo", 0);

        Log.d(LOG_TAG, "Die ID: " + sharePreferencesId + " wurde aus dem Shared Preferences ausgelesen.");

        //Auslesen des Eintrags aus der Datenbank
        Cursor cursor = db.viewOneEntryInTable(DBMyHelper.TABLEAssets_NAME, sharePreferencesId);

        if(cursor == null){
            Toast.makeText(this, "Fehler beim Laden", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Fehler beim auslesen aus der Datenbank -> Cursor = null");
        }
        try {
            int categoryIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Category);
            int nameIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Name);
            int monthlyCostsIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyCosts);
            int monthlyEarningsIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_MonthlyEarnings);
            int financialAssetIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_FinancialAsset);
            int creditIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Credit);
            int imagePathindex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_ImagePath);
            int noteIndex = cursor.getColumnIndex(DBMyHelper.COLUMNAssets_Note);

            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(categoryIndex) + " " +
                            cursor.getString(nameIndex) + " " +
                            cursor.getString(monthlyCostsIndex) + " " +
                            cursor.getString(monthlyEarningsIndex) + " " +
                            cursor.getString(financialAssetIndex) + " " +
                            cursor.getString(creditIndex) + " " +
                            cursor.getString(imagePathindex) + " " +
                            cursor.getString(noteIndex));

                    //Format für die Ausgabe anpassencre
                    double monthlyCosts = cursor.getDouble(monthlyCostsIndex);
                    String monthlyCostsStringPrepare = DBService.doubleInStringToView(monthlyCosts);

                    double monthlyEarnings = cursor.getDouble(monthlyEarningsIndex);
                    String monthlyEarningsStringPrepare = DBService.doubleInStringToView(monthlyEarnings);

                    double financialAsset = cursor.getDouble(financialAssetIndex);
                    String financialAssetStringPrepare = DBService.doubleInStringToView(financialAsset);

                    double credit = cursor.getDouble(creditIndex);
                    String creditStringPrepare = DBService.doubleInStringToView(credit);


                    textViewCategory.setText(cursor.getString(categoryIndex));
                    textViewName.setText(cursor.getString(nameIndex));
                    textViewMonthlyCosts.setText(monthlyCostsStringPrepare);
                    textViewMonthlyEarnings.setText(monthlyEarningsStringPrepare);
                    textViewFinancialAssets.setText(financialAssetStringPrepare);
                    textViewCredit.setText(creditStringPrepare);
                    textViewNote.setText(cursor.getString(noteIndex));                            //Note gibt den Übergabewert nur auf der Console aus!!

                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void changeAsset(View view){
        Intent i = new Intent(AssetsDetails.this, AssetsDetailsChange.class);
        finishAndRemoveTask();
        startActivity(i);

    }

    public void deleteAsset(View view){
        showDialogDeleteAsset();
    }

    public void showDialogDeleteAsset(){
        AlertDialog.Builder dialogPopUp = new AlertDialog.Builder(AssetsDetails.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialog_delete_popup = inflater.inflate(R.layout.dialog_delete_popup, null);

        dialogPopUp.setView(dialog_delete_popup);
        dialogPopUp.show();
    }

    public void dialogCancelButton(View view){
        //Weiterleitung
        Intent i = new Intent(AssetsDetails.this, AssetsDetails.class);
        Toast.makeText(this, "Vorgang abgebrochen", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
        startActivity(i);

    }

    public void dialogDeleteButton(View view){

        boolean success = db.deleteOneEntryInTable(DBMyHelper.TABLEAssets_NAME, sharePreferencesId);
        if(success){
            Log.d(LOG_TAG, "Datensatz mit der ID: " + sharePreferencesId + " gelöscht.");
            Toast.makeText(this, "Datensatz gelöscht", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG_TAG, "Fehler beim löschen des Datensatz mit der ID: " + sharePreferencesId + ".");
            Toast.makeText(this, "Fehler beim löschen", Toast.LENGTH_SHORT).show();
        }

        //Weiterleitung
        Intent i = new Intent(AssetsDetails.this, AssetsOverview.class);
        Toast.makeText(this, "Assets gelöscht", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
        startActivity(i);

    }
    public void NavBackDetailsToAssetsOV(View view){
        Intent i = new Intent(AssetsDetails.this, AssetsOverview.class);
        finishAndRemoveTask();
        startActivity(i);
    }
}