package com.example.somc.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.formulasData;
import com.example.somc.secondsViews.seeFormula;


import java.util.ArrayList;

public class adapterFormulas extends RecyclerView.Adapter<adapterFormulas.ViewHolder> {
    private ArrayList<formulasData> formulasData;
    private Context context;

    public adapterFormulas(ArrayList<formulasData> formulasData, Context context) {
        this.formulasData = formulasData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_formula, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        formulasData formulas = formulasData.get(position);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombre_formula;
        private TextView fecha_registro;
        private TextView status;
        private ImageView imgFormula;
        private Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre_formula = itemView.findViewById(R.id.nombre_formula);
            fecha_registro = itemView.findViewById(R.id.fecha_registrado);
            status = itemView.findViewById(R.id.estado);
            imgFormula = itemView.findViewById(R.id.img_formula);

        }

        public void bindData(formulasData data) {
            nombre_formula.setText(data.getName());
            fecha_registro.setText(data.getRegister());
            status.setText(data.getStatus());
            imgFormula.setImageResource(data.getImgFormula());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                formulasData formulas = formulasData.get(position);

                // Crear un Intent para la actividad de destino
                Intent intent = new Intent(context, seeFormula.class);

                // Agrega datos adicionales al Intent si es necesario
                intent.putExtra("clave", "clase");

                // Inicia la actividad de destino
                context.startActivity(intent);
            }
        }
    }
}
