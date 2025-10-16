package com.sustria.codcoz.ui.cardapio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.actions.DetalhesReceitaActivity;
import com.sustria.codcoz.databinding.FragmentCardapioBinding;
import com.sustria.codcoz.model.MockDataProvider;
import com.sustria.codcoz.model.Produto;
import com.sustria.codcoz.model.Receita;
import com.sustria.codcoz.actions.CardapioSemanal;

import java.util.ArrayList;
import java.util.List;

public class CardapioFragment extends Fragment {

    private FragmentCardapioBinding binding;
    private ReceitaAdapter receitaAdapter;
    private List<Receita> receitas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CardapioViewModel cardapioViewModel =
                new ViewModelProvider(this).get(CardapioViewModel.class);

        binding = FragmentCardapioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura o header do fragment
        setupHeader();
        setupRecyclerView();
        botao();
        setupBusca();

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
        binding.itemAtivRecentes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.itemAtivRecentes.setAdapter(receitaAdapter);

        // Carregar dados mockados
        receitas = MockDataProvider.getMockReceitas();
        receitaAdapter.setReceitas(receitas);
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
        List<Receita> filtrados = new ArrayList<>();

        if (termo.isEmpty()) {
            receitaAdapter.setReceitas(receitas);
            return;
        }

        for (Receita r : receitas) {
            if (r.getNome().toLowerCase().contains(termo)) {
                filtrados.add(r);
            }
        }
        receitaAdapter.setReceitas(filtrados);
    }

    private void botao() {
        binding.cardsResumo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardapioSemanal.class);
            startActivity(intent);
        });
    }

    // Adapter para o RecyclerView de receitas
    private class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder> {
        private List<Receita> receitas = new ArrayList<>();

        public void setReceitas(List<Receita> receitas) {
            this.receitas = receitas;
            notifyDataSetChanged();
        }

        @Override
        public ReceitaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_receita, parent, false);
            return new ReceitaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReceitaViewHolder holder, int position) {
            Receita receita = receitas.get(position);
            holder.tvNomeReceita.setText(receita.getNome());
            holder.tvPorcoes.setText(receita.getPorcoes() + " porções possíveis");

            // Configurar clique no item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetalhesReceitaActivity.class);
                intent.putExtra("RECEITA_ID", receita.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return receitas.size();
        }

        class ReceitaViewHolder extends RecyclerView.ViewHolder {
            TextView tvNomeReceita;
            TextView tvPorcoes;

            ReceitaViewHolder(View itemView) {
                super(itemView);
                tvNomeReceita = itemView.findViewById(R.id.tv_nome);
                tvPorcoes = itemView.findViewById(R.id.tv_unidades);
            }
        }
    }
}