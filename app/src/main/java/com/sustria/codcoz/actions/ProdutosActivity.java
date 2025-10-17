package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.adapter.ProdutoAdapter;
import com.sustria.codcoz.api.model.ProdutoResponse;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.databinding.ActivityProdutosBinding;
import com.sustria.codcoz.model.MockDataProvider;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private String titulo;
    private ProdutoAdapter produtoAdapter;
    private ProdutoService produtoService;
    private boolean telaProximosValidade = false;
    private boolean telaEstoqueBaixo = false;
    private List<ProdutoResponse> produtos = new ArrayList<>();
    private Long idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        produtoService = new ProdutoService();
        idEmpresa = Long.valueOf(UserDataManager.getInstance(this).getEmpresaId());

        setupHeader();
        setupRecyclerView();
        setupSearch();
        loadProdutos();
    }

    private void setupHeader() {
        Bundle envelope = getIntent().getExtras();
        if (envelope != null) {
            titulo = envelope.getString("tituloHeader", "");
            telaProximosValidade = "Próximos à validade".equals(titulo);
            telaEstoqueBaixo = "Estoque baixo".equals(titulo);
        }

        binding.headerProdutos.headerActivityBackTitle.setText(titulo);
        binding.headerProdutos.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        produtoAdapter = new ProdutoAdapter();

        if (telaEstoqueBaixo) {
            produtoAdapter.setDisplayMode(ProdutoAdapter.DisplayMode.ESTOQUE_BAIXO);
        } else if (telaProximosValidade) {
            produtoAdapter.setDisplayMode(ProdutoAdapter.DisplayMode.PROXIMOS_VALIDADE);
        }

        binding.produtos.setLayoutManager(new LinearLayoutManager(this));
        binding.produtos.setAdapter(produtoAdapter);
        produtoAdapter.setOnProdutoClickListener(this::showProdutoDetails);
    }

    private void loadProdutos() {
        if (idEmpresa == null) {
            Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        ProdutoService.ProdutoCallback<List<ProdutoResponse>> callback = new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(List<ProdutoResponse> result) {
                runOnUiThread(() -> {
                    produtos = result;
                    produtoAdapter.setProdutos(produtos);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ProdutosActivity.this, error, Toast.LENGTH_SHORT).show();
                    produtos = MockDataProvider.getMockProduto();
                    produtoAdapter.setProdutos(produtos);
                });
            }
        };

        if (telaEstoqueBaixo) {
            produtoService.listarEstoqueBaixo(idEmpresa, callback);
        } else if (telaProximosValidade) {
            produtoService.listarProximoValidade(idEmpresa, callback);
        } else {
            produtoService.listarEstoque(idEmpresa, callback);
        }
    }

    private void setupSearch() {
        binding.editTextBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchProdutos();
            }
        });
    }

    private void searchProdutos() {
        String termo = binding.editTextBusca.getText() == null ? "" :
                binding.editTextBusca.getText().toString().trim().toLowerCase();

        if (termo.isEmpty()) {
            produtoAdapter.setProdutos(produtos);
            return;
        }

        List<ProdutoResponse> filtrados = new ArrayList<>();
        for (ProdutoResponse p : produtos) {
            if (p.getNome() != null && p.getNome().toLowerCase().contains(termo)) {
                filtrados.add(p);
            }
        }
        produtoAdapter.setProdutos(filtrados);
    }

    private void showProdutoDetails(ProdutoResponse produto) {
        AlertDialog.Builder produtoDetalhe = new AlertDialog.Builder(ProdutosActivity.this);
        LayoutInflater li = LayoutInflater.from(produtoDetalhe.getContext());
        View view = li.inflate(R.layout.produto_popup, null);
        produtoDetalhe.setView(view);

        AlertDialog dialog = produtoDetalhe.create();
        dialog.setCanceledOnTouchOutside(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setDimAmount(0.5f);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView nome = view.findViewById(R.id.nome_produto);
        TextView codigoEan = view.findViewById(R.id.tv_codigoEan);
        TextView quantidade = view.findViewById(R.id.tv_quantidade);
        TextView marca = view.findViewById(R.id.tv_marca);
        TextView validade = view.findViewById(R.id.tv_validade);
        TextView descricao = view.findViewById(R.id.tv_descricao);

        nome.setText(produto.getNome());
        codigoEan.setText(produto.getCodigoEan());
        quantidade.setText(String.valueOf(produto.getQuantidade()));
        marca.setText(produto.getMarca());
        validade.setText(produto.getValidade() != null
                ? produto.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "Sem validade");
        descricao.setText(produto.getDescricao());

        Button sair = view.findViewById(R.id.btnSair);
        sair.setOnClickListener(v2 -> dialog.dismiss());

        dialog.show();
    }
}