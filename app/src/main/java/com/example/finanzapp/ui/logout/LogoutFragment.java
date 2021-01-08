package com.example.finanzapp.ui.logout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
                System.exit(0);
            }
      

}