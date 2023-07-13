package com.example.somc.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.somc.R;
import com.example.somc.activities.login;
public class mas extends Fragment {

    public mas() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mas, container, false);



        LinearLayout contenedorProveedores = view.findViewById(R.id.contenedor_proveedores);

        contenedorProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, proveedores.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Mas")
                        .commit();
            }

        });

        LinearLayout contenedorLogOut = view.findViewById(R.id.contenedor_logout);

        contenedorLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), login.class);
                startActivity(intent);
            }
        });

        return view;
    }
}