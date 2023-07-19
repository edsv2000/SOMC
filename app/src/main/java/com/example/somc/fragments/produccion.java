package com.example.somc.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterPedidos;
import com.example.somc.adaptadores.adapterProduccion;
import com.example.somc.data.pedidosData;
import com.example.somc.data.produccionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class produccion extends Fragment {
    adapterProduccion adapter;
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


        RecyclerView recyclerView  = view.findViewById(R.id.recyclerProduccion);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new adapterProduccion(produccionData_list);


        FetchPedidosEnProduccionTask task = new FetchPedidosEnProduccionTask();
        task.execute("https://api.habtek.com.mx/somcback/tables/produccion/getAll.php");

        recyclerView.setAdapter(adapter);

        return view;
    }

    private class FetchPedidosEnProduccionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            String response = "";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }

                    bufferedReader.close();
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor: " + connection.getResponseCode(), Toast.LENGTH_SHORT).show();
                }

                connection.disconnect();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al realizar la solicitud: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equals("success")) {
                    JSONArray pedidosArray = jsonObject.getJSONArray("pedidos_en_produccion");
                    // Procesar los datos recibidos, por ejemplo, mostrarlos en una lista o RecyclerView
                    processPedidos(pedidosArray);
                } else {
                    String message = jsonObject.getString("message");
                    // Manejar el error, por ejemplo, mostrar un mensaje de error al usuario
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error al analizar la respuesta JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // Manejar el error, por ejemplo, mostrar un mensaje de error al usuario
                Toast.makeText(getContext(), "Error al analizar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processPedidos(JSONArray pedidosArray) {
        try {
            // Aquí puedes procesar los datos recibidos, por ejemplo, mostrarlos en una lista o RecyclerView
            for (int i = 0; i < pedidosArray.length(); i++) {
                JSONObject pedidoObj = pedidosArray.getJSONObject(i);

                // Extraer los datos del pedido
                String ID_pedido = pedidoObj.getString("ID_pedido");
                String Fecha_pedido = pedidoObj.getString("Fecha_pedido");
                String Estado = pedidoObj.getString("Estado");
                String Cliente = pedidoObj.getString("Cliente");
                String Usuario = pedidoObj.getString("Usuario");

                // Obtener el JSONArray de detalles del pedido
                JSONArray detallesArray = pedidoObj.getJSONArray("Detalles");




                produccionData_list.add(new produccionData(ID_pedido,Cliente,ID_pedido,Estado,detallesArray));
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error al procesar el JSONArray de pedidos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            // Manejar el error, por ejemplo, mostrar un mensaje de error al usuario
            Toast.makeText(getContext(), "Error al procesar los datos de pedidos.", Toast.LENGTH_SHORT).show();
        }
    }


}
