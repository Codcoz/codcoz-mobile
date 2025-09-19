package com.sustria.codcoz.ui.historico;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sustria.codcoz.databinding.FragmentHistoricoBinding;

public class HistoricoFragment extends Fragment {
    private FragmentHistoricoBinding binding;

    public HistoricoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HistoricoViewModel historicoEstoquistaViewModel =
                new ViewModelProvider(this).get(HistoricoViewModel.class);

        binding = FragmentHistoricoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura o header do fragment
        setupHeader();

        return root;
    }

    private void setupHeader() {
        // Configura header
        binding.headerHistorico.headerFragmentTitle.setText("Histórico de Baixas");

    }
}