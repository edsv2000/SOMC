package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.pedidosData;

import java.util.ArrayList;

public class adapterPedidos extends RecyclerView.Adapter<adapterPedidos.ViewHolder> {
    private ArrayList<pedidosData> datos;

    public adapterPedidos(ArrayList<pedidosData> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        pedidosData pedido = datos.get(position);
        holder.bindData(pedido);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numero_pedido;
        private TextView empresa;
        private TextView fecha;
        private TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            numero_pedido = itemView.findViewById(R.id.numero_pedido);
            empresa = itemView.findViewById(R.id.nombre_empresa);
            fecha = itemView.findViewById(R.id.fecha_registrado);
            status = itemView.findViewById(R.id.estado);
        }

        public void bindData(pedidosData data) {
            numero_pedido.setText(data.getNumero());
            fecha.setText(data.getFecha());
            empresa.setText(data.getEmpresa());
           status.setText(data.getStatus());
        }
    }
}
