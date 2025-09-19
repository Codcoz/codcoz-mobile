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
    private final MutableLiveData<List<com.sustria.codcoz.ui.inicio.InicioFragment.Atividade>> atividadesRecentes;

    public InicioViewModel() {
        estoquePercentual = new MutableLiveData<>();
        estoqueStatus = new MutableLiveData<>();
        estoquePercentualAnterior = new MutableLiveData<>();
        estoqueStatusAnterior = new MutableLiveData<>();
        atividadesRecentes = new MutableLiveData<>();

        // Inicializar com dados padrão
        loadDefaultData();
    }

    private void loadDefaultData() {
        // Dados simulados do estoque
        estoquePercentual.setValue(78);
        estoqueStatus.setValue("Ótimo!");
        estoquePercentualAnterior.setValue(62);
        estoqueStatusAnterior.setValue("bom");

        // Dados simulados de atividades recentes
        List<com.sustria.codcoz.ui.inicio.InicioFragment.Atividade> atividades = new ArrayList<>();
        atividades.add(new com.sustria.codcoz.ui.inicio.InicioFragment.Atividade(
                "Fulano de Tal",
                "XML importado - 32 produtos",
                "16:50",
                "hoje"
        ));
        atividades.add(new com.sustria.codcoz.ui.inicio.InicioFragment.Atividade(
                "Raphael Veiga",
                "Produto cadastrado - Arroz Integral",
                "16:20",
                "hoje"
        ));
        atividades.add(new com.sustria.codcoz.ui.inicio.InicioFragment.Atividade(
                "João Silva",
                "Saída registrada - 5 produtos",
                "15:30",
                "hoje"
        ));

        atividadesRecentes.setValue(atividades);
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

    // Getter para atividades recentes
    public LiveData<List<com.sustria.codcoz.ui.inicio.InicioFragment.Atividade>> getAtividadesRecentes() {
        return atividadesRecentes;
    }

    // Métodos para atualizar dados (para uso futuro com banco de dados)
    public void updateEstoqueData(int percentual, String status) {
        estoquePercentual.setValue(percentual);
        estoqueStatus.setValue(status);
    }

    public void addAtividade(com.sustria.codcoz.ui.inicio.InicioFragment.Atividade atividade) {
        List<com.sustria.codcoz.ui.inicio.InicioFragment.Atividade> currentList = atividadesRecentes.getValue();
        if (currentList != null) {
            currentList.add(0, atividade); // Adicionar no início
            atividadesRecentes.setValue(currentList);
        }
    }

    // Metodo para carregar dados do banco de dados (implementação futura)
    public void loadDataFromDatabase() {
        // Implementar carregamento de dados do banco de dados
        // Por enquanto, usa dados simulados
        loadDefaultData();
    }
}