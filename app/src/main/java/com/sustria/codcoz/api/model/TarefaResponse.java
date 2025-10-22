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
    @SerializedName("dataCriacao")
    private String dataCriacaoString;

    @SerializedName("dataLimite")
    public String dataLimiteString;

    @SerializedName("dataConclusao")
    private String dataConclusaoString;

    public Long getId() {
        return id;
    }

    public void setId(Long value) {
        this.id = value;
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
        if (dataCriacaoString == null || dataCriacaoString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataCriacaoString);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDate getDataLimite() {
        if (dataLimiteString == null || dataLimiteString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataLimiteString);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDate getDataConclusao() {
        if (dataConclusaoString == null || dataConclusaoString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataConclusaoString);
        } catch (Exception e) {
            return null;
        }
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusaoString = dataConclusao != null ? dataConclusao.toString() : null;
    }

}
