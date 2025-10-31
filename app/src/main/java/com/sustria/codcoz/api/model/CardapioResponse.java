package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CardapioResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("data_inicio")
    private String dataInicio;

    @SerializedName("data_fim")
    private String dataFim;

    @SerializedName("empresa_id")
    private Object empresaId; // Aceita tanto String quanto Integer/Number

    @SerializedName("nomeCardapio")
    private String nomeCardapio; // camelCase do MongoDB

    @SerializedName("nome_cardapio")
    private String nomeCardapioSnakeCase; // snake_case da API

    @SerializedName("periodicidade")
    private String periodicidade;

    @SerializedName("cardapio_semanal")
    private List<DiaSemanaResponse> cardapioSemanal;

    // Construtor padrão
    public CardapioResponse() {
    }

    // Construtor principal
    public CardapioResponse(String id, String dataInicio, String dataFim, String empresaId, List<DiaSemanaResponse> cardapioSemanal) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.empresaId = empresaId;
        this.cardapioSemanal = cardapioSemanal;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getEmpresaId() {
        // Converte empresaId para String se for número
        if (empresaId == null) {
            return null;
        }
        if (empresaId instanceof String) {
            return (String) empresaId;
        }
        if (empresaId instanceof Number) {
            return String.valueOf(((Number) empresaId).intValue());
        }
        return empresaId.toString();
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public String getNomeCardapio() {
        // Retorna nomeCardapio (camelCase) se disponível, caso contrário retorna nome_cardapio (snake_case)
        if (nomeCardapio != null && !nomeCardapio.isEmpty()) {
            return nomeCardapio;
        }
        return nomeCardapioSnakeCase != null ? nomeCardapioSnakeCase : "";
    }

    public void setNomeCardapio(String nomeCardapio) {
        this.nomeCardapio = nomeCardapio;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

    public List<DiaSemanaResponse> getCardapioSemanal() {
        return cardapioSemanal;
    }

    public void setCardapioSemanal(List<DiaSemanaResponse> cardapioSemanal) {
        this.cardapioSemanal = cardapioSemanal;
    }
}
