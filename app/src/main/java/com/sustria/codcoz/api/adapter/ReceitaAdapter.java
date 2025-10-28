package com.sustria.codcoz.api.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.actions.DetalhesReceitaActivity;
import com.sustria.codcoz.api.model.ReceitaResponse;

import java.util.ArrayList;
import java.util.List;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder> {
    private List<ReceitaResponse> receitas = new ArrayList<>();

    public void setReceitas(List<ReceitaResponse> receitas) {
        this.receitas = receitas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receita, parent, false);
        return new ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        ReceitaResponse receita = receitas.get(position);
        holder.tvNomeReceita.setText(receita.getNome());
        holder.tvPorcoes.setText(receita.getPorcoes() + " porções possíveis");

        // Configurar clique no item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetalhesReceitaActivity.class);
            intent.putExtra("RECEITA_API", receita);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    static class ReceitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeReceita;
        TextView tvPorcoes;

        ReceitaViewHolder(View itemView) {
            super(itemView);
            tvNomeReceita = itemView.findViewById(R.id.tvNome);
            tvPorcoes = itemView.findViewById(R.id.tvQuantidades);
        }
    }
}