package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

public class UnidadeMedida {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    // Construtor padrão
    public UnidadeMedida() {}

    // Construtor principal
    public UnidadeMedida(Long id, Long empresaId, String nome, String descricao) {
        this.id = id;
        this.empresaId = empresaId;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
