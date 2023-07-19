package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.pedidosData;

import java.util.ArrayList;

public class adapterPedidos extends RecyclerView.Adapter<adapterPedidos.ViewHolder> {
    private ArrayList<pedidosData> datos;
    private static ListenerPedido listenerPedido;

    public adapterPedidos(ArrayList<pedidosData> datos) {
        this.datos = datos;
    }

    public void setListenerPedido(ListenerPedido listener) {
        this.listenerPedido = listener;
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

        private Spinner option;

        public ViewHolder(View itemView) {
            super(itemView);
            numero_pedido = itemView.findViewById(R.id.numero_pedido);
            empresa = itemView.findViewById(R.id.nombre_empresa);
            fecha = itemView.findViewById(R.id.fecha_registrado);
            status = itemView.findViewById(R.id.estado);
            option = itemView.findViewById(R.id.spinnerOption);
        }

        public void bindData(pedidosData data) {
            numero_pedido.setText(data.getNumero());
            fecha.setText(data.getFecha());
            empresa.setText(data.getEmpresa());
            status.setText(data.getStatus());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_spinner_item) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if (position == 0) {
                        view.setVisibility(View.GONE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    return view;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    if (position == 0) {
                        view.setEnabled(false);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.add("Mis opciones");

            if (data.getStatus().equals("Pendiente")) {
                adapter.add("Autorizar pedido");
            } else if (data.getStatus().equals("Autorizado")) {
                adapter.add("Mandar a producción");
            }
            adapter.add("Cancelar");

            option.setAdapter(adapter);

            option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                private boolean isFirstSelection = true;

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (isFirstSelection) {
                        isFirstSelection = false;
                    } else {
                        String selectedItem = (String) adapterView.getItemAtPosition(position);
                        if (listenerPedido != null) {
                            listenerPedido.onListenerPedido(data, selectedItem);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Acción cuando no se ha seleccionado ningún ítem
                }
            });
        }

    }

    public interface ListenerPedido {
        void onListenerPedido(pedidosData data, String selectedItem);
    }
}
