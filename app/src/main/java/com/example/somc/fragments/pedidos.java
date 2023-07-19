package com.example.somc.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterPedidos;
import com.example.somc.data.pedidosData;
import com.example.somc.secondsViews.registerPedido;

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


public class pedidos extends Fragment implements adapterPedidos.ListenerPedido {

    Button addPedido;
    adapterPedidos adapter2;
    private ArrayList<pedidosData> pedidosData_list = new ArrayList<>();

    public pedidos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter2 = new adapterPedidos(pedidosData_list);
        adapter2.setListenerPedido(this);
        recyclerView.setAdapter(adapter2);

        addPedido = (Button) view.findViewById(R.id.addPedido);

        addPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), registerPedido.class);
                startActivity(intent);
            }
        });

        PedidosRequest pedidosRequest = new PedidosRequest();
        pedidosRequest.execute();

        return view;
    }



    @Override
    public void onListenerPedido(pedidosData data, String selectedItem) {
        if (selectedItem != null ) {
            if (data.getStatus().equals("Autorizado") && selectedItem.equals("Mandar a producción")){
                // Crea el JSON con los datos a enviar al servidor
                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("ID_pedido", data.getNumero());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String urlString = "https://api.habtek.com.mx/somcback/tables/produccion/insert.php"; // Reemplaza con la URL de tu API
                setProduccion produccion = new setProduccion();
                produccion.execute(urlString, jsonData.toString());

                // Crea el JSON con los datos a enviar al servidor
                JSONObject jsonData2 = new JSONObject();
                try {
                    jsonData2.put("ID_pedido", data.getNumero());
                    jsonData2.put("Estado", "En produccion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String urlString2 = "https://api.habtek.com.mx/somcback/tables/pedidos/updateStatus.php";
                CambiarEstadoPedidoAsyncTask estado = new CambiarEstadoPedidoAsyncTask();
                estado.execute(urlString2, jsonData2.toString());

                adapter2.notifyDataSetChanged();
            } else if (selectedItem.equals("Autorizar pedido") && data.getStatus().equals("Pendiente")) {
                // Crea el JSON con los datos a enviar al servidor
                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("ID_pedido", data.getNumero());
                    jsonData.put("Estado", "Autorizado");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String urlString = "https://api.habtek.com.mx/somcback/tables/pedidos/updateStatus.php";
                CambiarEstadoPedidoAsyncTask estado = new CambiarEstadoPedidoAsyncTask();
                estado.execute(urlString, jsonData.toString());
            }else if (selectedItem.equals("Cancelar")) {
                // Crea el JSON con los datos a enviar al servidor
                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("ID_pedido", data.getNumero());
                    jsonData.put("Estado", "Cancelado");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String urlString = "https://api.habtek.com.mx/somcback/tables/pedidos/updateStatus.php";
                CambiarEstadoPedidoAsyncTask estado = new CambiarEstadoPedidoAsyncTask();
                estado.execute(urlString, jsonData.toString());
            }
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

    public class setProduccion extends AsyncTask<String, Void, String> {

        private static final String TAG = "setProduccion";

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

    public class PedidosRequest extends AsyncTask<Void, Void, JSONArray> {

        private static final String API_URL = "https://api.habtek.com.mx/somcback/tables/pedidos/getAll.php";

        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                // Establecer la conexión HTTP
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Leer la respuesta del servidor
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                bufferedReader.close();

                // Convertir la respuesta a un arreglo JSON
                return new JSONArray(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                // Procesar el arreglo JSON de pedidos
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject pedidoObj = response.getJSONObject(i);
                        // Obtener los datos del pedido
                        String idPedido = pedidoObj.getString("ID_pedido");
                        String Cliente = pedidoObj.getString("Nombre_cliente");
                        String fechaPedido = pedidoObj.getString("Fecha_pedido");
                        String estado = pedidoObj.getString("Estado");

                        /* Obtener los detalles del pedido
                        JSONArray detallesArray = pedidoObj.getJSONArray("detalles");
                        for (int j = 0; j < detallesArray.length(); j++) {
                            JSONObject detalleObj = detallesArray.getJSONObject(j);
                            // Obtener los datos del detalle del pedido
                            String idDetalle = detalleObj.getString("ID_detalle");
                            String idFormula = detalleObj.getString("ID_formula");
                            String cantidad = detalleObj.getString("Cantidad");

                            // Hacer lo que necesites con los datos del detalle del pedido
                            // Ejemplo: Imprimir los datos del detalle del pedido
                            Log.d("Detalle Pedido", "ID Pedido: " + idPedido + ", ID Detalle: " + idDetalle + ", ID Fórmula: " + idFormula + ", Cantidad: " + cantidad);
                        }
                        */
                        pedidosData_list.add(new pedidosData(idPedido, Cliente, fechaPedido, estado));
                        adapter2.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Manejar el caso en que el objeto JSON no tenga las claves esperadas
                        // ...
                    }
                }
            } else {
                // Manejar el caso en que no se pudo obtener una respuesta del servidor
                // ...
            }
        }


    }
}
