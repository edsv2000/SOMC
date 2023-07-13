package com.example.somc.adaptadores;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.formulasData;
import com.example.somc.secondsViews.detailsFormulas;

import java.util.ArrayList;

public class adapterFormulas extends RecyclerView.Adapter<adapterFormulas.ViewHolder> {
    private ArrayList<com.example.somc.data.formulasData> formulasData;
    private ArrayList<formulasData> formulasData_list;

    public adapterFormulas(ArrayList<com.example.somc.data.formulasData> formulasData) {
        this.formulasData = formulasData;
    }

    

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_formula, parent, false);

        // Crea un objeto ViewHolder y obtén la referencia del botón mediante findViewById
        ViewHolder viewHolder = new ViewHolder(itemView);

        // Configura el evento OnClickListener para el botón
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acciones a realizar cuando se hace clic en el botón del elemento del RecyclerView
                int position = viewHolder.getAdapterPosition();
                Log.d(TAG, String.valueOf(position));
                // Obtén el contexto de la actividad
                Context context = view.getContext();

                // Crea un Intent para la actividad de destino
                Intent intent = new Intent(context, detailsFormulas.class);

                // Agrega datos adicionales al Intent si es necesario
                intent.putExtra("clave", "clase");

                // Inicia la actividad de destino
                context.startActivity(intent);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.example.somc.data.formulasData formulas = formulasData.get(position);
        holder.bindData(formulas);
    }

    @Override
    public int getItemCount() {
        return formulasData.size();
    }

    public void setFilteredList(ArrayList<formulasData> filteredList) {
        formulasData.clear();
        formulasData.addAll(filteredList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button button;
        private TextView nombre_formula;
        private TextView fecha_registro;
        private TextView status;
        private ImageView imgFormula;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre_formula = itemView.findViewById(R.id.nombre_formula);
            fecha_registro = itemView.findViewById(R.id.fecha_registrado);
            status = itemView.findViewById(R.id.estado);
            imgFormula = itemView.findViewById(R.id.img_formula);
            button = itemView.findViewById(R.id.btnSee);

        }

        public void bindData(com.example.somc.data.formulasData data) {
            nombre_formula.setText(data.getName());
            fecha_registro.setText(data.getRegister());
            status.setText(data.getStatus());
            imgFormula.setImageResource(data.getImgFormula());
        }
    }
}
