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
import com.example.somc.data.inventarioData;
import com.example.somc.secondsViews.seeFormula;


import java.util.ArrayList;

public class adapterInventario extends RecyclerView.Adapter<adapterInventario.ViewHolder> {
    private ArrayList<inventarioData> inventarioData;
    private Context context;

    public adapterInventario(ArrayList<inventarioData> inventarioData, Context context) {
        this.inventarioData = inventarioData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_inventario, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        inventarioData inventariod = inventarioData.get(position);
        holder.bindData(inventariod);
    }

    @Override
    public int getItemCount() {
        return inventarioData.size();
    }

    public void setFilteredList(ArrayList<inventarioData> filteredList) {
        inventarioData.clear();
        inventarioData.addAll(filteredList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id;

        private TextView nombre;

        private TextView medida;

        private TextView cantidad;

        private TextView precio_unitario;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            nombre = itemView.findViewById(R.id.nombre);
            medida = itemView.findViewById(R.id.medida);
            cantidad= itemView.findViewById(R.id.cantidad);
            precio_unitario = itemView.findViewById(R.id.precio);

        }

        public void bindData(inventarioData data) {
            id.setText(data.getId());
            nombre.setText(data.getNombre());
            medida.setText(data.getMedida());
            cantidad.setText(data.getCantidad());
            precio_unitario.setText(data.getPrecio_unitario());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

            }
        }
    }
}
