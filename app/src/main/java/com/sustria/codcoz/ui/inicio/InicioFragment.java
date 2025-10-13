package com.sustria.codcoz.ui.inicio;

import android.app.AlertDialog;
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
import com.sustria.codcoz.actions.PerfilActivity;
import com.sustria.codcoz.databinding.FragmentInicioBinding;
import com.sustria.codcoz.ui.inicio.produtos.ProdutoActivity;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        botoes();

        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        // Observar dados do estoque
        inicioViewModel.getEstoquePercentual().observe(getViewLifecycleOwner(), percentual -> {
            binding.txtPercentualAtual.setText(percentual + "%");
            PieChart pieChart = binding.chartEstoque;
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(percentual, ""));
            entries.add(new PieEntry(100f - percentual, ""));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(
                    ContextCompat.getColor(requireContext(), R.color.custom_green_success),
                    ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant)
            );
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

        binding.headerHome.headerPerfil.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PerfilActivity.class));
        });

        inicioViewModel.getEstoqueStatus().observe(getViewLifecycleOwner(), status -> {
            binding.tvStatusEstoque.setText(status);
        });
        inicioViewModel.getEstoquePercentualAnterior().observe(getViewLifecycleOwner(), percentualAnterior -> {
            binding.tvDiaAnteriorPercenual.setText("Percentual: " + percentualAnterior + "%");
        });
        inicioViewModel.getEstoqueStatusAnterior().observe(getViewLifecycleOwner(), statusAnterior -> {
            binding.tvStatusAnterior.setText("Status: " + statusAnterior);
        });

        // Futuramente: observar atividadesRecentes para atualizar lista

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
        Map<LocalDate, String> diaTarefa = new HashMap<>();
        diaTarefa.put(LocalDate.of(2025, 9, 23), "Realizar auditoria");

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
            String monthYear = yearMonth.getMonth()
                    .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"))
                    + " " + yearMonth.getYear();
            txtMonthYear.setText(monthYear);
            return null;
        });

        btnAnterior.setOnClickListener(v -> {
            YearMonth prevMonth = calendarView.findFirstVisibleMonth().getYearMonth().minusMonths(1);
            calendarView.smoothScrollToMonth(prevMonth);
        });

        btnProximo.setOnClickListener(v -> {
            YearMonth nextMonth = calendarView.findFirstVisibleMonth().getYearMonth().plusMonths(1);
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

                LocalDate date = LocalDate.of(
                        data.getDate().getYear(),
                        data.getDate().getMonth(),
                        data.getDate().getDayOfMonth()
                );
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

                if (diaTarefa.containsKey(date)) {
                    container.bolinha.setVisibility(View.VISIBLE);
                }

                container.getView().setOnClickListener(v -> {
                    LocalDate dataTarefa = container.dia.getDate();

                    if (diaTarefa.containsKey(dataTarefa)) {
                        String tarefa = diaTarefa.get(dataTarefa);

                        new AlertDialog.Builder(requireContext())
                                .setTitle(String.valueOf(dataTarefa.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                                .setMessage(tarefa)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setTitle(String.valueOf(dataTarefa.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                                .setMessage("Nenhuma tarefa para este dia.")
                                .setPositiveButton("OK", null)
                                .show();
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
    }

    private void botoes() {
        binding.produtos.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProdutoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}