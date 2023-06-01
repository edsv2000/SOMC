package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.OCData;

import java.util.ArrayList;

public class adapterOC extends RecyclerView.Adapter<adapterOC.ViewHolder> {
    private ArrayList<OCData> datos;

    public adapterOC(ArrayList<OCData> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ordenes_de_compra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OCData oc = datos.get(position);
        holder.bindData(oc);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numeroOC;
        private TextView fechaOC;

        private TextView estado;

        public ViewHolder(View itemView) {
            super(itemView);
            numeroOC = itemView.findViewById(R.id.numero_pedido);
            fechaOC = itemView.findViewById(R.id.fecha_registrado);
            estado = itemView.findViewById(R.id.estado);
        }

        public void bindData(OCData data) {
            numeroOC.setText(data.getNumeroOC());
            fechaOC.setText(data.getFechaOC());
            estado.setText(data.getStatus());

        }
    }
}
