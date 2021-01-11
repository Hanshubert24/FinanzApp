package com.example.finanzapp.ui.Shares;

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

public class IncomeAndContractsFragment extends Fragment {

    private IncomeAndContractsViewModel incomeAndContractsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        incomeAndContractsViewModel =
                new ViewModelProvider(this).get(IncomeAndContractsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_income_and_contracts, container, false);
        final TextView textView = root.findViewById(R.id.text_shares);
        incomeAndContractsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}