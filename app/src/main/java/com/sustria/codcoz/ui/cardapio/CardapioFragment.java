package com.sustria.codcoz.ui.cardapio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sustria.codcoz.databinding.FragmentCardapioBinding;
import com.sustria.codcoz.ui.cardapio.actions.CardapioSemanal;

public class CardapioFragment extends Fragment {

    private FragmentCardapioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CardapioViewModel cardapioViewModel =
                new ViewModelProvider(this).get(CardapioViewModel.class);

        binding = FragmentCardapioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura o header do fragment
        setupHeader();
        botao();

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

    private void botao() {
        binding.cardsResumo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardapioSemanal.class);
            startActivity(intent);
        });
    }
}