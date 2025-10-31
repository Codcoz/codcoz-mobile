package com.sustria.codcoz.ui.historico;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sustria.codcoz.api.model.HistoricoBaixaResponse;
import com.sustria.codcoz.api.model.RegistroHistoricoResponse;
import com.sustria.codcoz.api.service.HistoricoService;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoricoViewModel extends ViewModel {

    private final MutableLiveData<List<RegistroHistoricoResponse>> historicoData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final HistoricoService historicoService;

    // Lista para armazenar os dados originais (sem filtros)
    private List<RegistroHistoricoResponse> dadosOriginais = new ArrayList<>();

    public HistoricoViewModel() {
        historicoData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        historicoService = new HistoricoService();

        // Inicializar com estado de carregamento
        isLoading.setValue(false);
    }

    public LiveData<List<RegistroHistoricoResponse>> getHistoricoData() {
        return historicoData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadRealData() {
        Log.d("HistoricoViewModel", "Iniciando carregamento de dados reais");
        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Obtendo o ID da empresa do usuário logado
        Integer empresaId = UserDataManager.getInstance().getEmpresaId();
        Log.d("HistoricoViewModel", "ID da empresa: " + empresaId);

        if (empresaId == null) {
            Log.w("HistoricoViewModel", "ID da empresa não encontrado");
            errorMessage.setValue("ID da empresa não encontrado. Faça login novamente.");
            isLoading.setValue(false);
            historicoData.setValue(new ArrayList<>());
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

                        if (result == null || result.isEmpty()) {
                            Log.d("HistoricoViewModel", "Nenhum registro retornado pela API");
                            dadosOriginais = new ArrayList<>();
                            historicoData.setValue(new ArrayList<>());
                        } else {
                            List<RegistroHistoricoResponse> registros = convertHistorical(result);
                            dadosOriginais = registros;
                            historicoData.setValue(registros);
                        }
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("HistoricoViewModel", "Erro na API: " + error);
                        errorMessage.setValue(error);
                        isLoading.setValue(false);
                        dadosOriginais = new ArrayList<>();
                        historicoData.setValue(new ArrayList<>());
                    }
                }
        );
    }

    public void filterData(String busca, String tipoFiltro, String periodoFiltro) {
        if (dadosOriginais == null || dadosOriginais.isEmpty()) {
            return;
        }

        List<RegistroHistoricoResponse> dadosFiltrados = new ArrayList<>(dadosOriginais);

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
                RegistroHistoricoResponse.TipoMovimentacao tipo = RegistroHistoricoResponse.TipoMovimentacao.valueOf(tipoFiltro);
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
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(registro -> passPeriodFilter(periodoFiltro, registro.getEpochMillis(), agora))
                    .collect(java.util.stream.Collectors.toList());
        }

        historicoData.setValue(dadosFiltrados);
    }

    private boolean passPeriodFilter(String periodoFiltro, long epoch, long agora) {
        long umDia = 24L * 60 * 60 * 1000;
        long diaEpoch = epoch / umDia;
        long diaAgora = agora / umDia;

        switch (periodoFiltro) {
            case "HOJE":
                return diaEpoch == diaAgora;
            case "ONTEM":
                return diaEpoch == (diaAgora - 1);
            case "DIAS7":
                return agora - epoch <= 7L * umDia;
            case "DIAS15":
                return agora - epoch <= 15L * umDia;
            case "DIAS30":
                return agora - epoch <= 30L * umDia;
            default:
                return true;
        }
    }

    public void applyOrder(String sortOrder) {
        List<RegistroHistoricoResponse> dadosAtuais = historicoData.getValue();
        if (dadosAtuais == null) {
            return;
        }

        List<RegistroHistoricoResponse> dadosOrdenados = new ArrayList<>(dadosAtuais);

        if ("MAIS_RECENTES".equals(sortOrder)) {
            dadosOrdenados.sort((a, b) -> Long.compare(b.getEpochMillis(), a.getEpochMillis()));
        } else if ("MAIS_ANTIGOS".equals(sortOrder)) {
            dadosOrdenados.sort(Comparator.comparingLong(RegistroHistoricoResponse::getEpochMillis));
        }

        historicoData.setValue(dadosOrdenados);
    }

    public void cleanFilters() {
        // Restaurar dados originais sem filtros
        historicoData.setValue(new ArrayList<>(dadosOriginais));
    }

    private List<RegistroHistoricoResponse> convertHistorical(List<HistoricoBaixaResponse> responses) {
        List<RegistroHistoricoResponse> registros = new ArrayList<>();

        for (HistoricoBaixaResponse response : responses) {
            RegistroHistoricoResponse registro = new RegistroHistoricoResponse();
            registro.setId(response.getId() != null ? response.getId() : "");
            registro.setNome(response.getNome_produto());
            registro.setUnidades(response.getQuantidade() != null ? response.getQuantidade() : 0);
            registro.setCodigo(response.getCodigo_produto());
            registro.setObservacoes(response.getObservacoes());
            registro.setProdutoId(response.getId_produto());

            // Converter data mantendo o horário quando disponível
            LocalDateTime dataAcontecimento = response.getDataAcontecimentoAsLocalDateTime();
            if (dataAcontecimento != null) {
                long epochMillis = dataAcontecimento
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                registro.setEpochMillis(epochMillis);
            } else {
                registro.setEpochMillis(System.currentTimeMillis());
            }

            // Converter tipo de registro (Entrada/Saída para ENTRADA/BAIXA)
            if (response.getTipo_registro() != null) {
                String tipoStr = response.getTipo_registro();
                if (tipoStr.equalsIgnoreCase("Entrada")) {
                    registro.setTipo(RegistroHistoricoResponse.TipoMovimentacao.ENTRADA);
                } else if (tipoStr.equalsIgnoreCase("Saída")) {
                    registro.setTipo(RegistroHistoricoResponse.TipoMovimentacao.BAIXA);
                } else {
                    try {
                        registro.setTipo(RegistroHistoricoResponse.TipoMovimentacao.valueOf(tipoStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        registro.setTipo(RegistroHistoricoResponse.TipoMovimentacao.BAIXA);
                    }
                }
            } else {
                registro.setTipo(RegistroHistoricoResponse.TipoMovimentacao.BAIXA);
            }

            registros.add(registro);
        }

        return registros;
    }
}