package com.sustria.codcoz.ui.inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.sustria.codcoz.actions.TarefaDetalheBottomSheetDialogFragment;
import com.sustria.codcoz.api.adapter.TarefaAdapter;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.databinding.FragmentInicioBinding;
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
    private final Map<LocalDate, List<TarefaResponse>> tarefasPorData = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // Carregar dados do usuário do cache
        loadUserData();

        // Observar dados do estoque
        inicioViewModel.getEstoquePercentual().observe(getViewLifecycleOwner(), percentual -> {
            binding.txtPercentualAtual.setText(percentual + "%");
            PieChart pieChart = binding.chartEstoque;
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(percentual, ""));
            entries.add(new PieEntry(100f - percentual, ""));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.custom_green_success),
                    ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
            dataSet.setDrawValues(false);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setRotationEnabled(false);
            pieChart.setHoleRadius(75f);
            pieChart.invalidate();

            // Atualizar cor do status
            if (percentual >= 70) {
                binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
            } else if (percentual >= 40) {
                binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_orange_dark, null));
            } else {
                binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
            }
        });

        binding.headerHome.headerPerfil.setOnClickListener(v -> startActivity(new Intent(getContext(), PerfilActivity.class)));

        inicioViewModel.getEstoqueStatus().observe(getViewLifecycleOwner(),
                status -> binding.tvStatusEstoque.setText(status));
        inicioViewModel.getEstoquePercentualAnterior().observe(getViewLifecycleOwner(),
                percentualAnterior -> binding.tvDiaAnteriorPercenual.
                        setText("Percentual: " + percentualAnterior + "%"));
        inicioViewModel.getEstoqueStatusAnterior().observe(getViewLifecycleOwner(),
                statusAnterior -> binding.tvStatusAnterior.setText("Status: " + statusAnterior));

        // Configurar RecyclerView de tarefas
        setupRecyclerViewTask();

        // Observar dados das tarefas
        watchTasks();

        return root;
    }

    private void loadEstoqueData() {
        binding.tvStatusEstoque.setText("Ótimo!");
        binding.tvDiaAnterior.setText("Dia anterior: Percentual: 62%");
        binding.tvStatusAnterior.setText("Status: bom");

        float percentualAtual = 78;
        if (percentualAtual >= 70) {
            binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
        } else if (percentualAtual >= 40) {
            binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_orange_dark, null));
        } else {
            binding.tvStatusEstoque.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
        }
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

        CalendarView calendarView = binding.calendarView;

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

                // Verificar se há tarefas para esta data
                if (tarefasPorData.containsKey(date)) {
                    container.bolinha.setVisibility(View.VISIBLE);
                } else {
                    container.bolinha.setVisibility(View.GONE);
                }

                container.getView().setOnClickListener(v -> {
                    LocalDate dataTarefa = container.dia.getDate();

                    if (tarefasPorData.containsKey(dataTarefa)) {
                        List<TarefaResponse> tarefasDoDia = tarefasPorData.get(dataTarefa);
                        if (tarefasDoDia != null && tarefasDoDia.size() == 1) {
                            TarefaDetalheBottomSheetDialogFragment.newInstance(
                                            tarefasDoDia.get(0).getTipoTarefa(),
                                            tarefasDoDia.get(0).getIngrediente(),
                                            tarefasDoDia.get(0).getRelator(),
                                            tarefasDoDia.get(0).getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    )
                                    .show(getParentFragmentManager(), "TarefaDetalheBottomSheetDialog");
                        }
                    } else {
                        ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Nenhuma tarefa para este dia.");
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
            inicioViewModel.loadTarefas();
        } else {
            userDataManager.loadDataFromPreferences(requireContext(), () -> inicioViewModel.loadTarefas());
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
    }

    private void setupRecyclerViewTask() {
        tarefaAdapter = new TarefaAdapter();
        if (binding.tarefas != null) {
            binding.tarefas.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        if (binding.tarefas != null) {
            binding.tarefas.setAdapter(tarefaAdapter);
        }

        // Listeners para cliques nas tarefas
        tarefaAdapter.setOnTarefaClickListener(this::openDialogTask);
    }

    private void watchTasks() {
        inicioViewModel.getTarefas().observe(getViewLifecycleOwner(), tarefas -> {
            if (tarefas != null) {
                tarefaAdapter.setTarefas(tarefas);
                stageTasksByDay(tarefas);
            }
        });

        // Observar o estado de carregamento
        inicioViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
//             binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Tratar mensagens de erro
        inicioViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), errorMessage);
            }
        });
    }

    private void stageTasksByDay(List<TarefaResponse> tarefas) {
        tarefasPorData.clear();

        for (TarefaResponse tarefa : tarefas) {
            if (tarefa.getDataLimite() != null) {
                LocalDate dataLimite = tarefa.getDataLimite();
                if (!tarefasPorData.containsKey(dataLimite)) {
                    tarefasPorData.put(dataLimite, new ArrayList<>());
                }
                tarefasPorData.get(dataLimite).add(tarefa);
            }
        }
    }

    private void openDialogTask(TarefaResponse tarefa) {
        TarefaDetalheBottomSheetDialogFragment.newInstance(
                tarefa.getTipoTarefa(),
                tarefa.getIngrediente(),
                tarefa.getRelator(),
                tarefa.getDataLimite() != null ? tarefa.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null
        ).show(getParentFragmentManager(), "TarefaDetalheBottomSheetDialog");
    }

    private boolean isAuditoria(String tipo) {
        String t = tipo.toLowerCase(Locale.ROOT);
        return t.contains("auditor") || t.contains("confer") || t.contains("estoque");
    }

    private boolean isAtividade(String tipo) {
        String t = tipo.toLowerCase(Locale.ROOT);
        return t.contains("ativ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}