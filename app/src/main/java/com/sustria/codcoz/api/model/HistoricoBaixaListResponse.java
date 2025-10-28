package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoricoBaixaListResponse {
    @SerializedName("historico_baixas")
    private List<HistoricoBaixaResponse> historicoBaixas;

    public HistoricoBaixaListResponse() {
    }

    public List<HistoricoBaixaResponse> getHistoricoBaixas() {
        return historicoBaixas;
    }

    public void setHistoricoBaixas(List<HistoricoBaixaResponse> historicoBaixas) {
        this.historicoBaixas = historicoBaixas;
    }
}

