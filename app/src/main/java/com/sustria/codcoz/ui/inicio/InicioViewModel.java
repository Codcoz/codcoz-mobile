package com.sustria.codcoz.ui.inicio;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<Integer> estoquePercentual;
    private final MutableLiveData<String> estoqueStatus;
    private final MutableLiveData<Integer> estoquePercentualAnterior;
    private final MutableLiveData<String> estoqueStatusAnterior;
    private final MutableLiveData<List<TarefaResponse>> tarefas;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Integer> quantidadeEstoque;
    private final MutableLiveData<Integer> quantidadeEstoqueBaixo;
    private final MutableLiveData<Integer> quantidadeProximoValidade;

    private final TarefaService tarefaService;
    private final ProdutoService produtoService;

    public InicioViewModel() {
        estoquePercentual = new MutableLiveData<>();
        estoqueStatus = new MutableLiveData<>();
        estoquePercentualAnterior = new MutableLiveData<>();
        estoqueStatusAnterior = new MutableLiveData<>();
        tarefas = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        quantidadeEstoque = new MutableLiveData<>();
        quantidadeEstoqueBaixo = new MutableLiveData<>();
        quantidadeProximoValidade = new MutableLiveData<>();

        tarefaService = new TarefaService();
        produtoService = new ProdutoService();

        // Inicializar com os dados padrão
        loadDefaultData();
        loadQuantidadeDados();
    }

    private void loadDefaultData() {
        // Dados simulados do estoque por enquanto
        estoquePercentual.setValue(78);
        estoqueStatus.setValue("Ótimo!");
        estoquePercentualAnterior.setValue(62);
        estoqueStatusAnterior.setValue("bom");
    }

    private void loadQuantidadeDados() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        Integer empresaId = userDataManager.getEmpresaId();
        
        if (empresaId != null) {
            produtoService.getQuantidadeEstoque(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    quantidadeEstoque.setValue(result);
                }

                @Override
                public void onError(String error) {
                    quantidadeEstoque.setValue(0);
                }
            });

            produtoService.getQuantidadeEstoqueBaixo(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    quantidadeEstoqueBaixo.setValue(result);
                }

                @Override
                public void onError(String error) {
                    quantidadeEstoqueBaixo.setValue(0);
                }
            });

            produtoService.getQuantidadeProximoValidade(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    quantidadeProximoValidade.setValue(result);
                }

                @Override
                public void onError(String error) {
                    quantidadeProximoValidade.setValue(0);
                }
            });
        }
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


    public LiveData<Integer> getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public LiveData<Integer> getQuantidadeEstoqueBaixo() {
        return quantidadeEstoqueBaixo;
    }

    public LiveData<Integer> getQuantidadeProximoValidade() {
        return quantidadeProximoValidade;
    }


    // Métodos para atualizar dados (para uso futuro com banco de dados)
    public void updateEstoqueData(int percentual, String status) {
        estoquePercentual.setValue(percentual);
        estoqueStatus.setValue(status);
    }

    // Getters para dados das tarefas
    public LiveData<List<TarefaResponse>> getTarefas() {
        return tarefas;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Metodo para carregar tarefas da API
    public void loadTarefas() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String email = userDataManager.getEmail();

        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Carregar tarefas do período de 6 meses
        LocalDate hoje = LocalDate.now();
        LocalDate dataInicio = hoje.minusMonths(3);
        LocalDate dataFim = hoje.plusMonths(3);

        tarefaService.buscarPorPeriodo(email, dataInicio.toString(), dataFim.toString(), new TarefaService.TarefaCallback<List<TarefaResponse>>() {
            @Override
            public void onSuccess(List<TarefaResponse> result) {
                isLoading.setValue(false);
                tarefas.setValue(result);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
                tarefas.setValue(new ArrayList<>());
            }
        });
    }

    // Metodo para finalizar uma tarefa
    public void finalizarTarefa(Long tarefaId) {
        if (tarefaId == null) {
            errorMessage.setValue("ID da tarefa inválido");
            return;
        }

        isLoading.setValue(true);
        
        tarefaService.finalizarTarefa(tarefaId, new TarefaService.TarefaCallback<TarefaResponse>() {
            @Override
            public void onSuccess(TarefaResponse result) {
                isLoading.setValue(false);
                // Recarregar a lista de tarefas após finalizar
                loadTarefas();
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }
}