package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.produccionData;

import java.util.ArrayList;

public class adapterProduccion extends RecyclerView.Adapter<adapterProduccion.ViewHolder> {
    private ArrayList<produccionData> datos;

    public adapterProduccion(ArrayList<produccionData> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_produccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        produccionData produccion = datos.get(position);
        holder.bindData(produccion);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numero_produccion;
        private TextView numero_lote;
        private TextView numero_pedido;
        private TextView estado;

        public ViewHolder(View itemView) {
            super(itemView);
            numero_produccion = itemView.findViewById(R.id.numero_produccion);
            numero_lote = itemView.findViewById(R.id.numero_lote);
            numero_pedido = itemView.findViewById(R.id.numero_pedido);
            estado = itemView.findViewById(R.id.estado);
        }

        public void bindData(produccionData data) {
            numero_produccion.setText(data.getNumeroProduccion());
            numero_lote.setText(data.getNumeroLote());
            numero_pedido.setText(data.getNumeroPedido());
            estado.setText(data.getStatus());

        }
    }
}
