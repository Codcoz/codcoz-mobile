package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.BottomsheetProdutoRegistroConfirmacaoBinding;

public class ConfirmarRegistroBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "confirmar_registro_request";
    public static final String RESULT_CONFIRMED = "result_confirmed";

    private static final String ARG_NOME = "arg_nome";
    private static final String ARG_ESTOQUE_ANTIGO = "arg_estoque_antigo";
    private static final String ARG_ESTOQUE_ATUALIZADO = "arg_estoque_atualizado";

    private BottomsheetProdutoRegistroConfirmacaoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetProdutoRegistroConfirmacaoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setNavigationBarColor(
                    ContextCompat.getColor(requireContext(), R.color.colorSurface)
            );
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String nome = args != null ? args.getString(ARG_NOME) : null;
        Integer estoqueAntigo = args != null ? (Integer) args.getInt(ARG_ESTOQUE_ANTIGO, -1) : null;
        Integer estoqueAtualizado = args != null ? (Integer) args.getInt(ARG_ESTOQUE_ATUALIZADO, -1) : null;

        if (nome != null) {
            binding.txtNomeProduto.setText(nome);
        }
        if (estoqueAntigo != null && estoqueAntigo >= 0) {
            binding.txtEstoqueAntigo.setText(String.valueOf(estoqueAntigo));
        }
        if (estoqueAtualizado != null && estoqueAtualizado >= 0) {
            binding.txtEstoqueAtualizado.setText(String.valueOf(estoqueAtualizado));
        }

        binding.btnCancelar.setOnClickListener(v -> dismiss());
        binding.btnConfirmar.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putBoolean(RESULT_CONFIRMED, true);
            getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
            dismiss();
        });
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        ConfirmarRegistroBottomSheetDialogFragment fragment = new ConfirmarRegistroBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOME, nome);
        if (estoqueAntigo != null) args.putInt(ARG_ESTOQUE_ANTIGO, estoqueAntigo);
        if (estoqueAtualizado != null) args.putInt(ARG_ESTOQUE_ATUALIZADO, estoqueAtualizado);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmarRegistroBottomSheetDialogFragment");
    }
    
    private static void dismissExistingBottomSheets(@NonNull FragmentManager fm) {
        // Fechar todos os bottom sheets existentes
        if (fm.findFragmentByTag("ProdutoBottomSheetDialogFragment") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("ProdutoBottomSheetDialogFragment")).dismiss();
        }
        if (fm.findFragmentByTag("ConfirmacaoBottomSheetDialogFragment") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("ConfirmacaoBottomSheetDialogFragment")).dismiss();
        }
        if (fm.findFragmentByTag("ConfirmarRegistroBottomSheetDialogFragment") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("ConfirmarRegistroBottomSheetDialogFragment")).dismiss();
        }
        if (fm.findFragmentByTag("FiltrosBottomSheetDialogFragment") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("FiltrosBottomSheetDialogFragment")).dismiss();
        }
        if (fm.findFragmentByTag("AuditoriaQuantidadeBottomSheetDialog") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("AuditoriaQuantidadeBottomSheetDialog")).dismiss();
        }
        if (fm.findFragmentByTag("AtividadeEscolhaEntradaBottomSheetDialog") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("AtividadeEscolhaEntradaBottomSheetDialog")).dismiss();
        }
        if (fm.findFragmentByTag("TarefaDetalheBottomSheetDialog") != null) {
            ((BottomSheetDialogFragment) fm.findFragmentByTag("TarefaDetalheBottomSheetDialog")).dismiss();
        }
    }
}

