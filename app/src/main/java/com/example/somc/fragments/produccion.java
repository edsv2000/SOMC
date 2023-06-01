package com.example.somc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterPedidos;
import com.example.somc.adaptadores.adapterProduccion;
import com.example.somc.data.pedidosData;
import com.example.somc.data.produccionData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link produccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class produccion extends Fragment {

    private ArrayList<produccionData> produccionData_list = new ArrayList<>();
    public produccion() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_produccion, container, false);
        produccionData_list.add(new produccionData("12345678","LT1829","PDO86499","EN PRODUCCION"));
        produccionData_list.add(new produccionData("12344678","LT1829","PDO86500","EN PRODUCCION"));
        produccionData_list.add(new produccionData("12347878","LT1829","PDO86501","EN PRODUCCION"));
        produccionData_list.add(new produccionData("12345678","LT1829","PDO86502","EN PRODUCCION"));
        produccionData_list.add(new produccionData("12347378","LT1829","PDO86503","EN PRODUCCION"));
        RecyclerView recyclerView  = view.findViewById(R.id.recyclerProduccion);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterProduccion adapter = new adapterProduccion(produccionData_list);

        recyclerView.setAdapter(adapter);

        return view;
    }
}