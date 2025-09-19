package com.sustria.codcoz.ui.inicio;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.FragmentInicioBinding;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    private InicioViewModel inicioViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar ViewModel
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
                    ContextCompat.getColor(requireContext(), R.color.md_theme_onCorrect),
                    ContextCompat.getColor(requireContext(), R.color.md_theme_onSurfaceVariant)
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

    private void loadAtividadesRecentes() {
//         Simular carregamento de atividades recentes

//         carregar dinamicamente (só nao sei ainda)

//         Exemplo de como adicionar atividades dinamicamente:
        /*
        Atividade atividade1 = new Atividade(
            "Fulano de Tal",
            "XML importado - 32 produtos",
            "16:50",
            "hoje"
        );
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //    Classe para representar uma atividade, ainda nao usado dinamicamente,
//    futuramentevou ver como faz isso 🥲
    public static class Atividade {
        private String nome;
        private String descricao;
        private String hora;
        private String data;

        public Atividade(String nome, String descricao, String hora, String data) {
            this.nome = nome;
            this.descricao = descricao;
            this.hora = hora;
            this.data = data;
        }

        // Getters
        public String getNome() {
            return nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getHora() {
            return hora;
        }

        public String getData() {
            return data;
        }
    }
}