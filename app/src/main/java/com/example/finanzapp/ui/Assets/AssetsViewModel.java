package com.example.finanzapp.ui.Assets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssetsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AssetsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier steht dann das Vermögen");
    }

    public LiveData<String> getText() {
        return mText;
    }
}