package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.EstoquistaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EstoquistaApi {

    @GET("funcionario/buscar/{email}")
    Call<EstoquistaResponse> buscarPorEmail(@Path(value = "email", encoded = true) String email);
}