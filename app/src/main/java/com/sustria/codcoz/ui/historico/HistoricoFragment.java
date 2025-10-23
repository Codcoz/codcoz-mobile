package com.sustria.codcoz.ui.historico;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.actions.FiltrosBottomSheetDialogFragment;
import com.sustria.codcoz.databinding.FragmentHistoricoBinding;
import com.sustria.codcoz.api.model.MockDataProvider;
import com.sustria.codcoz.api.model.RegistroHistorico;
import com.sustria.codcoz.utils.EmptyStateAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoricoFragment extends Fragment {
    private FragmentHistoricoBinding binding;
    private HistoricoListAdapter adapter;
    private EmptyStateAdapter emptyStateAdapter;
    private final List<RegistroHistorico> dadosOriginais = new ArrayList<>();
    private SortOrder sortOrder = SortOrder.MAIS_RECENTES;
    private TipoFiltro tipoFiltro = TipoFiltro.TODOS;
    private PeriodoFiltro periodoFiltro = PeriodoFiltro.TODOS;


    public HistoricoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HistoricoViewModel historicoEstoquistaViewModel =
                new ViewModelProvider(this).get(HistoricoViewModel.class);

        binding = FragmentHistoricoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupLista();
        seedDadosMock();
        setupBusca();

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
                    int sort = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_SORT,
                            0);
                    int tipo = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_TIPO,
                            0);
                    int periodo = result.getInt(FiltrosBottomSheetDialogFragment.RESULT_PERIODO,
                            0);

                    sortOrder = (sort == 0) ? SortOrder.MAIS_RECENTES : SortOrder.MAIS_ANTIGOS;
                    tipoFiltro = (tipo == 1) ? TipoFiltro.ENTRADA : (tipo == 2 ? TipoFiltro.BAIXA
                            : TipoFiltro.TODOS);
                    periodoFiltro = mapIntToPeriodo(periodo);
                    aplicarFiltrosEBusca();
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

    private void seedDadosMock() {
        dadosOriginais.clear();
        // Mostrar estado vazio em vez de dados mockados
        emptyStateAdapter.setEmptyState(true, "Nenhum histórico encontrado", "Não há registros de histórico disponíveis.");
        aplicarFiltrosEBusca();
    }

    private void setupBusca() {
        binding.editTextBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                aplicarFiltrosEBusca();
            }
        });
    }

    private void aplicarFiltrosEBusca() {
        String termo = binding.editTextBusca.getText() == null ? "" : binding.editTextBusca.getText().toString().trim().toLowerCase();
        long now = System.currentTimeMillis();
        List<RegistroHistorico> filtrados = new ArrayList<>();
        for (RegistroHistorico r : dadosOriginais) {
            if (!termo.isEmpty() && !r.getNome().toLowerCase().contains(termo)) continue;
            if (tipoFiltro != TipoFiltro.TODOS && !r.getTipo().toString().equals(tipoFiltro.toString()))
                continue;
            if (!periodoFiltro.passa(r.getEpochMillis(), now)) continue;
            filtrados.add(r);
        }
        Comparator<RegistroHistorico> comp = Comparator.comparingLong(RegistroHistorico::getEpochMillis);
        if (sortOrder == SortOrder.MAIS_RECENTES) filtrados.sort(comp.reversed());
        else filtrados.sort(comp);
        adapter.submit(filtrados);
        
        // Atualizar estado vazio
        if (filtrados.isEmpty()) {
            emptyStateAdapter.setEmptyState(true, "Nenhum registro encontrado", 
                "Não há registros de histórico para os filtros selecionados.\nTente ajustar os filtros ou verifique novamente mais tarde.");
        } else {
            emptyStateAdapter.setEmptyState(false);
        }
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


    static class HistoricoListAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<HistoricoListAdapter.VH> {
        private final List<RegistroHistorico> itens = new ArrayList<>();

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(com.sustria.codcoz.R.layout.item_historico_de_baixas, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH h, int position) {
            RegistroHistorico r = itens.get(position);
            h.nome.setText(r.getNome());
            h.unidades.setText(String.valueOf(r.getUnidades()));
            h.codigo.setText(r.getCodigo());
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM | HH:mm", java.util.Locale.getDefault());
            h.data.setText(sdf.format(new java.util.Date(r.getEpochMillis())));
        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        void submit(List<RegistroHistorico> novos) {
            itens.clear();
            itens.addAll(novos);
            notifyDataSetChanged();
        }

        static class VH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            final android.widget.TextView nome;
            final android.widget.TextView unidades;
            final android.widget.TextView codigo;
            final android.widget.TextView data;

            VH(View itemView) {
                super(itemView);
                nome = itemView.findViewById(com.sustria.codcoz.R.id.tv_nome);
                unidades = itemView.findViewById(com.sustria.codcoz.R.id.tv_unidades);
                codigo = itemView.findViewById(com.sustria.codcoz.R.id.tv_cod_produto);
                data = itemView.findViewById(com.sustria.codcoz.R.id.dataMovimentacao);
            }
        }
    }

}