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

}
