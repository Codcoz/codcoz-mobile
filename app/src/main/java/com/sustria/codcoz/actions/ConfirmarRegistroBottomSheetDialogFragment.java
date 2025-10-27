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
import com.sustria.codcoz.api.model.HistoricoBaixaRequest;
import com.sustria.codcoz.api.model.ProdutoResponse;
import com.sustria.codcoz.api.service.HistoricoService;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.databinding.BottomsheetProdutoRegistroConfirmacaoBinding;
import com.sustria.codcoz.utils.UserDataManager;

import java.time.LocalDate;
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

    private BottomsheetProdutoRegistroConfirmacaoBinding binding;
    private ProdutoService produtoService;
    private HistoricoService historicoService;

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
        
        Bundle args = getArguments();
        String nome = args != null ? args.getString(ARG_NOME) : null;
        Integer estoqueAntigo = args != null && args.containsKey(ARG_ESTOQUE_ANTIGO) ? args.getInt(ARG_ESTOQUE_ANTIGO, -1) : null;
        Integer estoqueAtualizado = args != null && args.containsKey(ARG_ESTOQUE_ATUALIZADO) ? args.getInt(ARG_ESTOQUE_ATUALIZADO, -1) : null;

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
            android.util.Log.d("ConfirmarRegistro", "Botão confirmar clicado");
            executarOperacoes();
        });
    }

    private void executarOperacoes() {
        Bundle args = getArguments();
        if (args == null) {
            android.util.Log.e("ConfirmarRegistro", "Argumentos não disponíveis");
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
            android.util.Log.d("ConfirmarRegistro", "Dados não disponíveis - apenas confirmando (auditoria)");
            dismiss();
            ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
            return;
        }

        android.util.Log.d("ConfirmarRegistro", "Executando operação de estoque - Tipo: " + tipoMov + ", Quantidade: " + quantidade);

        // Executar operação de estoque (entrada ou baixa)
        ProdutoService.ProdutoCallback<Void> callbackProduto = new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                android.util.Log.d("ConfirmarRegistro", "Operação de estoque realizada com sucesso");
                registrarHistorico(codigoEan, quantidade, tipoMov);
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("ConfirmarRegistro", "Erro na operação de estoque: " + error);
                dismiss();
                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Erro ao atualizar estoque: " + error);
            }
        };

        if (tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.BAIXA) {
            android.util.Log.d("ConfirmarRegistro", "Chamando API de baixa de estoque");
            produtoService.baixaEstoque(codigoEan, quantidade, callbackProduto);
        } else {
            android.util.Log.d("ConfirmarRegistro", "Chamando API de entrada de estoque");
            produtoService.entradaEstoque(codigoEan, quantidade, callbackProduto);
        }
    }

    private void registrarHistorico(String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov) {
        Bundle args = getArguments();
        if (args == null) {
            android.util.Log.e("ConfirmarRegistro", "Argumentos não disponíveis para histórico");
            return;
        }

        String nomeProduto = args.getString(ARG_NOME);
        String idProduto = args.getString(ARG_ID_PRODUTO);
        Long idEmpresa = Long.valueOf(UserDataManager.getInstance().getEmpresaId());
        
        android.util.Log.d("ConfirmarRegistro", "Registrando histórico - Empresa: " + idEmpresa + ", Produto: " + nomeProduto + ", ID: " + idProduto);

        // Criar request para o histórico
        HistoricoBaixaRequest request = new HistoricoBaixaRequest(
                idProduto != null ? idProduto : "",
                nomeProduto != null ? nomeProduto : "",
                codigoEan,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                tipoMov == ProdutoBottomSheetDialogFragment.TipoMovimento.BAIXA ? "Saída" : "Entrada"
        );

        // Registrar no histórico
        historicoService.registrarHistoricoBaixa(idEmpresa, request, new HistoricoService.HistoricoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                android.util.Log.d("ConfirmarRegistro", "Histórico registrado com sucesso");
                if (isAdded() && getParentFragmentManager() != null) {
                    dismiss();
                    ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                }
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("ConfirmarRegistro", "Erro ao registrar histórico: " + error);
                // Mesmo se o histórico falhar, a operação de estoque foi bem-sucedida
                if (isAdded() && getParentFragmentManager() != null) {
                    dismiss();
                    ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                }
            }
        });
    }

    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                           String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov) {
        show(fm, nome, estoqueAntigo, estoqueAtualizado, codigoEan, quantidade, tipoMov, null);
    }
    
    public static void show(FragmentManager fm, String nome, Integer estoqueAntigo, Integer estoqueAtualizado,
                           String codigoEan, Integer quantidade, ProdutoBottomSheetDialogFragment.TipoMovimento tipoMov, String idProduto) {
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

