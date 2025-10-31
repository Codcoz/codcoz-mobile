package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.EmpresaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path; 

public interface EmpresaApi {
    @GET("empresa/ocupacao-estoque/{id_empresa}")
    Call<EmpresaResponse> calcularPorcentagemEstoque(@Path("id_empresa") String idEmpresa);
}