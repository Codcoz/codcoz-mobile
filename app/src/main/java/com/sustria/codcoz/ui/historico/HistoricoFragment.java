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

import com.sustria.codcoz.databinding.FragmentHistoricoBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoricoFragment extends Fragment {
    private FragmentHistoricoBinding binding;
    private HistoricoListAdapter adapter;
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
        binding.recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHistorico.setAdapter(adapter);
    }

    private void seedDadosMock() {
        dadosOriginais.clear();
        long now = System.currentTimeMillis();
        dadosOriginais.add(new RegistroHistorico("Arroz", 10, "0001",
                now - 60 * 60 * 1000, TipoFiltro.ENTRADA));
        dadosOriginais.add(new RegistroHistorico("Feijão", 5, "0002",
                now - 26 * 60 * 60 * 1000, TipoFiltro.BAIXA));
        dadosOriginais.add(new RegistroHistorico("Macarrão", 12, "0003",
                now - 5 * 24 * 60 * 60 * 1000, TipoFiltro.ENTRADA));
        dadosOriginais.add(new RegistroHistorico("Açúcar", 2, "0004",
                now - 9 * 24 * 60 * 60 * 1000, TipoFiltro.BAIXA));
        dadosOriginais.add(new RegistroHistorico("Sal", 18, "0005",
                now - 20 * 24 * 60 * 60 * 1000, TipoFiltro.BAIXA));
        dadosOriginais.add(new RegistroHistorico("Óleo", 7, "0006",
                now - 29L * 24 * 60 * 60 * 1000, TipoFiltro.ENTRADA));
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
            if (!termo.isEmpty() && !r.nome.toLowerCase().contains(termo)) continue;
            if (tipoFiltro != TipoFiltro.TODOS && r.tipo != tipoFiltro) continue;
            if (!periodoFiltro.passa(r.epochMillis, now)) continue;
            filtrados.add(r);
        }
        Comparator<RegistroHistorico> comp = Comparator.comparingLong(a -> a.epochMillis);
        if (sortOrder == SortOrder.MAIS_RECENTES) filtrados.sort(comp.reversed());
        else filtrados.sort(comp);
        adapter.submit(filtrados);
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

    static class RegistroHistorico {
        final String nome;
        final int unidades;
        final String codigo;
        final long epochMillis;
        final TipoFiltro tipo;

        RegistroHistorico(String nome, int unidades, String codigo, long epochMillis, TipoFiltro tipo) {
            this.nome = nome;
            this.unidades = unidades;
            this.codigo = codigo;
            this.epochMillis = epochMillis;
            this.tipo = tipo;
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
            h.nome.setText(r.nome);
            h.unidades.setText(String.valueOf(r.unidades));
            h.codigo.setText(r.codigo);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM | HH:mm", java.util.Locale.getDefault());
            h.data.setText(sdf.format(new java.util.Date(r.epochMillis)));
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