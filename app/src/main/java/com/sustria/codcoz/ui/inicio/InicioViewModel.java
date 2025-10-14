package com.sustria.codcoz.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.RetrofitClient;
import com.sustria.codcoz.api.TarefaApi;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<Integer> estoquePercentual;
    private final MutableLiveData<String> estoqueStatus;
    private final MutableLiveData<Integer> estoquePercentualAnterior;
    private final MutableLiveData<String> estoqueStatusAnterior;
    private final MutableLiveData<List<TarefaResponse>> tarefas;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    private TarefaApi tarefaApi;

    public InicioViewModel() {
        estoquePercentual = new MutableLiveData<>();
        estoqueStatus = new MutableLiveData<>();
        estoquePercentualAnterior = new MutableLiveData<>();
        estoqueStatusAnterior = new MutableLiveData<>();
        tarefas = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();

        // API
        tarefaApi = RetrofitClient.getInstance().create(TarefaApi.class);

        // Inicializar com os dados padrão
        loadDefaultData();
    }

    private void loadDefaultData() {
        // Dados simulados do estoque por enquanto
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

        Call<List<TarefaResponse>> call = tarefaApi.buscarPorPeriodo(email, dataInicio.toString(), dataFim.toString());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    tarefas.setValue(response.body());
                } else {
                    errorMessage.setValue("Erro ao carregar tarefas: " + response.message());
                    tarefas.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erro de conexão: " + t.getMessage());
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
        Call<TarefaResponse> call = tarefaApi.finalizarTarefa(tarefaId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TarefaResponse> call, Response<TarefaResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful()) {
                    // Recarregar a lista de tarefas após finalizar
                    loadTarefas();
                } else {
                    errorMessage.setValue("Erro ao finalizar tarefa: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TarefaResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}