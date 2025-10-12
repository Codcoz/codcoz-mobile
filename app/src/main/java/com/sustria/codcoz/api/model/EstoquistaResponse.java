package com.sustria.codcoz.api.model;

import java.io.Serializable;

public class EstoquistaResponse implements Serializable {
    private String email;
    private String nome;
    private String sobrenome;
    private Integer empresaId;
    private String dataContratacao;
    private String status;

    public EstoquistaResponse() {}

    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public Integer getEmpresaId() { return empresaId; }
    public String getDataContratacao() { return dataContratacao; }
    public String getStatus() { return status; }
}



