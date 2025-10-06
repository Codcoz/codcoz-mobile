package com.sustria.codcoz.model;

import com.google.gson.annotations.SerializedName;

public class Empresa {
    @SerializedName("id")
    private Long id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("cnpj")
    private String cnpj;

    @SerializedName("sigla")
    private String sigla;

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;

    // Construtor padrão
    public Empresa() {}

    // Construtor principal
    public Empresa(Long id, String nome, String cnpj, String sigla, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.sigla = sigla;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
