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
import com.sustria.codcoz.utils.CardapioMockData;
import com.sustria.codcoz.api.model.DiaSemanaResponse;
import com.sustria.codcoz.api.model.ItemRefeicaoResponse;
import com.sustria.codcoz.api.model.AlmocoResponse;
import com.sustria.codcoz.api.model.LancheResponse;
import com.sustria.codcoz.api.model.ItemReceitaResponse;
import com.sustria.codcoz.api.model.ItemReceitaIngredienteResponse;
import com.sustria.codcoz.api.model.ItemReceitaIngredienteCompletoResponse;

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
        
        // Para usar dados mockados, descomente a linha abaixo e comente carregarCardapios()
        // carregarCardapiosMockados();
        carregarCardapios();
    }

    private void setupHeader() {
        binding.headerCardapioSemanal.headerActivityBackTitle.setText("Cardápio Semanal");
        binding.headerCardapioSemanal.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    /**
     * Carrega cardápios da API
     * Se a API retornar 200 mas com dados vazios, usa automaticamente os dados mockados
     */
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
                    // Verifica se os dados estão vazios (mesmo com resposta 200)
                    boolean dadosVazios = cardapios == null || cardapios.isEmpty();
                    
                    if (!dadosVazios) {
                        CardapioResponse cardapio = cardapios.get(0);
                        // Verifica se o cardápio semanal está vazio
                        if (cardapio.getCardapioSemanal() == null || cardapio.getCardapioSemanal().isEmpty()) {
                            dadosVazios = true;
                        }
                    }
                    
                    if (dadosVazios) {
                        // Se estiver vazio, usa dados mockados automaticamente
                        carregarCardapiosMockados();
                    } else {
                        // Usa os dados da API
                        CardapioResponse cardapio = cardapios.get(0);
                        setupExpandableListView(cardapio.getCardapioSemanal());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    // Em caso de erro, também usa dados mockados para não deixar a tela vazia
                    carregarCardapiosMockados();
                });
            }
        });
    }

    /**
     * Carrega dados mockados para teste
     * Use este método para testar a interface sem precisar da API
     */
    private void carregarCardapiosMockados() {
        // Simula um delay de rede para teste
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            List<CardapioResponse> cardapios = CardapioMockData.getMockCardapios();
            
            if (cardapios != null && !cardapios.isEmpty()) {
                // Usar o primeiro cardápio encontrado
                CardapioResponse cardapio = cardapios.get(0);
                if (cardapio.getCardapioSemanal() != null && !cardapio.getCardapioSemanal().isEmpty()) {
                    setupExpandableListView(cardapio.getCardapioSemanal());
                } else {
                    mostrarEstadoVazio("Nenhum cardápio encontrado", "Não há cardápios disponíveis para esta semana.");
                }
            } else {
                mostrarEstadoVazio("Nenhum cardápio encontrado", "Não há cardápios disponíveis para esta semana.");
            }
        }, 500); // Simula delay de 500ms
    }

    private void setupExpandableListView(List<DiaSemanaResponse> cardapioSemanal) {
        diaSemana = new ArrayList<>();
        itemRefeicao = new HashMap<>();

        for (DiaSemanaResponse dia : cardapioSemanal) {
            // Usa diaSemana se disponível, caso contrário usa data formatada
            String tituloDia = dia.getDiaSemana();
            if (tituloDia == null || tituloDia.isEmpty()) {
                tituloDia = dia.getData() != null ? dia.getData() : "Dia";
            }
            diaSemana.add(tituloDia);

            List<String> refeicoesTexto = new ArrayList<>();

            // Processar estrutura antiga (compatibilidade)
            if (dia.getRefeicoes() != null && !dia.getRefeicoes().isEmpty()) {
                for (ItemRefeicaoResponse refeicao : dia.getRefeicoes()) {
                    StringBuilder refeicaoTxt = new StringBuilder();
                    refeicaoTxt.append(refeicao.getTipo()).append(":");
                    if (refeicao.getItens() != null) {
                        for (String item : refeicao.getItens()) {
                            refeicaoTxt.append("\n- ").append(item);
                        }
                    }
                    refeicoesTexto.add(refeicaoTxt.toString());
                }
            }

            // Processar estrutura nova do MongoDB
            if (dia.getAlmoco() != null) {
                StringBuilder almocoTxt = new StringBuilder();
                almocoTxt.append("Almoço:");
                AlmocoResponse almoco = dia.getAlmoco();

                if (almoco.getArrozIntegral() != null && almoco.getArrozIntegral().getReceitaId() != null) {
                    almocoTxt.append("\n- Arroz Integral");
                }
                if (almoco.getArroz() != null && almoco.getArroz().getReceitaId() != null) {
                    almocoTxt.append("\n- Arroz");
                }
                if (almoco.getFeijao() != null && almoco.getFeijao().getReceitaId() != null) {
                    almocoTxt.append("\n- Feijão");
                }
                if (almoco.getProteinas() != null && !almoco.getProteinas().isEmpty()) {
                    for (ItemReceitaResponse proteina : almoco.getProteinas()) {
                        if (proteina.getReceitaId() != null) {
                            almocoTxt.append("\n- Proteína");
                        }
                    }
                }
                if (almoco.getGuarnicao() != null && almoco.getGuarnicao().getReceitaId() != null) {
                    almocoTxt.append("\n- Guarnição");
                }
                if (almoco.getSaladas() != null && !almoco.getSaladas().isEmpty()) {
                    for (ItemReceitaResponse salada : almoco.getSaladas()) {
                        if (salada.getReceitaId() != null) {
                            almocoTxt.append("\n- Salada");
                        }
                    }
                }
                if (almoco.getMolhoSalada() != null && almoco.getMolhoSalada().getReceitaId() != null) {
                    almocoTxt.append("\n- Molho de Salada");
                }
                if (almoco.getSobremesa() != null && almoco.getSobremesa().getReceitaId() != null) {
                    almocoTxt.append("\n- Sobremesa");
                }
                refeicoesTexto.add(almocoTxt.toString());
            }

            if (dia.getLancheManha() != null) {
                StringBuilder lancheManhaTxt = new StringBuilder();
                lancheManhaTxt.append("Lanche da Manhã:");
                LancheResponse lancheManha = dia.getLancheManha();

                if (lancheManha.getOpcoes() != null && !lancheManha.getOpcoes().isEmpty()) {
                    for (ItemReceitaIngredienteCompletoResponse opcao : lancheManha.getOpcoes()) {
                        if (opcao.getReceitaId() != null) {
                            lancheManhaTxt.append("\n- Opção");
                        }
                    }
                }
                if (lancheManha.getFruta() != null && lancheManha.getFruta().getIngredienteId() != null) {
                    lancheManhaTxt.append("\n- Fruta");
                }
                if (lancheManha.getOpcoesFixas() != null && lancheManha.getOpcoesFixas().getReceitaId() != null) {
                    lancheManhaTxt.append("\n- Opção Fixa");
                }
                refeicoesTexto.add(lancheManhaTxt.toString());
            }

            if (dia.getLancheTarde() != null) {
                StringBuilder lancheTardeTxt = new StringBuilder();
                lancheTardeTxt.append("Lanche da Tarde:");
                LancheResponse lancheTarde = dia.getLancheTarde();

                if (lancheTarde.getOpcoes() != null && !lancheTarde.getOpcoes().isEmpty()) {
                    for (ItemReceitaIngredienteCompletoResponse opcao : lancheTarde.getOpcoes()) {
                        if (opcao.getReceitaId() != null) {
                            lancheTardeTxt.append("\n- Opção");
                        }
                    }
                }
                if (lancheTarde.getFruta() != null && lancheTarde.getFruta().getIngredienteId() != null) {
                    lancheTardeTxt.append("\n- Fruta");
                }
                if (lancheTarde.getOpcoesFixas() != null && lancheTarde.getOpcoesFixas().getReceitaId() != null) {
                    lancheTardeTxt.append("\n- Opção Fixa");
                }
                refeicoesTexto.add(lancheTardeTxt.toString());
            }

            // Se não há refeições, adiciona mensagem padrão
            if (refeicoesTexto.isEmpty()) {
                refeicoesTexto.add("Nenhuma refeição cadastrada para este dia.");
            }

            itemRefeicao.put(tituloDia, refeicoesTexto);
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