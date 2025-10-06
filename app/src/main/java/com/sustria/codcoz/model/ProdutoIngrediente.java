package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

public class ProdutoIngrediente {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("produto_id")
    private Long produtoId;

    @SerializedName("ingrediente_id")
    private Long ingredienteId;

    // Construtor padrão
    public ProdutoIngrediente() {}

    // Construtor principal

    public ProdutoIngrediente(Long id, Long empresaId, Long produtoId, Long ingredienteId) {
        this.id = id;
        this.empresaId = empresaId;
        this.produtoId = produtoId;
        this.ingredienteId = ingredienteId;
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

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }
}
