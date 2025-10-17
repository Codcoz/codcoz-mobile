package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class ProdutoResponse {
    @SerializedName("codigoEan")
    private String codigoEan;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("quantidade")
    private Integer quantidade;

    @SerializedName("marca")
    private String marca;

    @SerializedName("validade")
    private LocalDate validade;


    // Construtor padrão
    public ProdutoResponse() {
    }

    // Construtor principal
    public ProdutoResponse(String codigoEan, String nome, String descricao, Integer quantidade, String marca, LocalDate validade) {
        this.codigoEan = codigoEan;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.marca = marca;
        this.validade = validade;
    }

    // Getters e Setters

    public String getCodigoEan() {
        return codigoEan;
    }

    public void setCodigoEan(String codigoEan) {
        this.codigoEan = codigoEan;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }
}
