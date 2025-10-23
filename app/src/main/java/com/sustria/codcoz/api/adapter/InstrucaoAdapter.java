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

public class InstrucaoAdapter extends RecyclerView.Adapter<InstrucaoAdapter.InstrucaoViewHolder> {
    private List<ReceitaResponse.ModoPreparoApi> instrucoesApi = new ArrayList<>();

    public void setInstrucoes(List<ReceitaResponse.ModoPreparoApi> instrucoesApi) {
        this.instrucoesApi = instrucoesApi;
        notifyDataSetChanged();
    }

    @Override
    public InstrucaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instrucao, parent, false);
        return new InstrucaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstrucaoViewHolder holder, int position) {
        if (!instrucoesApi.isEmpty()) {
            ReceitaResponse.ModoPreparoApi instrucao = instrucoesApi.get(position);
            holder.tvNumeroInstrucao.setText(String.valueOf(instrucao.getOrdem()));
            holder.tvTextoInstrucao.setText(instrucao.getPasso());
        } else {
            // excecao de erro para quando nao houver ingredientes
            throw new IllegalArgumentException("Instrucoes nao encontradas");

        }
    }

    @Override
    public int getItemCount() {
        return instrucoesApi.size();
    }

    static class InstrucaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroInstrucao;
        TextView tvTextoInstrucao;

        InstrucaoViewHolder(View itemView) {
            super(itemView);
            tvNumeroInstrucao = itemView.findViewById(R.id.tvNumeroInstrucao);
            tvTextoInstrucao = itemView.findViewById(R.id.tvTextoInstrucao);
        }
    }
}
