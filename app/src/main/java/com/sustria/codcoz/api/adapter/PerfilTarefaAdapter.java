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

public class PerfilTarefaAdapter extends RecyclerView.Adapter<PerfilTarefaAdapter.TarefaViewHolder> {
    
    private List<TarefaResponse> tarefas = new ArrayList<>();
    
    public void setTarefas(List<TarefaResponse> tarefas) {
        this.tarefas = tarefas != null ? tarefas : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historicos_perfil, parent, false);
        return new TarefaViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        TarefaResponse tarefa = tarefas.get(position);
        
        // Configurar nome da tarefa
        holder.tvNome.setText(tarefa.getTipoTarefa() != null ? tarefa.getTipoTarefa() : "Tarefa");
        
        // Configurar descrição com ingrediente e empresa
        StringBuilder descricao = new StringBuilder();
        if (tarefa.getIngrediente() != null) {
            descricao.append(tarefa.getIngrediente());
        }
        if (tarefa.getEmpresa() != null) {
            if (descricao.length() > 0) {
                descricao.append(" - ");
            }
            descricao.append(tarefa.getEmpresa());
        }
        holder.tvUnidades.setText(descricao.toString());
        
        // Configurar código/pedido
        String codigo = tarefa.getPedido() != null ? tarefa.getPedido() : 
                       (tarefa.getId() != null ? "ID: " + tarefa.getId() : "");
        holder.tvCodigo.setText(codigo);
        
        // Configurar data
        if (tarefa.getDataCriacao() != null) {
            holder.tvData.setText(tarefa.getDataCriacao().format(DateTimeFormatter.ofPattern("dd MMMM | HH:mm")));
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
        TextView tvUnidades;
        TextView tvCodigo;
        TextView tvData;
        
        TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome);
            tvUnidades = itemView.findViewById(R.id.tv_unidades);
            tvCodigo = itemView.findViewById(R.id.tv_cod_produto);
            tvData = itemView.findViewById(R.id.dataMovimentacao);
        }
    }
}
