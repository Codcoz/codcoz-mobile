package com.sustria.codcoz.api.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.TarefaResponse;

import java.util.ArrayList;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<TarefaResponse> tarefas = new ArrayList<>();
    private OnTarefaClickListener listener;

    public interface OnTarefaClickListener {
        void onTarefaClick(TarefaResponse tarefa);

        void onRegistrarClick(TarefaResponse tarefa);
    }

    public void setOnTarefaClickListener(OnTarefaClickListener listener) {
        this.listener = listener;
    }

    public void setTarefas(List<TarefaResponse> tarefas) {
        this.tarefas = tarefas != null ? tarefas : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        TarefaResponse tarefa = tarefas.get(position);

        // Configurar o título da tarefa
        holder.tvTitle.setText(tarefa.getTipoTarefa() != null ? tarefa.getTipoTarefa() : "Tarefa");

        // Configurar o código/pedido
        String codigo = tarefa.getPedido() != null ? tarefa.getPedido() :
                (tarefa.getId() != null ? "ID: " + tarefa.getId() : "");
        holder.tvId.setText(codigo);

        // Configurar cliques
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTarefaClick(tarefa);
            }
        });

        holder.button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRegistrarClick(tarefa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvId;
        Button button;

        TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_card);
            tvId = itemView.findViewById(R.id.tv_id_card);
            button = itemView.findViewById(R.id.button);
        }
    }
}
