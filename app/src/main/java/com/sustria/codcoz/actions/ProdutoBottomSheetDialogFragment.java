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
import com.sustria.codcoz.databinding.BottomsheetProdutoEscaneadoBinding;
import com.sustria.codcoz.api.model.ProdutoResponse;

public class ProdutoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private BottomsheetProdutoEscaneadoBinding binding;
    private static final String ARG_CODIGO = "arg_codigo";
    private static final String ARG_TIPO_MOV = "arg_tipo_mov"; // "baixa" ou "entrada"
    //    private ProdutoRepository produtoRepository;
    private ProdutoResponse produto;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomsheetProdutoEscaneadoBinding.inflate(inflater, container, false);
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
//        produtoRepository = new ProdutoRepository(requireContext());

        String codigo = getArguments() != null ? getArguments().getString(ARG_CODIGO) : null;
        String tipoMov = getArguments() != null ? getArguments().getString(ARG_TIPO_MOV) : null;

        if (codigo != null) {
//            produto = produtoRepository.buscarPorCodigo(codigo);
        }

        if (produto == null) {
            dismiss();
            ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Produto não encontrado para o código informado");
            return;
        }

        if (produto != null) {
//            binding.codigoProduto.setText(produto.getCodigo());
//            binding.nomeProduto.setText(produto.getNome());
//            binding.fornecedorProduto.setText(produto.getFornecedor());
//            binding.pesoUnit.setText(produto.getPesoUnit());
//            binding.qntTotal.setText(produto.getQuantidadeTotal());
        }

        binding.btnClosePopup.setOnClickListener(v -> dismiss());

        // Ouve o resultado de confirmação do bottomsheet de confirmação
        getParentFragmentManager().setFragmentResultListener(
                ConfirmarRegistroBottomSheetDialogFragment.REQUEST_KEY,
                this,
                (requestKey, result) -> {
                    boolean confirmed = result.getBoolean(ConfirmarRegistroBottomSheetDialogFragment.RESULT_CONFIRMED, false);
                    if (!confirmed || produto == null) return;
                    try {
                        if ("baixa".equalsIgnoreCase(tipoMov)) {
//                            produtoRepository.realizarBaixa(produto);
                        } else if ("entrada".equalsIgnoreCase(tipoMov)) {
//                            produtoRepository.realizarEntrada(produto);
                        }
                        dismiss();
                        ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                    } catch (Exception e) {
                        dismiss();
                        String mensagem = e.getMessage() != null ? e.getMessage() : "Falha ao processar a operação";
                        ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), mensagem);
                    }
                }
        );

        binding.btnConfirmar.setOnClickListener(v -> {
            if (produto == null) {
                dismiss();
                return;
            }
            Integer estoqueAntigo = null;
            Integer estoqueAtualizado = null;
            try {
                estoqueAntigo = produto.getQuantidade();
                estoqueAtualizado = produto.getQuantidade();
            } catch (Exception ignored) {}

            ConfirmarRegistroBottomSheetDialogFragment.show(
                    getParentFragmentManager(),
                    produto.getNome(),
                    estoqueAntigo,
                    estoqueAtualizado
            );
        });
    }

    public static void show(FragmentManager fm, String codigo, String tipoMov) {
        ProdutoBottomSheetDialogFragment fragment = new ProdutoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CODIGO, codigo);
        args.putString(ARG_TIPO_MOV, tipoMov);
        fragment.setArguments(args);
        fragment.show(fm, "ProdutoBottomSheetDialogFragment");
    }

}


