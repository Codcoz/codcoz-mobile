package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Funcionario {
    @SerializedName("id")
    private Long id;

    @SerializedName("empresa_id")
    private Long empresaId;

    @SerializedName("funcao_id")
    private Long funcaoId;

    @SerializedName("nome")
    private String nome;

    @SerializedName("sobrenome")
    private String sobrenome;

    @SerializedName("status")
    private String status;

    @SerializedName("email")
    private String email;

    @SerializedName("data_contratacao")
    private Date dataContratacao;

    // Construtor padrão
    public Funcionario() {}

    // Construtor principal

    public Funcionario(Long id, Long empresaId, Long funcaoId, String nome, String sobrenome, String status, String email, Date dataContratacao) {
        this.id = id;
        this.empresaId = empresaId;
        this.funcaoId = funcaoId;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.status = status;
        this.email = email;
        this.dataContratacao = dataContratacao;
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

    public Long getFuncaoId() {
        return funcaoId;
    }

    public void setFuncaoId(Long funcaoId) {
        this.funcaoId = funcaoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(Date dataContratacao) {
        this.dataContratacao = dataContratacao;
    }
}
