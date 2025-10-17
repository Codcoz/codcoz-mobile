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


    /**
     * Registra entrada de estoque para um produto
     *
     * @param codEan     Código EAN do produto
     * @param quantidade Quantidade a ser adicionada
     * @param callback   Callback para resultado da operação
     */
    public void registrarEntrada(String codEan, Integer quantidade, EstoqueCallback callback) {
        produtoService.entradaEstoque(codEan, quantidade, new ProdutoService.ProdutoCallback<Void>() {
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

    /**
     * Registra baixa de estoque para um produto
     *
     * @param codEan     Código EAN do produto
     * @param quantidade Quantidade a ser removida
     * @param callback   Callback para resultado da operação
     */
    public void registrarBaixa(String codEan, Integer quantidade, EstoqueCallback callback) {
        produtoService.baixaEstoque(codEan, quantidade, new ProdutoService.ProdutoCallback<Void>() {
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
