package com.sustria.codcoz.api.service;

import com.sustria.codcoz.api.client.RetrofitClientNoSql;
import com.sustria.codcoz.api.endpoints.ReceitaApi;
import com.sustria.codcoz.api.model.ReceitaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceitaService {

    private final ReceitaApi receitaApi;

    public ReceitaService() {
        this.receitaApi = RetrofitClientNoSql.getInstance().create(ReceitaApi.class);
    }

    public interface ReceitaCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    // uscar receitas da API NoSQL
    public void getReceitas(ReceitaCallback<List<ReceitaResponse>> callback) {
        receitaApi.getReceitas().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ReceitaResponse>> call, Response<List<ReceitaResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar receitas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReceitaResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
