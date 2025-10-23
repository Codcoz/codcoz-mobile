package com.sustria.codcoz.actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityDetalhesReceitaBinding;
import com.sustria.codcoz.model.MockDataProvider;
import com.sustria.codcoz.model.Receita;
import com.sustria.codcoz.api.model.ReceitaApi;

import java.util.ArrayList;
import java.util.List;

public class DetalhesReceitaActivity extends AppCompatActivity {

    private ActivityDetalhesReceitaBinding binding;
    private IngredienteAdapter ingredienteAdapter;
    private InstrucaoAdapter instrucaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesReceitaBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupHeader();
        setupRecyclerViews();
        loadReceitaData();
    }

    private void setupHeader() {
        binding.headerDetalhesReceita.headerActivityBackTitle.setText("Detalhes receita");
        binding.headerDetalhesReceita.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupRecyclerViews() {
        // Ingredientes
        ingredienteAdapter = new IngredienteAdapter();
        binding.recyclerViewIngredientes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewIngredientes.setAdapter(ingredienteAdapter);

        // Instruções
        instrucaoAdapter = new InstrucaoAdapter();
        binding.recyclerViewInstrucoes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewInstrucoes.setAdapter(instrucaoAdapter);
    }

    private void loadReceitaData() {
        Intent intent = getIntent();
        ReceitaApi receitaApi = (ReceitaApi) intent.getSerializableExtra("RECEITA_API");

        if (receitaApi != null) {
            displayReceitaApi(receitaApi);
        } else {
            // Fallback para dados mockados se não receber ReceitaApi
            String receitaId = intent.getStringExtra("RECEITA_ID");
            if (receitaId != null) {
                List<Receita> receitas = MockDataProvider.getMockReceitas();
                for (Receita r : receitas) {
                    if (r.getId().equals(receitaId)) {
                        displayReceita(r);
                        break;
                    }
                }
            }
        }
    }

    private void displayReceita(Receita receita) {
        // Título
        binding.tvTituloReceita.setText(receita.getNome());

        // Tempos e porções
        binding.tvTempoPreparo.setText("Tempo de preparo: " + receita.getTempoPreparo() + " minutos");
        binding.tvTempoCozimento.setText("Tempo de cozimento: " + (receita.getTempoPreparo() + 15) + " minutos");
        binding.tvPorcoes.setText("Rende " + receita.getPorcoes() + " porções");

        // Ingredientes
        if (receita.getIngredientes() != null) {
            ingredienteAdapter.setIngredientes(receita.getIngredientes());
        }

        // Instruções
        if (receita.getInstrucoes() != null) {
            instrucaoAdapter.setInstrucoes(receita.getInstrucoes());
        }
    }

    private void displayReceitaApi(ReceitaApi receitaApi) {
        // Título
        binding.tvTituloReceita.setText(receitaApi.getNome());

        // Descrição
        if (receitaApi.getDescricao() != null) {
            binding.tvDescricaoReceita.setText(receitaApi.getDescricao());
        }

        // Tempos e porções
        if (receitaApi.getTempoPreparoMinutos() != null) {
            binding.tvTempoPreparo.setText("Tempo de preparo: " + receitaApi.getTempoPreparoMinutos() + " minutos");
        }
        if (receitaApi.getTempoCozimentoMinutos() != null) {
            binding.tvTempoCozimento.setText("Tempo de cozimento: " + receitaApi.getTempoCozimentoMinutos() + " minutos");
        }
        if (receitaApi.getPorcoes() != null) {
            binding.tvPorcoes.setText("Rende " + receitaApi.getPorcoes() + " porções");
        }

        // Ingredientes
        if (receitaApi.getIngredientes() != null) {
            ingredienteAdapter.setIngredientesApi(receitaApi.getIngredientes());
        }

        // Instruções
        if (receitaApi.getModoPreparo() != null) {
            instrucaoAdapter.setInstrucoesApi(receitaApi.getModoPreparo());
        }
    }

    // Adapter para ingredientes
    private static class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder> {
        private List<Receita.Ingrediente> ingredientes = new ArrayList<>();
        private List<ReceitaApi.IngredienteApi> ingredientesApi = new ArrayList<>();

        public void setIngredientes(List<Receita.Ingrediente> ingredientes) {
            this.ingredientes = ingredientes;
            notifyDataSetChanged();
        }

        public void setIngredientesApi(List<ReceitaApi.IngredienteApi> ingredientesApi) {
            this.ingredientesApi = ingredientesApi;
            notifyDataSetChanged();
        }

        @Override
        public IngredienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ingrediente, parent, false);
            return new IngredienteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IngredienteViewHolder holder, int position) {
            if (!ingredientesApi.isEmpty()) {
                ReceitaApi.IngredienteApi ingrediente = ingredientesApi.get(position);
                holder.tvNomeIngrediente.setText(ingrediente.getNome());
                holder.tvQuantidadeIngrediente.setText(ingrediente.getQuantidade() + " " + ingrediente.getUnidade_medida());
            } else {
                Receita.Ingrediente ingrediente = ingredientes.get(position);
                holder.tvNomeIngrediente.setText(ingrediente.getNome());
                holder.tvQuantidadeIngrediente.setText(ingrediente.getQuantidade() + " " + ingrediente.getUnidade());
            }
        }

        @Override
        public int getItemCount() {
            return !ingredientesApi.isEmpty() ? ingredientesApi.size() : ingredientes.size();
        }

        static class IngredienteViewHolder extends RecyclerView.ViewHolder {
            TextView tvNomeIngrediente;
            TextView tvQuantidadeIngrediente;

            IngredienteViewHolder(View itemView) {
                super(itemView);
                tvNomeIngrediente = itemView.findViewById(R.id.tvNomeIngrediente);
                tvQuantidadeIngrediente = itemView.findViewById(R.id.tvQuantidadeIngrediente);
            }
        }
    }

    // Adapter para instruções
    private static class InstrucaoAdapter extends RecyclerView.Adapter<InstrucaoAdapter.InstrucaoViewHolder> {
        private List<String> instrucoes = new ArrayList<>();
        private List<ReceitaApi.ModoPreparoApi> instrucoesApi = new ArrayList<>();

        public void setInstrucoes(List<String> instrucoes) {
            this.instrucoes = instrucoes;
            notifyDataSetChanged();
        }

        public void setInstrucoesApi(List<ReceitaApi.ModoPreparoApi> instrucoesApi) {
            this.instrucoesApi = instrucoesApi;
            notifyDataSetChanged();
        }

        @Override
        public InstrucaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_instrucao, parent, false);
            return new InstrucaoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(InstrucaoViewHolder holder, int position) {
            if (!instrucoesApi.isEmpty()) {
                ReceitaApi.ModoPreparoApi instrucao = instrucoesApi.get(position);
                holder.tvNumeroInstrucao.setText(String.valueOf(instrucao.getOrdem()));
                holder.tvTextoInstrucao.setText(instrucao.getPasso());
            } else {
                String instrucao = instrucoes.get(position);
                holder.tvNumeroInstrucao.setText(String.valueOf(position + 1));
                holder.tvTextoInstrucao.setText(instrucao);
            }
        }

        @Override
        public int getItemCount() {
            return !instrucoesApi.isEmpty() ? instrucoesApi.size() : instrucoes.size();
        }

        static class InstrucaoViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumeroInstrucao;
            TextView tvTextoInstrucao;

            InstrucaoViewHolder(View itemView) {
                super(itemView);
                tvNumeroInstrucao = itemView.findViewById(R.id.tvNumeroInstrucao);
                tvTextoInstrucao = itemView.findViewById(R.id.tvTextoInstrucao);
            }
        }
    }
}
