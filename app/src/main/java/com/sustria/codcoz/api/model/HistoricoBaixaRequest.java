package com.sustria.codcoz.api.model;

public class HistoricoBaixaRequest {
    private String id_produto;
    private String nome_produto;
    private String codigo_produto;
    private String data_acontecimento;
    private String tipo_registro;

    public HistoricoBaixaRequest() {}

    public HistoricoBaixaRequest(String id_produto, String nome_produto, String codigo_produto, 
                               String data_acontecimento, String tipo_registro) {
        this.id_produto = id_produto;
        this.nome_produto = nome_produto;
        this.codigo_produto = codigo_produto;
        this.data_acontecimento = data_acontecimento;
        this.tipo_registro = tipo_registro;
    }

    // Getters e Setters
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

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }
}
