package com.sustria.codcoz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model para representar um dia da semana no cardápio
 */
public class DiaSemana {

    @SerializedName("dia")
    private String dia;

    @SerializedName("refeicoes")
    private List<ItemRefeicao> refeicoes;

    @SerializedName("data")
    private String data; // Formato: YYYY-MM-DD

    @SerializedName("observacoes")
    private String observacoes;

    // Construtor padrão
    public DiaSemana() {
    }

    // Construtor principal
    public DiaSemana(String dia, List<ItemRefeicao> refeicoes) {
        this.dia = dia;
        this.refeicoes = refeicoes;
    }

    // Construtor completo
    public DiaSemana(String dia, List<ItemRefeicao> refeicoes, String data, String observacoes) {
        this.dia = dia;
        this.refeicoes = refeicoes;
        this.data = data;
        this.observacoes = observacoes;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /**
     * Classe para resposta da API do cardápio semanal
     */
    public static class CardapioSemanalResponse {
        @SerializedName("success")
        private boolean success;

        @SerializedName("data")
        private List<DiaSemana> data;

        @SerializedName("message")
        private String message;

        @SerializedName("semana_inicio")
        private String semanaInicio;

        @SerializedName("semana_fim")
        private String semanaFim;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<DiaSemana> getData() {
            return data;
        }

        public void setData(List<DiaSemana> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSemanaInicio() {
            return semanaInicio;
        }

        public void setSemanaInicio(String semanaInicio) {
            this.semanaInicio = semanaInicio;
        }

        public String getSemanaFim() {
            return semanaFim;
        }

        public void setSemanaFim(String semanaFim) {
            this.semanaFim = semanaFim;
        }
    }

    /**
     * Classe para requisição de cardápio por período
     */
    public static class CardapioRequest {
        @SerializedName("data_inicio")
        private String dataInicio;

        @SerializedName("data_fim")
        private String dataFim;

        @SerializedName("incluir_finais_semana")
        private boolean incluirFinaisSemana = false;

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

        public boolean isIncluirFinaisSemana() {
            return incluirFinaisSemana;
        }

        public void setIncluirFinaisSemana(boolean incluirFinaisSemana) {
            this.incluirFinaisSemana = incluirFinaisSemana;
        }
    }
}




