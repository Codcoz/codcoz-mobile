package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemRefeicaoResponse {

    @SerializedName("tipo")
    private String tipo; // "Café da Manhã", "Almoço", "Café da Tarde"´v

    @SerializedName("itens")
    private List<String> itens;


    // Construtor padrão
    public ItemRefeicaoResponse() {
    }

    // Construtor principal
    public ItemRefeicaoResponse(String tipo, List<String> itens) {
        this.tipo = tipo;
        this.itens = itens;
    }


    // Getters e Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getItens() {
        return itens;
    }

    public void setItens(List<String> itens) {
        this.itens = itens;
    }
}