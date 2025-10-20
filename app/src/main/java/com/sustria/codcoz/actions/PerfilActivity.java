package com.sustria.codcoz.actions;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityPerfilBinding;
import com.sustria.codcoz.api.adapter.PerfilTarefaAdapter;
import com.sustria.codcoz.ui.perfil.PerfilViewModel;
import com.sustria.codcoz.utils.UserDataManager;
import com.sustria.codcoz.utils.EmptyStateAdapter;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private PerfilTarefaAdapter adapter;
    private EmptyStateAdapter emptyStateAdapter;
    private int periodoSelecionado = 7; // 7 dias por padrão
    private boolean isAtividadesSelecionado = true; // Atividades por padrão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita o modo Edge-to-Edge (tela inteira)
        EdgeToEdge.enable(this);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupHeader();
        setupUi();
        setupRecyclerView();
        setupViewModel();
        carregarDadosUsuario();
        
        // Carregar tarefas iniciais
        carregarTarefas();
    }

    private void setupHeader() {
        binding.headerPerfil.headerActivityBackTitle.setText("Perfil");
        binding.headerPerfil.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupUi() {
        // Tabs Atividades/Auditorias
        binding.tabAtividades.setOnClickListener(v -> {
            selectTab(true);
            carregarTarefas();
        });
        binding.tabAuditorias.setOnClickListener(v -> {
            selectTab(false);
            carregarTarefas();
        });

        // Período
        binding.op7dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 7;
            carregarTarefas();
        });
        binding.op30dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 30;
            carregarTarefas();
        });
        binding.op90dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 90;
            carregarTarefas();
        });
    }

    private void selectTab(boolean atividades) {
        isAtividadesSelecionado = atividades;
        if (atividades) {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_unselected);
        } else {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_unselected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_selected);
        }
    }

    private void resetPeriodoButtons() {
        binding.op7dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op30dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op90dias.setBackgroundResource(R.drawable.bg_tab_unselected);
    }

    private void carregarDadosUsuario() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        
        if (userDataManager.isDataLoaded()) {
            // Dados já estão no cache, usar diretamente
            atualizarDadosPerfil();
        } else {
            // Dados não estão no cache, usar dados padrão
            binding.tvNome.setText("Usuário");
            binding.tvFuncao.setText("Estoquista");
            binding.tvDesde.setText("Desde --/--/----");
        }
    }

    private void atualizarDadosPerfil() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String nomeCompleto = userDataManager.getNomeCompleto();
        String dataFormatada = userDataManager.getDataContratacaoFormatada();
        
        binding.tvNome.setText(nomeCompleto);
        binding.tvFuncao.setText("Estoquista");
        binding.tvDesde.setText("Desde " + dataFormatada);
    }

    private void setupRecyclerView() {
        adapter = new PerfilTarefaAdapter();
        emptyStateAdapter = new EmptyStateAdapter(adapter);
        binding.recyclerViewHistoricoPerfil.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistoricoPerfil.setAdapter(emptyStateAdapter);
    }

    private void setupViewModel() {
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        
        // Observar lista de tarefas
        perfilViewModel.getTarefas().observe(this, tarefas -> {
            if (tarefas != null) {
                adapter.setTarefas(tarefas);
                
                // Atualizar estado vazio
                if (tarefas.isEmpty()) {
                    String emptyTitle = "Nenhuma atividade encontrada";
                    String emptyMessage = "Não há atividades registradas no período selecionado.\nTente alterar o período ou verifique novamente mais tarde.";
                    
                    if (!isAtividadesSelecionado) {
                        emptyTitle = "Nenhuma auditoria encontrada";
                        emptyMessage = "Não há auditorias registradas no período selecionado.\nTente alterar o período ou verifique novamente mais tarde.";
                    }
                    
                    emptyStateAdapter.setEmptyState(true, emptyTitle, emptyMessage);
                } else {
                    emptyStateAdapter.setEmptyState(false);
                }
            }
        });

        // Observar estado de carregamento
        perfilViewModel.getIsLoading().observe(this, isLoading -> {
            // Aqui você pode mostrar/ocultar um indicador de carregamento se necessário
        });

        // Observar mensagens de erro
        perfilViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Mostrar erro para o usuário (opcional)
            }
        });
    }

    private void carregarTarefas() {
        if (isAtividadesSelecionado) {
            perfilViewModel.loadAtividades(periodoSelecionado);
        } else {
            perfilViewModel.loadAuditorias(periodoSelecionado);
        }
    }
}