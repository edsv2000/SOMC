package com.example.somc.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterInventario;
import com.example.somc.data.inventarioData;
import com.example.somc.secondsViews.registerInventario;

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

public class inventario extends Fragment {

    private ArrayList<inventarioData> inventarioData_list = new ArrayList<>();
    private adapterInventario adapter;

    private Button add,ajuste;

    public inventario() {
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
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerInventario);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new adapterInventario(inventarioData_list, getContext()); // Mover la inicialización del adaptador aquí
        recyclerView.setAdapter(adapter);

        // Realizar la consulta a la API para obtener los datos del inventario
        new GetInventarioTask().execute();

        add = (Button) view.findViewById(R.id.button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), registerInventario.class);
                startActivity(intent);
            }
        });

        ajuste = (Button) view.findViewById(R.id.button5);

        ajuste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });




        return view;

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_ajust, null);
        builder.setView(dialogView);

        // Obtén las referencias a los elementos de interfaz de usuario en el diálogo
        EditText editText1 = dialogView.findViewById(R.id.editTextID);
        EditText editText2 = dialogView.findViewById(R.id.editTextCantidad);

        // Agrega botones u otros elementos de interfaz de usuario según sea necesario

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar cuando se hace clic en el botón "Guardar"
                String input1 = editText1.getText().toString();
                String input2 = editText2.getText().toString();

                if (!input1.isEmpty() && !input2.isEmpty()) {




                    // Crear el objeto JSON con los datos a enviar
                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonData.put("id_ingrediente", 1);
                        jsonData.put("cantidad", 10);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    inventario.UpdateInventoryTask task = new inventario.UpdateInventoryTask(new UpdateInventoryTask.OnUpdateInventoryListener() {
                        @Override
                        public void onUpdateInventorySuccess(String message) {

                        }

                        @Override
                        public void onUpdateInventoryError(String error) {

                        }

                        @Override
                        public void onIngredientByIdError(String message) {
                            // Manejar el error
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        });

                    task.execute(jsonData);

                } else {
                    Toast.makeText(getContext(), "Faltan datos por llenar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class GetInventarioTask extends AsyncTask<Void, Void, ArrayList<inventarioData>> {
        @Override
        protected ArrayList<inventarioData> doInBackground(Void... voids) {
            // Realizar la llamada a la API y obtener la respuesta JSON
            String apiUrl = "http://api.habtek.com.mx/somcback/tables/inventario/getAll.php";
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

            ArrayList<inventarioData> inventarioList = new ArrayList<>();

            if (response != null && !response.toString().isEmpty()) {
                try {
                    // Procesar la respuesta JSON y agregar los datos a la lista
                    JSONObject jsonObject = new JSONObject(response.toString());
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            String id = dataObject.getString("ID_ingrediente");
                            String Nombre_ingrediente = dataObject.getString("Nombre_ingrediente");
                            String Unidad_medida = dataObject.getString("Unidad_medida");
                            String Cantidad = dataObject.getString("Cantidad");
                            String Precio_unitario = dataObject.getString("Precio_unitario");
                            inventarioList.add(new inventarioData(id, Nombre_ingrediente, Unidad_medida, Cantidad, Precio_unitario));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return inventarioList;
        }


        @Override
        protected void onPostExecute(ArrayList<inventarioData> inventarioList) {
            if (inventarioList != null) {
                inventarioData_list.clear();
                inventarioData_list.addAll(inventarioList);

                if (inventarioList.size() == 0) {
                    // Mostrar un mensaje de "sin resultados" si no hay datos en el inventario
                    // Puedes agregar un elemento especial a la lista para mostrar el mensaje
                    inventarioData_list.add(new inventarioData("No hay ingredientes disponibles", "", "", "", ""));
                }

                adapter.notifyDataSetChanged();
            }
        }
    }

    public static class UpdateInventoryTask extends AsyncTask<JSONObject, Void, String> {


        private static final String SERVER_URL = "http://api.habtek.com.mx/somcback/tables/ingredientes/update.php";

        private OnUpdateInventoryListener listener;

        public UpdateInventoryTask(OnUpdateInventoryListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            if (jsonObjects.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Crear la conexión HTTP
                URL url = new URL(SERVER_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Obtener el objeto JSON para enviar
                JSONObject jsonData = jsonObjects[0];

                // Escribir el objeto JSON en el cuerpo de la solicitud
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(jsonData.toString().getBytes("UTF-8"));
                outputStream.close();

                // Leer la respuesta del servidor
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            } catch (IOException e) {
                Log.d( "Error en la solicitud HTTP: " , e.getMessage());
            } finally {
                // Cerrar la conexión y los flujos de lectura
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.d("Error al cerrar el lector de la respuesta: " , e.getMessage());
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Procesar la respuesta del servidor
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String message = jsonResponse.getString("message");
                    if (success) {
                        listener.onUpdateInventorySuccess(message);
                    } else {
                        listener.onUpdateInventoryError(message);
                    }
                } catch (JSONException e) {
                    Log.d( "Error al analizar la respuesta JSON: " , e.getMessage());
                    listener.onUpdateInventoryError("Error en la respuesta del servidor");
                }
            } else {
                listener.onUpdateInventoryError("Error en la solicitud HTTP");
            }
        }

        public interface OnUpdateInventoryListener {
            void onUpdateInventorySuccess(String message);
            void onUpdateInventoryError(String error);

            void onIngredientByIdError(String message);
        }
    }
}
