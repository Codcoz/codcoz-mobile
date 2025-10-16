package com.sustria.codcoz.ui.inicio.produtos;

import android.os.Bundle;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ProdutosActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private String titulo;
    private ProdutoAdapter produtoAdapter;
    private Boolean telaProximosValidade = false;
    private Boolean telaEstoqueBaixo = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        try {
            setupRecyclerView();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

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
    // TODO: fazer a parte de pesquisa

    private void setupRecyclerView() throws ParseException {
        produtoAdapter = new ProdutoAdapter();
        binding.produtos.setLayoutManager(new LinearLayoutManager(this));
        binding.produtos.setAdapter(produtoAdapter);

        List<Produto> produtos = MockDataProvider.getMockProduto();
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

                produtoAdapter.setProdutos(produtosEstoqueBaixo);
            } else if (telaProximosValidade) {
                List<Produto> produtosProximosAValidade = new ArrayList<>();
                for (Produto p : produtos) {

                    long dias = ChronoUnit.DAYS.between(LocalDate.now(), p.getValidade());

                    if (dias <= 14 && dias >= 0) {
                        produtosProximosAValidade.add(p);
                    }
                }
                produtoAdapter.setProdutos(produtosProximosAValidade);

            } else {
                produtoAdapter.setProdutos(produtos);
            }
        }
        
    }

    private class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
        private List<Produto> produtos = new ArrayList<>();

        public void setProdutos(List<Produto> produtos) {
            this.produtos = produtos;
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
                validade.setText(String.valueOf(produto.getValidade()));
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
