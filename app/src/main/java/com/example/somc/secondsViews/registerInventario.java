package com.example.somc.secondsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class registerInventario extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText descripcionEditText;
    private EditText unidadMedidaEditText;
    private EditText cantidadEditText;
    private EditText precioUnitarioEditText;

    private Button agregarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_inventario);
        getSupportActionBar().hide();
        nombreEditText = findViewById(R.id.editTextDescripcion);
        descripcionEditText = findViewById(R.id.editTextDescripcion2);
        unidadMedidaEditText = findViewById(R.id.editTextDescripcion3);
        cantidadEditText = findViewById(R.id.editTextDescripcion4);
        precioUnitarioEditText = findViewById(R.id.editTextDescripcion5);

        agregarButton = findViewById(R.id.button2);
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarInventario();
            }
        });
    }

    private void agregarInventario() {
        String nombre = nombreEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();
        String unidadMedida = unidadMedidaEditText.getText().toString();
        String proveedor = "1";
        String cantidadText = cantidadEditText.getText().toString();
        String precioUnitarioText = precioUnitarioEditText.getText().toString();

        if (!nombre.isEmpty() && !descripcion.isEmpty() && !unidadMedida.isEmpty()
                && !cantidadText.isEmpty() && !precioUnitarioText.isEmpty()) {
            double cantidad = Double.parseDouble(cantidadText);
            double precioUnitario = Double.parseDouble(precioUnitarioText);

            JSONObject inventarioData = new JSONObject();
            try {
                inventarioData.put("nombre_ingrediente", nombre);
                inventarioData.put("descripcion", descripcion);
                inventarioData.put("unidad_medida", unidadMedida);
                inventarioData.put("proveedor", proveedor);
                inventarioData.put("cantidad", cantidad);
                inventarioData.put("precio_unitario", precioUnitario);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new AddInventarioTask().execute(inventarioData.toString());
        }else{
            Toast.makeText(this, "Verifique que no haya campos vacios", Toast.LENGTH_SHORT).show();
        }

    }

    private class AddInventarioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonInputString = params[0];

            try {
                URL url = new URL("http://api.habtek.com.mx/somcback/src/tables/ingredientes/insert.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonInputString.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                Log.d("AddInventarioTask", "Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                return responseBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success) {
                        Toast.makeText(registerInventario.this, message, Toast.LENGTH_SHORT).show();
                        // Realizar cualquier acción adicional después de agregar el inventario correctamente
                        finish();
                    } else {
                        Toast.makeText(registerInventario.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(registerInventario.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(registerInventario.this, "No response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
