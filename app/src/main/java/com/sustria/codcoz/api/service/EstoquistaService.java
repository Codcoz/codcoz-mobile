package com.sustria.codcoz.api.service;

import com.sustria.codcoz.api.client.RetrofitClient;
import com.sustria.codcoz.api.endpoints.EstoquistaApi;
import com.sustria.codcoz.api.model.EstoquistaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstoquistaService {

    private final EstoquistaApi estoquistaApi;

    public EstoquistaService() {
        this.estoquistaApi = RetrofitClient.getInstance().create(EstoquistaApi.class);
    }

    public interface EstoquistaCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    public void buscarPorEmail(String email, EstoquistaCallback<EstoquistaResponse> callback) {
        estoquistaApi.buscarPorEmail(email).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EstoquistaResponse> call, Response<EstoquistaResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar estoquista: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EstoquistaResponse> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
