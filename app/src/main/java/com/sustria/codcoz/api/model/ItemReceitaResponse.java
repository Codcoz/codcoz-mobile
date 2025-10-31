package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

public class ItemReceitaResponse {

    @SerializedName("prioridade")
    private Integer prioridade;

    @SerializedName("receitaId")
    private String receitaId;

    @SerializedName("receita_id")
    private String receitaIdSnakeCase;

    public ItemReceitaResponse() {
    }

    public ItemReceitaResponse(Integer prioridade, String receitaId) {
        this.prioridade = prioridade;
        this.receitaId = receitaId;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public String getReceitaId() {
        // Tenta receitaId primeiro, depois receita_id (snake_case)
        if (receitaId != null && !receitaId.isEmpty()) {
            return receitaId;
        }
        return receitaIdSnakeCase != null ? receitaIdSnakeCase : null;
    }

    public void setReceitaId(String receitaId) {
        this.receitaId = receitaId;
    }
}

