package com.sustria.codcoz.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<Integer> estoquePercentual;
    private final MutableLiveData<String> estoqueStatus;
    private final MutableLiveData<Integer> estoquePercentualAnterior;
    private final MutableLiveData<String> estoqueStatusAnterior;

    public InicioViewModel() {
        estoquePercentual = new MutableLiveData<>();
        estoqueStatus = new MutableLiveData<>();
        estoquePercentualAnterior = new MutableLiveData<>();
        estoqueStatusAnterior = new MutableLiveData<>();

        // Inicializar com dados padrão
        loadDefaultData();
    }

    private void loadDefaultData() {
        // Dados simulados do estoque
        estoquePercentual.setValue(78);
        estoqueStatus.setValue("Ótimo!");
        estoquePercentualAnterior.setValue(62);
        estoqueStatusAnterior.setValue("bom");
    }

    // Getters para dados do estoque
    public LiveData<Integer> getEstoquePercentual() {
        return estoquePercentual;
    }

    public LiveData<String> getEstoqueStatus() {
        return estoqueStatus;
    }

    public LiveData<Integer> getEstoquePercentualAnterior() {
        return estoquePercentualAnterior;
    }

    public LiveData<String> getEstoqueStatusAnterior() {
        return estoqueStatusAnterior;
    }


    // Métodos para atualizar dados (para uso futuro com banco de dados)
    public void updateEstoqueData(int percentual, String status) {
        estoquePercentual.setValue(percentual);
        estoqueStatus.setValue(status);
    }

    // Metodo para carregar dados do banco de dados (implementação futura)
    public void loadDataFromDatabase() {
        // Implementar carregamento de dados do banco de dados
        // Por enquanto, usa dados simulados
        loadDefaultData();
    }
}