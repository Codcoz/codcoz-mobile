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
import com.sustria.codcoz.api.model.ProdutoResponse;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.databinding.BottomsheetProdutoEscaneadoBinding;

public class ProdutoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public enum TipoMovimento {
        ENTRADA,
        BAIXA
    }

    private BottomsheetProdutoEscaneadoBinding binding;
    private static final String ARG_CODIGO = "arg_codigo";
    private static final String ARG_TIPO_MOV = "arg_tipo_mov";

    private ProdutoService produtoService;
    private ProdutoResponse produto;
    private Integer quantidade = 1;

    public static void show(@NonNull FragmentManager fm, @NonNull String codigo, @NonNull TipoMovimento tipoMov) {
        ProdutoBottomSheetDialogFragment fragment = new ProdutoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CODIGO, codigo);
        args.putSerializable(ARG_TIPO_MOV, tipoMov);
        fragment.setArguments(args);
        fragment.show(fm, "ProdutoBottomSheetDialogFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetProdutoEscaneadoBinding.inflate(inflater, container, false);
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
        Bundle args = getArguments();
        if (args == null) {
            showErrorAndDismiss("Argumentos inválidos.");
            return;
        }

        String codigoEan = args.getString(ARG_CODIGO);
        fetchProductData(codigoEan);
        TipoMovimento tipoMov = (TipoMovimento) args.getSerializable(ARG_TIPO_MOV);

        if (tipoMov == null) {
            showErrorAndDismiss("Código ou tipo de movimento não informado.");
            return;
        }

        setupClickListeners(tipoMov);
        fetchProductData(codigoEan);
        setupFragmentResultListener(tipoMov);

        binding.etQuantidade.addTextChangedListener(new android.text.TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (isUpdating) return;

                String texto = s.toString().trim();

                if (texto.isEmpty()) {
                    // não altera a variável quantidade ainda, permite que o usuário digite
                    return;
                }

                try {
                    int valorDigitado = Integer.parseInt(texto);
                    if (valorDigitado < 1) {
                        quantidade = 1;
                    } else {
                        quantidade = valorDigitado;
                    }
                } catch (NumberFormatException e) {
                    quantidade = 1;
                }

                // atualiza os botões sem recursão
                isUpdating = true;
                binding.btnMenos.setEnabled(quantidade > 1);
                binding.btnMais.setEnabled(true); // sempre permite aumentar
                isUpdating = false;
            }
        });

    }

    private void fetchProductData(String codigoEan) {
        setLoadingState(true);
        produtoService.buscarProdutoPorEan(codigoEan, new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(ProdutoResponse result) {
                setLoadingState(false);
                if (result != null) {
                    produto = result;
                    atualizarViews();
                } else {
                    showErrorAndDismiss("Produto não encontrado.");
                }
            }

            @Override
            public void onError(String error) {
                setLoadingState(false);
                showErrorAndDismiss("Erro ao buscar produto: " + error);
            }
        });
    }


    private void setupClickListeners(TipoMovimento tipoMov) {
        binding.btnClosePopup.setOnClickListener(v -> dismiss());

        binding.btnMais.setOnClickListener(v -> {
            quantidade++;
            atualizarQuantidade();
        });

        binding.btnMenos.setOnClickListener(v -> {
            if (quantidade > 1) {
                quantidade--;
                atualizarQuantidade();
            }
        });

        binding.btnConfirmar.setOnClickListener(v -> {
            if (produto == null || produto.getQuantidade() == null) {

                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Dados do produto indisponíveis.");
                return;
            }

            Integer estoqueAntigo = produto.getQuantidade();
            Integer estoqueAtualizado;

            if (tipoMov == TipoMovimento.BAIXA) {
                estoqueAtualizado = estoqueAntigo - quantidade;
            } else { // ENTRADA
                estoqueAtualizado = estoqueAntigo + quantidade;
            }

            ConfirmarRegistroBottomSheetDialogFragment.show(
                    getParentFragmentManager(),
                    produto.getNome(),
                    estoqueAntigo,
                    estoqueAtualizado
            );
        });
    }

    private void setupFragmentResultListener(TipoMovimento tipoMov) {
        getParentFragmentManager().setFragmentResultListener(
                ConfirmarRegistroBottomSheetDialogFragment.REQUEST_KEY,
                this,
                (requestKey, result) -> {
                    try {
                        boolean confirmed = result.getBoolean(ConfirmarRegistroBottomSheetDialogFragment.RESULT_CONFIRMED, false);
                        if (!confirmed || produto == null) {
                            return;
                        }

                        ProdutoService.ProdutoCallback<Void> callback = new ProdutoService.ProdutoCallback<>() {
                            @Override
                            public void onSuccess(Void res) {
                                dismiss();
                                ConfirmacaoBottomSheetDialogFragment.showSucesso(getParentFragmentManager());
                            }

                            @Override
                            public void onError(String error) {
                                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), error);
                            }
                        };

                        if (tipoMov == TipoMovimento.BAIXA) {
                            produtoService.baixaEstoque(produto.getCodigoEan(), quantidade, callback);
                        } else { // ENTRADA
                            produtoService.entradaEstoque(produto.getCodigoEan(), quantidade, callback);
                        }
                    } catch (Exception e) {
                        String message = e.getMessage() != null ? e.getMessage() : "Falha ao processar a operação.";
                        showErrorAndDismiss(message);
                    }
                }
        );
    }


    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.contentLayout.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.contentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorAndDismiss(String message) {

        if (isAdded()) {
            ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), message);
        }
        dismiss();
    }

    private void atualizarViews() {
        if (produto == null) return;

        binding.tvNomeProduto.setText(produto.getNome());
        binding.tvCodigoProduto.setText(produto.getCodigoEan());
        binding.tvFornecedorProduto.setText(produto.getMarca());
        binding.tvEstoqueAtual.setText(String.valueOf(produto.getQuantidade()));

        atualizarQuantidade();
    }

    private void atualizarQuantidade() {
        binding.btnMenos.setEnabled(quantidade > 1);
        binding.btnMais.setEnabled(true);
        String atual = binding.etQuantidade.getText().toString();
        String nova = String.valueOf(quantidade);
        if (!atual.equals(nova)) {
            binding.etQuantidade.setText(nova);
        }
    }
}