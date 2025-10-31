package com.sustria.codcoz.actions;

import android.content.Intent;
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
import com.sustria.codcoz.databinding.BottomsheetAtividadeEscolhaBinding;

public class AtividadeEscolhaEntradaBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TAREFA_ID = "arg_tarefa_id";
    private static final String ARG_INGREDIENTE = "arg_ingrediente";
    
    private BottomsheetAtividadeEscolhaBinding binding;
    private Long tarefaId;
    private String ingredienteEsperado;

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

        Bundle args = getArguments();
        tarefaId = args != null && args.containsKey(ARG_TAREFA_ID) ? args.getLong(ARG_TAREFA_ID, -1) : null;
        if (tarefaId != null && tarefaId <= 0) {
            tarefaId = null;
        }
        ingredienteEsperado = args != null ? args.getString(ARG_INGREDIENTE) : null;

        binding.btnScanner.setOnClickListener(v -> {
            dismiss();
            Intent i = new Intent(requireContext(), EntradaScanActivity.class);
            if (tarefaId != null) {
                i.putExtra("tarefa_id", tarefaId);
            }
            if (ingredienteEsperado != null) {
                i.putExtra("ingrediente_esperado", ingredienteEsperado);
            }
            startActivity(i);
        });

        binding.btnManual.setOnClickListener(v -> {
            dismiss();
            Intent i = new Intent(requireContext(), ManualActivity.class);
            i.putExtra("is_entrada", true);
            if (tarefaId != null) {
                i.putExtra("tarefa_id", tarefaId);
            }
            if (ingredienteEsperado != null) {
                i.putExtra("ingrediente_esperado", ingredienteEsperado);
            }
            startActivity(i);
        });

        binding.btnCancelar.setOnClickListener(v -> dismiss());
    }

    public static void show(@NonNull FragmentManager manager, @Nullable Long tarefaId) {
        show(manager, tarefaId, null, "AtividadeEscolhaEntradaBottomSheetDialog");
    }

    public static void show(@NonNull FragmentManager manager, @Nullable Long tarefaId, @Nullable String ingredienteEsperado) {
        show(manager, tarefaId, ingredienteEsperado, "AtividadeEscolhaEntradaBottomSheetDialog");
    }

    public static void show(@NonNull FragmentManager manager, @Nullable Long tarefaId, @Nullable String ingredienteEsperado, @Nullable String tag) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(manager);
        
        AtividadeEscolhaEntradaBottomSheetDialogFragment fragment = new AtividadeEscolhaEntradaBottomSheetDialogFragment();
        Bundle args = new Bundle();
        if (tarefaId != null && tarefaId > 0) {
            args.putLong(ARG_TAREFA_ID, tarefaId);
        }
        if (ingredienteEsperado != null) {
            args.putString(ARG_INGREDIENTE, ingredienteEsperado);
        }
        fragment.setArguments(args);
        fragment.show(manager, tag);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(manager);
        super.show(manager, tag);
    }

    private static void dismissExistingBottomSheets(@NonNull FragmentManager fm) {
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


