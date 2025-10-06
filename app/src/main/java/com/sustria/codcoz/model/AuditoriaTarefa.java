package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AuditoriaTarefa {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("tarefa_id")
    private Long tarefaId;

    @SerializedName("data_modificacao")
    private Date dataModificacao;

    // Construtor padrão
    public AuditoriaTarefa() {}

    // Construtor principal

    public AuditoriaTarefa(Long id, Long empresaId, Long tarefaId, Date dataModificacao) {
        this.id = id;
        this.empresaId = empresaId;
        this.tarefaId = tarefaId;
        this.dataModificacao = dataModificacao;
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

    public Long getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }
}
