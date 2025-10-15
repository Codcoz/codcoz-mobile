package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.BottomsheetAuditoriaQuantidadeBinding;

public class AuditoriaQuantidadeBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TITULO = "arg_titulo";
    private static final String ARG_PRODUTO = "arg_produto";

    private BottomsheetAuditoriaQuantidadeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetAuditoriaQuantidadeBinding.inflate(inflater, container, false);
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

        String titulo = getArguments() != null ? getArguments().getString(ARG_TITULO) : "Auditoria";
        String produto = getArguments() != null ? getArguments().getString(ARG_PRODUTO) : null;

        binding.txtTitulo.setText(titulo);
        if (produto != null) {
            binding.txtProduto.setText(produto);
        }

        EditText edt = binding.inputQuantidade;
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        binding.btnCancelar.setOnClickListener(v -> dismiss());
        binding.btnConfirmar.setOnClickListener(v -> {
            // Aqui vamos enviar a quantidade para a API. Por ora, apenas exibe sucesso.
            dismiss();
            ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
        });
    }

    public static AuditoriaQuantidadeBottomSheetDialogFragment newInstance(String titulo, String produto) {
        AuditoriaQuantidadeBottomSheetDialogFragment f = new AuditoriaQuantidadeBottomSheetDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TITULO, titulo);
        b.putString(ARG_PRODUTO, produto);
        f.setArguments(b);
        return f;
    }
}


