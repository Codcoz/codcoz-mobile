package com.sustria.codcoz.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<List<TarefaResponse>> tarefas;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> tipoFiltro;

    private TarefaService tarefaService;

    public PerfilViewModel() {
        tarefas = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        tipoFiltro = new MutableLiveData<>();

        // Inicializar API
        tarefaService = new TarefaService();
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

        tarefaService.buscarPorTipo(email, inicio, fim, tipo, new TarefaService.TarefaCallback<>() {
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

    public void loadAtividades(int dias) {
        loadTarefasPorTipo("Atividade", dias);
    }

    public void loadAuditorias(int dias) {
        loadTarefasPorTipo("Conferência de Estoque", dias);
    }
}