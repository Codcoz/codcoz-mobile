package com.sustria.codcoz.ui.baixa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sustria.codcoz.databinding.FragmentBaixaManualBinding;

public class BaixaManual extends Fragment {

    private @NonNull FragmentBaixaManualBinding binding;

    public BaixaManual() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BaixaManualViewModel baixaManualModel =
                new ViewModelProvider(this).get(BaixaManualViewModel.class);

        binding = FragmentBaixaManualBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura o header do fragment
        setupHeader();

        return root;
    }

    private void setupHeader() {
        // Configura header
        binding.headerBaixa.headerFragmentTitle.setText("Baixa Manual");
    }
}