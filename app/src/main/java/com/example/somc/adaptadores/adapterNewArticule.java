package com.example.somc.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.formulasData;

import java.util.ArrayList;

public class adapterNewArticule extends RecyclerView.Adapter<adapterNewArticule.ViewHolder> {
    private ArrayList<formulasData> formulasData;
    private Context context;
    private ArticuleListener articuleListener;

    // ...

    public adapterNewArticule(ArrayList<formulasData> formulasData, Context context) {
        this.formulasData = formulasData;
        this.context = context;
    }

    public void setArticuleListener(ArticuleListener listener) {
        articuleListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_articule, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        formulasData articule = formulasData.get(position);
        holder.bindData(articule);
    }

    @Override
    public int getItemCount() {
        return formulasData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreArticule;
        private TextView id;
        private EditText cantidad;
        private Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            nombreArticule = itemView.findViewById(R.id.nombre);
            cantidad = itemView.findViewById(R.id.cantidad);
            delete = itemView.findViewById(R.id.delate);
        }

        public void bindData(formulasData data) {
            nombreArticule.setText(data.getName());
            id.setText(data.getId());
            cantidad.setText(data.getStatus());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llama al método onArticuleClicked del listener
                    if (articuleListener != null) {
                        articuleListener.onArticuleClicked(data);
                    }
                }
            });
        }
    }

    public interface ArticuleListener {
        void onArticuleClicked(formulasData articule);
    }

}
