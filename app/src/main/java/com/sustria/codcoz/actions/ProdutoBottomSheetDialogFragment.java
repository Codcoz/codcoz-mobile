package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sustria.codcoz.api.service.HistoricoService;
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
    private static final String ARG_TAREFA_ID = "arg_tarefa_id";
    private static final String ARG_INGREDIENTE_ESPERADO = "arg_ingrediente_esperado";

    private ProdutoService produtoService;
    private HistoricoService historicoService;
    private ProdutoResponse produto;
    private Integer quantidade = 1;
    private boolean isUpdatingQuantidade = false;
    private Long tarefaId;
    private String ingredienteEsperado;

    public static void show(@NonNull FragmentManager fm, @NonNull String codigo, @NonNull TipoMovimento tipoMov) {
        show(fm, codigo, tipoMov, null);
    }

    public static void show(@NonNull FragmentManager fm, @NonNull String codigo, @NonNull TipoMovimento tipoMov, @Nullable Long tarefaId) {
        show(fm, codigo, tipoMov, tarefaId, null);
    }

    public static void show(@NonNull FragmentManager fm, @NonNull String codigo, @NonNull TipoMovimento tipoMov, @Nullable Long tarefaId, @Nullable String ingredienteEsperado) {
        dismissExistingBottomSheets(fm);

        ProdutoBottomSheetDialogFragment fragment = new ProdutoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CODIGO, codigo);
        args.putSerializable(ARG_TIPO_MOV, tipoMov);
        if (tarefaId != null && tarefaId > 0) {
            args.putLong(ARG_TAREFA_ID, tarefaId);
        }
        if (ingredienteEsperado != null) {
            args.putString(ARG_INGREDIENTE_ESPERADO, ingredienteEsperado);
        }
        fragment.setArguments(args);
        fragment.show(fm, "ProdutoBottomSheetDialogFragment");
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
        historicoService = new HistoricoService();
        Bundle args = getArguments();
        if (args == null) {
            showErrorAndDismiss("Argumentos inválidos.");
            return;
        }

        String codigoEan = args.getString(ARG_CODIGO);
        TipoMovimento tipoMov = (TipoMovimento) args.getSerializable(ARG_TIPO_MOV);
        tarefaId = args.containsKey(ARG_TAREFA_ID) ? args.getLong(ARG_TAREFA_ID, -1) : null;
        if (tarefaId != null && tarefaId <= 0) {
            tarefaId = null;
        }
        ingredienteEsperado = args.getString(ARG_INGREDIENTE_ESPERADO);

        if (tipoMov == null) {
            showErrorAndDismiss("Código ou tipo de movimento não informado.");
            return;
        }

        setupClickListeners(tipoMov);
        
        // Só buscar produto se o código não estiver vazio
        // Se estiver vazio, aguardar o usuário digitar e clicar em buscar/confirmar
        if (codigoEan != null && !codigoEan.trim().isEmpty()) {
            fetchProductData(codigoEan, tipoMov);
        } else {
            // Modo manual: mostrar campos vazios e aguardar usuário preencher
            setLoadingState(false);
            // Mostrar mensagem ou deixar em branco para entrada manual
            binding.tvNomeProduto.setText("Digite o código do produto");
            binding.tvCodigoProduto.setText("");
            binding.tvFornecedorProduto.setText("");
            binding.tvEstoqueAtual.setText("--");
        }

        binding.etQuantidade.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating || isUpdatingQuantidade) return;

                String texto = s.toString().trim();

                if (texto.isEmpty()) {
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

                isUpdating = true;
                binding.btnMenos.setEnabled(quantidade > 1);
                binding.btnMais.setEnabled(true);
                isUpdating = false;
            }
        });


    }

    private void fetchProductData(String codigoEan, TipoMovimento tipoMov) {
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
                // Se o código foi escaneado mas produto não encontrado
                if (error.contains("não encontrado") || error.contains("404")) {
                    showErrorAndDismiss("Produto não encontrado no sistema.\nVerifique se o código está correto.");
                } else {
                    showErrorAndDismiss("Erro ao buscar produto: " + error);
                }
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
            // Se produto ainda não foi carregado, tentar buscar pelo código (modo manual)
            if (produto == null) {
                Bundle args = getArguments();
                String codigoEan = args != null ? args.getString(ARG_CODIGO) : null;
                if (codigoEan != null && !codigoEan.trim().isEmpty()) {
                    // Buscar produto primeiro
                    fetchProductData(codigoEan, tipoMov);
                    return;
                } else {
                    ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Digite o código do produto primeiro.");
                    return;
                }
            }

            if (produto.getQuantidade() == null) {
                ConfirmacaoBottomSheetDialogFragment.showErro(getParentFragmentManager(), "Dados do produto indisponíveis.");
                return;
            }

            // Validar se é o produto correto (quando há tarefaId e ingrediente esperado)
            if (tarefaId != null && ingredienteEsperado != null && !ingredienteEsperado.trim().isEmpty()) {
                String nomeProduto = produto.getNome();
                if (nomeProduto == null || !nomeProduto.equalsIgnoreCase(ingredienteEsperado.trim())) {
                    ConfirmacaoBottomSheetDialogFragment.showErro(
                            getParentFragmentManager(),
                            "Produto incorreto! Esperado: " + ingredienteEsperado + "\nEncontrado: " + (nomeProduto != null ? nomeProduto : "N/A")
                    );
                    return;
                }
            }

            Integer estoqueAntigo = produto.getQuantidade();
            Integer estoqueAtualizado;

            if (tipoMov == TipoMovimento.BAIXA) {
                estoqueAtualizado = estoqueAntigo - quantidade;
            } else { // ENTRADA
                estoqueAtualizado = estoqueAntigo + quantidade;
            }

            // Passar ingrediente esperado para validação final
            ConfirmarRegistroBottomSheetDialogFragment.show(
                    getParentFragmentManager(),
                    produto.getNome(),
                    estoqueAntigo,
                    estoqueAtualizado,
                    produto.getCodigoEan(),
                    quantidade,
                    tipoMov,
                    produto.getId() != null ? produto.getId().toString() : null,
                    tarefaId,
                    ingredienteEsperado
            );
        });
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
            isUpdatingQuantidade = true;
            binding.etQuantidade.setText(nova);
            isUpdatingQuantidade = false;
        }
    }
}