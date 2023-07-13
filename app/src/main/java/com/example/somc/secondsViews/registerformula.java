package com.example.somc.secondsViews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.somc.R;

public class registerformula extends AppCompatActivity {

    Button btnAdd, btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerformula);
        getSupportActionBar().hide();

        setup();
    }

    private void setup() {

        btnRegistrar = (Button) findViewById(R.id.button3);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });


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

                // Realiza las operaciones necesarias con los datos ingresados
            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();
    }

}