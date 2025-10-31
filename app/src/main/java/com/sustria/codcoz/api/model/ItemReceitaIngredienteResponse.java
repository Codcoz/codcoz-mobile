package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

public class ItemReceitaIngredienteResponse {

    @SerializedName("receitaId")
    private String receitaId;

    @SerializedName("ingredienteId")
    private String ingredienteId;

    public ItemReceitaIngredienteResponse() {
    }

    public ItemReceitaIngredienteResponse(String receitaId, String ingredienteId) {
        this.receitaId = receitaId;
        this.ingredienteId = ingredienteId;
    }

    public String getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(String receitaId) {
        this.receitaId = receitaId;
    }

    public String getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(String ingredienteId) {
        this.ingredienteId = ingredienteId;
    }
}

