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
import com.sustria.codcoz.databinding.BottomsheetFiltrosBinding;

public class FiltrosBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "historico_filtros_result";
    public static final String RESULT_SORT = "result_sort";
    public static final String RESULT_TIPO = "result_tipo";
    public static final String RESULT_PERIODO = "result_periodo";
    public static final String RESULT_LIMPAR = "result_limpar";

    public static final String ARG_SORT = "arg_sort";
    public static final String ARG_TIPO = "arg_tipo";
    public static final String ARG_PERIODO = "arg_periodo";

    private BottomsheetFiltrosBinding binding;

    private int sortOrder;   // 0 = mais recentes, 1 = mais antigos
    private int tipoFiltro;  // 0 = todos, 1 = entrada, 2 = baixa
    private int periodo;     // 0 = todos, 1 = hoje, 2 = ontem,3=7,4=15,5=30

    public static void show(@NonNull FragmentManager fm, int sortOrder, int tipoFiltro,
                            int periodo) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);
        
        FiltrosBottomSheetDialogFragment fragment = new FiltrosBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT, sortOrder);
        args.putInt(ARG_TIPO, tipoFiltro);
        args.putInt(ARG_PERIODO, periodo);
        fragment.setArguments(args);
        fragment.show(fm, "FiltrosBottomSheetDialogFragment");
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomsheetFiltrosBinding.inflate(inflater, container, false);
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

        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        sortOrder = args.getInt(ARG_SORT, 0);
        tipoFiltro = args.getInt(ARG_TIPO, 0);
        periodo = args.getInt(ARG_PERIODO, 0);

        setupInitialSelection();
        setupClicks();
    }

    private void setupInitialSelection() {
        select(binding.opMaisRecentes, binding.opMaisAntigos, sortOrder == 0 ? 0 : 1);

        int tipoIndex;
        switch (tipoFiltro) {
            case 1:
                tipoIndex = 0;
                break; // entrada
            case 2:
                tipoIndex = 1;
                break; // baixa
            default:
                tipoIndex = -1;
                break; // todos
        }
        clearAndMaybeSelect(tipoIndex, binding.opTipoEntrada, binding.opTipoBaixa);

        int periodoIndex;
        switch (periodo) {
            case 1:
                periodoIndex = 0;
                break; // hoje
            case 2:
                periodoIndex = 1;
                break; // ontem
            case 3:
                periodoIndex = 2;
                break; // 7
            case 4:
                periodoIndex = 3;
                break; // 15
            case 5:
                periodoIndex = 4;
                break; // 30
            default:
                periodoIndex = -1;
                break; // todos
        }
        clearAndMaybeSelect(periodoIndex, binding.opHoje, binding.opOntem, binding.op7dias,
                binding.op15dias, binding.op30dias);
    }

    private void setupClicks() {
        binding.btnClosePopup.setOnClickListener(v -> dismiss());

        // ordenar
        binding.opMaisRecentes.setOnClickListener(v -> {
            sortOrder = 0;
            select(binding.opMaisRecentes, binding.opMaisAntigos, 0);
        });
        binding.opMaisAntigos.setOnClickListener(v -> {
            sortOrder = 1;
            select(binding.opMaisRecentes, binding.opMaisAntigos, 1);
        });

        // Tipo
        binding.opTipoEntrada.setOnClickListener(v -> {
            tipoFiltro = 1;
            clearAndMaybeSelect(0, binding.opTipoEntrada, binding.opTipoBaixa);
        });
        binding.opTipoBaixa.setOnClickListener(v -> {
            tipoFiltro = 2;
            clearAndMaybeSelect(1, binding.opTipoEntrada, binding.opTipoBaixa);
        });

        // periodo
        binding.opHoje.setOnClickListener(v -> {
            periodo = 1;
            clearAndMaybeSelect(0, binding.opHoje, binding.opOntem, binding.op7dias,
                    binding.op15dias, binding.op30dias);
        });
        binding.opOntem.setOnClickListener(v -> {
            periodo = 2;
            clearAndMaybeSelect(1, binding.opHoje, binding.opOntem, binding.op7dias,
                    binding.op15dias, binding.op30dias);
        });
        binding.op7dias.setOnClickListener(v -> {
            periodo = 3;
            clearAndMaybeSelect(2, binding.opHoje, binding.opOntem, binding.op7dias,
                    binding.op15dias, binding.op30dias);
        });
        binding.op15dias.setOnClickListener(v -> {
            periodo = 4;
            clearAndMaybeSelect(3, binding.opHoje, binding.opOntem, binding.op7dias,
                    binding.op15dias, binding.op30dias);
        });
        binding.op30dias.setOnClickListener(v -> {
            periodo = 5;
            clearAndMaybeSelect(4, binding.opHoje, binding.opOntem, binding.op7dias,
                    binding.op15dias, binding.op30dias);
        });

        binding.btnAplicarFiltros.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putInt(RESULT_SORT, sortOrder);
            result.putInt(RESULT_TIPO, tipoFiltro);
            result.putInt(RESULT_PERIODO, periodo);
            getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
            dismiss();
        });

        binding.btnLimparFiltros.setOnClickListener(v -> {
            sortOrder = 0;
            tipoFiltro = 0;
            periodo = 0;
            setupInitialSelection();
            
            // Comunicar que os filtros foram limpos
            Bundle result = new Bundle();
            result.putInt(RESULT_SORT, sortOrder);
            result.putInt(RESULT_TIPO, tipoFiltro);
            result.putInt(RESULT_PERIODO, periodo);
            result.putBoolean(RESULT_LIMPAR, true);
            getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
            dismiss();
        });
    }

    private void select(View first, View second, int which) {
        if (which == 0) {
            first.setBackgroundResource(R.drawable.bg_tab_selected);
            second.setBackgroundResource(R.drawable.bg_tab_unselected);
        } else {
            first.setBackgroundResource(R.drawable.bg_tab_unselected);
            second.setBackgroundResource(R.drawable.bg_tab_selected);
        }
    }

    private void clearAndMaybeSelect(int indexToSelect, View... views) {
        for (View view : views) {
            view.setBackgroundResource(R.drawable.bg_tab_unselected);
        }
        if (indexToSelect >= 0 && indexToSelect < views.length) {
            views[indexToSelect].setBackgroundResource(R.drawable.bg_tab_selected);
        }
    }
}


