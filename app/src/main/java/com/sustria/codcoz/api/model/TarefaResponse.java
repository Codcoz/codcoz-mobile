package com.sustria.codcoz.api.model;

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
    private String situacao;
    private LocalDate dataCriacao;
    private LocalDate dataLimite;
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

    public void setEmpresa(String value) {
        this.empresa = value;
    }

    public String getTipoTarefa() {
        return tipoTarefa;
    }

    public void setTipoTarefa(String value) {
        this.tipoTarefa = value;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String value) {
        this.ingrediente = value;
    }

    public String getRelator() {
        return relator;
    }

    public void setRelator(String value) {
        this.relator = value;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String value) {
        this.responsavel = value;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String value) {
        this.pedido = value;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String value) {
        this.situacao = value;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate value) {
        this.dataCriacao = value;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate value) {
        this.dataLimite = value;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate value) {
        this.dataConclusao = value;
    }

}
