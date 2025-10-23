package com.sustria.codcoz.utils;

import com.sustria.codcoz.api.service.ProdutoService;

public class EstoqueManager {
    private final ProdutoService produtoService;

    public EstoqueManager() {
        this.produtoService = new ProdutoService();
    }

    public interface EstoqueCallback {
        void onSuccess();

        void onError(String error);
    }

    public void registrarEntrada(String codEan, Integer quantidade, EstoqueCallback callback) {
        produtoService.entradaEstoque(codEan, quantidade, new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                callback.onSuccess();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void registrarBaixa(String codEan, Integer quantidade, EstoqueCallback callback) {
        produtoService.baixaEstoque(codEan, quantidade, new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                callback.onSuccess();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
