package com.example.finanzapp.ui.financebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzapp.R;

public class FinanceBookFragment extends Fragment {

    private FinanceBookViewModel financeBookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        financeBookViewModel =
                new ViewModelProvider(this).get(FinanceBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_financebook, container, false);
        final TextView textView = root.findViewById(R.id.text_financebook);
        financeBookViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;


    }


}