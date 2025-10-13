package com.sustria.codcoz.ui.inicio.produtos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityProdutosBinding;
import com.sustria.codcoz.model.MockDataProvider;
import com.sustria.codcoz.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoActivity extends AppCompatActivity {

    private ActivityProdutosBinding binding;
    private String titulo;
    private ProdutoAdapter produtoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProdutosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        setupRecyclerView();
    }

    private void setupHeader() {
        binding.headerProdutos.headerActivityBackTitle.setText(titulo);
        binding.headerProdutos.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        produtoAdapter = new ProdutoAdapter();
        binding.produtos.setAdapter(produtoAdapter);

        List<Produto> produtos = MockDataProvider.getMockProduto();
        produtoAdapter.setProdutos(produtos);
    }

    private class ProdutoAdapter extends RecyclerView.Adapter<ProdutoActivity.ProdutoAdapter.ProdutoViewHolder> {
        private List<Produto> produtos = new ArrayList<>();

        public void setProdutos(List<Produto> produtos) {
            this.produtos = produtos;
            notifyDataSetChanged();
        }

        @Override
        public ProdutoActivity.ProdutoAdapter.ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_produto, parent, false);
            return new ProdutoActivity.ProdutoAdapter.ProdutoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProdutoActivity.ProdutoAdapter.ProdutoViewHolder holder, int position) {
            Produto produto = produtos.get(position);
            holder.tvNomeProduto.setText(produto.getNome());
            holder.tvUnidades.setText(produto.getQuantidade() + " unidades em estoque");

            // Configurar clique no item
            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), ProdutoDetalheActivity.class);
//                intent.putExtra("PRODUTO_ID", produto.getId());
//                startActivity(intent);
                AlertDialog.Builder produtoDetalhe = new AlertDialog.Builder(ProdutoActivity.this);
                LayoutInflater li = LayoutInflater.from(produtoDetalhe.getContext());
                View view = li.inflate(R.layout.produto_popup, null);
                produtoDetalhe.setView(view);

                Button sair = view.findViewById(R.id.btnSair);
                sair.setOnClickListener( v2 -> {

                });
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
