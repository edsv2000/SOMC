package com.example.somc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.somc.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        setup();
    }

    private void setup() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = binding.password.getText().toString();
                String email = binding.user.getText().toString();

                if (pass.isEmpty() || email.isEmpty()) {
                    Toast.makeText(login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    login(pass, email);
                    Toast.makeText(login.this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });
    }

    private void login(String pass, String email) {
        String url = "https://api.habtek.com.mx/somcback/tables/usuarios/login.php";
        String jsonInputString = "{\"contrasena\":\"" + pass + "\",\"correo_electronico\":\"" + email + "\"}";

        new ApiCallTask(email).execute(url, jsonInputString);
    }

    private class ApiCallTask extends AsyncTask<String, Void, String> {
        private String email;

        public ApiCallTask(String email) {
            this.email = email;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String jsonInputString = params[1];

            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.writeBytes(jsonInputString);
                    outputStream.flush();
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String response;
                    StringBuilder responseBuilder = new StringBuilder();
                    while ((response = reader.readLine()) != null) {
                        responseBuilder.append(response);
                    }
                    return responseBuilder.toString();
                }
            } catch (IOException e) {
                Log.e("API Error", "Error durante la llamada a la API", e);
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null && !response.startsWith("Error")) {
                try {
                    // Analiza la cadena de texto en un objeto JSON
                    JSONObject jsonObject = new JSONObject(response);

                    // Obtén el valor booleano del campo "success"
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        // El resultado es verdadero (true)
                        // Realiza las acciones necesarias para pasar a la otra pantalla
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // El resultado es falso (false)
                        // Puedes mostrar un mensaje de error o realizar alguna otra acción
                        Toast.makeText(login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Maneja cualquier excepción que pueda ocurrir durante el análisis del JSON
                }
            } else {
                Log.d("API Response", "Error: " + response);
            }
        }

    }
}
