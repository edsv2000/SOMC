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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somc.R;
import com.example.somc.adaptadores.adapterNewFormula;
import com.example.somc.data.newFormulaData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class registerformula extends AppCompatActivity implements adapterNewFormula.FormulaListener {

    private ArrayList<newFormulaData> formulaData = new ArrayList<>();
    private static adapterNewFormula adapter;

    Button btnAdd, btnRegistrar;
    EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerformula);
        getSupportActionBar().hide();

        setup();
    }

    private void setup() {
        btnAdd = findViewById(R.id.button3);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
        btnRegistrar = findViewById(R.id.button2);
        descripcion = findViewById(R.id.editTextDescripcion);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descripcion = findViewById(R.id.editTextDescripcion);
                String datadescripcion = descripcion.getText().toString();

                if (datadescripcion.isEmpty())  {
                    Toast.makeText(registerformula.this, "Ingrese el nombre de la formula", Toast.LENGTH_SHORT).show();
                } else if (formulaData.isEmpty()) {
                    Toast.makeText(registerformula.this, "La lista de ingredientes está vacía", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray ingredientesArray = new JSONArray();

                    for (newFormulaData data : formulaData) {
                        try {
                            JSONObject ingredienteObj = new JSONObject();
                            ingredienteObj.put("idIngrediente", data.getId());
                            ingredienteObj.put("cantidad", data.getCantidad());

                            ingredientesArray.put(ingredienteObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Crear el objeto JSON con la estructura completa
                    JSONObject json = new JSONObject();
                    try {
                        json.put("descripcion", datadescripcion);
                        json.put("idUsuario", "1");
                        json.put("ingredientes", ingredientesArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "http://api.habtek.com.mx/somcback/src/tables/formulas/insert.php";
                    String jsonString = json.toString();

                    InsertFormulaTask insertFormulaTask = new InsertFormulaTask(new InsertFormulaTask.InsertFormulaListener() {
                        @Override
                        public void onFormulaInserted(boolean success) {
                            if (success) {
                                Toast.makeText(registerformula.this, "Fórmula insertada exitosamente", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(registerformula.this, "Error al insertar la fórmula", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    insertFormulaTask.execute(url, jsonString);
                }

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new adapterNewFormula(formulaData, this);
        recyclerView.setAdapter(adapter);

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_register_formula, null);
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
                    for (newFormulaData data : formulaData) {
                        if (data.getId().equals(input1)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        Toast.makeText(registerformula.this, "El ingrediente ya existe en la lista", Toast.LENGTH_SHORT).show();
                    } else {
                        // Guardar en recycler
                        GetIngredientByIdTask task = new GetIngredientByIdTask(new GetIngredientByIdTask.GetIngredientByIdListener() {
                            @Override
                            public void onIngredientByIdError(String message) {
                                // Manejar el error
                                Toast.makeText(registerformula.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }, formulaData, input2);

                        task.execute("https://api.habtek.com.mx/somcback/src/tables/ingredientes/search.php", input1);
                    }
                } else {
                    Toast.makeText(registerformula.this, "Faltan datos por llenar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onFormulaClicked(newFormulaData formula) {
        // Elimina el elemento de la lista
        formulaData.remove(formula);
        adapter.notifyDataSetChanged();
    }

    //INSERTAR FORMULA
    private static class InsertFormulaTask extends AsyncTask<String, Void, Boolean> {

        private InsertFormulaListener listener;

        public InsertFormulaTask(InsertFormulaListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String urlStr = params[0];
            String jsonInputString = params[1];

            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(jsonInputString.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                Log.d("InsertFormulaTask", "Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String response = responseBuilder.toString();
                Log.d("InsertFormulaTask", "Response: " + response);

                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (listener != null) {
                listener.onFormulaInserted(success);
            }
        }

        public interface InsertFormulaListener {
            void onFormulaInserted(boolean success);
        }
    }

    //BUSCAR INGREDIENTE
    private static class GetIngredientByIdTask extends AsyncTask<String, Void, String> {

        private GetIngredientByIdListener listener;
        private ArrayList<newFormulaData> formulaData;
        private String cantidad;

        public GetIngredientByIdTask(GetIngredientByIdListener listener, ArrayList<newFormulaData> formulaData, String cantidad) {
            this.listener = listener;
            this.formulaData = formulaData;
            this.cantidad = cantidad;
        }

        @Override
        protected String doInBackground(String... params) {
            String urlStr = params[0];
            String idIngrediente = params[1];

            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject requestData = new JSONObject();
                requestData.put("idIngrediente", idIngrediente);

                String jsonInputString = requestData.toString();

                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(jsonInputString.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                Log.d("GetIngredientByIdTask", "Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                return responseBuilder.toString();
            } catch (IOException | JSONException e) {
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

                    if (success) {
                        JSONObject ingredientData = jsonObject.getJSONObject("data");
                        String idIngrediente = ingredientData.getString("ID_ingrediente");
                        String nombreIngrediente = ingredientData.getString("Nombre_ingrediente");

                        newFormulaData newData = new newFormulaData(nombreIngrediente, idIngrediente, cantidad);
                        formulaData.add(newData);
                        adapter.notifyDataSetChanged();
                    } else {
                        String message = jsonObject.getString("message");
                        listener.onIngredientByIdError(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onIngredientByIdError("Error parsing JSON response");
                }
            } else {
                listener.onIngredientByIdError("No response from server");
            }
        }

        public interface GetIngredientByIdListener {
            void onIngredientByIdError(String message);
        }
    }
}
