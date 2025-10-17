package com.sustria.codcoz.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.ProdutoResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    public enum DisplayMode {
        NORMAL,
        ESTOQUE_BAIXO,
        PROXIMOS_VALIDADE
    }

    private List<ProdutoResponse> produtos = new ArrayList<>();
    private OnProdutoClickListener onProdutoClickListener;
    private DisplayMode displayMode = DisplayMode.NORMAL;

    public interface OnProdutoClickListener {
        void onProdutoClick(ProdutoResponse produto);
    }

    public void setOnProdutoClickListener(OnProdutoClickListener listener) {
        this.onProdutoClickListener = listener;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public void setProdutos(List<ProdutoResponse> produtos) {
        this.produtos = produtos != null ? produtos : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addProduto(ProdutoResponse produto) {
        this.produtos.add(produto);
        notifyItemInserted(this.produtos.size() - 1);
    }

    public void removeProduto(int position) {
        if (position >= 0 && position < produtos.size()) {
            this.produtos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateProduto(int position, ProdutoResponse produto) {
        if (position >= 0 && position < produtos.size()) {
            this.produtos.set(position, produto);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        ProdutoResponse produto = produtos.get(position);
        holder.bind(produto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ProdutoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNome;
        private TextView tvUnidades;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome);
            tvUnidades = itemView.findViewById(R.id.tv_unidades);

            itemView.setOnClickListener(v -> {
                if (onProdutoClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onProdutoClickListener.onProdutoClick(produtos.get(position));
                    }
                }
            });
        }

        public void bind(ProdutoResponse produto) {
            tvNome.setText(produto.getNome());
            Context context = itemView.getContext();

            switch (displayMode) {
                case ESTOQUE_BAIXO:
                    tvUnidades.setText(produto.getQuantidade() + " unidades em estoque");
                    tvUnidades.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.custom_red_link_opacity_44));
                    tvUnidades.setTextColor(ContextCompat.getColor(context, R.color.custom_red_link));
                    break;

                case PROXIMOS_VALIDADE:
                    if (produto.getValidade() != null) {
                        long dias = ChronoUnit.DAYS.between(LocalDate.now(), produto.getValidade());
                        tvUnidades.setText("Vence em " + dias + " dias");
                        tvUnidades.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.custom_yellow_link_opacity_44));
                        tvUnidades.setTextColor(ContextCompat.getColor(context, R.color.custom_orange_link));
                    } else {
                        tvUnidades.setText("Sem data de validade");
                    }
                    break;

                default:
                    StringBuilder info = new StringBuilder();
                    info.append(produto.getQuantidade()).append(" unidades");

                    if (produto.getMarca() != null && !produto.getMarca().isEmpty()) {
                        info.append(" • ").append(produto.getMarca());
                    }
                    if (produto.getValidade() != null) {
                        info.append(" • Vence: ").append(produto.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                    tvUnidades.setText(info.toString());
                    break;
            }
        }
    }
}