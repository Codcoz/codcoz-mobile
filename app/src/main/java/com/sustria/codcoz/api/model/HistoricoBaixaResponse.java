package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HistoricoBaixaResponse {
    private String id;
    private String id_produto;
    private String nome_produto;
    private String codigo_produto;
    private String data_acontecimento;
    private String tipo_registro;
    @SerializedName("quantidade_movimentada")
    private String quantidade_movimentada;
    private String unidade_medida;
    private String observacoes;

    public HistoricoBaixaResponse() {
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public String getCodigo_produto() {
        return codigo_produto;
    }

    public void setCodigo_produto(String codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    public String getData_acontecimento() {
        return data_acontecimento;
    }

    public void setData_acontecimento(String data_acontecimento) {
        this.data_acontecimento = data_acontecimento;
    }

    // Método auxiliar para converter data_acontecimento para LocalDate
    public LocalDate getDataAcontecimentoAsLocalDate() {
        LocalDateTime dateTime = getDataAcontecimentoAsLocalDateTime();
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    // Método auxiliar para converter data_acontecimento para LocalDateTime (mantendo hora)
    public LocalDateTime getDataAcontecimentoAsLocalDateTime() {
        if (data_acontecimento == null || data_acontecimento.isEmpty()) {
            return null;
        }
        try {
            // Tenta primeiro com formato com hora
            DateTimeFormatter formatterComHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(data_acontecimento, formatterComHora);
        } catch (DateTimeParseException e) {
            try {
                // Tenta com formato sem hora (usa início do dia)
                DateTimeFormatter formatterSemHora = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(data_acontecimento, formatterSemHora);
                return date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public String getQuantidade_movimentada() {
        return quantidade_movimentada;
    }

    public void setQuantidade_movimentada(String quantidade_movimentada) {
        this.quantidade_movimentada = quantidade_movimentada;
    }

    // Método auxiliar para converter quantidade_movimentada para Integer
    public Integer getQuantidade() {
        if (quantidade_movimentada == null || quantidade_movimentada.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(quantidade_movimentada);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
