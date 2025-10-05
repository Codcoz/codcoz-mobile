package com.sustria.codcoz.models;

import com.google.gson.annotations.SerializedName;

public class Ingrediente {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("categoria_ingrediente_id")
    private Long categoriaIngredienteId;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    // Construtor padrão
    public Ingrediente() {}

    // Construtor principal

    public Ingrediente(Long id, Long empresaId, Long categoriaIngredienteId, String nome, String descricao) {
        this.id = id;
        this.empresaId = empresaId;
        this.categoriaIngredienteId = categoriaIngredienteId;
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

    public Long getCategoriaIngredienteId() {
        return categoriaIngredienteId;
    }

    public void setCategoriaIngredienteId(Long categoriaIngredienteId) {
        this.categoriaIngredienteId = categoriaIngredienteId;
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
