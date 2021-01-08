package com.example.finanzapp.ui.wealth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WealthViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WealthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier steht dann das Vermögen");
    }

    public LiveData<String> getText() {
        return mText;
    }
}