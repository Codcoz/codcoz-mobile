package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaSemanaResponse {

    @SerializedName("dia")
    private String dia;

    @SerializedName("refeicoes")
    private List<ItemRefeicaoResponse> refeicoes;


    // Construtor padrão
    public DiaSemanaResponse() {
    }

    // Construtor principal
    public DiaSemanaResponse(String dia, List<ItemRefeicaoResponse> refeicoes) {
        this.dia = dia;
        this.refeicoes = refeicoes;
    }


    // Getters e Setters
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public List<ItemRefeicaoResponse> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<ItemRefeicaoResponse> refeicoes) {
        this.refeicoes = refeicoes;
    }
}