package com.sustria.codcoz.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pedido {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("data_compra")
    private Date dataCompra;

    @SerializedName("data_previsao")
    private Date dataPrevisao;

    @SerializedName("data_recebimento")
    private Date dataRecebimento;

    @SerializedName("descricao")
    private String descricao;

    // Construtor padrão
    public Pedido() {}

    // Construtor principal

    public Pedido(Long id, Long empresaId, Date dataCompra, Date dataPrevisao, Date dataRecebimento, String descricao) {
        this.id = id;
        this.empresaId = empresaId;
        this.dataCompra = dataCompra;
        this.dataPrevisao = dataPrevisao;
        this.dataRecebimento = dataRecebimento;
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

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Date getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
