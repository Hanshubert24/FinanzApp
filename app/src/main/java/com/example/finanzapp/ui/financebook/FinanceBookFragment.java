package com.example.finanzapp.ui.financebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzapp.R;
import com.example.finanzapp.ui.CostsHierarchy.CostsHierarchyOverview;
import com.example.finanzapp.ui.Income.IncomeAddNew;
import com.example.finanzapp.ui.home.HomeTwoActivity;

public class FinanceBookFragment extends Fragment{

    private FinanceBookViewModel financeBookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        financeBookViewModel = new ViewModelProvider(this).get(FinanceBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_financebook, container, false);






        ImageButton hierachryTemplateButtonFinanceBook = (ImageButton) root.findViewById(R.id.fb_imagebutton_template_hierachy_overwiew);
        hierachryTemplateButtonFinanceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CostsHierarchyOverview.class);
                startActivity(intent);
            }
        });

        ImageButton chartButtonFinanceBook = (ImageButton) root.findViewById(R.id.fb_imagebutton_charts);
        chartButtonFinanceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent intent = new Intent(getActivity(), HomeTwoActivity.class);
                startActivity(intent);
            }
        });


        Button fbAddButton = (Button) root.findViewById(R.id.fb_add_button);
        fbAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                Intent intent = new Intent(getActivity(), IncomeAddNew.class);
                startActivity(intent);
            }
        });


        return root;


    }


}