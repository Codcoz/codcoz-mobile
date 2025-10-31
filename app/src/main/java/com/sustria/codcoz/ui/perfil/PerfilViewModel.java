package com.sustria.codcoz.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.api.service.TarefaService;
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
        // Para 30 dias: queremos tarefas dos últimos 30 dias incluindo hoje
        // Exemplo: hoje 31/10, 30 dias = de 02/10 até 31/10 (30 dias incluindo hoje)
        // IMPORTANTE: Buscamos um período maior na API (dias+1) porque ela pode estar
        // filtrando por outra data (dataLimite, dataCriacao), não dataConclusao
        LocalDate hoje = LocalDate.now();
        LocalDate dataFim = hoje;
        LocalDate dataInicioAPI = hoje.minusDays(dias); // Busca dias dias atrás para garantir que não perca nada
        
        String inicio = dataInicioAPI.toString();
        String fim = dataFim.toString();

        tarefaService.buscarPorTipo(email, inicio, fim, tipo, new TarefaService.TarefaCallback<>() {
            @Override
            public void onSuccess(List<TarefaResponse> result) {
                isLoading.setValue(false);
                
                // Filtro no cliente para garantir que está usando dataConclusao
                // A API pode estar filtrando por outra data (dataLimite, dataCriacao)
                // então filtramos aqui usando dataConclusao para garantir o período correto
                // Para 30 dias: de (hoje - 29 dias) até hoje = 30 dias incluindo hoje
                LocalDate hoje = LocalDate.now();
                LocalDate dataInicioFiltro = hoje.minusDays(dias - 1); // (dias-1) para ter dias dias total incluindo hoje
                
                List<TarefaResponse> tarefasFiltradas = new ArrayList<>();
                for (TarefaResponse tarefa : result) {
                    LocalDate dataConclusao = tarefa.getDataConclusao();
                    
                    // Só inclui tarefas que tenham dataConclusao válida
                    // e que esteja dentro do período selecionado (usando dataConclusao)
                    if (dataConclusao != null) {
                        // Verifica se a data de conclusão está no período (inclusive)
                        // dataConclusao >= dataInicioFiltro && dataConclusao <= hoje
                        boolean dentroDoPeriodo = !dataConclusao.isBefore(dataInicioFiltro) && !dataConclusao.isAfter(hoje);
                        
                        if (dentroDoPeriodo) {
                            tarefasFiltradas.add(tarefa);
                        }
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