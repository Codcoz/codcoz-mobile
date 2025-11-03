package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.HistoricoBaixaRequest;
import com.sustria.codcoz.api.model.TarefaResponse;
import com.sustria.codcoz.api.service.HistoricoService;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.api.service.TarefaService;
import com.sustria.codcoz.ui.inicio.InicioViewModel;

import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import com.sustria.codcoz.databinding.BottomsheetProdutoRegistroConfirmacaoBinding;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfirmarRegistroBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "confirmar_registro_request";
    public static final String RESULT_CONFIRMED = "result_confirmed";

    private static final String ARG_NOME = "arg_nome";
    private static final String ARG_ESTOQUE_ANTIGO = "arg_estoque_antigo";
    private static final String ARG_ESTOQUE_ATUALIZADO = "arg_estoque_atualizado";
    private static final String ARG_CODIGO_EAN = "arg_codigo_ean";
    private static final String ARG_QUANTIDADE = "arg_quantidade";
    private static final String ARG_TIPO_MOVIMENTO = "arg_tipo_movimento";
    private static final String ARG_ID_PRODUTO = "arg_id_produto";
    private static final String ARG_TAREFA_ID = "arg_tarefa_id";
    private static final String ARG_INGREDIENTE_ESPERADO = "arg_ingrediente_esperado";

    private BottomsheetProdutoRegistroConfirmacaoBinding binding;
    private ProdutoService produtoService;
    private HistoricoService historicoService;
    private TarefaService tarefaService;
    private Long tarefaId;
    private String ingredienteEsperado;

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

        produtoService = new ProdutoService();
        historicoService = new HistoricoService();
        tarefaService = new TarefaService();
        
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_TAREFA_ID)) {
            tarefaId = args.getLong(ARG_TAREFA_ID, -1);
            if (tarefaId <= 0) {
                tarefaId = null;
            }
        }
        ingredienteEsperado = args != null ? args.getString(ARG_INGREDIENTE_ESPERADO) : null;

        String nome = args != null ? args.getString(ARG_NOME) : null;
        Integer estoqueAntigo = null;
        Integer estoqueAtualizado = null;
        
        if (args != null) {
            if (args.containsKey(ARG_ESTOQUE_ANTIGO)) {
                int valorAntigo = args.getInt(ARG_ESTOQUE_ANTIGO, -1);
                if (valorAntigo >= 0) {
                    estoqueAntigo = valorAntigo;
                }
            }
            if (args.containsKey(ARG_ESTOQUE_ATUALIZADO)) {
                int valorAtualizado = args.getInt(ARG_ESTOQUE_ATUALIZADO, -1);
                if (valorAtualizado >= 0) {
                    estoqueAtualizado = valorAtualizado;
                }
            }
        }

        if (nome != null) {
            binding.txtNomeProduto.setText(nome);
        }
        if (estoqueAntigo != null) {
            binding.txtEstoqueAntigo.setText(String.valueOf(estoqueAntigo));
        } else {
            binding.txtEstoqueAntigo.setText("--");
        }
        if (estoqueAtualizado != null) {
            binding.txtEstoqueAtualizado.setText(String.valueOf(estoqueAtualizado));
        } else {
            binding.txtEstoqueAtualizado.setText("--");
        }

        binding.btnCancelar.setOnClickListener(v -> dismiss());
        binding.btnConfirmar.setOnClickListener(v -> {
            Log.d("ConfirmarRegistro", "Botão confirmar clicado");
            executarOperacoes();
        });
    }

    private void executarOperacoes() {
        Bundle args = getArguments();
        if (args == null) {
            Log.e("ConfirmarRegistro", "Argumentos não disponíveis");
            dismiss();
            ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Dados inválidos");
            return;
        }

        String codigoEan = args.getString(ARG_CODIGO_EAN);
        Integer quantidade = args.containsKey(ARG_QUANTIDADE) ? args.getInt(ARG_QUANTIDADE, 1) : null;
        ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov =
                (ProdutoBottomSheetDialogFragment.TipoMovimento) args.getSerializable(ARG_TIPO_MOVIMENTO);

        // Se os dados não estiverem disponíveis (por exemplo, na auditoria), apenas fechar
        if (codigoEan == null || tipoMov == null || quantidade == null) {
            Log.d("ConfirmarRegistro", "Dados não disponíveis - apenas confirmando (auditoria)");
            // Se for auditoria e houver tarefaId, finalizar a auditoria com a contagem
            if (tarefaId != null && tarefaId > 0) {
                Integer estoqueAtualizado = args.containsKey(ARG_ESTOQUE_ATUALIZADO) ? args.getInt(ARG_ESTOQUE_ATUALIZADO, -1) : null;
                if (estoqueAtualizado != null && estoqueAtualizado >= 0) {
                    finalizarAuditoria(estoqueAtualizado);
                } else {
                    // Se não houver estoqueAtualizado, usar o método antigo
                    finalizarTarefaAtividade();
                }
            } else {
                dismiss();
                ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
            }
            return;
        }

        Log.d("ConfirmarRegistro", "Executando operação de estoque - Tipo: " + tipoMov + ", Quantidade: " + quantidade);

        // Executar operação de estoque (entrada ou baixa)
        ProdutoService.ProdutoCallback<Void> callbackProduto = new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                Log.d("ConfirmarRegistro", "Operação de estoque realizada com sucesso");
                registrarHistorico(codigoEan, quantidade, tipoMov);
            }

            @Override
            public void onError(String error) {
                Log.e("ConfirmarRegistro", "Erro na operação de estoque: " + error);
                dismiss();
                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Erro ao atualizar estoque: " + error);
            }
        };

        if (tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.BAIXA) {
            Log.d("ConfirmarRegistro", "Chamando API de baixa de estoque");
            produtoService.baixaEstoque(codigoEan, quantidade, callbackProduto);
        } else {
            Log.d("ConfirmarRegistro", "Chamando API de entrada de estoque");
            produtoService.entradaEstoque(codigoEan, quantidade, callbackProduto);
        }
    }

    private void registrarHistorico(String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov) {
        Bundle args = getArguments();
        if (args == null) {
            Log.e("ConfirmarRegistro", "Argumentos não disponíveis para histórico");
            return;
        }

        String nomeProduto = args.getString(ARG_NOME);
        String idProduto = args.getString(ARG_ID_PRODUTO);
        Long idEmpresa = Long.valueOf(UserDataManager.getInstance().getEmpresaId());

        Log.d("ConfirmarRegistro", "Registrando histórico - Empresa: " + idEmpresa + ", Produto: " + nomeProduto + ", ID: " + idProduto);

        // Criar request para o histórico
        HistoricoBaixaRequest request = new HistoricoBaixaRequest(
                idProduto != null ? idProduto : "",
                nomeProduto != null ? nomeProduto : "",
                codigoEan,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                quantidade,
                tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.BAIXA ? "Saída" : "Entrada"
        );

        // Registrar no histórico
        historicoService.registrarHistoricoBaixa(idEmpresa, request, new HistoricoService.HistoricoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                Log.d("ConfirmarRegistro", "Histórico registrado com sucesso");
                // Se houver tarefaId, finalizar a tarefa (pode ser atividade com entrada ou auditoria)
                if (tarefaId != null && tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.ENTRADA) {
                    // Entrada de atividade
                    finalizarTarefaAtividade();
                } else if (tarefaId != null) {
                    // Outros casos com tarefaId (não deveria acontecer com auditoria aqui, mas por segurança)
                    if (isAdded()) {
                        dismiss();
                        ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                    }
                } else {
                    if (isAdded()) {
                        dismiss();
                        ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e("ConfirmarRegistro", "Erro ao registrar histórico: " + error);
                // Mesmo se o histórico falhar, a operação de estoque foi bem-sucedida
                // Se for uma entrada de atividade, ainda assim finalizar a tarefa
                if (tarefaId != null && tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.ENTRADA) {
                    finalizarTarefaAtividade();
                } else if (tarefaId != null) {
                    // Outros casos com tarefaId
                    if (isAdded()) {
                        dismiss();
                        ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                    }
                } else {
                    if (isAdded()) {
                        dismiss();
                        ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                    }
                }
            }
        });
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                            String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov) {
        show(fm, nome, estoqueAntigo, estoqueAtualizado, codigoEan, quantidade, tipoMov, null, null);
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                            String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov, String idProduto) {
        show(fm, nome, estoqueAntigo, estoqueAtualizado, codigoEan, quantidade, tipoMov, idProduto, null);
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                            String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov, String idProduto, Long tarefaId) {
        show(fm, nome, estoqueAntigo, estoqueAtualizado, codigoEan, quantidade, tipoMov, idProduto, tarefaId, null);
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                            String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov, String idProduto, Long tarefaId, String ingredienteEsperado) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(fm);

        ConfirmarRegistroBottomSheetDialogFragment fragment = new ConfirmarRegistroBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOME, nome);
        if (estoqueAntigo != null) args.putInt(ARG_ESTOQUE_ANTIGO, estoqueAntigo);
        if (estoqueAtualizado != null) args.putInt(ARG_ESTOQUE_ATUALIZADO, estoqueAtualizado);
        if (codigoEan != null) args.putString(ARG_CODIGO_EAN, codigoEan);
        if (quantidade != null) args.putInt(ARG_QUANTIDADE, quantidade);
        if (tipoMov != null) args.putSerializable(ARG_TIPO_MOVIMENTO, tipoMov);
        if (idProduto != null) args.putString(ARG_ID_PRODUTO, idProduto);
        if (tarefaId != null && tarefaId > 0) args.putLong(ARG_TAREFA_ID, tarefaId);
        if (ingredienteEsperado != null) args.putString(ARG_INGREDIENTE_ESPERADO, ingredienteEsperado);
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

    private void finalizarTarefaAtividade() {
        if (tarefaId == null || tarefaId <= 0 || tarefaService == null) {
            Log.e("ConfirmarRegistro", "Não é possível finalizar tarefa: tarefaId inválido ou serviço não disponível");
            mostrarSucessoFinalizacao();
            return;
        }

        // Validar se o produto corresponde ao ingrediente esperado antes de finalizar
        if (ingredienteEsperado != null && !ingredienteEsperado.trim().isEmpty()) {
            Bundle args = getArguments();
            String nomeProduto = args != null ? args.getString(ARG_NOME) : null;
            if (nomeProduto == null || !nomeProduto.equalsIgnoreCase(ingredienteEsperado.trim())) {
                Log.e("ConfirmarRegistro", "Produto não corresponde ao esperado. Esperado: " + ingredienteEsperado + ", Encontrado: " + nomeProduto);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (isAdded()) {
                            dismiss();
                            ConfirmacaoBottomSheetDialogFragment.showErro(
                                    getParentFragmentManager(),
                                    "Produto incorreto! A tarefa não foi finalizada.\nEsperado: " + ingredienteEsperado + "\nRegistrado: " + (nomeProduto != null ? nomeProduto : "N/A"),
                                    false
                            );
                        }
                    });
                }
                return;
            }
        }

        Log.d("ConfirmarRegistro", "Finalizando tarefa de atividade: " + tarefaId);
        tarefaService.finalizarTarefa(tarefaId, new TarefaService.TarefaCallback<>() {
            @Override
            public void onSuccess(TarefaResponse result) {
                Log.d("ConfirmarRegistro", "Tarefa finalizada com sucesso");
                
                // Notificar que a tarefa foi finalizada através de FragmentResult
                Bundle resultBundle = new Bundle();
                resultBundle.putBoolean("tarefa_finalizada", true);
                resultBundle.putLong("tarefa_id", tarefaId);
                getParentFragmentManager().setFragmentResult("tarefa_finalizada", resultBundle);

                // Recarregar tarefas na página de início
                recarregarTarefasInicio();
                
                // Fechar este bottom sheet e qualquer TarefaDetalheBottomSheetDialogFragment aberto
                fecharBottomSheetsRelacionados();
                
                mostrarSucessoFinalizacao();
            }

            @Override
            public void onError(String error) {
                Log.e("ConfirmarRegistro", "Erro ao finalizar tarefa: " + error);
                // Mesmo se a finalização falhar, a auditoria foi bem-sucedida
                mostrarSucessoFinalizacao();
            }
        });
    }

    private void finalizarAuditoria(Integer contagem) {
        if (tarefaId == null || tarefaId <= 0 || tarefaService == null) {
            Log.e("ConfirmarRegistro", "Não é possível finalizar auditoria: tarefaId inválido ou serviço não disponível");
            mostrarSucessoFinalizacao();
            return;
        }

        Log.d("ConfirmarRegistro", "Finalizando auditoria: " + tarefaId + " com contagem: " + contagem);
        tarefaService.finalizarAuditoria(tarefaId, contagem, new TarefaService.TarefaCallback<>() {
            @Override
            public void onSuccess(TarefaResponse result) {
                Log.d("ConfirmarRegistro", "Auditoria finalizada com sucesso. Chamando finalizar tarefa para atualizar status.");
                // Após finalizar a auditoria, precisa chamar finalizar-tarefa para atualizar o status
                tarefaService.finalizarTarefa(tarefaId, new TarefaService.TarefaCallback<>() {
                    @Override
                    public void onSuccess(TarefaResponse tarefaResult) {
                        Log.d("ConfirmarRegistro", "Tarefa finalizada com sucesso");
                        
                        // Notificar que a tarefa foi finalizada através de FragmentResult
                        Bundle resultBundle = new Bundle();
                        resultBundle.putBoolean("tarefa_finalizada", true);
                        resultBundle.putLong("tarefa_id", tarefaId);
                        getParentFragmentManager().setFragmentResult("tarefa_finalizada", resultBundle);

                        // Recarregar tarefas na página de início
                        recarregarTarefasInicio();
                        
                        // Fechar este bottom sheet e qualquer TarefaDetalheBottomSheetDialogFragment aberto
                        fecharBottomSheetsRelacionados();
                        
                        mostrarSucessoFinalizacao();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("ConfirmarRegistro", "Erro ao finalizar tarefa após auditoria: " + error);
                        // Mesmo se a finalização da tarefa falhar, a auditoria foi bem-sucedida
                        mostrarSucessoFinalizacao();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("ConfirmarRegistro", "Erro ao finalizar auditoria: " + error);
                // Se a auditoria falhar, não tenta finalizar a tarefa
                mostrarSucessoFinalizacao();
            }
        });
    }

    private void mostrarSucessoFinalizacao() {
        if (getActivity() == null) {
            Log.e("ConfirmarRegistro", "Activity é null, não é possível mostrar sucesso");
            return;
        }

        // Salvar FragmentManager e Activity antes de fazer qualquer coisa
        final androidx.fragment.app.FragmentManager fragmentManager = getParentFragmentManager();
        final android.app.Activity activity = getActivity();
        
        getActivity().runOnUiThread(() -> {
            try {
                // Fechar este bottom sheet primeiro
                if (isAdded() && isVisible()) {
                    dismiss();
                }
                
                // Usar Handler para adicionar um pequeno delay e garantir que o dismiss foi processado
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    try {
                        if (activity != null && !activity.isFinishing() && !fragmentManager.isStateSaved()) {
                            // Usar shouldFinishActivity = false para não fechar a Activity
                            ConfirmacaoBottomSheetDialogFragment.showSucesso(fragmentManager, false);
                        }
                    } catch (Exception e) {
                        Log.e("ConfirmarRegistro", "Erro ao mostrar sucesso após delay: " + e.getMessage(), e);
                    }
                }, 300); // 300ms de delay
            } catch (Exception e) {
                Log.e("ConfirmarRegistro", "Erro ao mostrar sucesso: " + e.getMessage(), e);
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
                        InicioViewModel inicioViewModel = new ViewModelProvider(getActivity()).get(InicioViewModel.class);
                        inicioViewModel.loadTasks();
                        Log.d("ConfirmarRegistro", "Tarefas recarregadas na página de início");
                    } catch (Exception e) {
                        Log.e("ConfirmarRegistro", "Erro ao recarregar tarefas: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("ConfirmarRegistro", "Erro ao agendar recarregamento de tarefas: " + e.getMessage());
            }
        }
    }

    private void fecharBottomSheetsRelacionados() {
        // Fechar TarefaDetalheBottomSheetDialogFragment se estiver aberto
        Log.d("ConfirmarRegistro", "Fechando bottom sheets relacionados");
        Fragment tarefaDetalheFragment = getParentFragmentManager().findFragmentByTag("TarefaDetalheBottomSheetDialog");
        if (tarefaDetalheFragment instanceof BottomSheetDialogFragment) {
            ((BottomSheetDialogFragment) tarefaDetalheFragment).dismiss();
        }
    }
}

