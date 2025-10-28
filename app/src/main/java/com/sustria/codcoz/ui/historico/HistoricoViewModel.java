package com.sustria.codcoz.ui.historico;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.model.HistoricoBaixaResponse;
import com.sustria.codcoz.api.model.MockDataProvider;
import com.sustria.codcoz.api.model.RegistroHistorico;
import com.sustria.codcoz.api.service.HistoricoService;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class HistoricoViewModel extends ViewModel {

    private final MutableLiveData<List<RegistroHistorico>> historicoData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final HistoricoService historicoService;

    public HistoricoViewModel() {
        historicoData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        historicoService = new HistoricoService();

        // Inicializar com estado de carregamento
        isLoading.setValue(false);
    }

    public LiveData<List<RegistroHistorico>> getHistoricoData() {
        return historicoData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void carregarDadosMockados() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        try {
            List<RegistroHistorico> dados = MockDataProvider.getMockRegistrosHistorico();
            historicoData.setValue(dados);
        } catch (Exception e) {
            errorMessage.setValue("Erro ao carregar dados: " + e.getMessage());
        } finally {
            isLoading.setValue(false);
        }
    }

    public void carregarDadosReais() {
        Log.d("HistoricoViewModel", "Iniciando carregamento de dados reais");
        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Obter ID da empresa do usuário logado
        Integer empresaId = UserDataManager.getInstance().getEmpresaId();
        Log.d("HistoricoViewModel", "ID da empresa: " + empresaId);
        
        if (empresaId == null) {
            Log.w("HistoricoViewModel", "ID da empresa não encontrado");
            errorMessage.setValue("ID da empresa não encontrado. Faça login novamente.");
            isLoading.setValue(false);
            // Fallback para dados mockados
            carregarDadosMockados();
            return;
        }

        Long idEmpresa = empresaId.longValue();
        Log.d("HistoricoViewModel", "Chamando API com ID da empresa: " + idEmpresa);

        historicoService.listarHistoricoBaixas(
                idEmpresa,
                new HistoricoService.HistoricoCallback<>() {
                    @Override
                    public void onSuccess(List<HistoricoBaixaResponse> result) {
                        Log.d("HistoricoViewModel", "API retornou sucesso com " + (result != null ? result.size() : 0) + " registros");
                        List<RegistroHistorico> registros = converterParaRegistroHistorico(result);
                        historicoData.setValue(registros);
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("HistoricoViewModel", "Erro na API: " + error);
                        errorMessage.setValue(error);
                        isLoading.setValue(false);
                        // Em caso de erro, carregar dados mockados como fallback
                        carregarDadosMockados();
                    }
                }
        );
    }

    public void filtrarDados(String busca, String tipoFiltro, String periodoFiltro) {
        List<RegistroHistorico> dadosOriginais = historicoData.getValue();
        if (dadosOriginais == null) {
            return;
        }

        List<RegistroHistorico> dadosFiltrados = new ArrayList<>(dadosOriginais);

        // Filtro por busca (nome do produto)
        if (busca != null && !busca.trim().isEmpty()) {
            String buscaLower = busca.toLowerCase().trim();
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(registro -> registro.getNome() != null &&
                            registro.getNome().toLowerCase().contains(buscaLower))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Filtro por tipo de movimentação
        if (tipoFiltro != null && !tipoFiltro.equals("TODOS")) {
            try {
                RegistroHistorico.TipoMovimentacao tipo = RegistroHistorico.TipoMovimentacao.valueOf(tipoFiltro);
                dadosFiltrados = dadosFiltrados.stream()
                        .filter(registro -> registro.getTipo() == tipo)
                        .collect(java.util.stream.Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Tipo inválido, manter todos os dados
            }
        }

        // Filtro por período
        if (periodoFiltro != null && !periodoFiltro.equals("TODOS")) {
            long agora = System.currentTimeMillis();
            long limiteTempo = calcularLimiteTempo(periodoFiltro, agora);

            dadosFiltrados = dadosFiltrados.stream()
                    .filter(registro -> registro.getEpochMillis() >= limiteTempo)
                    .collect(java.util.stream.Collectors.toList());
        }

        historicoData.setValue(dadosFiltrados);
    }

    public void aplicarOrdenacao(String sortOrder) {
        List<RegistroHistorico> dadosAtuais = historicoData.getValue();
        if (dadosAtuais == null) {
            return;
        }

        List<RegistroHistorico> dadosOrdenados = new ArrayList<>(dadosAtuais);

        if ("MAIS_RECENTES".equals(sortOrder)) {
            dadosOrdenados.sort((a, b) -> Long.compare(b.getEpochMillis(), a.getEpochMillis()));
        } else if ("MAIS_ANTIGOS".equals(sortOrder)) {
            dadosOrdenados.sort((a, b) -> Long.compare(a.getEpochMillis(), b.getEpochMillis()));
        }

        historicoData.setValue(dadosOrdenados);
    }

    /**
     * Limpa todos os filtros e recarrega os dados originais
     */
    public void limparFiltros() {
        // Recarregar dados originais
        carregarDadosReais();
    }

    private long calcularLimiteTempo(String periodoFiltro, long agora) {
        switch (periodoFiltro) {
            case "HOJE":
                // Início do dia atual
                return agora - (agora % (24 * 60 * 60 * 1000));
            case "ONTEM":
                // Início do dia anterior
                return agora - (agora % (24 * 60 * 60 * 1000)) - (24 * 60 * 60 * 1000);
            case "DIAS7":
                return agora - (7L * 24 * 60 * 60 * 1000);
            case "DIAS15":
                return agora - (15L * 24 * 60 * 60 * 1000);
            case "DIAS30":
                return agora - (30L * 24 * 60 * 60 * 1000);
            default:
                return 0; // Todos os dados
        }
    }

    /**
     * Converte lista de HistoricoBaixaResponse para RegistroHistorico
     */
    private List<RegistroHistorico> converterParaRegistroHistorico(List<HistoricoBaixaResponse> responses) {
        List<RegistroHistorico> registros = new ArrayList<>();

        for (HistoricoBaixaResponse response : responses) {
            RegistroHistorico registro = new RegistroHistorico();
            registro.setId(response.getId() != null ? response.getId().toString() : "");
            registro.setNome(response.getNome_produto());
            registro.setUnidades(response.getQuantidade() != null ? response.getQuantidade() : 0);
            registro.setCodigo(response.getCodigo_produto());
            registro.setObservacoes(response.getObservacoes());
            registro.setProdutoId(response.getId_produto());

            // Converter LocalDate para epochMillis
            if (response.getData_acontecimento() != null) {
                long epochMillis = response.getData_acontecimento()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                registro.setEpochMillis(epochMillis);
            } else {
                registro.setEpochMillis(System.currentTimeMillis());
            }

            // Converter tipo de registro
            if (response.getTipo_registro() != null) {
                try {
                    registro.setTipo(RegistroHistorico.TipoMovimentacao.valueOf(response.getTipo_registro().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    registro.setTipo(RegistroHistorico.TipoMovimentacao.BAIXA);
                }
            } else {
                registro.setTipo(RegistroHistorico.TipoMovimentacao.BAIXA);
            }

            registros.add(registro);
        }

        return registros;
    }
}