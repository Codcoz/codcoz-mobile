package com.sustria.codcoz.api.service;


import android.util.Log;

import com.sustria.codcoz.api.client.RetrofitClient;
import com.sustria.codcoz.api.endpoints.TarefaApi;
import com.sustria.codcoz.api.model.TarefaResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarefaService {

    private final TarefaApi tarefaApi;

    public TarefaService() {
        this.tarefaApi = RetrofitClient.getInstance().create(TarefaApi.class);
    }

    public interface TarefaCallback<T> { 
        void onSuccess(T result);

        void onError(String error);
    }

    public void buscarPorData(String email, String data, TarefaCallback<List<TarefaResponse>> callback) {
        tarefaApi.buscarPorData(email, data).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas por data: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void buscarPorPeriodo(String email, String dataInicio, String dataFim, TarefaCallback<List<TarefaResponse>> callback) {
        tarefaApi.buscarPorPeriodo(email, dataInicio, dataFim).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas por período: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void buscarPorTipo(String email, String dataInicio, String dataFim, String tipo, TarefaCallback<List<TarefaResponse>> callback) {
        tarefaApi.buscarPorTipo(email, dataInicio, dataFim, tipo).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas por tipo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void finalizarTarefa(Long id, TarefaCallback<TarefaResponse> callback) {
        tarefaApi.finalizarTarefa(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // A API retorna texto plain, não JSON
                    // Consideramos sucesso se o status code foi 200
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String responseText = body.string();
                            Log.d("TarefaService", "Tarefa finalizada. Resposta: " + responseText);
                        } catch (Exception e) {
                            Log.e("TarefaService", "Erro ao ler resposta (mas requisição foi bem-sucedida): " + e.getMessage());
                        }
                    }
                    // Retornar null para indicar sucesso (status 200)
                    callback.onSuccess(null);
                } else {
                    String errorMsg = "Erro ao finalizar tarefa: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e("TarefaService", "Erro ao ler errorBody: " + e.getMessage());
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TarefaService", "Falha na requisição: " + t.getMessage(), t);
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void buscarConcluidas(Long empresaId, int dias, TarefaCallback<List<TarefaResponse>> callback) {
        tarefaApi.buscarConcluidas(empresaId, dias).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas concluídas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void finalizarAuditoria(Long id, Integer contagem, TarefaCallback<TarefaResponse> callback) {
        tarefaApi.finalizarAuditoria(id, contagem).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Consideramos sucesso se o status code foi 200
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String responseText = body.string();
                            Log.d("TarefaService", "Auditoria finalizada. Resposta: " + responseText);
                        } catch (Exception e) {
                            Log.e("TarefaService", "Erro ao ler resposta (mas requisição foi bem-sucedida): " + e.getMessage());
                        }
                    }
                    // Retornar null para indicar sucesso (status 200)
                    callback.onSuccess(null);
                } else {
                    String errorMsg = "Erro ao finalizar auditoria: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            errorMsg += " - " + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e("TarefaService", "Erro ao ler errorBody: " + e.getMessage());
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TarefaService", "Falha na requisição: " + t.getMessage(), t);
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
