package com.sustria.codcoz.api.service;

import android.util.Log;

import com.sustria.codcoz.api.client.RetrofitClientRedis;
import com.sustria.codcoz.api.endpoints.HistoricoApi;
import com.sustria.codcoz.api.model.HistoricoBaixaListResponse;
import com.sustria.codcoz.api.model.HistoricoBaixaRequest;
import com.sustria.codcoz.api.model.HistoricoBaixaResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Body vazio conforme especificado na API
        Map<String, Object> emptyBody = new HashMap<>();

        historicoApi.listarHistoricoBaixas(idEmpresa, emptyBody)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<HistoricoBaixaListResponse> call, Response<HistoricoBaixaListResponse> response) {
                        Log.d("HistoricoService", "Resposta recebida - Status: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            List<HistoricoBaixaResponse> historicoList = response.body().getHistoricoBaixas();
                            Log.d("HistoricoService", "Sucesso! Dados: " + (historicoList != null ? historicoList.size() + " registros" : "null"));
                            callback.onSuccess(historicoList);
                        } else {
                            Log.e("HistoricoService", "Erro HTTP: " + response.code() + " - " + response.message());
                            callback.onError("Erro ao buscar histórico: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoricoBaixaListResponse> call, Throwable t) {
                        Log.e("HistoricoService", "Falha na requisição: " + t.getMessage());
                        Log.e("HistoricoService", "Stack trace: ", t);
                        callback.onError("Erro de conexão: " + t.getMessage());
                    }
                });
    }

    public void registrarHistoricoBaixa(Long idEmpresa, HistoricoBaixaRequest request, HistoricoCallback<Void> callback) {
        Log.d("HistoricoService", "Registrando histórico de baixa:");
        Log.d("HistoricoService", "- ID Empresa: " + idEmpresa);
        Log.d("HistoricoService", "- ID Produto: " + request.getId_produto());
        Log.d("HistoricoService", "- Nome Produto: " + request.getNome_produto());
        Log.d("HistoricoService", "- Código Produto: " + request.getCodigo_produto());
        Log.d("HistoricoService", "- Data Acontecimento: " + request.getData_acontecimento());
        Log.d("HistoricoService", "- Quantidade: " + request.getQuantidade());
        Log.d("HistoricoService", "- Tipo Registro: " + request.getTipo_registro());

        historicoApi.registrarHistoricoBaixa(idEmpresa, request)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("HistoricoService", "Resposta recebida - Status: " + response.code());
                        Log.d("HistoricoService", "Headers: " + response.headers());

                        if (response.isSuccessful()) {
                            Log.d("HistoricoService", "Histórico registrado com sucesso!");
                            callback.onSuccess(null);
                        } else {
                            Log.e("HistoricoService", "Erro HTTP: " + response.code() + " - " + response.message());
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("HistoricoService", "Erro body: " + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("HistoricoService", "Erro ao ler error body: " + e.getMessage());
                                }
                            }
                            callback.onError("Erro ao registrar histórico: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("HistoricoService", "Falha na requisição: " + t.getMessage());
                        Log.e("HistoricoService", "Stack trace: ", t);
                        callback.onError("Erro de conexão: " + t.getMessage());
                    }
                });
    }
}
