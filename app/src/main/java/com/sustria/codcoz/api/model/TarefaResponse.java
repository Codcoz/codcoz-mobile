package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;

public class TarefaResponse implements Serializable {
    private Long id;
    private String empresa;
    private String tipoTarefa;
    private String ingrediente;
    private String relator;
    private String responsavel;
    private String pedido;
    private Boolean notificacaoLido;
    private String situacao;
    @SerializedName("data_criacao")
    private LocalDate dataCriacao;
    
    @SerializedName("data_limite")
    private LocalDate dataLimite;
    
    @SerializedName("data_conclusao")
    private LocalDate dataConclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTipoTarefa() {
        return tipoTarefa;
    }

    public String getIngrediente() {
        return ingrediente;
    }


    public String getRelator() {
        return relator;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public Boolean getNotificacaoLido() {
        return notificacaoLido;
    }

    public void setNotificacaoLido(Boolean value) {
        this.notificacaoLido = value;
    }

    public String getPedido() {
        return pedido;
    }

    public String getSituacao() {
        return situacao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

}
