package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.databinding.BottomsheetTarefaDetalheBinding;
import com.sustria.codcoz.ui.inicio.InicioViewModel;

import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

public class TarefaDetalheBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TIPO = "arg_tipo";
    private static final String ARG_PRODUTO = "arg_produto";
    private static final String ARG_RELATOR = "arg_relator";
    private static final String ARG_DATA = "arg_data";
    private static final String ARG_SITUACAO = "arg_situacao";
    private static final String ARG_TAREFA_ID = "arg_tarefa_id";

    private BottomsheetTarefaDetalheBinding binding;
    private TarefaService tarefaService;
    private Long tarefaId;
    private boolean tarefaConcluida = false;

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

        // Inicializar serviço
        tarefaService = new TarefaService();

        Bundle args = getArguments();
        String tipo = args != null ? args.getString(ARG_TIPO) : null;
        String produto = args != null ? args.getString(ARG_PRODUTO) : null;
        String relator = args != null ? args.getString(ARG_RELATOR) : null;
        String data = args != null ? args.getString(ARG_DATA) : null;
        String situacao = args != null ? args.getString(ARG_SITUACAO) : null;
        tarefaId = args != null ? args.getLong(ARG_TAREFA_ID, -1) : -1;

        if (tipo != null) binding.txtTitulo.setText(tipo);
        if (produto != null) binding.txtProduto.setText(produto);
        if (relator != null) binding.txtRelator.setText(relator);
        if (data != null) binding.txtData.setText(data);

        // Verificar situação e ajustar bottomsheet
        verificarSituacaoTarefa(situacao, data);

        binding.btnFechar.setOnClickListener(v -> dismiss());

        // Após confirmar, exibe sucesso
        getParentFragmentManager().setFragmentResultListener(
                ConfirmarRegistroBottomSheetDialogFragment.REQUEST_KEY,
                this,
                (requestKey, result) -> {
                    boolean confirmed = result.getBoolean(ConfirmarRegistroBottomSheetDialogFragment.RESULT_CONFIRMED, false);
                    if (!confirmed) return;
                    dismiss();
                    ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                }
        );

        // Listener para quando uma tarefa for finalizada (para atualizar o bottom sheet)
        getParentFragmentManager().setFragmentResultListener(
                "tarefa_finalizada",
                this,
                (requestKey, result) -> {
                    boolean tarefaFinalizada = result.getBoolean("tarefa_finalizada", false);
                    Long idTarefaFinalizada = result.getLong("tarefa_id", -1);
                    
                    // Se a tarefa finalizada é esta tarefa, atualizar a UI e fechar
                    if (tarefaFinalizada && idTarefaFinalizada > 0 && idTarefaFinalizada.equals(tarefaId)) {
                        // Marcar como concluída para evitar tentativas futuras
                        tarefaConcluida = true;
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                // Atualizar situação para "Concluída"
                                configurarTarefaConcluida();
                                // Recarregar tarefas no início
                                recarregarTarefasInicio();
                                // Fechar após um pequeno delay para mostrar a atualização
                                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                                    if (isAdded()) {
                                        dismiss();
                                    }
                                }, 500);
                            });
                        }
                    }
                }
        );

        // Armazenar situação inicial
        if (situacao != null && situacao.toLowerCase().contains("concluída")) {
            tarefaConcluida = true;
        }

        binding.btnRegistrar.setOnClickListener(v -> {
            // Verificar se a tarefa já foi concluída (verificação dinâmica)
            if (tarefaConcluida || (situacao != null && situacao.toLowerCase().contains("concluída"))) {
                return;
            }

            // Verificar o tipo de tarefa e seguir o fluxo apropriado
            if (tipo != null && tipo.equalsIgnoreCase("Atividade")) {
                // Se for atividade, abrir bottomsheet de escolha (entrada/saída)
                if (tarefaId != null && tarefaId > 0) {
                    // Passar ingrediente esperado para validar se o produto está correto
                    String ingredienteEsperado = produto != null ? produto : null;
                    AtividadeEscolhaEntradaBottomSheetDialogFragment.show(getParentFragmentManager(), tarefaId, ingredienteEsperado);
                } else {
                    ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "ID da tarefa inválido");
                }
            } else if (tipo != null && tipo.equalsIgnoreCase("Conferência de Estoque")) {
                // Se for conferência de estoque, abrir bottomsheet de auditoria de quantidade
                if (tarefaId != null && tarefaId > 0) {
                    AuditoriaQuantidadeBottomSheetDialogFragment.show(
                            getParentFragmentManager(),
                            tipo,
                            produto,
                            tarefaId
                    );
                } else {
                    ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "ID da tarefa inválido");
                }
            } else {
                // Para outros tipos de tarefa, finalizar diretamente
                if (tarefaId != null && tarefaId > 0) {
                    finalizarTarefa();
                } else {
                    ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "ID da tarefa inválido");
                }
            }
        });
    }

    public static TarefaDetalheBottomSheetDialogFragment newInstance(String tipo, String produto, String relator, String data, String situacao, Long tarefaId) {
        TarefaDetalheBottomSheetDialogFragment f = new TarefaDetalheBottomSheetDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TIPO, tipo);
        b.putString(ARG_PRODUTO, produto);
        b.putString(ARG_RELATOR, relator);
        b.putString(ARG_DATA, data);
        b.putString(ARG_SITUACAO, situacao);
        b.putLong(ARG_TAREFA_ID, tarefaId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void show(@NonNull androidx.fragment.app.FragmentManager manager, @Nullable String tag) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(manager);
        super.show(manager, tag);
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

    private void verificarSituacaoTarefa(String situacao, String dataLimite) {
        if (situacao != null && situacao.toLowerCase().contains("concluída")) {
            // Tarefa já foi concluída
            configurarTarefaConcluida();
        } else if (situacao != null && situacao.toLowerCase().contains("concluída") &&
                verificarSeAtrasada(dataLimite)) {
            // Tarefa concluída com atraso
            configurarTarefaConcluidaComAtraso();
        } else {
            // Tarefa pendente
            configurarTarefaPendente();
        }
    }

    private boolean verificarSeAtrasada(String dataLimite) {
        if (dataLimite == null || dataLimite.trim().isEmpty()) {
            return false;
        }

        try {
            java.time.LocalDate limite = java.time.LocalDate.parse(dataLimite);
            java.time.LocalDate hoje = java.time.LocalDate.now();
            return limite.isBefore(hoje);
        } catch (Exception e) {
            return false;
        }
    }

    private void configurarTarefaConcluida() {
        binding.btnRegistrar.setText("Concluída");
        binding.btnRegistrar.setBackgroundResource(R.drawable.bg_tab_selected);
        binding.btnRegistrar.setEnabled(false);
        binding.btnRegistrar.setAlpha(0.6f);
    }

    private void configurarTarefaConcluidaComAtraso() {
        binding.btnRegistrar.setText("Atrasada");
        binding.btnRegistrar.setBackgroundResource(R.drawable.bg_tab_error);
        binding.btnRegistrar.setEnabled(false);
        binding.btnRegistrar.setAlpha(0.6f);
    }

    private void configurarTarefaPendente() {
        binding.btnRegistrar.setText("Registrar");
        binding.btnRegistrar.setBackgroundResource(R.drawable.bg_tab_selected);
        binding.btnRegistrar.setEnabled(true);
        binding.btnRegistrar.setAlpha(1.0f);
    }

    private void finalizarTarefa() {
        if (tarefaService == null || tarefaId == null || tarefaId <= 0) {
            ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Erro ao finalizar tarefa: dados inválidos");
            return;
        }

        // Mostrar loading
        binding.btnRegistrar.setText("Finalizando...");
        binding.btnRegistrar.setEnabled(false);

        tarefaService.finalizarTarefa(tarefaId, new TarefaService.TarefaCallback<>() {
            @Override
            public void onSuccess(TarefaResponse result) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Notificar que a tarefa foi finalizada através de FragmentResult
                        Bundle resultBundle = new Bundle();
                        resultBundle.putBoolean("tarefa_finalizada", true);
                        resultBundle.putLong("tarefa_id", tarefaId);
                        getParentFragmentManager().setFragmentResult("tarefa_finalizada", resultBundle);

                        // Recarregar tarefas no início
                        recarregarTarefasInicio();
                        
                        // Marcar como concluída e atualizar UI
                        tarefaConcluida = true;
                        configurarTarefaConcluida();
                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            if (isAdded()) {
                                dismiss();
                                ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                            }
                        }, 500);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        binding.btnRegistrar.setText("Registrar");
                        binding.btnRegistrar.setEnabled(true);
                        ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Erro ao finalizar tarefa: " + error);
                    });
                }
            }
        });
    }

    private void recarregarTarefasInicio() {
        // Tentar recarregar as tarefas na página de início
        // Obtém o InicioViewModel através da Activity (se disponível)
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            try {
                // Usar Handler para garantir que está na thread principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    try {
                        InicioViewModel inicioViewModel = new ViewModelProvider((AppCompatActivity) getActivity()).get(InicioViewModel.class);
                        inicioViewModel.loadTasks();
                        Log.d("TarefaDetalhe", "Tarefas recarregadas na página de início");
                    } catch (Exception e) {
                        Log.e("TarefaDetalhe", "Erro ao recarregar tarefas: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("TarefaDetalhe", "Erro ao agendar recarregamento de tarefas: " + e.getMessage());
            }
        }
    }
}


