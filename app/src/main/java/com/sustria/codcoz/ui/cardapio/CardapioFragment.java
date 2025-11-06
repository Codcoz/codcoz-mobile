package com.sustria.codcoz.ui.cardapio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.actions.CardapioSemanal;
import com.sustria.codcoz.api.adapter.ReceitaAdapter;
import com.sustria.codcoz.api.model.ReceitaResponse;
import com.sustria.codcoz.api.service.ReceitaService;
import com.sustria.codcoz.databinding.FragmentCardapioBinding;
import com.sustria.codcoz.utils.EmptyStateAdapter;
import com.sustria.codcoz.utils.UserDataManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CardapioFragment extends Fragment {

    private FragmentCardapioBinding binding;
    private ReceitaAdapter receitaAdapter;
    private EmptyStateAdapter emptyStateAdapter;
    private List<ReceitaResponse> receitas = new ArrayList<>();
    private ReceitaService receitaService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCardapioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        receitaService = new ReceitaService();

        setupHeader();
        setupRecyclerView();
        botao();
        setupBusca();
        carregarReceitas();
        carregarNomeCardapio();

        return root;
    }

    private void setupHeader() {
        // Configura o título do header
        binding.headerCardapio.headerFragmentTitle.setText("Cardápio do Estoque");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupRecyclerView() {
        // Configurar RecyclerView de receitas
        receitaAdapter = new ReceitaAdapter();
        emptyStateAdapter = new EmptyStateAdapter(receitaAdapter);
        binding.itemAtivRecentes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.itemAtivRecentes.setAdapter(emptyStateAdapter);
    }

    private void carregarReceitas() {
        Integer empresaId = UserDataManager.getInstance().getEmpresaId();
        if (empresaId == null) {
            Log.e("Cardapio", "EmpresaId não encontrado");
            return;
        }

        // Mostra loading ao iniciar
        emptyStateAdapter.setLoadingState(true);

        receitaService.getReceitas(empresaId.toString(), new ReceitaService.ReceitaCallback<List<ReceitaResponse>>() {
            @Override
            public void onSuccess(List<ReceitaResponse> receitasApi) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Remove loading
                        emptyStateAdapter.setLoadingState(false);
                        
                        receitas = receitasApi;
                        receitaAdapter.setReceitas(receitas);

                        // Controla o estado vazio
                        if (receitas.isEmpty()) {
                            emptyStateAdapter.setEmptyState(true, "Nenhuma receita encontrada",
                                    "Não há receitas disponíveis no momento.\nVerifique novamente mais tarde.");
                        } else {
                            emptyStateAdapter.setEmptyState(false);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Remove loading e mostra estado vazio em caso de erro
                        emptyStateAdapter.setLoadingState(false);
                        Log.e("Cardapio", "Erro ao carregar receitas: " + error);
                        emptyStateAdapter.setEmptyState(true, "Erro ao carregar receitas",
                                "Não foi possível carregar as receitas.\nVerifique sua conexão e tente novamente.");
                    });
                }
            }
        });
    }

    private void setupBusca() {
        binding.editTextBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                aplicarBusca();
            }
        });
    }

    private void aplicarBusca() {
        String termo = binding.editTextBusca.getText() == null ? "" : binding.editTextBusca.getText().toString().trim().toLowerCase();
        List<ReceitaResponse> filtrados = new ArrayList<>();

        if (termo.isEmpty()) {
            receitaAdapter.setReceitas(receitas);
            // Controla o estado vazio para a lista original
            if (receitas.isEmpty()) {
                emptyStateAdapter.setEmptyState(true, "Nenhuma receita encontrada",
                        "Não há receitas disponíveis no momento.\nVerifique novamente mais tarde.");
            } else {
                emptyStateAdapter.setEmptyState(false);
            }
            return;
        }

        for (ReceitaResponse r : receitas) {
            if (r.getNome().toLowerCase().contains(termo)) {
                filtrados.add(r);
            }
        }
        receitaAdapter.setReceitas(filtrados);

        // Controla o estado vazio para os resultados da busca
        if (filtrados.isEmpty()) {
            emptyStateAdapter.setEmptyState(true, "Nenhuma receita encontrada",
                    "Não foram encontradas receitas com o termo \"" + termo + "\".\nTente outro termo de busca.");
        } else {
            emptyStateAdapter.setEmptyState(false);
        }
    }

    private void botao() {
        binding.cardsResumo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardapioSemanal.class);
            startActivity(intent);
        });
    }

    private void carregarNomeCardapio() {
        // Calcula o período da semana atual (segunda a sexta)
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        
        // Encontra a segunda-feira da semana atual
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        int diasParaSegunda;
        
        // Calendar.DAY_OF_WEEK: Domingo=1, Segunda=2, Terça=3, ..., Sábado=7
        if (diaSemana == Calendar.SUNDAY) {
            // Se for domingo, volta 6 dias para chegar na segunda-feira anterior
            diasParaSegunda = -6;
        } else {
            // Para outros dias, calcula quantos dias precisa voltar para chegar na segunda
            diasParaSegunda = Calendar.MONDAY - diaSemana;
        }
        
        calendar.add(Calendar.DAY_OF_MONTH, diasParaSegunda);
        Calendar segundaFeira = (Calendar) calendar.clone();
        
        // Calcula a sexta-feira (4 dias depois da segunda)
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        Calendar sextaFeira = (Calendar) calendar.clone();
        
        // Formata as datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataInicio = sdf.format(segundaFeira.getTime());
        String dataFim = sdf.format(sextaFeira.getTime());
        
        // Cria o nome do cardápio
        String nomeCardapio = "Cardápio Semanal - " + dataInicio + " a " + dataFim;
        binding.nomeCardapio.setText(nomeCardapio);
    }
}