package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Tarefa {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("tipo_tarefa_id")
    private Long tipoTarefaId;

    @SerializedName("ingrediente_id")
    private Long ingredienteId;

    @SerializedName("relator_id")
    private Long relatorId;

    @SerializedName("responsavel_id")
    private Long responsavelId;

    @SerializedName("pedido_id")
    private Long pedidoId;

    @SerializedName("situacao")
    private String situacao;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("data_criacao")
    private Date dataCriacao;

    @SerializedName("data_tarefa")
    private Date dataTarefa;

    @SerializedName("data_limite")
    private Date dataLimite;

    @SerializedName("data_conclusao")
    private Date dataConclusao;

    // Construtor padrão
    public Tarefa() {}

    // Construtor principal
    public Tarefa(Long id, Long empresaId, Long tipoTarefaId, Long ingredienteId, Long relatorId, Long responsavelId, Long pedidoId, String situacao, String motivo, Date dataCriacao, Date dataTarefa, Date dataLimite, Date dataConclusao) {
        this.id = id;
        this.empresaId = empresaId;
        this.tipoTarefaId = tipoTarefaId;
        this.ingredienteId = ingredienteId;
        this.relatorId = relatorId;
        this.responsavelId = responsavelId;
        this.pedidoId = pedidoId;
        this.situacao = situacao;
        this.motivo = motivo;
        this.dataCriacao = dataCriacao;
        this.dataTarefa = dataTarefa;
        this.dataLimite = dataLimite;
        this.dataConclusao = dataConclusao;
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

    public Long getTipoTarefaId() {
        return tipoTarefaId;
    }

    public void setTipoTarefaId(Long tipoTarefaId) {
        this.tipoTarefaId = tipoTarefaId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public Long getRelatorId() {
        return relatorId;
    }

    public void setRelatorId(Long relatorId) {
        this.relatorId = relatorId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataTarefa() {
        return dataTarefa;
    }

    public void setDataTarefa(Date dataTarefa) {
        this.dataTarefa = dataTarefa;
    }

    public Date getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(Date dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
