package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class ProdutoResponse {
    @SerializedName("id")
    private Long id;

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
    private String validadeString;


    // Construtor padrão
    public ProdutoResponse() {
    }

    // Construtor principal
    public ProdutoResponse(Long id, String codigoEan, String nome, String descricao, Integer quantidade, String marca, String validadeString) {
        this.id = id;
        this.codigoEan = codigoEan;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.marca = marca;
        this.validadeString = validadeString;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getCodigoEan() {
        return codigoEan;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMarca() {
        return marca;
    }

    public LocalDate getValidade() {
        if (validadeString == null || validadeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(validadeString);
        } catch (Exception e) {
            return null;
        }
    }

}
