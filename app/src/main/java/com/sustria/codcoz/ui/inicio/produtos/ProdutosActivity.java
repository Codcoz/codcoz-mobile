package com.sustria.codcoz.ui.inicio.produtos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityProdutosBinding;
import com.sustria.codcoz.model.MockDataProvider;
import com.sustria.codcoz.model.Produto;
import com.sustria.codcoz.model.RegistroHistorico;
import com.sustria.codcoz.ui.historico.HistoricoFragment;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private String titulo;
    private ProdutoAdapter produtoAdapter;
    private Boolean telaProximosValidade = false;
    private Boolean telaEstoqueBaixo = false;
    private List<Produto> produtos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        seedDadosMock();
        try {
            setupRecyclerView();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        setupBusca();
    }

    // Colocar o título do header
    private void setupHeader() {
        Bundle envelope = getIntent().getExtras();
        if (envelope != null) {
            titulo = envelope.getString("tituloHeader");
            if (titulo.equals("Próximos à validade")) {
                telaProximosValidade = true;
            } else if (titulo.equals("Estoque baixo")) {
                telaEstoqueBaixo = true;
            }
        }
        binding.headerProdutos.headerActivityBackTitle.setText(titulo);
        binding.headerProdutos.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    // Colocar os dados mock
    private void seedDadosMock() {
        produtos.clear();
        // Usar dados mockados do MockDataProvider
        produtos.addAll(MockDataProvider.getMockProduto());
    }

    // Colocando os produtos na recycler view
    private void setupRecyclerView() throws ParseException {
        produtoAdapter = new ProdutoAdapter();
        binding.produtos.setLayoutManager(new LinearLayoutManager(this));
        binding.produtos.setAdapter(produtoAdapter);

        // verificando se tem algum filtro
        Bundle envelope = getIntent().getExtras();
        if (envelope != null) {
            String filtro = envelope.getString("filtro");
            if (telaEstoqueBaixo) {
                List<Produto> produtosEstoqueBaixo = new ArrayList<>();
                for (Produto p : produtos) {
                    if (p.getQuantidade() <= Integer.parseInt(filtro)) {
                        produtosEstoqueBaixo.add(p);
                    }
                }
                produtos = produtosEstoqueBaixo;
                produtoAdapter.setProdutos(produtos);
            } else if (telaProximosValidade) {
                List<Produto> produtosProximosAValidade = new ArrayList<>();
                for (Produto p : produtos) {

                    long dias = ChronoUnit.DAYS.between(LocalDate.now(), p.getValidade());

                    if (dias <= 14 && dias >= 0) {
                        produtosProximosAValidade.add(p);
                    }
                }
                produtos = produtosProximosAValidade;
                produtoAdapter.setProdutos(produtos);

            } else {
                produtoAdapter.setProdutos(produtos);
            }
        }
    }

    // parte de busca
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
        List<Produto> filtrados = new ArrayList<>();

        if (termo.isEmpty()) {
            produtoAdapter.setProdutos(produtos);
            return;
        }

        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(termo)) {
                filtrados.add(p);
            }
        }
        produtoAdapter.setProdutos(filtrados);
    }

    // adapter
    private class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
        private List<Produto> produtos = new ArrayList<>();

        public void setProdutos(List<Produto> produtos) {
            this.produtos = produtos;
            notifyDataSetChanged();
        }

        void submit(List<Produto> novos) {
            produtos.clear();
            produtos.addAll(novos);
            notifyDataSetChanged();
        }

        @Override
        public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_produto, parent, false);
            return new ProdutoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProdutoViewHolder holder, int position) {
            Produto produto = produtos.get(position);
            holder.tvNomeProduto.setText(produto.getNome());
            holder.tvUnidades.setText(produto.getQuantidade() + " unidades em estoque");

            if (telaEstoqueBaixo) {
                holder.tvUnidades.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(R.color.custom_red_link_opacity_44));
                holder.tvUnidades.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.custom_red_link));
            } else if (telaProximosValidade) {
                long dias = ChronoUnit.DAYS.between(LocalDate.now(), produto.getValidade());
                holder.tvUnidades.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(R.color.custom_yellow_link_opacity_44));
                holder.tvUnidades.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.custom_orange_link));
                holder.tvUnidades.setText("Vence em " + dias + " dias"); // TODO: mudar aqui para a quantidade certa de dias
            }

            // Configurar clique no item
            holder.itemView.setOnClickListener(v -> {
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
                validade.setText(String.valueOf(produto.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                descricao.setText(produto.getDescricao());

                Button sair = view.findViewById(R.id.btnSair);
                sair.setOnClickListener(v2 -> {
                    dialog.dismiss();
                });

                dialog.show();
            });
        }

        @Override
        public int getItemCount() {
            return produtos.size();
        }

        class ProdutoViewHolder extends RecyclerView.ViewHolder {
            TextView tvNomeProduto;
            TextView tvUnidades;

            ProdutoViewHolder(View itemView) {
                super(itemView);
                tvNomeProduto = itemView.findViewById(R.id.tv_nome);
                tvUnidades = itemView.findViewById(R.id.tv_unidades);
            }
        }
    }

}
