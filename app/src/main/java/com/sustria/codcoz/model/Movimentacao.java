package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movimentacao {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("produto_id")
    private Long produtoId;

    @SerializedName("tipo_movimentacao_id")
    private Long tipoMovimentacaoId;

    @SerializedName("data")
    private Date data;

    // Construtor padrão
    public Movimentacao() {}

    // Construtor principal

    public Movimentacao(Long id, Long empresaId, Long produtoId, Long tipoMovimentacaoId, Date data) {
        this.id = id;
        this.empresaId = empresaId;
        this.produtoId = produtoId;
        this.tipoMovimentacaoId = tipoMovimentacaoId;
        this.data = data;
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

    public Long getTipoMovimentacaoId() {
        return tipoMovimentacaoId;
    }

    public void setTipoMovimentacaoId(Long tipoMovimentacaoId) {
        this.tipoMovimentacaoId = tipoMovimentacaoId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
