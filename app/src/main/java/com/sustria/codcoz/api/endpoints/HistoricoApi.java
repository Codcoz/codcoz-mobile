package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.HistoricoBaixaRequest;
import com.sustria.codcoz.api.model.HistoricoBaixaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoricoApi {

    @GET("api/v1/empresa/{id_empresa}/historico_baixas")
    Call<List<HistoricoBaixaResponse>> listarHistoricoBaixas(
            @Path("id_empresa") Long idEmpresa
    );

    @POST("api/v1/empresa/{id_empresa}/historico_baixas")
    Call<Void> registrarHistoricoBaixa(
            @Path("id_empresa") Long idEmpresa,
            @Body HistoricoBaixaRequest request
    );
}
