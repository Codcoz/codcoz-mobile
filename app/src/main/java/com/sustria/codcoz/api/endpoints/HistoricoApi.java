package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.HistoricoBaixaListResponse;
import com.sustria.codcoz.api.model.HistoricoBaixaRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoricoApi {

    @POST("api/v1/empresa/{id_empresa}/historico_baixas/leitura")
    Call<HistoricoBaixaListResponse> listarHistoricoBaixas(
            @Path("id_empresa") Long idEmpresa,
            @Body Object body
    );

    @POST("api/v1/empresa/{id_empresa}/historico_baixas")
    Call<Void> registrarHistoricoBaixa(
            @Path("id_empresa") Long idEmpresa,
            @Body HistoricoBaixaRequest request
    );
}
