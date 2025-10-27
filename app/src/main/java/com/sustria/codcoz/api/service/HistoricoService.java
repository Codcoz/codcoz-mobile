package com.sustria.codcoz.api.service;

import android.util.Log;

import com.sustria.codcoz.api.client.RetrofitClientRedis;
import com.sustria.codcoz.api.endpoints.HistoricoApi;
import com.sustria.codcoz.api.model.HistoricoBaixaRequest;
import com.sustria.codcoz.api.model.HistoricoBaixaResponse;
import com.sustria.codcoz.api.model.HistoricoListRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoService {

    private final HistoricoApi historicoApi;

    public HistoricoService() {
        this.historicoApi = RetrofitClientRedis.getInstance().create(HistoricoApi.class);
    }

    public interface HistoricoCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    public void listarHistoricoBaixas(Long idEmpresa, HistoricoCallback<List<HistoricoBaixaResponse>> callback) {
        
        Log.d("HistoricoService", "Fazendo requisição para listar histórico:");
        Log.d("HistoricoService", "- ID Empresa: " + idEmpresa);

        HistoricoListRequest requestBody = new HistoricoListRequest();
        historicoApi.listarHistoricoBaixas(idEmpresa, requestBody)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<HistoricoBaixaResponse>> call, Response<List<HistoricoBaixaResponse>> response) {
                        Log.d("HistoricoService", "Resposta recebida - Status: " + response.code());
                        Log.d("HistoricoService", "Headers: " + response.headers());
                        
                        if (response.isSuccessful()) {
                            Log.d("HistoricoService", "Sucesso! Dados: " + (response.body() != null ? response.body().size() + " registros" : "null"));
                            callback.onSuccess(response.body());
                        } else {
                            Log.e("HistoricoService", "Erro HTTP: " + response.code() + " - " + response.message());
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("HistoricoService", "Erro body: " + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("HistoricoService", "Erro ao ler error body: " + e.getMessage());
                                }
                            }
                            callback.onError("Erro ao buscar histórico: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HistoricoBaixaResponse>> call, Throwable t) {
                        Log.e("HistoricoService", "Falha na requisição: " + t.getMessage());
                        Log.e("HistoricoService", "Stack trace: ", t);
                        callback.onError("Erro de conexão: " + t.getMessage());
                    }
                });
    }

    public void registrarHistoricoBaixa(Long idEmpresa, HistoricoBaixaRequest request, HistoricoCallback<Void> callback) {
        historicoApi.registrarHistoricoBaixa(idEmpresa, request)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(null);
                        } else {
                            callback.onError("Erro ao registrar histórico: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Erro de conexão: " + t.getMessage());
                    }
                });
    }
}
