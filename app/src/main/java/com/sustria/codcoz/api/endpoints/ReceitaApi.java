package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.ReceitaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReceitaApi {

    // Endpoint para receitas da API NoSQL
    @GET("api/v1/receita")
    Call<List<ReceitaResponse>> getReceitas();
}
