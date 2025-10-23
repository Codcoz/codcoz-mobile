package com.sustria.codcoz.api.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.ReceitaResponse;

import java.util.ArrayList;
import java.util.List;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder> {
    private List<ReceitaResponse.IngredienteApi> ingredientesApi = new ArrayList<>();

    public void setIngredientes(List<ReceitaResponse.IngredienteApi> ingredientesApi) {
        this.ingredientesApi = ingredientesApi;
        notifyDataSetChanged();
    }

    @Override
    public IngredienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingrediente, parent, false);
        return new IngredienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredienteViewHolder holder, int position) {
        if (!ingredientesApi.isEmpty()) {
            ReceitaResponse.IngredienteApi ingrediente = ingredientesApi.get(position);
            holder.tvNomeIngrediente.setText(ingrediente.getNome());
            holder.tvQuantidadeIngrediente.setText(ingrediente.getQuantidade() + " " + ingrediente.getUnidade_medida());
        } else {
            // excecao de erro para quando nao houver ingredientes
            throw new IllegalArgumentException("Ingredientes nao encontrados");
        }
    }

    @Override
    public int getItemCount() {
        return ingredientesApi.size();
    }

    static class IngredienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeIngrediente;
        TextView tvQuantidadeIngrediente;

        IngredienteViewHolder(View itemView) {
            super(itemView);
            tvNomeIngrediente = itemView.findViewById(R.id.tvNomeIngrediente);
            tvQuantidadeIngrediente = itemView.findViewById(R.id.tvQuantidadeIngrediente);
        }
    }
}