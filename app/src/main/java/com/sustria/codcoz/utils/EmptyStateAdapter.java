package com.sustria.codcoz.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sustria.codcoz.R;

public class EmptyStateAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private static final int TYPE_EMPTY = 0;
    private static final int TYPE_CONTENT = 1;

    private RecyclerView.Adapter<VH> originalAdapter;
    private String emptyTitle;
    private String emptyMessage;
    private boolean showEmptyState = false;

    public EmptyStateAdapter(RecyclerView.Adapter<VH> originalAdapter) {
        this.originalAdapter = originalAdapter;
        this.emptyTitle = "Nenhum item encontrado";
        this.emptyMessage = "Não há dados para exibir no momento.\nTente novamente mais tarde.";
    }

    public void setEmptyState(boolean showEmpty, String title, String message) {
        this.showEmptyState = showEmpty;
        this.emptyTitle = title != null ? title : "Nenhum item encontrado";
        this.emptyMessage = message != null ? message : "Não há dados para exibir no momento.\nTente novamente mais tarde.";
        notifyDataSetChanged();
    }

    public void setEmptyState(boolean showEmpty) {
        setEmptyState(showEmpty, null, null);
    }

    @Override
    public int getItemViewType(int position) {
        if (showEmptyState) {
            return TYPE_EMPTY;
        }
        return TYPE_CONTENT;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_state_layout, parent, false);
            return new EmptyStateViewHolder(view);
        } else {
            return originalAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyStateViewHolder) {
            EmptyStateViewHolder emptyHolder = (EmptyStateViewHolder) holder;
            emptyHolder.bind(emptyTitle, emptyMessage);
        } else {
            originalAdapter.onBindViewHolder((VH) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (showEmptyState) {
            return 1;
        }
        return originalAdapter.getItemCount();
    }

    static class EmptyStateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvMessage;

        EmptyStateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_empty_title);
            tvMessage = itemView.findViewById(R.id.tv_empty_message);
        }

        void bind(String title, String message) {
            tvTitle.setText(title);
            tvMessage.setText(message);
        }
    }
}
