package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.ProdutoResponse;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.databinding.ActivityBaixaManualBinding;

public class ManualActivity extends AppCompatActivity {

    private ActivityBaixaManualBinding binding;
    private ProdutoService produtoService;
    private ProdutoResponse produtoEncontrado;
    private boolean isEntrada; // true para entrada, false para baixa
    private Long tarefaId;
    private String ingredienteEsperado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaixaManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        produtoService = new ProdutoService();
        isEntrada = getIntent().getBooleanExtra("is_entrada", false);
        
        // Verificar se há tarefaId e ingrediente esperado (para validação de atividade)
        if (getIntent().hasExtra("tarefa_id")) {
            long id = getIntent().getLongExtra("tarefa_id", -1);
            if (id > 0) {
                tarefaId = id;
            }
        }
        if (getIntent().hasExtra("ingrediente_esperado")) {
            ingredienteEsperado = getIntent().getStringExtra("ingrediente_esperado");
        }

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        setupHeader();
        setupClickListeners();
    }

    private void setupHeader() {
        String titulo = isEntrada ? "Realizar Nova Entrada" : "Realizar Nova Baixa";
        binding.headerBaixaManual.headerActivityBackTitle.setText(titulo);
        binding.headerBaixaManual.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        binding.btnAvancar.setOnClickListener(v -> {
            String codigo = binding.etCodigoProduto.getText() != null ? binding.etCodigoProduto.getText().toString().trim() : "";
            if (codigo.isEmpty()) {
                binding.etCodigoProduto.setError("Informe o código do produto");
                return;
            }

            buscarProduto(codigo);
        });
    }

    private void buscarProduto(String codigo) {
        setLoadingState(true);

        produtoService.buscarProdutoPorEan(codigo, new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(ProdutoResponse produto) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    produtoEncontrado = produto;
                    mostrarDetalhesProduto(produto);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    Log.e("BaixaManual", "Erro ao buscar produto: " + error);
                    Toast.makeText(ManualActivity.this, "Erro ao buscar produto: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarDetalhesProduto(ProdutoResponse produto) {
        ProdutoBottomSheetDialogFragment.show(
                getSupportFragmentManager(),
                produto.getCodigoEan(),
                isEntrada ? ProdutoBottomSheetDialogFragment.TipoMovimento.ENTRADA : ProdutoBottomSheetDialogFragment.TipoMovimento.BAIXA,
                tarefaId,
                ingredienteEsperado
        );
    }

    private void setLoadingState(boolean loading) {
        binding.btnAvancar.setEnabled(!loading);
        binding.btnAvancar.setText(loading ? "Buscando..." : "Avançar");
        binding.etCodigoProduto.setEnabled(!loading);
    }
}




