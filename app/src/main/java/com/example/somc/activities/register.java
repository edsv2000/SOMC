package com.example.somc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.somc.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        setRegister();
    }

    private void setRegister() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre_usuario = binding.name.getText().toString();
                String contrasena = binding.password.getText().toString();
                String confirmContrasena = binding.confirmPassword.getText().toString();
                String puesto = binding.puesto.getText().toString();
                String correo_electronico = binding.email.getText().toString();
                String numero_telefono = binding.telefono.getText().toString();

                if (!nombre_usuario.isEmpty() && !contrasena.isEmpty() && !puesto.isEmpty() && !correo_electronico.isEmpty() && !numero_telefono.isEmpty() && contrasena.equals(confirmContrasena) ) {

                    // Crear el objeto JSON con la estructura completa
                    JSONObject json = new JSONObject();
                    try {

                        json.put("nombre_usuario", nombre_usuario);
                        json.put("contrasena", contrasena);
                        json.put("puesto", puesto);
                        json.put("correo_electronico", correo_electronico);
                        json.put("numero_telefono", numero_telefono);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "https://api.habtek.com.mx/somcback/tables/usuarios/insert.php";
                    String jsonString = json.toString();
                    InsertUserTask insertUserTask = new InsertUserTask(new InsertUserTask.InsertUserListener() {
                        @Override
                        public void onUserInserted(boolean success) {
                            if (success) {
                                Toast.makeText(register.this, "¡Bienvenido, "+nombre_usuario+" !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Main.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(register.this, "¡Error al registrar al usuario: Usuario ya existe!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    insertUserTask.execute(url, jsonString);

                }else{
                    Toast.makeText(register.this, "Verifique los datos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }

    private static class InsertUserTask extends AsyncTask<String, Void, Boolean> {

        private register.InsertUserTask.InsertUserListener listener;

        public InsertUserTask(register.InsertUserTask.InsertUserListener listener) {
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
                Log.d("InsertUserTask", "Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String response = responseBuilder.toString();
                Log.d("InsertUserTask", "Response: " + response);

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
                listener.onUserInserted(success);
            }
        }

        public interface InsertUserListener {
            void onUserInserted(boolean success);
        }
    }

}

