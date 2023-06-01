package com.example.somc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterOC;
import com.example.somc.adaptadores.adapterPedidos;
import com.example.somc.data.OCData;
import com.example.somc.data.pedidosData;

import java.util.ArrayList;

public class ordenes_de_compras extends Fragment {

    private ArrayList<OCData> OCData_list = new ArrayList<>();
    public ordenes_de_compras() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordenes_de_compras, container, false);
        OCData_list.add(new OCData("1","21/02/2021","APROBADA"));
        OCData_list.add(new OCData("2","21/02/2021","APROBADA"));
        OCData_list.add(new OCData("3","21/02/2021","APROBADA"));
        OCData_list.add(new OCData("4","21/02/2021","APROBADA"));
        OCData_list.add(new OCData("5","21/02/2021","APROBADA"));
        RecyclerView recyclerView  = view.findViewById(R.id.recyclerOC);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterOC adapter = new adapterOC(OCData_list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}