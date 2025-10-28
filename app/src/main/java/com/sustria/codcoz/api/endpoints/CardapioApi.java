package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.CardapioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CardapioApi {

    @GET("api/v1/empresa/{empresaId}/cardapio")
    Call<List<CardapioResponse>> getCardapios(@Path("empresaId") String empresaId);
}
