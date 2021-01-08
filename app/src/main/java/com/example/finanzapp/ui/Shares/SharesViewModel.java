package com.example.finanzapp.ui.Shares;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SharesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier kommen dann die Aktien hin");
    }

    public LiveData<String> getText() {
        return mText;
    }
}