package com.sustria.codcoz.ui.cardapio.actions;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.databinding.ActivityCardapioSemanalBinding;
import com.sustria.codcoz.models.DiaSemana;
import com.sustria.codcoz.models.ItemRefeicao;
import com.sustria.codcoz.models.MockDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardapioSemanal extends AppCompatActivity {

    private ActivityCardapioSemanalBinding binding;
    private ExpandableListAdapter adapter;
    private List<String> diaSemana;
    private HashMap<String, List<String>> itemRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCardapioSemanalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        setupExpandableListView();
    }

    private void setupHeader() {
        binding.headerCardapioSemanal.headerActivityBackTitle.setText("Cardápio Semanal");
        binding.headerCardapioSemanal.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupExpandableListView() {
        List<DiaSemana> cardapioSemanal = MockDataProvider.getMockCardapioSemanal();

        diaSemana = new ArrayList<>();
        itemRefeicao = new HashMap<>();

        for (DiaSemana dia : cardapioSemanal) {
            diaSemana.add(dia.getDia());

            List<String> refeicoesTexto = new ArrayList<>();
            for (ItemRefeicao refeicao : dia.getRefeicoes()) {
                StringBuilder refeicaoTxt = new StringBuilder();
                refeicaoTxt.append(refeicao.getTipo()).append(":");
                for (String item : refeicao.getItens()) {
                    refeicaoTxt.append("\n- ").append(item);
                }
                refeicoesTexto.add(refeicaoTxt.toString());
            }

            itemRefeicao.put(dia.getDia(), refeicoesTexto);
        }

        adapter = new com.sustria.codcoz.ui.cardapio.actions.ExpandableListAdapter(this, diaSemana, itemRefeicao);
        binding.expandableListView.setAdapter(adapter);
    }
}