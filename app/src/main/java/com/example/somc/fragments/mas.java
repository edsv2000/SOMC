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
import com.example.somc.data.formulasData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mas extends Fragment {



    public mas() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mas, container, false);



        return view;
    }
}