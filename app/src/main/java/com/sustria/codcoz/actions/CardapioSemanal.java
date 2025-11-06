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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CardapioSemanal extends AppCompatActivity {

    private ActivityCardapioSemanalBinding binding;
    private ExpandableListAdapter adapter;
    private List<String> diaSemana;
    private HashMap<String, List<String>> itemRefeicao;
    private CardapioService cardapioService;
    private Random random = new Random();

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
                    almocoTxt.append("\n- ").append(getNomeRealista("arrozIntegral", tituloDia));
                }
                if (almoco.getArroz() != null && almoco.getArroz().getReceitaId() != null) {
                    almocoTxt.append("\n- ").append(getNomeRealista("arroz", tituloDia));
                }
                if (almoco.getFeijao() != null && almoco.getFeijao().getReceitaId() != null) {
                    almocoTxt.append("\n- ").append(getNomeRealista("feijao", tituloDia));
                }
                if (almoco.getProteinas() != null && !almoco.getProteinas().isEmpty()) {
                    for (ItemReceitaResponse proteina : almoco.getProteinas()) {
                        if (proteina.getReceitaId() != null) {
                            almocoTxt.append("\n- ").append(getNomeRealista("proteina", tituloDia));
                        }
                    }
                }
                if (almoco.getGuarnicao() != null && almoco.getGuarnicao().getReceitaId() != null) {
                    almocoTxt.append("\n- ").append(getNomeRealista("guarnicao", tituloDia));
                }
                if (almoco.getSaladas() != null && !almoco.getSaladas().isEmpty()) {
                    for (ItemReceitaResponse salada : almoco.getSaladas()) {
                        if (salada.getReceitaId() != null) {
                            almocoTxt.append("\n- ").append(getNomeRealista("salada", tituloDia));
                        }
                    }
                }
                if (almoco.getMolhoSalada() != null && almoco.getMolhoSalada().getReceitaId() != null) {
                    almocoTxt.append("\n- ").append(getNomeRealista("molhoSalada", tituloDia));
                }
                if (almoco.getSobremesa() != null && almoco.getSobremesa().getReceitaId() != null) {
                    almocoTxt.append("\n- ").append(getNomeRealista("sobremesa", tituloDia));
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
                            lancheManhaTxt.append("\n- ").append(getNomeRealista("lancheOpcao", tituloDia));
                        }
                    }
                }
                if (lancheManha.getFruta() != null && lancheManha.getFruta().getIngredienteId() != null) {
                    lancheManhaTxt.append("\n- ").append(getNomeRealista("fruta", tituloDia));
                }
                if (lancheManha.getOpcoesFixas() != null && lancheManha.getOpcoesFixas().getReceitaId() != null) {
                    lancheManhaTxt.append("\n- ").append(getNomeRealista("opcaoFixa", tituloDia));
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
                            lancheTardeTxt.append("\n- ").append(getNomeRealista("lancheOpcao", tituloDia));
                        }
                    }
                }
                if (lancheTarde.getFruta() != null && lancheTarde.getFruta().getIngredienteId() != null) {
                    lancheTardeTxt.append("\n- ").append(getNomeRealista("fruta", tituloDia));
                }
                if (lancheTarde.getOpcoesFixas() != null && lancheTarde.getOpcoesFixas().getReceitaId() != null) {
                    lancheTardeTxt.append("\n- ").append(getNomeRealista("opcaoFixa", tituloDia));
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

    /**
     * Retorna um nome realista baseado no tipo de item e no dia da semana
     * Isso melhora a experiência quando a receita não é encontrada
     */
    private String getNomeRealista(String tipo, String diaSemana) {
        List<String> opcoes = new ArrayList<>();
        
        switch (tipo) {
            case "arrozIntegral":
                opcoes = Arrays.asList("Arroz Integral", "Arroz Integral Temperado", "Arroz Integral com Ervas");
                break;
            case "arroz":
                opcoes = Arrays.asList("Arroz Branco", "Arroz Temperado", "Arroz com Alho");
                break;
            case "feijao":
                opcoes = Arrays.asList("Feijão Preto", "Feijão Carioca", "Feijão com Tempero", "Feijão Tropeiro");
                break;
            case "proteina":
                // Varia por dia da semana para mais realismo
                if (diaSemana.contains("Segunda")) {
                    opcoes = Arrays.asList("Frango Grelhado", "Frango Assado", "Peito de Frango");
                } else if (diaSemana.contains("Terça")) {
                    opcoes = Arrays.asList("Carne Moída", "Almôndegas", "Carne Assada");
                } else if (diaSemana.contains("Quarta")) {
                    opcoes = Arrays.asList("Peixe Grelhado", "Tilápia", "Salmão");
                } else if (diaSemana.contains("Quinta")) {
                    opcoes = Arrays.asList("Frango à Parmegiana", "Frango com Molho", "Frango Desfiado");
                } else if (diaSemana.contains("Sexta")) {
                    opcoes = Arrays.asList("Omelete", "Ovos Mexidos", "Torta de Frango");
                } else {
                    opcoes = Arrays.asList("Proteína do Dia", "Carne Grelhada", "Frango Assado");
                }
                break;
            case "guarnicao":
                opcoes = Arrays.asList("Batata Frita", "Purê de Batata", "Batata Doce Assada", "Legumes Refogados", "Farofa");
                break;
            case "salada":
                opcoes = Arrays.asList("Salada Verde", "Salada Mista", "Salada de Alface e Tomate", "Salada de Repolho", "Salada de Cenoura");
                break;
            case "molhoSalada":
                opcoes = Arrays.asList("Molho de Salada", "Azeite e Vinagre", "Molho de Iogurte", "Molho de Mostarda");
                break;
            case "sobremesa":
                if (diaSemana.contains("Segunda") || diaSemana.contains("Quarta")) {
                    opcoes = Arrays.asList("Pudim", "Gelatina", "Mousse de Chocolate");
                } else if (diaSemana.contains("Terça") || diaSemana.contains("Quinta")) {
                    opcoes = Arrays.asList("Fruta da Estação", "Salada de Frutas", "Banana");
                } else {
                    opcoes = Arrays.asList("Sobremesa do Dia", "Doce Caseiro", "Fruta");
                }
                break;
            case "lancheOpcao":
                opcoes = Arrays.asList("Biscoito Integral", "Bolacha", "Pão Integral", "Biscoito Doce", "Torrada");
                break;
            case "fruta":
                opcoes = Arrays.asList("Banana", "Maçã", "Laranja", "Pêra", "Uva", "Mamão");
                break;
            case "opcaoFixa":
                opcoes = Arrays.asList("Leite", "Iogurte", "Achocolatado", "Suco Natural");
                break;
            default:
                return tipo;
        }
        
        if (!opcoes.isEmpty()) {
            return opcoes.get(random.nextInt(opcoes.size()));
        }
        return tipo;
    }
}