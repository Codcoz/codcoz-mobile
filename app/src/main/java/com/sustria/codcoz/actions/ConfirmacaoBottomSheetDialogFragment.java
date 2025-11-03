package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.BottomSheetErrorBinding;
import com.sustria.codcoz.databinding.BottomsheetProdutoConfirmadoBinding;

public class ConfirmacaoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_IS_ERRO = "arg_is_erro";
    private static final String ARG_MSG_ERRO = "arg_msg_erro";
    private static final String ARG_SHOULD_FINISH_ACTIVITY = "arg_should_finish_activity";
    private static final String ARG_IS_INFO = "arg_is_info";

    private BottomsheetProdutoConfirmadoBinding bindingSucesso;
    private BottomSheetErrorBinding bindingErro;
    private boolean isErro;
    private boolean isInfo;
    private String mensagemErro;
    private boolean shouldFinishActivity = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        isErro = args != null && args.getBoolean(ARG_IS_ERRO, false);
        isInfo = args != null && args.getBoolean(ARG_IS_INFO, false);
        mensagemErro = args != null ? args.getString(ARG_MSG_ERRO) : null;
        shouldFinishActivity = args != null && args.getBoolean(ARG_SHOULD_FINISH_ACTIVITY, true);
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
            
            // Se for info, usa o ícone laranja
            if (isInfo && bindingErro.ivErrorIcon != null) {
                bindingErro.ivErrorIcon.setImageResource(R.drawable.ic_info_popup);
            }
            
            bindingErro.btnFechar.setOnClickListener(v -> {
                dismiss();
                if (shouldFinishActivity && getActivity() != null) {
                    getActivity().finish();
                }
            });
        } else if (bindingSucesso != null) {
            bindingSucesso.btnFechar.setOnClickListener(v -> {
                dismiss();
                if (shouldFinishActivity && getActivity() != null) {
                    getActivity().finish();
                }
            });
        }
    }


    public static void showSucesso(FragmentManager fm) {
        showSucesso(fm, true);
    }

    public static void showSucesso(FragmentManager fm, boolean shouldFinishActivity) {
        if (fm == null || fm.isStateSaved()) {
            Log.e("ConfirmacaoBottomSheet", "FragmentManager é null ou estado já foi salvo, não é possível mostrar sucesso");
            return;
        }
        
        try {
            // Fechar qualquer bottom sheet existente antes de abrir um novo
            dismissExistingBottomSheets(fm);
            
            ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
            Bundle args = new Bundle();
            args.putBoolean(ARG_IS_ERRO, false);
            args.putBoolean(ARG_SHOULD_FINISH_ACTIVITY, shouldFinishActivity);
            fragment.setArguments(args);
            fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
        } catch (Exception e) {
            Log.e("ConfirmacaoBottomSheet", "Erro ao mostrar sucesso: " + e.getMessage(), e);
        }
    }

    public static void showErro(FragmentManager fm, String mensagem) {
        showErro(fm, mensagem, true);
    }

    public static void showErro(FragmentManager fm, String mensagem, boolean shouldFinishActivity) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, true);
        args.putString(ARG_MSG_ERRO, mensagem);
        args.putBoolean(ARG_SHOULD_FINISH_ACTIVITY, shouldFinishActivity);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
    }
    
    public static void showInfo(FragmentManager fm, String mensagem) {
        showInfo(fm, mensagem, false);
    }
    
    public static void showInfo(FragmentManager fm, String mensagem, boolean shouldFinishActivity) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        ConfirmacaoBottomSheetDialogFragment fragment = new ConfirmacaoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ERRO, true);
        args.putBoolean(ARG_IS_INFO, true);
        args.putString(ARG_MSG_ERRO, mensagem);
        args.putBoolean(ARG_SHOULD_FINISH_ACTIVITY, shouldFinishActivity);
        fragment.setArguments(args);
        fragment.show(fm, "ConfirmacaoBottomSheetDialogFragment");
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