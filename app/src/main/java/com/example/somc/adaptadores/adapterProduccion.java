package com.example.somc.adaptadores;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.produccionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class adapterProduccion extends RecyclerView.Adapter<adapterProduccion.ViewHolder> {

    private ArrayList<produccionData> datos;
    private ListenerProduction listenerProduccion;

    public adapterProduccion(ArrayList<produccionData> datos) {
        this.datos = datos;
    }

    public void setListenerProduccion(ListenerProduction listener) {
        this.listenerProduccion = listener;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numero_produccion;
        private TextView numero_lote;
        private TextView numero_pedido;
        private TextView estado;
        private TableLayout details;
        private Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            numero_produccion = itemView.findViewById(R.id.numero_produccion);
            numero_lote = itemView.findViewById(R.id.numero_lote);
            numero_pedido = itemView.findViewById(R.id.numero_pedido);
            estado = itemView.findViewById(R.id.estado);
            details = itemView.findViewById(R.id.tableLayout);
            spinner = itemView.findViewById(R.id.spinnerOption);

            // Spinner status
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.status_produccion, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position != 0) { // Omitir el primer elemento si no es necesario (puede ser un hint)
                        String selectedItem = (String) adapterView.getItemAtPosition(position);
                        if (listenerProduccion != null) {
                            listenerProduccion.onListenerProduction(datos.get(getAdapterPosition()), selectedItem);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Acción cuando no se ha seleccionado ningún ítem
                }
            });
        }

        @SuppressLint("ResourceAsColor")
        public void bindData(produccionData data) {
            numero_produccion.setText(data.getNumeroProduccion());
            numero_lote.setText(data.getNumeroLote());
            numero_pedido.setText(data.getNumeroPedido());
            estado.setText(data.getStatus());


// Spinner status
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.status_produccion, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


            // Suponiendo que tienes un JSONArray llamado "jsonArray" con los datos
            try {
                JSONArray jsonArray = data.getDetails(); // Obtén el JSONArray desde donde lo estés obteniendo (por ejemplo, una API o una base de datos)



                // Agregar los datos específicos del JSONArray a la tabla
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("ID_formula");
                    String nombre = jsonObject.getString("Formula");
                    String ciudad = jsonObject.getString("Cantidad_formula");

                    TableRow tableRow = new TableRow(itemView.getContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );

                    tableRow.setLayoutParams(layoutParams);

                    // Crea celdas para "nombre" y "ciudad" y agrégales los datos
                    LinearLayout.LayoutParams cellLayoutParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f
                    );
                    cellLayoutParams.setMargins(1, 1, 1, 1);

                    // Crea celdas para "nombre" y "ciudad" y agrégales los datos
                    TextView textViewId = new TextView(itemView.getContext());
                    textViewId.setText(id);
                    textViewId.setPadding(10, 10, 10, 10);
                    textViewId.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.background_light));
                    textViewId.setGravity(Gravity.CENTER);
                    tableRow.addView(textViewId);

                    TextView textViewNombre = new TextView(itemView.getContext());
                    textViewNombre.setText(nombre);
                    textViewNombre.setPadding(10, 10, 10, 10);
                    textViewNombre.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.background_light));
                    textViewNombre.setGravity(Gravity.CENTER);
                    tableRow.addView(textViewNombre);

                    TextView textViewCiudad = new TextView(itemView.getContext());
                    textViewCiudad.setText(ciudad);
                    textViewCiudad.setPadding(10, 10, 10, 10);
                    textViewCiudad.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.background_light));
                    textViewCiudad.setGravity(Gravity.CENTER);
                    tableRow.addView(textViewCiudad);

                    details.addView(tableRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ListenerProduction {
        void onListenerProduction(produccionData data, String selectedItem);
    }
}
