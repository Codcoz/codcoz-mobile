package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlmocoResponse {

    @SerializedName("arrozIntegral")
    private ItemReceitaIngredienteResponse arrozIntegral;

    @SerializedName("arroz")
    private ItemReceitaIngredienteResponse arroz;

    @SerializedName("feijao")
    private ItemReceitaIngredienteResponse feijao;

    @SerializedName("proteinas")
    private List<ItemReceitaResponse> proteinas;

    @SerializedName("guarnicao")
    private ItemReceitaIngredienteResponse guarnicao;

    @SerializedName("saladas")
    private List<ItemReceitaResponse> saladas;

    @SerializedName("molhoSalada")
    private ItemReceitaIngredienteResponse molhoSalada;

    @SerializedName("sobremesa")
    private ItemReceitaIngredienteResponse sobremesa;

    public AlmocoResponse() {
    }

    public ItemReceitaIngredienteResponse getArrozIntegral() {
        return arrozIntegral;
    }

    public void setArrozIntegral(ItemReceitaIngredienteResponse arrozIntegral) {
        this.arrozIntegral = arrozIntegral;
    }

    public ItemReceitaIngredienteResponse getArroz() {
        return arroz;
    }

    public void setArroz(ItemReceitaIngredienteResponse arroz) {
        this.arroz = arroz;
    }

    public ItemReceitaIngredienteResponse getFeijao() {
        return feijao;
    }

    public void setFeijao(ItemReceitaIngredienteResponse feijao) {
        this.feijao = feijao;
    }

    public List<ItemReceitaResponse> getProteinas() {
        return proteinas;
    }

    public void setProteinas(List<ItemReceitaResponse> proteinas) {
        this.proteinas = proteinas;
    }

    public ItemReceitaIngredienteResponse getGuarnicao() {
        return guarnicao;
    }

    public void setGuarnicao(ItemReceitaIngredienteResponse guarnicao) {
        this.guarnicao = guarnicao;
    }

    public List<ItemReceitaResponse> getSaladas() {
        return saladas;
    }

    public void setSaladas(List<ItemReceitaResponse> saladas) {
        this.saladas = saladas;
    }

    public ItemReceitaIngredienteResponse getMolhoSalada() {
        return molhoSalada;
    }

    public void setMolhoSalada(ItemReceitaIngredienteResponse molhoSalada) {
        this.molhoSalada = molhoSalada;
    }

    public ItemReceitaIngredienteResponse getSobremesa() {
        return sobremesa;
    }

    public void setSobremesa(ItemReceitaIngredienteResponse sobremesa) {
        this.sobremesa = sobremesa;
    }
}

