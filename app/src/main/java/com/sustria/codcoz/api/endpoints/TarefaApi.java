package com.sustria.codcoz.api.endpoints;

import com.sustria.codcoz.api.model.TarefaResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TarefaApi {

    @GET("tarefa/buscar-data/{email}")
    Call<List<TarefaResponse>> buscarPorData(
            @Path(value = "email", encoded = true) String email,
            @Query("data") String data
    );

    @GET("tarefa/buscar-periodo/{email}")
    Call<List<TarefaResponse>> buscarPorPeriodo(
            @Path(value = "email", encoded = true) String email,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim
    );

    @GET("tarefa/buscar-por-tipo/{email}")
    Call<List<TarefaResponse>> buscarPorTipo(
            @Path(value = "email", encoded = true) String email,
            @Query("data_inicio") String dataInicio,
            @Query("data_fim") String dataFim,
            @Query("tipo") String tipo
    );

    @PUT("tarefa/finalizar-tarefa/{id}")
    Call<ResponseBody> finalizarTarefa(
            @Path(value = "id", encoded = true) Long id
    );

    @GET("tarefa/buscar-concluidas/{empresaId}")
    Call<List<TarefaResponse>> buscarConcluidas(
            @Path(value = "empresaId", encoded = true) Long empresaId,
            @Query("dias") Integer dias
    );

    @PUT("tarefa/finalizar-auditoria/{id}")
    Call<ResponseBody> finalizarAuditoria(
            @Path(value = "id", encoded = true) Long id,
            @Query("contagem") Integer contagem
    );
}
