package com.example.somc.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.somc.R;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class produccion extends Fragment implements  adapterProduccion.ListenerProduction{
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerProduccion);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Configurar el adaptador con el listener antes de realizar la solicitud de la API
        adapter = new adapterProduccion(produccionData_list);
        adapter.setListenerProduccion(this); // Configura el ListenerProduction en el adaptador
        recyclerView.setAdapter(adapter);

        FetchPedidosEnProduccionTask task = new FetchPedidosEnProduccionTask();
        task.execute("https://api.habtek.com.mx/somcback/src/tables/produccion/getAll.php");

        return view;
    }



    @Override
    public void onListenerProduction(produccionData data, String selectedItem) {
       if (data.getStatus().equals("En produccion")){
           // Crea el JSON con los datos a enviar al servidor
           JSONObject jsonData = new JSONObject();
           try {
               jsonData.put("ID_pedido", data.getNumeroPedido());
               jsonData.put("Estado", "Surtido");
           } catch (JSONException e) {
               e.printStackTrace();
           }

           String urlString = "https://api.habtek.com.mx/somcback/src/tables/pedidos/updateStatus.php";
           CambiarEstadoPedidoAsyncTask estado = new CambiarEstadoPedidoAsyncTask();
           estado.execute(urlString, jsonData.toString());
       }
    }

    public class CambiarEstadoPedidoAsyncTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "CambiarEstadoPedido";

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String jsonData = params[1];

            String result = "";

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(jsonData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();

                    result = stringBuilder.toString();
                } else {
                    result = "Error en la conexión: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "Error en la conexión: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                // Aquí puedes manejar la respuesta del servidor según tus necesidades

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
