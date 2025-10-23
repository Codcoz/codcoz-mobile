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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.actions.CardapioSemanal;
import com.sustria.codcoz.api.adapter.ReceitaAdapter;
import com.sustria.codcoz.api.model.ReceitaResponse;
import com.sustria.codcoz.api.service.ReceitaService;
import com.sustria.codcoz.databinding.FragmentCardapioBinding;
import com.sustria.codcoz.utils.EmptyStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardapioFragment extends Fragment {

    private FragmentCardapioBinding binding;
    private ReceitaAdapter receitaAdapter;
    private EmptyStateAdapter emptyStateAdapter;
    private List<ReceitaResponse> receitas = new ArrayList<>();
    private ReceitaService receitaService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CardapioViewModel cardapioViewModel =
                new ViewModelProvider(this).get(CardapioViewModel.class);

        binding = FragmentCardapioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa o serviço
        receitaService = new ReceitaService();

        // Configura o header do fragment
        setupHeader();
        setupRecyclerView();
        botao();
        setupBusca();
        carregarReceitas();

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
        receitaService.getReceitas(new ReceitaService.ReceitaCallback<List<ReceitaResponse>>() {
            @Override
            public void onSuccess(List<ReceitaResponse> receitasApi) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
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
                        // Em caso de erro, mostrar estado vazio
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
}