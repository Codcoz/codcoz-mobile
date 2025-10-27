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
import com.sustria.codcoz.databinding.BottomSheetErrorBinding;
import com.sustria.codcoz.databinding.BottomsheetProdutoConfirmadoBinding;

public class ConfirmacaoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_IS_ERRO = "arg_is_erro";
    private static final String ARG_MSG_ERRO = "arg_msg_erro";

    private BottomsheetProdutoConfirmadoBinding bindingSucesso;
    private BottomSheetErrorBinding bindingErro;
    private boolean isErro;
    private String mensagemErro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        isErro = args != null && args.getBoolean(ARG_IS_ERRO, false);
        mensagemErro = args != null ? args.getString(ARG_MSG_ERRO) : null;
        if (isErro) {
            bindingErro = BottomSheetErrorBinding.inflate(inflater, container, false);
            return bindingErro.getRoot();
        } else {
            bindingSucesso = BottomsheetProdutoConfirmadoBinding.inflate(inflater, container, false);
            return bindingSucesso.getRoot();
        }
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

        if (isErro && bindingErro != null) {
            if (mensagemErro != null) {
                bindingErro.tvErrorTitle.setText(mensagemErro);
            }
            bindingErro.btnFechar.setOnClickListener(v -> {
                dismiss();
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        } else if (bindingSucesso != null) {
            bindingSucesso.btnFechar.setOnClickListener(v -> {
                dismiss();
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        }
    }


    public static void showSucesso(androidx.fragment.app.FragmentManager fm) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, false);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
    }

    public static void showErro(androidx.fragment.app.FragmentManager fm, String mensagem) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, true);
        args.putString(ARG_MSG_ERRO, mensagem);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
    }
    
    private static void dismissExistingBottomSheets(@NonNull androidx.fragment.app.FragmentManager fm) {
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