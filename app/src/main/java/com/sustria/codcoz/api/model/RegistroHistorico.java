package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistroHistorico {

    @SerializedName("id")
    private String id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("unidades")
    private int unidades;

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("data_movimentacao")
    private long epochMillis;

    @SerializedName("tipo")
    private TipoMovimentacao tipo;

    @SerializedName("observacoes")
    private String observacoes;

    @SerializedName("usuario_id")
    private String usuarioId;

    @SerializedName("produto_id")
    private String produtoId;

    // Construtor padrão
    public RegistroHistorico() {
    }

    // Construtor completo
    public RegistroHistorico(String id, String nome, int unidades, String codigo,
                             long epochMillis, TipoMovimentacao tipo, String observacoes,
                             String usuarioId, String produtoId) {
        this.id = id;
        this.nome = nome;
        this.unidades = unidades;
        this.codigo = codigo;
        this.epochMillis = epochMillis;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
    }

    // Construtor simplificado para compatibilidade
    public RegistroHistorico(String nome, int unidades, String codigo,
                             long epochMillis, TipoMovimentacao tipo) {
        this.nome = nome;
        this.unidades = unidades;
        this.codigo = codigo;
        this.epochMillis = epochMillis;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getEpochMillis() {
        return epochMillis;
    }

    public void setEpochMillis(long epochMillis) {
        this.epochMillis = epochMillis;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public enum TipoMovimentacao {
        @SerializedName("entrada")
        ENTRADA,
        @SerializedName("baixa")
        BAIXA,
        @SerializedName("ajuste")
        AJUSTE,
        @SerializedName("transferencia")
        TRANSFERENCIA
    }
}




