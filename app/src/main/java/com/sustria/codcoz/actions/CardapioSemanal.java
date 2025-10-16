package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityCardapioSemanalBinding;
import com.sustria.codcoz.model.DiaSemana;
import com.sustria.codcoz.model.ItemRefeicao;
import com.sustria.codcoz.model.MockDataProvider;

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

        // Habilita o modo Edge-to-Edge (tela inteira)
        EdgeToEdge.enable(this);
        binding = ActivityCardapioSemanalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

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

        adapter = new com.sustria.codcoz.api.adapter.ExpandableListAdapter(this, diaSemana, itemRefeicao);
        binding.expandableListView.setAdapter(adapter);
    }
}