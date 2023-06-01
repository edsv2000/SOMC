package com.example.somc.adaptadores;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;

import java.util.ArrayList;

public class adapterFormulas extends RecyclerView.Adapter<adapterFormulas.ViewHolder> {
    private ArrayList<com.example.somc.data.formulasData> formulasData;

    public adapterFormulas(ArrayList<com.example.somc.data.formulasData> formulasData) {
        this.formulasData = formulasData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_formula, parent, false);
        return new ViewHolder(view);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }

        public void bindData(com.example.somc.data.formulasData data) {
            nombre_formula.setText(data.getName());
            fecha_registro.setText(data.getRegister());
            status.setText(data.getStatus());
            imgFormula.setImageResource(data.getImgFormula());
        }
    }
}
