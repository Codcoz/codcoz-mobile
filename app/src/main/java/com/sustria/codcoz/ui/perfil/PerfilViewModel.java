package com.sustria.codcoz.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.utils.UserDataManager;

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


    public void loadTarefasPorTipo(String tipo, int dias) {
        UserDataManager userDataManager = UserDataManager.getInstance();
        Integer empresaIdInt = userDataManager.getEmpresaId();

        if (empresaIdInt == null) {
            errorMessage.setValue("ID da empresa não encontrado");
            return;
        }

        Long empresaId = Long.valueOf(empresaIdInt);
        
        isLoading.setValue(true);
        errorMessage.setValue(null);
        tipoFiltro.setValue(tipo);

        // Usa a nova API que busca tarefas concluídas por empresa e dias
        tarefaService.buscarConcluidas(empresaId, dias, new TarefaService.TarefaCallback<>() {
            @Override
            public void onSuccess(List<TarefaResponse> result) {
                isLoading.setValue(false);
                
                // Filtra pelo tipo de tarefa
                List<TarefaResponse> tarefasFiltradas = new ArrayList<>();
                for (TarefaResponse tarefa : result) {
                    if (tarefa.getTipoTarefa() != null && tarefa.getTipoTarefa().equals(tipo)) {
                        tarefasFiltradas.add(tarefa);
                    }
                }
                
                tarefas.setValue(tarefasFiltradas);
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