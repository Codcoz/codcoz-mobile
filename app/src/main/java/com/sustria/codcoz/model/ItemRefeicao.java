package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model para representar uma refeição (café da manhã, almoço, café da tarde)
 */
public class ItemRefeicao {
    
    @SerializedName("tipo")
    private String tipo; // "Café da Manhã", "Almoço", "Café da Tarde"
    
    @SerializedName("itens")
    private List<String> itens;
    
    @SerializedName("horario")
    private String horario; // Formato: HH:mm
    
    @SerializedName("observacoes")
    private String observacoes;
    
    @SerializedName("calorias_estimadas")
    private int caloriasEstimadas;
    
    @SerializedName("dificuldade_preparo")
    private DificuldadePreparo dificuldadePreparo;

    // Construtor padrão
    public ItemRefeicao() {}

    // Construtor principal
    public ItemRefeicao(String tipo, List<String> itens) {
        this.tipo = tipo;
        this.itens = itens;
    }

    // Construtor completo
    public ItemRefeicao(String tipo, List<String> itens, String horario, 
                       String observacoes, int caloriasEstimadas, 
                       DificuldadePreparo dificuldadePreparo) {
        this.tipo = tipo;
        this.itens = itens;
        this.horario = horario;
        this.observacoes = observacoes;
        this.caloriasEstimadas = caloriasEstimadas;
        this.dificuldadePreparo = dificuldadePreparo;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getCaloriasEstimadas() {
        return caloriasEstimadas;
    }

    public void setCaloriasEstimadas(int caloriasEstimadas) {
        this.caloriasEstimadas = caloriasEstimadas;
    }

    public DificuldadePreparo getDificuldadePreparo() {
        return dificuldadePreparo;
    }

    public void setDificuldadePreparo(DificuldadePreparo dificuldadePreparo) {
        this.dificuldadePreparo = dificuldadePreparo;
    }

    /**
     * Enum para dificuldade de preparo
     */
    public enum DificuldadePreparo {
        @SerializedName("facil")
        FACIL,
        @SerializedName("medio")
        MEDIO,
        @SerializedName("dificil")
        DIFICIL
    }

    /**
     * Classe para resposta da API de itens de refeição
     */
    public static class ItemRefeicaoResponse {
        @SerializedName("success")
        private boolean success;
        
        @SerializedName("data")
        private List<ItemRefeicao> data;
        
        @SerializedName("message")
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<ItemRefeicao> getData() {
            return data;
        }

        public void setData(List<ItemRefeicao> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Classe para requisição de criação/atualização de item de refeição
     */
    public static class ItemRefeicaoRequest {
        @SerializedName("tipo")
        private String tipo;
        
        @SerializedName("itens")
        private List<String> itens;
        
        @SerializedName("horario")
        private String horario;
        
        @SerializedName("observacoes")
        private String observacoes;
        
        @SerializedName("calorias_estimadas")
        private int caloriasEstimadas;
        
        @SerializedName("dificuldade_preparo")
        private DificuldadePreparo dificuldadePreparo;
        
        @SerializedName("dia_semana")
        private String diaSemana;

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

        public String getHorario() {
            return horario;
        }

        public void setHorario(String horario) {
            this.horario = horario;
        }

        public String getObservacoes() {
            return observacoes;
        }

        public void setObservacoes(String observacoes) {
            this.observacoes = observacoes;
        }

        public int getCaloriasEstimadas() {
            return caloriasEstimadas;
        }

        public void setCaloriasEstimadas(int caloriasEstimadas) {
            this.caloriasEstimadas = caloriasEstimadas;
        }

        public DificuldadePreparo getDificuldadePreparo() {
            return dificuldadePreparo;
        }

        public void setDificuldadePreparo(DificuldadePreparo dificuldadePreparo) {
            this.dificuldadePreparo = dificuldadePreparo;
        }

        public String getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(String diaSemana) {
            this.diaSemana = diaSemana;
        }
    }
}




