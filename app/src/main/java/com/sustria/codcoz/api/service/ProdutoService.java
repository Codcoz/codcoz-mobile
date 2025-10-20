package com.sustria.codcoz.api.service;

import com.sustria.codcoz.api.client.RetrofitClient;
import com.sustria.codcoz.api.endpoints.ProdutoApi;
import com.sustria.codcoz.api.model.ProdutoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoService {

    private final ProdutoApi produtoApi;

    public ProdutoService() {
        this.produtoApi = RetrofitClient.getInstance().create(ProdutoApi.class);
    }

    public interface ProdutoCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }

    public void getQuantidadeEstoque(Long idEmpresa, ProdutoCallback<Integer> callback) {
        produtoApi.getQuantidadeEstoque(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar quantidade de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void getQuantidadeEstoqueBaixo(Long idEmpresa, ProdutoCallback<Integer> callback) {
        produtoApi.getQuantidadeEstoqueBaixo(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar quantidade de estoque baixo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void getQuantidadeProximoValidade(Long idEmpresa, ProdutoCallback<Integer> callback) {
        produtoApi.getQuantidadeProximoValidade(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar quantidade próximo validade: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void listarProximoValidade(Long idEmpresa, ProdutoCallback<List<ProdutoResponse>> callback) {
        produtoApi.listarProximoValidade(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao listar produtos próximo validade: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void listarEstoqueBaixo(Long idEmpresa, ProdutoCallback<List<ProdutoResponse>> callback) {
        produtoApi.listarEstoqueBaixo(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao listar produtos estoque baixo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void listarEstoque(Long idEmpresa, ProdutoCallback<List<ProdutoResponse>> callback) {
        produtoApi.listarEstoque(idEmpresa).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProdutoResponse>> call, Response<List<ProdutoResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao listar produtos do estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoResponse>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void buscarProdutoPorEan(String codigoEan, ProdutoCallback<ProdutoResponse> callback) {
        Call<ProdutoResponse> call = produtoApi.buscarProdutoPorEan(codigoEan);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProdutoResponse> call, Response<ProdutoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Produto não encontrado ou erro de resposta.");
                }
            }

            @Override
            public void onFailure(Call<ProdutoResponse> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }


    public void entradaEstoque(String codEan, Integer quantidade, ProdutoCallback<Void> callback) {
        produtoApi.entradaEstoque(codEan, quantidade).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Erro ao registrar entrada de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void baixaEstoque(String codEan, Integer quantidade, ProdutoCallback<Void> callback) {
        produtoApi.baixaEstoque(codEan, quantidade).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Erro ao registrar baixa de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
