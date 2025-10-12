package com.sustria.codcoz.api;

import com.sustria.codcoz.api.model.EstoquistaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstoquistaApi {

    @GET("estoquista/buscar")
    Call<EstoquistaResponse> buscarPorEmail(@Query("email") String email);
}



