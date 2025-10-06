package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.databinding.BottomsheetProdutoConfirmadoBinding;
import com.sustria.codcoz.databinding.BottomsheetProdutoErroBinding;

public class ConfirmacaoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_IS_ERRO = "arg_is_erro";
    private static final String ARG_MSG_ERRO = "arg_msg_erro";

    private BottomsheetProdutoConfirmadoBinding bindingSucesso;
    private BottomsheetProdutoErroBinding bindingErro;
    private boolean isErro;
    private String mensagemErro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        isErro = args != null && args.getBoolean(ARG_IS_ERRO, false);
        mensagemErro = args != null ? args.getString(ARG_MSG_ERRO) : null;
        if (isErro) {
            bindingErro = BottomsheetProdutoErroBinding.inflate(inflater, container, false);
            return bindingErro.getRoot();
        } else {
            bindingSucesso = BottomsheetProdutoConfirmadoBinding.inflate(inflater, container, false);
            return bindingSucesso.getRoot();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isErro && bindingErro != null) {
            if (mensagemErro != null && bindingErro.txtMensagemErro != null) {
                bindingErro.txtMensagemErro.setText(mensagemErro);
            }
            bindingErro.btnFechar.setOnClickListener(v -> {
                dismiss();
                requireActivity().finish();
            });
        } else if (bindingSucesso != null) {
            bindingSucesso.btnFechar.setOnClickListener(v -> {
                dismiss();
                requireActivity().finish();
            });
        }
    }


    public static void showSucesso(androidx.fragment.app.FragmentManager fm) {
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, false);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
    }

    public static void showErro(androidx.fragment.app.FragmentManager fm, String mensagem) {
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, true);
        args.putString(ARG_MSG_ERRO, mensagem);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
    }
}



