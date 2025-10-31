package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

public class ItemReceitaIngredienteCompletoResponse {

    @SerializedName("prioridade")
    private Integer prioridade;

    @SerializedName("receitaId")
    private String receitaId;

    @SerializedName("receita_id")
    private String receitaIdSnakeCase;

    @SerializedName("ingredienteId")
    private String ingredienteId;

    @SerializedName("ingrediente_id")
    private String ingredienteIdSnakeCase;

    public ItemReceitaIngredienteCompletoResponse() {
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

    public String getIngredienteId() {
        // Tenta ingredienteId primeiro, depois ingrediente_id (snake_case)
        if (ingredienteId != null && !ingredienteId.isEmpty()) {
            return ingredienteId;
        }
        return ingredienteIdSnakeCase != null ? ingredienteIdSnakeCase : null;
    }

    public void setIngredienteId(String ingredienteId) {
        this.ingredienteId = ingredienteId;
    }
}

