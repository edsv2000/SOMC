package com.example.somc.secondsViews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterNewArticule;
import com.example.somc.data.formulasData;
import com.example.somc.data.newFormulaData;
import com.example.somc.data.pedidosData;
import com.example.somc.databinding.ActivityRegisterPedidoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class registerPedido extends AppCompatActivity implements adapterNewArticule.ArticuleListener {

    ActivityRegisterPedidoBinding binding;
    private ArrayList<formulasData> formulaDatos = new ArrayList<>();
    private adapterNewArticule adapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPedidoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        setup();
    }

    private void setup() {
        // Spinner status
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_pedido, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);


        binding.recyclerRegisterPedido.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterRecycler = new adapterNewArticule(formulaDatos, registerPedido.this);
        adapterRecycler.setArticuleListener(this);
        binding.recyclerRegisterPedido.setAdapter(adapterRecycler);

        binding.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        binding.btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idClienteText = binding.cliente.getText().toString();
                String estado = binding.spinner.getSelectedItem().toString();
                String idUsuarioText = "1";

                // Validar que no haya campos vacíos
                if (idClienteText.isEmpty() || estado.isEmpty() || idUsuarioText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    int idCliente = Integer.parseInt(idClienteText);
                    int idUsuario = Integer.parseInt(idUsuarioText);

                    JSONArray detallesArray = new JSONArray();
                    for (formulasData detalle : formulaDatos) {
                        String idProducto = detalle.getId();
                        String cantidad = detalle.getStatus();

                        // Validar que no haya campos vacíos en los detalles
                        if (idProducto.isEmpty() || cantidad.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Todos los campos de los detalles son requeridos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            JSONObject detalleObject = new JSONObject();
                            detalleObject.put("ID_producto", idProducto);
                            detalleObject.put("Cantidad", cantidad);
                            detallesArray.put(detalleObject);
                        } catch (JSONException e) {
                            Toast.makeText(registerPedido.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    PostAsyncTask postAsyncTask = new PostAsyncTask(idCliente, estado, idUsuario, detallesArray);
                    postAsyncTask.execute();
                }
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_add_articule, null);
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

                    // Validar si el elemento ya existe en la lista
                    boolean exists = false;
                    for (formulasData data : formulaDatos) {
                        if (data.getId().equals(input1)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        Toast.makeText(registerPedido.this, "El artículo ya existe en la lista", Toast.LENGTH_SHORT).show();
                    } else {
                        String idFormula = input1; // El ID de la fórmula que deseas obtener
                        FormulasRequest request = new FormulasRequest(input1, input2, formulaDatos);
                        request.execute(idFormula);
                    }
                } else {
                    Toast.makeText(registerPedido.this, "Faltan datos por llenar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onArticuleClicked(formulasData formula) {
        // Elimina el elemento de la lista
        formulaDatos.remove(formula);
        adapterRecycler.notifyDataSetChanged();
    }

    public class PostAsyncTask extends AsyncTask<Void, Void, String> {
        private static final String TAG = "PostAsyncTask";
        private static final String API_URL = "https://api.habtek.com.mx/somcback/src/tables/pedidos/insert.php"; // Reemplaza con la URL correcta

        private int idCliente;
        private String estado;
        private int idUsuario;
        private JSONArray detallesArray;

        public PostAsyncTask(int idCliente, String estado, int idUsuario, JSONArray detallesArray) {
            this.idCliente = idCliente;
            this.estado = estado;
            this.idUsuario = idUsuario;
            this.detallesArray = detallesArray;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response = null;

            try {
                URL url = new URL(API_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                // Construir el objeto JSON
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("ID_cliente", idCliente);
                jsonRequest.put("Estado", estado);
                jsonRequest.put("ID_usuario", idUsuario);
                jsonRequest.put("detalles", detallesArray);

                // Escribir los datos JSON en el cuerpo de la solicitud
                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                outputStream.write(jsonRequest.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                // Obtener la respuesta del servidor
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    response = buffer.toString();
                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error en la solicitud HTTP: " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error al cerrar el lector de la respuesta HTTP: " + e.getMessage());
                    }
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // Aquí puedes manejar la respuesta del servidor
            if (response != null) {

                try {
                    JSONObject responseJson = new JSONObject(response);
                    String message = responseJson.getString("message");
                    // Utiliza la variable "message" como necesites
                    Toast.makeText(registerPedido.this,  message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Manejo de errores al analizar el JSON
                }
            }
        }
    }
    public class FormulasRequest extends AsyncTask<String, Void, JSONObject> {

        private static final String API_URL = "https://api.habtek.com.mx/somcback/src/tables/formulas/search.php";

        private String idFormula;
        private String cantidad;
        private ArrayList<formulasData> formulaDatosPost;

        public FormulasRequest(String idFormula, String cantidad, ArrayList<formulasData> formulaDatos) {
            this.idFormula = idFormula;
            this.cantidad = cantidad;
            this.formulaDatosPost = formulaDatos;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                // Construir el objeto JSON con los datos a enviar
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("idFormula", params[0]);

                // Establecer la conexión HTTP
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Escribir los datos JSON en el cuerpo de la solicitud
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonParam.toString().getBytes("UTF-8"));
                outputStream.close();

                // Leer la respuesta del servidor
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                bufferedReader.close();

                // Convertir la respuesta a un objeto JSON
                return new JSONObject(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        JSONObject formulaDataObj = response.getJSONObject("formulaData");
                        JSONArray ingredientesArray = response.getJSONArray("ingredientes");

                        formulaDatosPost.add(new formulasData(formulaDataObj.getString("ID_formula"), formulaDataObj.getString("Descripcion"), "", "", cantidad, 1));
                        adapterRecycler.notifyDataSetChanged(); // Notificar al adaptador sobre el cambio en los datos
                    } else {
                        String message = response.getString("message");
                        Toast.makeText(registerPedido.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Manejar el caso en que la respuesta no tenga el formato esperado
                    // ...
                }
            } else {
                // Manejar el caso en que no se pudo obtener una respuesta del servidor
                // ...
            }
        }
    }
}
