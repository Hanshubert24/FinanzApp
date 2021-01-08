package com.example.finanzapp.ui.financebook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FinanceBookViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FinanceBookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier kommt das FInaznbuch");
    }

    public LiveData<String> getText() {
        return mText;
    }
}