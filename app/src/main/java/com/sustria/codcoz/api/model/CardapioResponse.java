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
    private String empresaId;

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
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public List<DiaSemanaResponse> getCardapioSemanal() {
        return cardapioSemanal;
    }

    public void setCardapioSemanal(List<DiaSemanaResponse> cardapioSemanal) {
        this.cardapioSemanal = cardapioSemanal;
    }
}
