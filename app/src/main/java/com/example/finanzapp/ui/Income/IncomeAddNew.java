package com.example.finanzapp.ui.Income;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.DB.DBDataAccess;
import com.example.finanzapp.ui.DB.DBService;

public class IncomeAddNew extends AppCompatActivity {

    private static final String LOG_TAG = IncomeAddNew.class.getSimpleName();

    DBDataAccess db;

    EditText inputIncomeCategory;
    EditText inputIncomeCompany;
    EditText inputIncomeBrutto;
    EditText inputIncomeNetto;
    EditText inputIncomeNote;

    String inputIncomeCategoryString;
    String inputIncomeCompanyString;
    double inputIncomeBruttoDouble = 0;
    double inputIncomeNettoDouble = 0;
    String inputIncomeNoteString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add_new);

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
        Intent i = new Intent(IncomeAddNew.this, IncomeOverview.class);
        startActivity(i);
        finish();
    }

    public void SaveIncome(View view){

        boolean isCategory = false;
        boolean isCompany = false;
        double inputIncomeBruttoDoublePrepare;
        double inputIncomeNettoDoublePrepare;

        try {
            inputIncomeCategory = findViewById(R.id.editTextIncomeAddNewCategory);
            inputIncomeCompany = findViewById(R.id.editTextIncomeAddNewCompany);
            inputIncomeBrutto = findViewById(R.id.editTextIncomeAddNewBrutto);
            inputIncomeNetto = findViewById(R.id.editTextIncomeAddNewNetto);
            inputIncomeNote = findViewById(R.id.editTextIncomeAddNewNote);

            //Prüfung, welche Felder beim betätigen des Speichern-Buttons befühlt sind.
            //Es sollen nur die eingetragenen Änderungen an die DB übergeben werden.
            if(isEditTextEmpty(inputIncomeCategory)){
                Toast.makeText(this, "Vertragstyp angeben.", Toast.LENGTH_SHORT).show();
                inputIncomeCategory.setHint("Bsp: Lohn / Gehalt / Sold");
            } else {
                isCategory = true;
                inputIncomeCategoryString = inputIncomeCategory.getText().toString();
            }
            if(isEditTextEmpty(inputIncomeCompany)){
                Toast.makeText(this, "Unternehmen angeben.", Toast.LENGTH_SHORT).show();
                inputIncomeCompany.setHint("Bsp: BWM / Systemhaus / Bundeswehr");
            } else {
                isCompany = true;
                inputIncomeCompanyString = inputIncomeCompany.getText().toString();
            }
            if(isEditTextEmpty(inputIncomeBrutto)){
                inputIncomeBruttoDoublePrepare = 0.00; //Wenn Brutto leer
            } else {
                inputIncomeBruttoDouble = Double.parseDouble(inputIncomeBrutto.getText().toString());
                inputIncomeBruttoDoublePrepare = DBService.doubleValueForDB(inputIncomeBruttoDouble);
            }
            if(isEditTextEmpty(inputIncomeNetto)){
                inputIncomeNettoDoublePrepare = 0.00; //Wenn Netto Leer
            } else {
                inputIncomeNettoDouble = Double.parseDouble(inputIncomeNetto.getText().toString());
                inputIncomeNettoDoublePrepare = DBService.doubleValueForDB(inputIncomeNettoDouble);
            }
            inputIncomeNoteString = inputIncomeNote.getText().toString();

            //DOUBLE-WERT Prüfen -> Ob eine Eingabe enthalten ist und ob der . (Punkt) und nicht das Komma gesetzt wurde.
            //Komma zulassen -> Wert vor der Eingabe in die Datenbank bearbeiten!

            //Funktion zum Speichern von Bildpfaden!

            //Übergabe an die Datenbank
            if(isCategory && isCompany) {
                db.addNewIncomeInDB(
                        inputIncomeCategoryString,
                        inputIncomeCompanyString,
                        inputIncomeBruttoDoublePrepare,
                        inputIncomeNettoDoublePrepare,
                        true,
                        inputIncomeNoteString);

                //Weiterleitung zurück auf die AssetsOverview Seite
                Intent i = new Intent(IncomeAddNew.this, IncomeOverview.class);
                Toast.makeText(this, "Verdienstvertrag hinzugefügt", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
                startActivity(i);

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