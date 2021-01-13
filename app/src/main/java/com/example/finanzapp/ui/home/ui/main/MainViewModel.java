
package com.example.finanzapp.ui.home.ui.main;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Die clevere Art Finanzen zu verwalten");
    }


    public LiveData<String> getText() {
        return mText;
    }
}