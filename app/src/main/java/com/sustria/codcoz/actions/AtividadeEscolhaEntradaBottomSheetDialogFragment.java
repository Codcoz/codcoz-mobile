package com.sustria.codcoz.actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.BottomsheetAtividadeEscolhaBinding;

public class AtividadeEscolhaEntradaBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private BottomsheetAtividadeEscolhaBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetAtividadeEscolhaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Muda a cor da barra de navegação para a cor de fundo
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setNavigationBarColor(
                    ContextCompat.getColor(requireContext(), R.color.colorSurface)
            );
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnScanner.setOnClickListener(v -> {
            dismiss();
            Intent i = new Intent(requireContext(), EntradaScanActivity.class);
            startActivity(i);
        });

        binding.btnManual.setOnClickListener(v -> {
            dismiss();
            ProdutoBottomSheetDialogFragment.show(getParentFragmentManager(), "", "entrada");
        });

        binding.btnCancelar.setOnClickListener(v -> dismiss());
    }
}


