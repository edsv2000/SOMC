package com.example.somc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somc.R;
import com.example.somc.data.newFormulaData;

import java.util.ArrayList;

public class adapterNewFormula extends RecyclerView.Adapter<adapterNewFormula.ViewHolder> {
    private ArrayList<newFormulaData> newFormulaData;
    private static FormulaListener formulaListener;

    public adapterNewFormula(ArrayList<newFormulaData> newFormulaData, FormulaListener formulaListener) {
        this.newFormulaData = newFormulaData;
        this.formulaListener = formulaListener;
    }

    @NonNull
    @Override
    public adapterNewFormula.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnewformula, parent, false);
        return new adapterNewFormula.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNewFormula.ViewHolder holder, int position) {
        newFormulaData formula = newFormulaData.get(position);
        holder.bindData(formula);
    }

    @Override
    public int getItemCount() {
        return newFormulaData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre;
        private EditText cantidad;
        private Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textView7);
            cantidad = itemView.findViewById(R.id.cantidad);
            deleteButton = itemView.findViewById(R.id.button4);
        }

        public void bindData(newFormulaData data) {
            nombre.setText(data.getName());
            cantidad.setText(data.getCantidad());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llama al método onFormulaClicked del listener
                    if (formulaListener != null) {
                        formulaListener.onFormulaClicked(data);
                    }
                }
            });
        }
    }

    // Interfaz de escucha para gestionar el clic en el botón de eliminación
    public interface FormulaListener {
        void onFormulaClicked(newFormulaData formula);
    }
}
