package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LancheResponse {

    @SerializedName("opcoes")
    private List<ItemReceitaIngredienteCompletoResponse> opcoes;

    @SerializedName("fruta")
    private FrutaResponse fruta;

    @SerializedName("opcoes_fixas")
    private ItemReceitaIngredienteCompletoResponse opcoesFixas;

    public LancheResponse() {
    }

    public List<ItemReceitaIngredienteCompletoResponse> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<ItemReceitaIngredienteCompletoResponse> opcoes) {
        this.opcoes = opcoes;
    }

    public FrutaResponse getFruta() {
        return fruta;
    }

    public void setFruta(FrutaResponse fruta) {
        this.fruta = fruta;
    }

    public ItemReceitaIngredienteCompletoResponse getOpcoesFixas() {
        return opcoesFixas;
    }

    public void setOpcoesFixas(ItemReceitaIngredienteCompletoResponse opcoesFixas) {
        this.opcoesFixas = opcoesFixas;
    }
}

