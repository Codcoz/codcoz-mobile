package com.sustria.codcoz.api.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.TarefaResponse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PerfilTarefaAdapter extends RecyclerView.Adapter<PerfilTarefaAdapter.TarefaViewHolder> {

    private List<TarefaResponse> tarefas = new ArrayList<>();

    public void setTarefas(List<TarefaResponse> tarefas) {
        this.tarefas = tarefas != null ? tarefas : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public boolean isEmpty() {
        return tarefas.isEmpty();
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa_perfil, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        TarefaResponse tarefa = tarefas.get(position);

        // Configura o nome da tarefa
        holder.tvNome.setText(tarefa.getTipoTarefa() != null ? tarefa.getTipoTarefa() : "Tarefa");

        // Configurar o ingrediente (prioridade) ou código/pedido
        String textoCodigo;
        if (tarefa.getIngrediente() != null && !tarefa.getIngrediente().trim().isEmpty()) {
            // Se tiver ingrediente, mostra o ingrediente
            textoCodigo = tarefa.getIngrediente();
        } else if (tarefa.getPedido() != null && !tarefa.getPedido().trim().isEmpty()) {
            // Se não tiver ingrediente mas tiver pedido, mostra o pedido
            textoCodigo = tarefa.getPedido();
        } else if (tarefa.getId() != null) {
            // Se não tiver nenhum dos dois, mostra o ID
            textoCodigo = "ID: " + tarefa.getId();
        } else {
            // Fallback
            textoCodigo = "";
        }
        holder.tvCodigo.setText(textoCodigo);

        if (tarefa.getDataConclusao() != null) {
            holder.tvData.setText(tarefa.getDataConclusao().format(DateTimeFormatter.ofPattern("dd MMMM", new Locale("pt", "BR"))));
        } else {
            holder.tvData.setText("Data não disponível");
        }
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome;
        TextView tvCodigo;
        TextView tvData;

        TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvCodigo = itemView.findViewById(R.id.tvCodProduto);
            tvData = itemView.findViewById(R.id.dataMovimentacao);
        }
    }
}
