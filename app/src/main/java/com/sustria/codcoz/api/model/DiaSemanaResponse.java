package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaSemanaResponse {

    @SerializedName("diaSemana")
    private String diaSemana;

    @SerializedName("dia")
    private String dia; // Mantido para compatibilidade com API antiga

    @SerializedName("data")
    private String data;

    @SerializedName("almoco")
    private AlmocoResponse almoco;

    @SerializedName("lanche_manha")
    private LancheResponse lancheManha;

    @SerializedName("lanche_tarde")
    private LancheResponse lancheTarde;

    // Campos antigos para compatibilidade
    @SerializedName("refeicoes")
    private List<ItemRefeicaoResponse> refeicoes;

    // Construtor padrão
    public DiaSemanaResponse() {
    }

    // Getters e Setters
    public String getDiaSemana() {
        // Retorna diaSemana se disponível, caso contrário retorna dia (compatibilidade)
        if (diaSemana != null && !diaSemana.isEmpty()) {
            return diaSemana;
        }
        return dia != null ? dia : "";
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public AlmocoResponse getAlmoco() {
        return almoco;
    }

    public void setAlmoco(AlmocoResponse almoco) {
        this.almoco = almoco;
    }

    public LancheResponse getLancheManha() {
        return lancheManha;
    }

    public void setLancheManha(LancheResponse lancheManha) {
        this.lancheManha = lancheManha;
    }

    public LancheResponse getLancheTarde() {
        return lancheTarde;
    }

    public void setLancheTarde(LancheResponse lancheTarde) {
        this.lancheTarde = lancheTarde;
    }

    // Métodos de compatibilidade com código antigo
    public String getDia() {
        return getDiaSemana();
    }

    public void setDia(String dia) {
        this.dia = dia;
        if (diaSemana == null) {
            this.diaSemana = dia;
        }
    }

    public List<ItemRefeicaoResponse> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<ItemRefeicaoResponse> refeicoes) {
        this.refeicoes = refeicoes;
    }
}