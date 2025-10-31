package com.sustria.codcoz.ui.historico;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.actions.FiltrosBottomSheetDialogFragment;
import com.sustria.codcoz.api.model.RegistroHistoricoResponse;
import com.sustria.codcoz.databinding.FragmentHistoricoBinding;
import com.sustria.codcoz.utils.EmptyStateAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoricoFragment extends Fragment {
    private FragmentHistoricoBinding binding;
    private HistoricoListAdapter adapter;
    private EmptyStateAdapter emptyStateAdapter;
    private HistoricoViewModel historicoEstoquistaViewModel;
    private SortOrder sortOrder = SortOrder.MAIS_RECENTES;
    private TipoFiltro tipoFiltro = TipoFiltro.TODOS;
    private PeriodoFiltro periodoFiltro = PeriodoFiltro.TODOS;


    public HistoricoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historicoEstoquistaViewModel =
                new ViewModelProvider(this).get(HistoricoViewModel.class);

        binding = FragmentHistoricoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupLista();
        setupObservers();
        loadData();
        searchSetup();

        binding.imageViewFiltro.setOnClickListener(v -> {
            int sort = (sortOrder == SortOrder.MAIS_RECENTES) ? 0 : 1;
            int tipo = (tipoFiltro == TipoFiltro.ENTRADA) ? 1 : (tipoFiltro == TipoFiltro.BAIXA ? 2 : 0);
            int periodo = mapPeriodoToInt(periodoFiltro);
            FiltrosBottomSheetDialogFragment.show(
                    getParentFragmentManager(),
                    sort,
                    tipo,
                    periodo
            );
        });

        setupHeader();

        getParentFragmentManager().setFragmentResultListener(
                FiltrosBottomSheetDialogFragment.REQUEST_KEY,
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    int sort = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_SORT, 0);
                    int tipo = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_TIPO, 0);
                    int periodo = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_PERIODO, 0);
                    boolean limparFiltros = result.getBoolean(FiltrosBottomSheetDialogFragment.RESULT_LIMPAR, false);

                    if (limparFiltros) {
                        // Limpar filtros
                        sortOrder = SortOrder.MAIS_RECENTES;
                        tipoFiltro = TipoFiltro.TODOS;
                        periodoFiltro = PeriodoFiltro.TODOS;
                        binding.editTextBusca.setText("");
                        historicoEstoquistaViewModel.cleanFilters();
                    } else {
                        // Aplicar filtros normalmente
                        sortOrder = (sort == 0) ? SortOrder.MAIS_RECENTES : SortOrder.MAIS_ANTIGOS;
                        tipoFiltro = (tipo == 1) ? TipoFiltro.ENTRADA : (tipo == 2 ? TipoFiltro.BAIXA : TipoFiltro.TODOS);
                        periodoFiltro = mapIntToPeriodo(periodo);
                        applyFilterSearch();
                    }
                }
        );

        return root;
    }

    private void setupHeader() {
        // Configura header
        binding.headerHistorico.headerFragmentTitle.setText("Histórico de Baixas");

    }

    private int mapPeriodoToInt(PeriodoFiltro p) {
        switch (p) {
            case HOJE:
                return 1;
            case ONTEM:
                return 2;
            case DIAS7:
                return 3;
            case DIAS15:
                return 4;
            case DIAS30:
                return 5;
            case TODOS:
            default:
                return 0;
        }
    }

    private PeriodoFiltro mapIntToPeriodo(int v) {
        switch (v) {
            case 1:
                return PeriodoFiltro.HOJE;
            case 2:
                return PeriodoFiltro.ONTEM;
            case 3:
                return PeriodoFiltro.DIAS7;
            case 4:
                return PeriodoFiltro.DIAS15;
            case 5:
                return PeriodoFiltro.DIAS30;
            case 0:
            default:
                return PeriodoFiltro.TODOS;
        }
    }

    private void setupLista() {
        adapter = new HistoricoListAdapter();
        emptyStateAdapter = new EmptyStateAdapter(adapter);
        binding.recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHistorico.setAdapter(emptyStateAdapter);
    }

    private void setupObservers() {
        // Observar estado de carregamento
        historicoEstoquistaViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (binding.progressBarHistorico != null) {
                    binding.progressBarHistorico.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
                binding.recyclerViewHistorico.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        // Observar mensagens de erro
        historicoEstoquistaViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            // Quando não há erro ou erro foi limpo, atualizar baseado nos dados
            if (errorMessage == null || errorMessage.isEmpty()) {
                List<RegistroHistoricoResponse> registros = historicoEstoquistaViewModel.getHistoricoData().getValue();
                updateDataList(registros, null);
            }
        });

        // Observar dados de histórico
        historicoEstoquistaViewModel.getHistoricoData().observe(getViewLifecycleOwner(), registros -> {
            String errorMessage = historicoEstoquistaViewModel.getErrorMessage().getValue();
            updateDataList(registros, errorMessage);
        });
    }

    private void updateDataList(List<RegistroHistoricoResponse> registros, String errorMessage) {
        // Se houver erro e não houver dados, mostrar mensagem de erro
        if (errorMessage != null && !errorMessage.isEmpty() && (registros == null || registros.isEmpty())) {
            emptyStateAdapter.setEmptyState(true, "Erro ao carregar dados", errorMessage);
            if (binding.progressBarHistorico != null) {
                binding.progressBarHistorico.setVisibility(View.GONE);
            }
            binding.recyclerViewHistorico.setVisibility(View.VISIBLE);
        }
        // Se houver dados (mesmo com erro), mostrar os dados
        else if (registros != null && !registros.isEmpty()) {
            adapter.submit(registros);
            emptyStateAdapter.setEmptyState(false);
        }
        // Se não houver dados e não houver erro, mostrar empty state
        else {
            emptyStateAdapter.setEmptyState(true, "Nenhum histórico encontrado", "Não há registros de histórico disponíveis.");
        }
    }

    private void loadData() {
        // Carregar dados reais da API
        historicoEstoquistaViewModel.loadRealData();
    }

    private void searchSetup() {
        binding.editTextBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                applyFilterSearch();
            }
        });
    }

    private void applyFilterSearch() {
        String termo = binding.editTextBusca.getText() == null ? "" : binding.editTextBusca.getText().toString().trim();
        String tipoFiltroStr = tipoFiltro == TipoFiltro.TODOS ? "TODOS" : tipoFiltro.toString();
        String periodoFiltroStr = periodoFiltro == PeriodoFiltro.TODOS ? "TODOS" : periodoFiltro.toString();
        String sortOrderStr = sortOrder == SortOrder.MAIS_RECENTES ? "MAIS_RECENTES" : "MAIS_ANTIGOS";

        historicoEstoquistaViewModel.filterData(termo, tipoFiltroStr, periodoFiltroStr);
        // Aplicar ordenação
        historicoEstoquistaViewModel.applyOrder(sortOrderStr);
    }


    enum SortOrder {MAIS_RECENTES, MAIS_ANTIGOS}

    enum TipoFiltro {TODOS, ENTRADA, BAIXA}

    enum PeriodoFiltro {
        TODOS, HOJE, ONTEM, DIAS7, DIAS15, DIAS30;

        boolean passa(long epoch, long now) {
            long umDia = 24L * 60 * 60 * 1000;
            switch (this) {
                case HOJE:
                    return (now / umDia) == (epoch / umDia);
                case ONTEM:
                    return (now / umDia - 1) == (epoch / umDia);
                case DIAS7:
                    return now - epoch <= 7 * umDia;
                case DIAS15:
                    return now - epoch <= 15 * umDia;
                case DIAS30:
                    return now - epoch <= 30 * umDia;
                case TODOS:
                default:
                    return true;
            }
        }
    }


    static class HistoricoListAdapter extends RecyclerView.Adapter<HistoricoListAdapter.VH> {
        private final List<RegistroHistoricoResponse> itens = new ArrayList<>();

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico_de_baixas, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int position) {
            RegistroHistoricoResponse r = itens.get(position);
            h.nome.setText(r.getNome());
            h.unidades.setText(String.valueOf(r.getUnidades()));
            h.codigo.setText(r.getCodigo());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM | HH:mm", Locale.getDefault());
            h.data.setText(sdf.format(new Date(r.getEpochMillis())));
        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        void submit(List<RegistroHistoricoResponse> novos) {
            itens.clear();
            itens.addAll(novos);
            notifyDataSetChanged();
        }

        static class VH extends RecyclerView.ViewHolder {
            final TextView nome;
            final TextView unidades;
            final TextView codigo;
            final TextView data;

            VH(View itemView) {
                super(itemView);
                nome = itemView.findViewById(R.id.tvNome);
                unidades = itemView.findViewById(R.id.tvQuantidades);
                codigo = itemView.findViewById(R.id.tvCodProduto);
                data = itemView.findViewById(R.id.dataMovimentacao);
            }
        }
    }

}