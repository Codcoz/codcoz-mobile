package com.sustria.codcoz.api.service;

import com.sustria.codcoz.api.client.RetrofitClient;
import com.sustria.codcoz.api.endpoints.EmpresaApi;
import com.sustria.codcoz.api.model.EmpresaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstoqueService {
    private final EmpresaApi empresaApi;

    public EstoqueService() {
        this.empresaApi = RetrofitClient.getInstance().create(EmpresaApi.class);
    }

    public interface EstoqueCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void calcularPorcentagemEstoque(String idEmpresa, EstoqueCallback<EmpresaResponse> callback) {
        empresaApi.calcularPorcentagemEstoque(idEmpresa).enqueue(new Callback<EmpresaResponse>() {
            @Override
            public void onResponse(Call<EmpresaResponse> call, Response<EmpresaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar porcentagem de ocupação: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmpresaResponse> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
