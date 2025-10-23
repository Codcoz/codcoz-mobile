package com.sustria.codcoz.api.service;

import com.sustria.codcoz.api.client.RetrofitClientNoSql;
import com.sustria.codcoz.api.endpoints.CardapioApi;
import com.sustria.codcoz.api.model.CardapioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardapioService {

    private final CardapioApi cardapioApi;

    public CardapioService() {
        this.cardapioApi = RetrofitClientNoSql.getInstance().create(CardapioApi.class);
    }

    public interface CardapioCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    public void getCardapios(String empresaId, CardapioCallback<List<CardapioResponse>> callback) {
        cardapioApi.getCardapios(empresaId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CardapioResponse>> call, Response<List<CardapioResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar cardápios: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CardapioResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
