package com.example.somc.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.somc.R;
import com.example.somc.adaptadores.adapterFormulas;
import com.example.somc.data.formulasData;
import  com.example.somc.secondsViews.registerformula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class formulas extends Fragment {
    private ArrayList<formulasData> formulasData_list = new ArrayList<>();
    private adapterFormulas adapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerFormulas);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new adapterFormulas(formulasData_list); // Mover la inicialización del adaptador aquí
        recyclerView.setAdapter(adapter);

        // Realizar la consulta a la API para obtener los datos de las fórmulas
        new GetFormulasTask().execute();

        Button btnAdd = view.findViewById(R.id.button);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), registerformula.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al iniciar la actividad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



    private class GetFormulasTask extends AsyncTask<Void, Void, ArrayList<formulasData>> {
        @Override
        protected ArrayList<formulasData> doInBackground(Void... voids) {
            // Realizar la llamada a la API y obtener la respuesta JSON
            String apiUrl = "https://api.habtek.com.mx/somcback/tables/formulas/getAll.php";
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<formulasData> formulasList = new ArrayList<>();

            if (response != null && !response.toString().isEmpty()) {
                try {
                    // Procesar la respuesta JSON y agregar los datos a la lista
                    JSONArray jsonArray = new JSONArray(response.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String descripcion = jsonObject.getString("Descripcion");
                        String fechaCreacion = jsonObject.getString("Fecha_creacion");
                        String idUsuario = jsonObject.getString("ID_usuario");

                        formulasList.add(new formulasData(descripcion, "", fechaCreacion, idUsuario, R.drawable.caja));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return formulasList;
        }

        @Override
        protected void onPostExecute(ArrayList<formulasData> formulasList) {
            if (formulasList != null) {
                formulasData_list.clear();
                formulasData_list.addAll(formulasList);

                if (formulasList.size() == 0) {
                    // Mostrar un mensaje de "sin resultados" si no hay fórmulas disponibles
                    // Puedes agregar un elemento especial a la lista para mostrar el mensaje
                    formulasData_list.add(new formulasData("No hay fórmulas disponibles", "", "", "", R.drawable.caratriste));
                }

                adapter.notifyDataSetChanged();
            }
        }
    }
}
