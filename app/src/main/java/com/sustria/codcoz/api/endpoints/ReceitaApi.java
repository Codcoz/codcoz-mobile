package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.ReceitaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReceitaApi {

    @GET("api/v1/{empresaId}/receita")
    Call<List<ReceitaResponse>> getReceitas(@Path("empresaId") String empresaId);
}
