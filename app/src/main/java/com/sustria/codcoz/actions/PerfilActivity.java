package com.sustria.codcoz.actions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sustria.codcoz.R;
import com.sustria.codcoz.auth.LoginActivity;
import com.sustria.codcoz.databinding.ActivityPerfilBinding;
import com.sustria.codcoz.api.adapter.PerfilTarefaAdapter;
import com.sustria.codcoz.ui.perfil.PerfilViewModel;
import com.sustria.codcoz.utils.UserDataManager;
import com.sustria.codcoz.utils.EmptyStateAdapter;
import com.sustria.codcoz.utils.CloudinaryManager;
import com.sustria.codcoz.utils.ImagePickerManager;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private PerfilTarefaAdapter adapter;
    private EmptyStateAdapter emptyStateAdapter;
    private int periodoSelecionado = 7; // 7 dias por padrão
    private boolean isAtividadesSelecionado = true; // Atividades por padrão

    private ImagePickerManager imagePickerManager;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita o modo Edge-to-Edge (tela inteira)
        EdgeToEdge.enable(this);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Inicializa o Firebase e Cloudinary
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CloudinaryManager.init(this);

        setupHeader();
        setupUi();
        setupRecyclerView();
        setupViewModel();
        setupImagePicker();
        carregarDadosUsuario();

        loadTasks();
    }

    private void setupHeader() {
        binding.headerPerfil.headerActivityBackTitle.setText("Perfil");
        binding.headerPerfil.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupUi() {
        // Tabs Atividades/Auditorias
        binding.tabAtividades.setOnClickListener(v -> {
            selectTab(true);
            loadTasks();
        });
        binding.tabAuditorias.setOnClickListener(v -> {
            selectTab(false);
            loadTasks();
        });

        // Período
        binding.op7dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 7;
            loadTasks();
        });
        binding.op30dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 30;
            loadTasks();
        });
        binding.op90dias.setOnClickListener(v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
            periodoSelecionado = 90;
            loadTasks();
        });

        // Botão de logout
        binding.btnLogout.setOnClickListener(v -> confirmarLogout());
    }

    private void selectTab(boolean atividades) {
        isAtividadesSelecionado = atividades;
        if (atividades) {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_unselected);
        } else {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_unselected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_selected);
        }
    }

    private void resetPeriodoButtons() {
        binding.op7dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op30dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op90dias.setBackgroundResource(R.drawable.bg_tab_unselected);
    }

    private void carregarDadosUsuario() {
        UserDataManager userDataManager = UserDataManager.getInstance();

        if (userDataManager.isDataLoaded()) {
            // Dados já estão no cache, usar diretamente
            atualizarDadosPerfil();
        } else {
            // Dados não estão no cache, usar dados padrão
            binding.tvNome.setText("Usuário");
            binding.tvFuncao.setText("Estoquista");
            binding.tvDesde.setText("Desde --/--/----");
        }
    }

    private void atualizarDadosPerfil() {
        UserDataManager userDataManager = UserDataManager.getInstance();
        String nomeCompleto = userDataManager.getNomeCompleto();
        String dataFormatada = userDataManager.getDataContratacaoFormatada();

        binding.tvNome.setText(nomeCompleto);
        binding.tvFuncao.setText("Estoquista");
        binding.tvDesde.setText("Desde " + dataFormatada);

        // Carregar imagem de perfil
        loadProfileImage();
    }

    private void setupRecyclerView() {
        adapter = new PerfilTarefaAdapter();
        emptyStateAdapter = new EmptyStateAdapter(adapter);
        binding.recyclerViewHistoricoPerfil.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistoricoPerfil.setAdapter(emptyStateAdapter);
    }

    private void setupViewModel() {
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Observa a lista de tarefas
        perfilViewModel.getTarefas().observe(this, tarefas -> {
            if (tarefas != null) {
                adapter.setTarefas(tarefas);

                // Atualiza o estado vazio
                if (tarefas.isEmpty()) {
                    String emptyTitle = "Nenhuma atividade encontrada";
                    String emptyMessage = "Não há atividades registradas no período selecionado.\nTente alterar o período ou verifique novamente mais tarde.";

                    if (!isAtividadesSelecionado) {
                        emptyTitle = "Nenhuma auditoria encontrada";
                        emptyMessage = "Não há auditorias registradas no período selecionado.\nTente alterar o período ou verifique novamente mais tarde.";
                    }

                    emptyStateAdapter.setEmptyState(true, emptyTitle, emptyMessage);
                } else {
                    emptyStateAdapter.setEmptyState(false);
                }
            }
        });

        // Observa estado de carregamento
        perfilViewModel.getIsLoading().observe(this, isLoading -> {
            // Aqui você pode mostrar/ocultar um indicador de carregamento se necessário
        });

        // Observar mensagens de erro
        perfilViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {

            }
        });
    }

    private void loadTasks() {
        if (isAtividadesSelecionado) {
            perfilViewModel.loadAtividades(periodoSelecionado);
        } else {
            perfilViewModel.loadAuditorias(periodoSelecionado);
        }
    }

    private void setupImagePicker() {
        imagePickerManager = new ImagePickerManager(this, new ImagePickerManager.ImagePickerCallback() {
            @Override
            public void onImageSelected(Uri imageUri) {
                uploadImageToCloudinary(imageUri);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PerfilActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnEditProfile.setOnClickListener(v -> {
            if (imagePickerManager != null) {
                imagePickerManager.showImagePickerOptions();
            } else {
                Toast.makeText(this, "ImagePicker não inicializado", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.ivAvatar.setOnClickListener(v -> {
            showFullScreenImage();
        });
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog("Carregando a imagem...");
        String userId = auth.getCurrentUser().getUid();

        CloudinaryManager.uploadImage(this, imageUri, userId, new CloudinaryManager.UploadCallbackListener() {
            @Override
            public void onSuccess(String url) {
                updateUserProfileImage(url);
            }

            @Override
            public void onError(String error) {
                hideProgressDialog();
                Toast.makeText(PerfilActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfileImage(String imageUrl) {
        if (auth.getCurrentUser() == null) {
            hideProgressDialog();
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid()  ;

        db.collection("usuarios").document(userId)
                .update("imagemPerfil", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Atualizar cache local
                    UserDataManager userDataManager = UserDataManager.getInstance();
                    if (userDataManager.getUserData() != null) {
                        userDataManager.getUserData().setImagemPerfil(imageUrl);
                        userDataManager.setUserData(userDataManager.getUserData(), PerfilActivity.this);
                    }

                        loadProfileImage();
                    hideProgressDialog();
                    Toast.makeText(PerfilActivity.this, "Imagem atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    hideProgressDialog();
                    Toast.makeText(PerfilActivity.this, "Erro ao atualizar imagem", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadProfileImage() {
        String imageUrl = UserDataManager.getInstance().getImagemPerfil();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_imagem_perfil)
                    .error(R.drawable.ic_imagem_perfil)
                    .circleCrop()
                    .into(binding.ivProfileImage);
        }
    }
    
    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
    
    private void showFullScreenImage() {
        String imageUrl = UserDataManager.getInstance().getImagemPerfil();
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_fullscreen_image, null);
        
        ImageView fullScreenImageView = dialogView.findViewById(R.id.iv_fullscreen_image);
        
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_imagem_perfil)
                .error(R.drawable.ic_imagem_perfil)
                .into(fullScreenImageView);
        
        builder.setView(dialogView);
        builder.setCancelable(true);
        
        AlertDialog dialog = builder.create();
        
        // Configurar clique na imagem para fechar
        fullScreenImageView.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    private void confirmarLogout() {
        LogoutBottomSheet.show(this, this::realizarLogout);
    }

    private void realizarLogout() {
        // Deslogar do Firebase
        if (auth != null) {
            auth.signOut();
        }

        // Limpar cache do usuário
        UserDataManager.getInstance().clearCache(this);

        // Redirecionar para tela de login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}