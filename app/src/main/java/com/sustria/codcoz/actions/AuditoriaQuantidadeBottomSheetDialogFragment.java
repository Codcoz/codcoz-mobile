package com.sustria.codcoz.actions;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.ProdutoResponse;
import com.sustria.codcoz.api.service.ProdutoService;
import com.sustria.codcoz.databinding.BottomsheetAuditoriaQuantidadeBinding;
import com.sustria.codcoz.utils.UserDataManager;

import java.util.List;

public class AuditoriaQuantidadeBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_TITULO = "arg_titulo";
    private static final String ARG_PRODUTO = "arg_produto";
    private static final String ARG_TAREFA_ID = "arg_tarefa_id";

    private BottomsheetAuditoriaQuantidadeBinding binding;
    private Long tarefaId;
    private ProdutoService produtoService;
    private Integer estoqueAntigo = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetAuditoriaQuantidadeBinding.inflate(inflater, container, false);
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
            
            // Configurar largura total para tablets através do BottomSheetBehavior
            View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                Configuration configuration = getResources().getConfiguration();
                int screenWidthDp = configuration.screenWidthDp;
                
                // Se for tablet (largura >= 600dp), remover limitação de largura
                if (screenWidthDp >= 600) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int screenWidth = displayMetrics.widthPixels;
                    behavior.setMaxWidth(screenWidth);
                    
                    // Também configurar no Window
                    WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
                    params.width = screenWidth;
                    getDialog().getWindow().setAttributes(params);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        produtoService = new ProdutoService();

        Bundle args = getArguments();
        String titulo = args != null ? args.getString(ARG_TITULO) : "Auditoria";
        String produto = args != null ? args.getString(ARG_PRODUTO) : null;
        tarefaId = args != null && args.containsKey(ARG_TAREFA_ID) ? args.getLong(ARG_TAREFA_ID, -1) : null;
        if (tarefaId != null && tarefaId <= 0) {
            tarefaId = null;
        }

        binding.txtTitulo.setText(titulo);
        if (produto != null) {
            binding.txtProduto.setText(produto);
            // Buscar o produto para obter o estoque antigo
            buscarProdutoPorNome(produto);
        }

        EditText edt = binding.inputQuantidade;
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        binding.btnCancelar.setOnClickListener(v -> dismiss());
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

        binding.btnConfirmar.setOnClickListener(v -> {
            // Após informar a quantidade, abre o bottomsheet de confirmação
            String nome = produto != null ? produto : titulo;
            Integer estoqueAtualizado = null;
            try {
                String qtdStr = edt.getText() != null ? edt.getText().toString().trim() : null;
                if (qtdStr != null && !qtdStr.isEmpty()) {
                    estoqueAtualizado = Integer.parseInt(qtdStr);
                }
            } catch (Exception ignored) { }

            ConfirmarRegistroBottomSheetDialogFragment.show(
                    getParentFragmentManager(),
                    nome,
                    estoqueAntigo, // Usar estoque antigo obtido da busca
                    estoqueAtualizado,
                    null, // codigoEan - não disponível na auditoria
                    null, // quantidade - não disponível na auditoria
                    null, // tipoMovimento - não disponível na auditoria
                    null, // idProduto - não disponível na auditoria
                    tarefaId // tarefaId para finalizar após auditoria
            );
        });
    }
    
    public static void show(@NonNull FragmentManager manager, @Nullable String titulo, @Nullable String produto, @Nullable Long tarefaId) {
        show(manager, titulo, produto, tarefaId, "AuditoriaQuantidadeBottomSheetDialog");
    }

    public static void show(@NonNull FragmentManager manager, @Nullable String titulo, @Nullable String produto, @Nullable Long tarefaId, @Nullable String tag) {
        // Fechar qualquer bottom sheet existente antes de abrir um novo
        dismissExistingBottomSheets(manager);
        
        AuditoriaQuantidadeBottomSheetDialogFragment fragment = new AuditoriaQuantidadeBottomSheetDialogFragment();
        Bundle args = new Bundle();
        if (titulo != null) args.putString(ARG_TITULO, titulo);
        if (produto != null) args.putString(ARG_PRODUTO, produto);
        if (tarefaId != null && tarefaId > 0) args.putLong(ARG_TAREFA_ID, tarefaId);
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

    private void buscarProdutoPorNome(String nomeProduto) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            return;
        }

        Long idEmpresa = Long.valueOf(UserDataManager.getInstance().getEmpresaId());
        produtoService.listarEstoque(idEmpresa, new ProdutoService.ProdutoCallback<>() {
            @Override
            public void onSuccess(List<ProdutoResponse> produtos) {
                if (produtos != null) {
                    // Procurar produto pelo nome (case-insensitive)
                    for (ProdutoResponse p : produtos) {
                        if (p.getNome() != null && p.getNome().equalsIgnoreCase(nomeProduto.trim())) {
                            estoqueAntigo = p.getQuantidade();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.d("Auditoria", "Erro ao buscar produto: " + error);
            }
        });
    }
}


