package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

public class FrutaResponse {

    @SerializedName("ingredienteId")
    private String ingredienteId;

    @SerializedName("ingrediente_id")
    private String ingredienteIdSnakeCase;

    public FrutaResponse() {
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

