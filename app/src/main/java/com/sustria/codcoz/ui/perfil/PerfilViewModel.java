package com.sustria.codcoz.ui.perfil;

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

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<List<TarefaResponse>> tarefas;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> tipoFiltro;

    private TarefaApi tarefaApi;

    public PerfilViewModel() {
        tarefas = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        tipoFiltro = new MutableLiveData<>();

        // Inicializar API
        tarefaApi = RetrofitClient.getInstance().create(TarefaApi.class);
    }

    // Getters
    public LiveData<List<TarefaResponse>> getTarefas() {
        return tarefas;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getTipoFiltro() {
        return tipoFiltro;
    }

    public void loadTarefasPorTipo(String tipo, int dias) {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String email = userDataManager.getEmail();

        if (email == null || email.isEmpty()) {
            errorMessage.setValue("Email do usuário não encontrado");
            return;
        }

        isLoading.setValue(true);
        errorMessage.setValue(null);
        tipoFiltro.setValue(tipo);

        // Calcular datas baseado no período selecionado
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusDays(dias);
        String inicio = dataInicio.toString();
        String fim = dataFim.toString();

        Call<List<TarefaResponse>> call = tarefaApi.buscarPorTipo(email, inicio, fim, tipo);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TarefaResponse>> call, Response<List<TarefaResponse>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    tarefas.setValue(response.body());
                } else {
                    String errorMsg = "Erro ao carregar tarefas: " + response.code() + " - " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            errorMsg += " - Erro ao ler corpo da resposta";
                        }
                    }
                    errorMessage.setValue(errorMsg);
                    tarefas.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<TarefaResponse>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erro de conexão: " + t.getMessage() + " - " + t.getClass().getSimpleName());
                tarefas.setValue(new ArrayList<>());
            }
        });
    }

    public void loadAtividades(int dias) {
        loadTarefasPorTipo("Atividade", dias);
    }

    public void loadAuditorias(int dias) {
        loadTarefasPorTipo("Conferência de Estoque", dias);
    }
}