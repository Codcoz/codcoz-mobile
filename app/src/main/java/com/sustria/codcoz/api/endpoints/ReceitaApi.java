package com.sustria.codcoz.api.endpoints;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReceitaApi {

    // Endpoint para receitas da API NoSQL
    @GET("api/v1/receita")
    Call<List<com.sustria.codcoz.api.model.ReceitaApi>> getReceitas();
}
