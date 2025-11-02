package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.ProdutoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProdutoApi {

    @GET("produto/quantidade-estoque/{id_empresa}")
    Call<Integer> getQuantidadeEstoque(@Path("id_empresa") Long idEmpresa);

    @GET("produto/quantidade/estoque-baixo/{id_empresa}")
    Call<Integer> getQuantidadeEstoqueBaixo(@Path("id_empresa") Long idEmpresa);

    @GET("produto/quantidade/proximo-validade/{id_empresa}")
    Call<Integer> getQuantidadeProximoValidade(@Path("id_empresa") Long idEmpresa);

    @GET("produto/listar/proximo-validade/{id_empresa}")
    Call<List<ProdutoResponse>> listarProximoValidade(@Path("id_empresa") Long idEmpresa);

    @GET("produto/listar/estoque-baixo/{id_empresa}")
    Call<List<ProdutoResponse>> listarEstoqueBaixo(@Path("id_empresa") Long idEmpresa);

    @GET("produto/listar/{id_empresa}")
    Call<List<ProdutoResponse>> listarEstoque(@Path("id_empresa") Long idEmpresa);

    @GET("produto/buscar/{cod_ean}")
    Call<ProdutoResponse> buscarProdutoPorEan(@Path("cod_ean") String codigoEan);

    @PUT("produto/entrada/{cod_ean}")
    Call<Void> entradaEstoque(@Path("cod_ean") String codEan, @Query("quantidade") Integer quantidade);

    @PUT("produto/baixa/{cod_ean}")
    Call<Void> baixaEstoque(@Path("cod_ean") String codEan, @Query("quantidade") Integer quantidade);
}
