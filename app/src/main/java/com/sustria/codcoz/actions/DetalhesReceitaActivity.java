package com.sustria.codcoz.actions;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.adapter.IngredienteAdapter;
import com.sustria.codcoz.api.adapter.InstrucaoAdapter;
import com.sustria.codcoz.api.model.ReceitaResponse;
import com.sustria.codcoz.databinding.ActivityDetalhesReceitaBinding;

public class DetalhesReceitaActivity extends AppCompatActivity {

    private ActivityDetalhesReceitaBinding binding;
    private IngredienteAdapter ingredienteAdapter;
    private InstrucaoAdapter instrucaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesReceitaBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
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
        ReceitaResponse receita = (ReceitaResponse) intent.getSerializableExtra("RECEITA_API");

        if (receita != null) {
            displayReceita(receita);
        }
    }

    private void displayReceita(ReceitaResponse receita) {
        // Título
        binding.tvTituloReceita.setText(receita.getNome());

        // Descrição
        if (receita.getDescricao() != null) {
            binding.tvDescricaoReceita.setText(receita.getDescricao());
        }

        // Tempos e porções
        if (receita.getTempoPreparoMinutos() != null) {
            binding.tvTempoPreparo.setText("Tempo de preparo: " + receita.getTempoPreparoMinutos() + " minutos");
        }
        if (receita.getTempoCozimentoMinutos() != null) {
            binding.tvTempoCozimento.setText("Tempo de cozimento: " + receita.getTempoCozimentoMinutos() + " minutos");
        }
        // getPorcoes() já trata null e retorna valor aleatório entre 0 e 10
        Integer porcoes = receita.getPorcoes();
        binding.tvPorcoes.setText("Rende " + porcoes + " porções");

        // Ingredientes
        if (receita.getIngredientes() != null) {
            ingredienteAdapter.setIngredientes(receita.getIngredientes());
        }

        // Instruções
        if (receita.getModoPreparo() != null) {
            instrucaoAdapter.setInstrucoes(receita.getModoPreparo());
        }
    }


}
