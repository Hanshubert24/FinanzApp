package com.example.finanzapp.ui.Assets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.R;

public class AssetsAddNew extends AppCompatActivity {

    private static final String LOG_TAG = AssetsAddNew.class.getSimpleName();

    DBDataAccess db;

    EditText inputAssetsCategory;
    EditText inputAssetsName;
    EditText inputAssetsMonthlyCosts;
    EditText inputAssetsMonthlyEarnings;
    EditText inputAssetsFinancialAssset;
    EditText inputAssetsCredit;
    EditText inputAssetsNote;

    String inputAssetsCategoryString;
    String inputAssetsNameString;
    double inputAssetsMonthlyCostsDouble = 0;
    double inputAssetsMonthlyEarningsDouble = 0;
    double inputAssetsFinancialAssetDouble = 0;
    double inputAssetsCreditDouble = 0;
    String inputAssetsNoteString;
    String inputAssetsImagePathString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_add_new);

        Log.d(LOG_TAG,"Das Datenquellen-Objekt wird angelegt.");
        db = new DBDataAccess(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffent,");
        db.open();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        db.close();
    }

    public void NavBack(View view){
        Intent i = new Intent(AssetsAddNew.this, AssetsOverview.class);
        startActivity(i);
        finish();
    }

    public void SaveContract(View view){

        boolean isCategory = false;
        boolean isName = false;

        try {
            inputAssetsCategory = findViewById(R.id.editTextAssetsCategory);
            inputAssetsName = findViewById(R.id.editTextAssetsName);
            inputAssetsMonthlyCosts = findViewById(R.id.editTextAssetsMonthlyCosts);
            inputAssetsMonthlyEarnings = findViewById(R.id.editTextAssetsMonthlyEarnings);
            inputAssetsFinancialAssset = findViewById(R.id.editTextAssetsFinancialAsset);
            inputAssetsCategory = findViewById(R.id.editTextAssetsCredit);
            inputAssetsNote = findViewById(R.id.editTextAssetsNote);

            //Prüfung, welche Felder beim betätigen des Ändern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if(isEditTextEmpty(inputAssetsCategory)){
                Toast.makeText(this, "Kategorie angeben.", Toast.LENGTH_SHORT).show();
                inputAssetsCategory.setHint("Bsp: Immobilien");
            } else {
                isCategory = true;
                inputAssetsCategoryString = inputAssetsCategory.getText().toString();
            }
            if(isEditTextEmpty(inputAssetsName)){
                Toast.makeText(this, "Name angeben.", Toast.LENGTH_SHORT).show();
                inputAssetsName.setHint("Bsp: BerlinerStr. 3 15230 Ffo");
            } else {
                isName = true;
                inputAssetsNameString = inputAssetsName.getText().toString();
            }
            if(isEditTextEmpty(inputAssetsMonthlyCosts)){
                inputAssetsMonthlyCostsDouble = 0.00; //Wenn Kosten leer
            } else {
                inputAssetsMonthlyCostsDouble = Double.parseDouble(inputAssetsMonthlyCosts.getText().toString());
            }
            if(isEditTextEmpty(inputAssetsMonthlyEarnings)){
                inputAssetsMonthlyEarningsDouble = 0.00; //Wenn Einnahmen Leer
            } else {
                inputAssetsMonthlyEarningsDouble = Double.parseDouble(inputAssetsMonthlyEarnings.getText().toString());
            }
            if(isEditTextEmpty(inputAssetsFinancialAssset)){
                inputAssetsFinancialAssetDouble = 0.00; //Wenn Eingabe leer
            } else {
                inputAssetsFinancialAssetDouble = Double.parseDouble(inputAssetsFinancialAssset.getText().toString());
            }
            if(isEditTextEmpty(inputAssetsCredit)){
                inputAssetsCreditDouble = 0.00; //Wenn Eingabe leer
            } else {
                inputAssetsCreditDouble = Double.parseDouble(inputAssetsCredit.getText().toString());
            }
            inputAssetsNoteString = inputAssetsNote.getText().toString();

            //DOUBLE-WERT Prüfen -> Ob eine Eingabe enthalten ist und ob der . (Punkt) und nicht das Komma gesetzt wurde.
            //Komma zulassen -> Wert vor der Eingabe in die Datenbank bearbeiten!

            //Funktion zum Speichern von Bildpfaden!

            //Übergabe an die Datenbank
            if(isCategory && isName) {
                db.addNewAssetInDB(
                        inputAssetsCategoryString,
                        inputAssetsNameString,
                        inputAssetsMonthlyCostsDouble,
                        inputAssetsMonthlyEarningsDouble,
                        inputAssetsFinancialAssetDouble,
                        inputAssetsCreditDouble,
                        null,                       //Methode noch implementieren!
                        inputAssetsNoteString);

                //Weiterleitung zurück auf die AssetsOverview Seite
                Intent i = new Intent(AssetsAddNew.this, AssetsOverview.class);
                startActivity(i);
                finish();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isEditTextEmpty(EditText editText){
        if(editText.getText().toString().trim().length() > 0){
            return false;
        } else { return true; }
    }
}

