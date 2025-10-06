package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

public class TipoMovimentacao {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("descricao")
    private String descricao;

    // Construtor padrão
    public TipoMovimentacao() {}

    // Construtor principal

    public TipoMovimentacao(Long id, Long empresaId, String motivo, String tipo, String descricao) {
        this.id = id;
        this.empresaId = empresaId;
        this.motivo = motivo;
        this.tipo = tipo;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
