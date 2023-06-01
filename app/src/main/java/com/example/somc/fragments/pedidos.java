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
import com.example.somc.data.pedidosData;

import java.util.ArrayList;


public class pedidos extends Fragment {

    private ArrayList<pedidosData> pedidosData_list = new ArrayList<>();
    public pedidos() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        pedidosData_list.add(new pedidosData("12345678","BIMBO","15/09/10","EN PRODUCCION"));
        pedidosData_list.add(new pedidosData("1678","GAMESA","15/09/10","EN PRODUCCION"));
        pedidosData_list.add(new pedidosData("123458","COMEX","15/09/10","EN PRODUCCION"));
        RecyclerView recyclerView  = view.findViewById(R.id.recyclerPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterPedidos adapter2 = new adapterPedidos(pedidosData_list);

        recyclerView.setAdapter(adapter2);

        return view;
    }
}