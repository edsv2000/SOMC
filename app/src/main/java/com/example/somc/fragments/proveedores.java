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
import com.example.somc.adaptadores.adapterProveedor;
import com.example.somc.data.OCData;
import com.example.somc.data.pedidosData;
import com.example.somc.data.proveedorData;

import java.util.ArrayList;


public class proveedores extends Fragment {

    private ArrayList<proveedorData> proveedorData_list = new ArrayList<>();

    public proveedores() {
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
        proveedorData_list.add(new proveedorData("1","PINTURAS","5567123967","pinturas@gmail.com"));
        proveedorData_list.add(new proveedorData("2","QUIMICOS","5567178967","quimicos@gmail.com"));
        proveedorData_list.add(new proveedorData("3","TINTES","5567155967","tintes@gmail.com"));
        proveedorData_list.add(new proveedorData("4","EMPRESAQUIMICA","5567178967","empresaquimica@gmail.com"));
        proveedorData_list.add(new proveedorData("5","QUIMICANETA","5567178967","quimicaneta@gmail.com"));
        RecyclerView recyclerView  = view.findViewById(R.id.recyclerOC);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterProveedor adapter = new adapterProveedor(proveedorData_list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}