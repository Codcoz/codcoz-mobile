package com.sustria.codcoz.api.model;

import java.io.Serializable;

public class EstoquistaResponse implements Serializable {
    private String email;
    private String nome;
    private String sobrenome;
    private Integer empresaId;
    private String dataContratacao;
    private String status;
    private String imagemPerfil;

    public EstoquistaResponse() {
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public Integer getEmpresaId() {
        return empresaId;
    }

    public String getDataContratacao() {
        return dataContratacao;
    }

    public String getStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }

    public void setDataContratacao(String dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }
}



