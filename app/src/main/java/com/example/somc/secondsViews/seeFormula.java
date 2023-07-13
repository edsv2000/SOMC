package com.example.somc.secondsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class seeFormula extends AppCompatActivity {

    private static final String TAG = "seeFormula";
    private static final String API_URL = "http://api.habtek.com.mx/somcback/tables/formulas/search.php";

    private EditText idFormulaEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_formula);

        getFormulaById("1");
    }

    private void getFormulaById(String idFormula) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    JSONObject requestData = new JSONObject();
                    requestData.put("idFormula", idFormula);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(requestData.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    Log.d(TAG, "Response Code: " + responseCode);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    String responseData = responseBuilder.toString();
                    Log.d(TAG, "Response: " + responseData);

                    JSONObject jsonObject = new JSONObject(responseData);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONObject formulaData = jsonObject.getJSONObject("data");
                        final String descripcion = formulaData.getString("descripcion");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(seeFormula.this, descripcion, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        final String message = jsonObject.getString("message");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(seeFormula.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(seeFormula.this, "Request failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
