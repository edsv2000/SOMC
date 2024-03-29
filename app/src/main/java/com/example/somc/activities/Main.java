package com.example.somc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import com.example.somc.R;
import com.example.somc.databinding.MainBinding;
import com.example.somc.fragments.formulas;
import com.example.somc.fragments.inventario;
import com.example.somc.fragments.mas;
import com.example.somc.fragments.pedidos;
import com.example.somc.fragments.produccion;


public class Main extends AppCompatActivity {
     MainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        configNav();
        configMain();
    }

    private void configMain() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main, inventario.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("Inventario")
                .commit();

        binding.name.setText("Inventario");

    }

    private void configNav() {


        binding.inventario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main, inventario.class, null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("Inventario")
                                    .commit();

                            binding.name.setText("Inventario");
                }
            });
        binding.formulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, formulas.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Formulas")
                        .commit();
                binding.name.setText("Formulas");
            }
        });

        binding.pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, pedidos.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Pedidos")
                        .commit();
                binding.name.setText("Pedidos");
            }
        });
        binding.produccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, produccion.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Produccion")
                        .commit();
                binding.name.setText("Produccion");
            }
        });
        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main, mas.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("Mas")
                        .commit();
                binding.name.setText("Mas");
            }
        });
    }



}