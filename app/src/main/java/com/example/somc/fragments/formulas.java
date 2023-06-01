package com.example.somc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterFormulas;
import com.example.somc.data.FormulasData;

import java.util.ArrayList;

public class formulas extends Fragment {
    private ArrayList<FormulasData> formulasData_list = new ArrayList<>();
    public formulas() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulas, container, false);
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA","","23/02/2021","1"));
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA 2","","23/02/2021","1"));
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA 3","","23/02/2021","1"));
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA 4","","23/02/2021","1"));
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA 5","","23/02/2021","1"));
        formulasData_list.add(new FormulasData("EJEMPLO DE FORMULA 6","","23/02/2021","1"));

        RecyclerView recyclerView  = view.findViewById(R.id.recyclerFormulas);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterFormulas adapter = new adapterFormulas(formulasData_list);

        recyclerView.setAdapter(adapter);

        return view;
    }
}