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
import com.sustria.codcoz.api.model.CardapioResponse;
import com.sustria.codcoz.api.service.CardapioService;
import com.sustria.codcoz.databinding.ActivityCardapioSemanalBinding;
import com.sustria.codcoz.utils.UserDataManager;
import com.sustria.codcoz.api.model.DiaSemanaResponse;
import com.sustria.codcoz.api.model.ItemRefeicaoResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardapioSemanal extends AppCompatActivity {

    private ActivityCardapioSemanalBinding binding;
    private ExpandableListAdapter adapter;
    private List<String> diaSemana;
    private HashMap<String, List<String>> itemRefeicao;
    private CardapioService cardapioService;

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

        // Inicializar serviço
        cardapioService = new CardapioService();
        
        setupHeader();
        carregarCardapios();
    }

    private void setupHeader() {
        binding.headerCardapioSemanal.headerActivityBackTitle.setText("Cardápio Semanal");
        binding.headerCardapioSemanal.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void carregarCardapios() {
        Integer empresaId = UserDataManager.getInstance().getEmpresaId();
        if (empresaId == null) {
            mostrarEstadoVazio("Erro", "Empresa não identificada");
            return;
        }
        
        cardapioService.getCardapios(empresaId.toString(), new CardapioService.CardapioCallback<>() {
            @Override
            public void onSuccess(List<CardapioResponse> cardapios) {
                runOnUiThread(() -> {
                    if (cardapios != null && !cardapios.isEmpty()) {
                        // Usar o primeiro cardápio encontrado
                        CardapioResponse cardapio = cardapios.get(0);
                        if (cardapio.getCardapioSemanal() != null && !cardapio.getCardapioSemanal().isEmpty()) {
                            setupExpandableListView(cardapio.getCardapioSemanal());
                        } else {
                            // Mostrar estado vazio se não houver cardápio semanal
                            mostrarEstadoVazio("Nenhum cardápio encontrado", "Não há cardápios disponíveis para esta semana.");
                        }
                    } else {
                        // Mostrar estado vazio se não houver cardápios
                        mostrarEstadoVazio("Nenhum cardápio encontrado", "Não há cardápios disponíveis para esta semana.");
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    // Mostrar estado vazio em caso de erro
                    mostrarEstadoVazio("Erro ao carregar cardápio", "Não foi possível carregar o cardápio. Tente novamente mais tarde.");
                });
            }
        });
    }

    private void setupExpandableListView(List<DiaSemanaResponse> cardapioSemanal) {
        diaSemana = new ArrayList<>();
        itemRefeicao = new HashMap<>();

        for (DiaSemanaResponse dia : cardapioSemanal) {
            diaSemana.add(dia.getDia());

            List<String> refeicoesTexto = new ArrayList<>();
            for (ItemRefeicaoResponse refeicao : dia.getRefeicoes()) {
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

    private void mostrarEstadoVazio(String titulo, String mensagem) {
        // Esconder o ExpandableListView e mostrar mensagem de estado vazio
        binding.expandableListView.setVisibility(android.view.View.GONE);
        
        // Criar e mostrar um layout de estado vazio simples
        android.widget.LinearLayout emptyLayout = new android.widget.LinearLayout(this);
        emptyLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
        emptyLayout.setGravity(android.view.Gravity.CENTER);
        emptyLayout.setPadding(32, 64, 32, 64);
        
        android.widget.TextView tituloView = new android.widget.TextView(this);
        tituloView.setText(titulo);
        tituloView.setTextSize(18);
        tituloView.setTextColor(getResources().getColor(R.color.colorPrimary));
        tituloView.setGravity(android.view.Gravity.CENTER);
        tituloView.setPadding(0, 0, 0, 16);
        
        android.widget.TextView mensagemView = new android.widget.TextView(this);
        mensagemView.setText(mensagem);
        mensagemView.setTextSize(14);
        mensagemView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        mensagemView.setGravity(android.view.Gravity.CENTER);
        
        emptyLayout.addView(tituloView);
        emptyLayout.addView(mensagemView);
        
        binding.getRoot().addView(emptyLayout);
    }
}