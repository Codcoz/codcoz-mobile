package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;
import com.sustria.codcoz.model.ItemRefeicao;

import java.util.List;

public class DiaSemana {

    @SerializedName("dia")
    private String dia;

    @SerializedName("refeicoes")
    private List<ItemRefeicao> refeicoes;


    // Construtor padrão
    public DiaSemana() {
    }

    // Construtor principal
    public DiaSemana(String dia, List<ItemRefeicao> refeicoes) {
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

    public List<ItemRefeicao> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<ItemRefeicao> refeicoes) {
        this.refeicoes = refeicoes;
    }
}