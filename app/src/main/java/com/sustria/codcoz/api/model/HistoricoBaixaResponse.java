package com.sustria.codcoz.api.model;

import java.time.LocalDate;

public class HistoricoBaixaResponse {
    private Long id;
    private String id_produto;
    private String nome_produto;
    private String codigo_produto;
    private LocalDate data_acontecimento;
    private String tipo_registro;
    private Integer quantidade;
    private String observacoes;

    public HistoricoBaixaResponse() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_produto() {
        return id_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }


    public String getCodigo_produto() {
        return codigo_produto;
    }

    public LocalDate getData_acontecimento() {
        return data_acontecimento;
    }

    public void setData_acontecimento(LocalDate data_acontecimento) {
        this.data_acontecimento = data_acontecimento;
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

}
