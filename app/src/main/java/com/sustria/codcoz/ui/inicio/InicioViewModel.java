package com.sustria.codcoz.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.model.EmpresaResponse;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.api.service.EstoqueService;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.utils.FirebaseHelper;
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
    private final EstoqueService estoqueService;
    private final FirebaseHelper firebaseHelper;

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
        estoqueService = new EstoqueService();
        firebaseHelper = FirebaseHelper.getInstance();

        // Carregar dados
        loadDataNumber();
        loadPercentualEstoque();
    }

    private void loadPercentualEstoque() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        Integer empresaId = userDataManager.getEmpresaId();

        if (empresaId != null) {
            // Primeiro, buscar o percentual atual da API
            estoqueService.calcularPorcentagemEstoque(String.valueOf(empresaId), new EstoqueService.EstoqueCallback<>() {
                @Override
                public void onSuccess(EmpresaResponse result) {
                    if (result != null && result.getOcupacaoEstoque() != null) {
                        Double percentualAtual = result.getOcupacaoEstoque();
                        estoquePercentual.setValue(percentualAtual.intValue());
                        estoqueStatus.setValue(firebaseHelper.calculateStatus(percentualAtual));

                        // Buscar o percentual do dia anterior ANTES de salvar o atual
                        firebaseHelper.searchPreviousDayPercentual(empresaId, new FirebaseHelper.PercentualDiarioCallback() {
                            @Override
                            public void onSuccess(Double percentualAnterior, LocalDate data) {
                                if (percentualAnterior != null) {
                                    estoquePercentualAnterior.setValue(percentualAnterior.intValue());
                                    estoqueStatusAnterior.setValue(firebaseHelper.calculateStatus(percentualAnterior));
                                } else {
                                    estoquePercentualAnterior.setValue(0);
                                    estoqueStatusAnterior.setValue("Sem dados");
                                }
                            }

                            @Override
                            public void onError(String error) {
                                estoquePercentualAnterior.setValue(0);
                                estoqueStatusAnterior.setValue("Sem dados");
                            }
                        });

                        // Salvar o percentual atual no Firebase para uso no dia seguinte
                        firebaseHelper.savePercentualAtual(percentualAtual, empresaId);
                    }
                }

                @Override
                public void onError(String error) {
                    // Em caso de erro na API, tenta buscar do Firebase
                    firebaseHelper.searchMostRecentPercentual(empresaId, new FirebaseHelper.PercentualCallback() {
                        @Override
                        public void onSuccess(Double percentual) {
                            if (percentual != null) {
                                estoquePercentual.setValue(percentual.intValue());
                                estoqueStatus.setValue(firebaseHelper.calculateStatus(percentual));
                            } else {
                                estoquePercentual.setValue(0);
                                estoqueStatus.setValue("Sem dados");
                            }
                        }

                        @Override
                        public void onError(String error2) {
                            estoquePercentual.setValue(0);
                            estoqueStatus.setValue("Sem dados");
                        }
                    });

                    // Buscar dia anterior também em caso de erro
                    firebaseHelper.searchPreviousDayPercentual(empresaId, new FirebaseHelper.PercentualDiarioCallback() {
                        @Override
                        public void onSuccess(Double percentualAnterior, LocalDate data) {
                            if (percentualAnterior != null) {
                                estoquePercentualAnterior.setValue(percentualAnterior.intValue());
                                estoqueStatusAnterior.setValue(firebaseHelper.calculateStatus(percentualAnterior));
                            } else {
                                estoquePercentualAnterior.setValue(0);
                                estoqueStatusAnterior.setValue("Sem dados");
                            }
                        }

                        @Override
                        public void onError(String error3) {
                            estoquePercentualAnterior.setValue(0);
                            estoqueStatusAnterior.setValue("Sem dados");
                        }
                    });
                }
            });
        }
    }

    private void loadDataNumber() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        Integer empresaId = userDataManager.getEmpresaId();

        if (empresaId != null) {
            produtoService.getQuantidadeEstoque(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<>() {
                @Override
                public void onSuccess(Integer result) {
                    quantidadeEstoque.setValue(result);
                }

                @Override
                public void onError(String error) {
                    quantidadeEstoque.setValue(0);
                }
            });

            produtoService.getQuantidadeEstoqueBaixo(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<>() {
                @Override
                public void onSuccess(Integer result) {
                    quantidadeEstoqueBaixo.setValue(result);
                }

                @Override
                public void onError(String error) {
                    quantidadeEstoqueBaixo.setValue(0);
                }
            });

            produtoService.getQuantidadeProximoValidade(Long.valueOf(empresaId), new ProdutoService.ProdutoCallback<>() {
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

    // Getters para dados das tarefas
    public LiveData<List<TarefaResponse>> getTarefas() {
        return tarefas;
    }

    // Metodo para carregar tarefas da API
    public void loadTasks() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String email = userDataManager.getEmail();

        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Carregar tarefas do período de 6 meses
        LocalDate hoje = LocalDate.now();
        LocalDate dataInicio = hoje.minusMonths(3);
        LocalDate dataFim = hoje.plusMonths(3);

        tarefaService.buscarPorPeriodo(email, dataInicio.toString(), dataFim.toString(),
                new TarefaService.TarefaCallback<>() {
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
}