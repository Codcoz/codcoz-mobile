package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

public class ItemPedido {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("produto_id")
    private Long produtoId;

    @SerializedName("pedido_id")
    private Long pedidoId;

    @SerializedName("quantidade")
    private Integer quantidade;

    @SerializedName("preco_unitario")
    private Double precoUnitario;

    // Construtor padrão
    public ItemPedido() {}

    // Construtor principal

    public ItemPedido(Long id, Long empresaId, Long produtoId, Long pedidoId, Integer quantidade, Double precoUnitario) {
        this.id = id;
        this.empresaId = empresaId;
        this.produtoId = produtoId;
        this.pedidoId = pedidoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
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

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
