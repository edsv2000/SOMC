package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.proveedorData;

import java.util.ArrayList;

public class adapterProveedor extends RecyclerView.Adapter<adapterProveedor.ViewHolder> {
    private ArrayList<proveedorData> datos;

    public adapterProveedor(ArrayList<proveedorData> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_proveedores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        proveedorData proveedor = datos.get(position);
        holder.bindData(proveedor);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numero_pedido;
        private TextView nombre_empresa;
        private  TextView telefono_proveedor;

        private TextView email_proveedor;

        public ViewHolder(View itemView) {
            super(itemView);
            numero_pedido = itemView.findViewById(R.id.numero_pedido);
            nombre_empresa = itemView.findViewById(R.id.nombre_empresa);
            telefono_proveedor = itemView.findViewById(R.id.telefono_proveedor);
            email_proveedor = itemView.findViewById(R.id.email_proveedor);
        }

        public void bindData(proveedorData data) {
            numero_pedido.setText(data.getNumeroProveedor());
            nombre_empresa.setText(data.getNombreProveedor());
            telefono_proveedor.setText(data.getNumeroProveedor());
            email_proveedor.setText(data.getEmailProveedor());

        }
    }
}
