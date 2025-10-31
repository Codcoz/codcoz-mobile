package com.sustria.codcoz.api.model;

public class EmpresaResponse {
    private Double ocupacaoEstoque;

    public EmpresaResponse() {
    }

    public EmpresaResponse(Double ocupacaoEstoque) {
        this.ocupacaoEstoque = ocupacaoEstoque;
    }

    public Double getOcupacaoEstoque() {
        return ocupacaoEstoque;
    }

    public void setOcupacaoEstoque(Double ocupacaoEstoque) {
        this.ocupacaoEstoque = ocupacaoEstoque;
    }
}
