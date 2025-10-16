package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Produto {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("nome")
    private String nome;

    @SerializedName("unidade_medida_id")
    private Long unidadeMedidaId;

    @SerializedName("codigo_ean")
    private String codigoEan;

    @SerializedName("quantidade")
    private Integer quantidade;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("marca")
    private String marca;

    @SerializedName("validade")
    private LocalDate validade;


    // Construtor padrão
    public Produto() {}

    // Construtor principal

    public Produto(Long id, Long empresaId, String nome, Long unidadeMedidaId, String codigoEan, Integer quantidade, String descricao, String marca, LocalDate validade) {
        this.id = id;
        this.empresaId = empresaId;
        this.nome = nome;
        this.unidadeMedidaId = unidadeMedidaId;
        this.codigoEan = codigoEan;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.marca = marca;
        this.validade = validade;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getUnidadeMedidaId() {
        return unidadeMedidaId;
    }

    public void setUnidadeMedidaId(Long unidadeMedidaId) {
        this.unidadeMedidaId = unidadeMedidaId;
    }

    public String getCodigoEan() {
        return codigoEan;
    }

    public void setCodigoEan(String codigoEan) {
        this.codigoEan = codigoEan;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
