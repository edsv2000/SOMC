package com.example.somc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import com.example.somc.databinding.MainBinding;


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

                            binding.name.setText("INVENTARIOS");
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
                binding.name.setText("FORMULAS");
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
                binding.name.setText("PEDIDOS");
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
                binding.name.setText("PRODUCCION");
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
                binding.name.setText("MAS");
            }
        });
    }

    private void setup() {

    }


}