package com.sustria.codcoz.ui.inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.sustria.codcoz.R;
import com.sustria.codcoz.actions.ConfirmacaoBottomSheetDialogFragment;
import com.sustria.codcoz.actions.PerfilActivity;
import com.sustria.codcoz.actions.ProdutosActivity;
import com.sustria.codcoz.actions.TarefaDetalheBottomSheetDialogFragment;
import com.sustria.codcoz.api.adapter.TarefaAdapter;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.databinding.FragmentInicioBinding;
import com.sustria.codcoz.utils.EmptyStateAdapter;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    private InicioViewModel inicioViewModel;
    private TarefaAdapter tarefaAdapter;
    private EmptyStateAdapter emptyStateAdapter;
    private final Map<LocalDate, List<TarefaResponse>> tarefasPorData = new HashMap<>();
    private CalendarView calendarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        botoes();

        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // Carrega os dados do usuário do cache
        loadUserData();

        // Observa os dados do estoque
        inicioViewModel.getEstoquePercentual().observe(getViewLifecycleOwner(), percentual -> {
            // Esconder o ProgressBar quando os dados chegarem
            if (binding.progressBarChartEstoque != null) {
                binding.progressBarChartEstoque.setVisibility(View.GONE);
            }
            
            binding.txtPercentualAtual.setText(percentual + "%");
            PieChart pieChart = binding.chartEstoque;
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(percentual, ""));
            entries.add(new PieEntry(100f - percentual, ""));
            PieDataSet dataSet = new PieDataSet(entries, "");
            
            // Definir cores baseado na porcentagem
            int corPercentual;
            int corStatus;
            if (percentual >= 70) {
                corPercentual = ContextCompat.getColor(requireContext(), R.color.custom_green_success);
                corStatus = getResources().getColor(android.R.color.holo_green_dark, null);
            } else if (percentual >= 40) {
                corPercentual = ContextCompat.getColor(requireContext(), R.color.custom_orange_link);
                corStatus = getResources().getColor(android.R.color.holo_orange_dark, null);
            } else {
                corPercentual = ContextCompat.getColor(requireContext(), R.color.custom_red_link);
                corStatus = getResources().getColor(android.R.color.holo_red_dark, null);
            }
            
            dataSet.setColors(corPercentual, ContextCompat.getColor(requireContext(), R.color.colorOnSecondary));
            dataSet.setDrawValues(false);
            dataSet.setSliceSpace(2f);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setRotationEnabled(false);
            pieChart.setHoleRadius(75f);
            pieChart.setTransparentCircleRadius(75f);
            pieChart.invalidate();

            // Atualizar cor do status
            binding.tvStatusEstoque.setTextColor(corStatus);
        });

        binding.headerHome.headerPerfil.setOnClickListener(v -> startActivity(new Intent(getContext(), PerfilActivity.class)));

        inicioViewModel.getEstoqueStatus().observe(getViewLifecycleOwner(),
                status -> binding.tvStatusEstoque.setText(status));
        inicioViewModel.getEstoquePercentualAnterior().observe(getViewLifecycleOwner(),
                percentualAnterior -> binding.tvDiaAnteriorPercenual.
                        setText("Percentual: " + percentualAnterior + "%"));
        inicioViewModel.getEstoqueStatusAnterior().observe(getViewLifecycleOwner(),
                statusAnterior -> binding.tvStatusAnterior.setText("Status: " + statusAnterior));

        // Observa os dados da API para os cards
        inicioViewModel.getQuantidadeEstoque().observe(getViewLifecycleOwner(),
                quantidade -> {
                    if (quantidade != null) {
                        binding.prodEstoques.setText(quantidade + "");
                    }
                });

        inicioViewModel.getQuantidadeEstoqueBaixo().observe(getViewLifecycleOwner(),
                quantidade -> {
                    if (quantidade != null) {
                        binding.baixoEstoque.setText(quantidade + "");
                    }
                });

        inicioViewModel.getQuantidadeProximoValidade().observe(getViewLifecycleOwner(),
                quantidade -> {
                    if (quantidade != null) {
                        binding.proximosValidade.setText(quantidade + "");
                    }
                });

        // Configura RecyclerView de tarefas
        setupRecyclerViewTask();
        watchTasks();

        // Listener para quando uma tarefa for finalizada recarregar
        setupTarefaFinalizadaListener();

        return root;
    }

    // Calendário
    public class DayViewContainer extends ViewContainer {
        public final TextView textView;
        public final View bolinha;
        public CalendarDay dia;

        public DayViewContainer(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.calendarDayText);
            bolinha = view.findViewById(R.id.calendarDayTask);
        }
    }

    public class MonthViewContainer extends ViewContainer {
        public final View view;

        public MonthViewContainer(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        calendarView = binding.calendarView;

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100);
        YearMonth endMonth = currentMonth.plusMonths(100);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

        TextView txtMonthYear = view.findViewById(R.id.mesTxt);
        ImageButton btnAnterior = view.findViewById(R.id.btnPrevMonth);
        ImageButton btnProximo = view.findViewById(R.id.btnNextMonth);

        calendarView.setMonthScrollListener(calendarMonth -> {
            YearMonth yearMonth = calendarMonth.getYearMonth();
            String monthYear = yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")) + " " + yearMonth.getYear();
            String monthYearFomated;
            monthYearFomated = monthYear.substring(0, 1).toUpperCase() + monthYear.substring(1);
            txtMonthYear.setText(monthYearFomated);
            return null;
        });

        btnAnterior.setOnClickListener(v -> {
            YearMonth prevMonth = calendarView.findFirstVisibleMonth().getYearMonth()
                    .minusMonths(1);
            calendarView.smoothScrollToMonth(prevMonth);
        });

        btnProximo.setOnClickListener(v -> {
            YearMonth nextMonth = calendarView.findFirstVisibleMonth().getYearMonth()
                    .plusMonths(1);
            calendarView.smoothScrollToMonth(nextMonth);
        });

        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View dayView) {
                return new DayViewContainer(dayView);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay data) {
                container.dia = data;

                LocalDate date = LocalDate.of(data.getDate().getYear(), data.getDate().getMonth(),
                        data.getDate().getDayOfMonth());
                int dia = date.getDayOfMonth();

                container.textView.setText(String.valueOf(dia));

                container.textView.setText(String.valueOf(data.getDate().getDayOfMonth()));
                if (data.getPosition() == DayPosition.MonthDate) {
                    container.textView.setTextColor(Color.parseColor("#0F1829"));
                } else {
                    container.textView.setTextColor(Color.parseColor("#D4D4D4"));
                }

                container.bolinha.setVisibility(View.GONE);
                container.textView.setBackground(null);

                if (date.equals(LocalDate.now(ZoneId.systemDefault()))) {
                    container.textView.setBackgroundResource(R.drawable.bolinha_normal);
                }

                // Verifica se há tarefas para esta data
                if (tarefasPorData.containsKey(date)) {
                    container.bolinha.setVisibility(View.VISIBLE);

                    List<TarefaResponse> tarefasDoDia = tarefasPorData.get(date);
                    boolean temTarefaVencida = false;
                    boolean todasTarefasConcluidas = false;

                    if (tarefasDoDia != null && !tarefasDoDia.isEmpty()) {
                        int totalTarefas = tarefasDoDia.size();
                        int tarefasConcluidas = 0;
                        
                        for (TarefaResponse tarefa : tarefasDoDia) {
                            // Verifica se a tarefa está concluída
                            if (tarefa.getSituacao() != null && tarefa.getSituacao().toLowerCase().contains("concluída")) {
                                tarefasConcluidas++;
                            }
                            // Verifica se a tarefa tem data limite e se está vencida
                            else if (tarefa.getDataLimite() != null && tarefa.getDataLimite().isBefore(LocalDate.now())) {
                                temTarefaVencida = true;
                            }
                        }
                        
                        // Só considera verde se TODAS as tarefas estiverem concluídas
                        todasTarefasConcluidas = (tarefasConcluidas == totalTarefas && totalTarefas > 0);
                    }

                    if (todasTarefasConcluidas) {
                        container.bolinha.setBackgroundResource(R.drawable.bolinha_verde); // Verde para todas concluídas
                    } else if (temTarefaVencida) {
                        container.bolinha.setBackgroundResource(R.drawable.bolinha_vermelha);
                    } else {
                        container.bolinha.setBackgroundResource(R.drawable.bolinha_azul);
                    }
                } else {
                    container.bolinha.setVisibility(View.GONE);
                }

                container.getView().setOnClickListener(v -> {
                    LocalDate dataTarefa = container.dia.getDate();

                    if (tarefasPorData.containsKey(dataTarefa)) {
                        List<TarefaResponse> tarefasDoDia = tarefasPorData.get(dataTarefa);
                        if (tarefasDoDia != null && !tarefasDoDia.isEmpty()) {
                            if (tarefasDoDia.size() == 1) {
                                // Se há apenas uma tarefa, mostra o BottomSheet diretamente
                                TarefaResponse tarefa = tarefasDoDia.get(0);
                                String tipoTarefa = tarefa.getTipoTarefa() != null ? tarefa.getTipoTarefa() : "Tarefa";
                                String ingrediente = tarefa.getIngrediente() != null ? tarefa.getIngrediente() : "Produto";
                                String relator = tarefa.getRelator() != null ? tarefa.getRelator() : "N/A";
                                String dataLimite = tarefa.getDataLimite() != null ?
                                        tarefa.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";

                                TarefaDetalheBottomSheetDialogFragment.newInstance(
                                                tipoTarefa,
                                                ingrediente,
                                                relator,
                                                dataLimite,
                                                tarefa.getSituacao(),
                                                tarefa.getId()
                                        )
                                        .show(getParentFragmentManager(), "TarefaDetalheBottomSheetDialog");
                            } else {
                                // Se há múltiplas tarefas, verificar se todas estão concluídas
                                int tarefasConcluidas = 0;
                                for (TarefaResponse tarefa : tarefasDoDia) {
                                    if (tarefa.getSituacao() != null && tarefa.getSituacao().toLowerCase().contains("concluída")) {
                                        tarefasConcluidas++;
                                    }
                                }
                                
                                if (tarefasConcluidas == tarefasDoDia.size()) {
                                    // Todas as tarefas estão concluídas
                                    ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager(), false);
                                } else {
                                    // Há tarefas pendentes - avisa para olhar na lista
                                    ConfirmacaoBottomSheetDialogFragment.showInfo(getParentFragmentManager(),
                                            "Existem " + tarefasDoDia.size() + " tarefas para este dia. Verifique a lista de tarefas abaixo para visualizá-las.", false);
                                }
                            }
                        }
                    } else {
                        ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Nenhuma tarefa para este dia.", false);
                    }
                });
            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, CalendarMonth calendarMonth) {
                ViewGroup titlesContainer = view.findViewById(R.id.titlesContainer);
                String[] dias = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};

                for (int i = 0; i < titlesContainer.getChildCount(); i++) {
                    View child = titlesContainer.getChildAt(i);
                    if (child instanceof TextView) {
                        TextView texto = (TextView) child;
                        texto.setText(dias[i]);
                    }
                }
            }
        });

        calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        UserDataManager userDataManager = UserDataManager.getInstance();

        if (userDataManager.isDataLoaded() && userDataManager.getEmail() != null) {
            inicioViewModel.loadTasks();
        } else {
            userDataManager.loadDataFromPreferences(requireContext(), () -> inicioViewModel.loadTasks());
        }

    }

    private void loadUserData() {
        UserDataManager userDataManager = UserDataManager.getInstance();

        if (userDataManager.isDataLoaded()) {
            // Dados já estão no cache, usar diretamente
            updateHeaderWithUserInfo();
        } else {
            // Dados não estão no cache, usar dados padrão
            binding.headerHome.headerNome.setText("Olá, Usuário!");
            binding.headerHome.headerFuncao.setText("Estoquista");
        }
    }

    private void updateHeaderWithUserInfo() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String nomeCompleto = userDataManager.getNomeCompleto();
        binding.headerHome.headerNome.setText("Olá, " + nomeCompleto + "!");
        binding.headerHome.headerFuncao.setText("Estoquista");

        // Carrega a imagem de perfil no header
        loadHeaderProfileImage();
    }

    private void loadHeaderProfileImage() {
        String imageUrl = UserDataManager.getInstance().getImagemPerfil();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_imagem_perfil)
                    .error(R.drawable.ic_imagem_perfil)
                    .circleCrop()
                    .into(binding.headerHome.headerPerfil);
        }
    }

    private void setupRecyclerViewTask() {
        tarefaAdapter = new TarefaAdapter();
        emptyStateAdapter = new EmptyStateAdapter(tarefaAdapter);

        if (binding.tarefas != null) {
            binding.tarefas.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        if (binding.tarefas != null) {
            binding.tarefas.setAdapter(emptyStateAdapter);
        }

        // Listeners para cliques nas tarefas
        tarefaAdapter.setOnTarefaClickListener(this::openDialogTask);
    }

    private void watchTasks() {
        // Observa o estado de loading
        inicioViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                // Mostra loading quando está carregando
                emptyStateAdapter.setLoadingState(true);
            }
        });

        // Observa as tarefas
        inicioViewModel.getTarefas().observe(getViewLifecycleOwner(), tarefas -> {
            // Remove loading quando os dados chegam
            emptyStateAdapter.setLoadingState(false);
            
            if (tarefas != null) {
                // Filtrar tarefas concluídas para o RecyclerView
                List<TarefaResponse> tarefasPendentes = new ArrayList<>();
                for (TarefaResponse tarefa : tarefas) {
                    if (tarefa.getSituacao() == null ||
                            !tarefa.getSituacao().toLowerCase().contains("concluída")) {
                        tarefasPendentes.add(tarefa);
                    }
                }

                tarefaAdapter.setTarefas(tarefasPendentes);
                stageTasksByDay(tarefas);

                // Atualiza o estado vazio
                if (tarefasPendentes.isEmpty()) {
                    emptyStateAdapter.setEmptyState(true, "Nenhuma tarefa encontrada",
                            "Não há tarefas pendentes no momento.\nVerifique novamente mais tarde.");
                } else {
                    emptyStateAdapter.setEmptyState(false);
                }
            }
        });
    }

    private void setupTarefaFinalizadaListener() {
        // Listener para recarregar tarefas quando uma for finalizada
        getParentFragmentManager().setFragmentResultListener(
                "tarefa_finalizada",
                this,
                (requestKey, result) -> {
                    boolean tarefaFinalizada = result.getBoolean("tarefa_finalizada", false);
                    if (tarefaFinalizada) {
                        // Recarregar tarefas automaticamente
                        if (inicioViewModel != null) {
                            inicioViewModel.loadTasks();
                            Log.d("InicioFragment", "Tarefas recarregadas após finalização");
                        }
                    }
                }
        );
    }

    private void stageTasksByDay(List<TarefaResponse> tarefas) {
        tarefasPorData.clear();

        for (TarefaResponse tarefa : tarefas) {
            LocalDate dataParaUsar = null;

            // Prioriza data limite, e não usa data atual
            if (tarefa.getDataLimite() != null) {
                dataParaUsar = tarefa.getDataLimite();
            } else {
                dataParaUsar = LocalDate.now();
            }

            if (dataParaUsar != null) {
                if (!tarefasPorData.containsKey(dataParaUsar)) {
                    tarefasPorData.put(dataParaUsar, new ArrayList<>());
                }
                tarefasPorData.get(dataParaUsar).add(tarefa);
            }
        }
        
        // Notifica o calendário para atualizar as bolinhas
        if (calendarView != null) {
            // Força o calendário a recarregar todos os dias visíveis
            YearMonth currentVisibleMonth = calendarView.findFirstVisibleMonth() != null 
                    ? calendarView.findFirstVisibleMonth().getYearMonth() 
                    : YearMonth.now();
            calendarView.notifyMonthChanged(currentVisibleMonth);
        }
    }

    private void openDialogTask(TarefaResponse tarefa) {
        String tipoTarefa = tarefa.getTipoTarefa() != null ? tarefa.getTipoTarefa() : "Tarefa";
        String ingrediente = tarefa.getIngrediente() != null ? tarefa.getIngrediente() : "Produto";
        String relator = tarefa.getRelator() != null ? tarefa.getRelator() : "N/A";
        String dataLimite = tarefa.getDataLimite() != null ?
                tarefa.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";

        TarefaDetalheBottomSheetDialogFragment.newInstance(
                tipoTarefa,
                ingrediente,
                relator,
                dataLimite,
                tarefa.getSituacao(),
                tarefa.getId()
        ).show(getParentFragmentManager(), "TarefaDetalheBottomSheetDialog");
    }

    private void botoes() {
        Intent intent = new Intent(getContext(), ProdutosActivity.class);

        binding.produtos.setOnClickListener(v -> {
            Bundle envelope = new Bundle();
            envelope.putString("tituloHeader", "Produtos em estoque");
            intent.putExtras(envelope);
            startActivity(intent);
        });

        binding.produtosBaixoEstoque.setOnClickListener(v -> {
            Bundle envelope = new Bundle();
            envelope.putString("tituloHeader", "Estoque baixo");
            envelope.putString("filtro", "11");
            intent.putExtras(envelope);
            startActivity(intent);
        });

        binding.produtosProximoValidade.setOnClickListener(v -> {
            Bundle envelope = new Bundle();
            envelope.putString("tituloHeader", "Próximos à validade");
            envelope.putString("filtro", "2025-10-12");
            intent.putExtras(envelope);
            startActivity(intent);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}