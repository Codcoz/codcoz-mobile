package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.BottomsheetTarefaDetalheBinding;

public class TarefaDetalheBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TIPO = "arg_tipo";
    private static final String ARG_PRODUTO = "arg_produto";
    private static final String ARG_RELATOR = "arg_relator";
    private static final String ARG_DATA = "arg_data";

    private BottomsheetTarefaDetalheBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetTarefaDetalheBinding.inflate(inflater, container, false);
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
        Bundle args = getArguments();
        String tipo = args != null ? args.getString(ARG_TIPO) : null;
        String produto = args != null ? args.getString(ARG_PRODUTO) : null;
        String relator = args != null ? args.getString(ARG_RELATOR) : null;
        String data = args != null ? args.getString(ARG_DATA) : null;

        if (tipo != null) binding.txtTitulo.setText(tipo);
        if (produto != null) binding.txtProduto.setText(produto);
        if (relator != null) binding.txtRelator.setText(relator);
        if (data != null) binding.txtData.setText(data);

        binding.btnFechar.setOnClickListener(v -> dismiss());
        binding.btnRegistrar.setOnClickListener(v -> {
            if (tipo == null) {
                dismiss();
                return;
            }
            String t = tipo.toLowerCase();
            if (t.contains("auditor") || t.contains("confer") || t.contains("estoque")) {
                dismiss();
                AuditoriaQuantidadeBottomSheetDialogFragment.newInstance("Definir quantidade", produto)
                        .show(getParentFragmentManager(), "AuditoriaQuantidadeBottomSheetDialog");
            } else if (t.contains("ativ")) {
                dismiss();
                new AtividadeEscolhaEntradaBottomSheetDialogFragment()
                        .show(getParentFragmentManager(), "AtividadeEscolhaEntradaBottomSheetDialog");
            } else {
                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Tipo de tarefa não reconhecido");
            }
        });
    }

    public static TarefaDetalheBottomSheetDialogFragment newInstance(String tipo, String produto, String relator, String data) {
        TarefaDetalheBottomSheetDialogFragment f = new TarefaDetalheBottomSheetDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TIPO, tipo);
        b.putString(ARG_PRODUTO, produto);
        b.putString(ARG_RELATOR, relator);
        b.putString(ARG_DATA, data);
        f.setArguments(b);
        return f;
    }
}


