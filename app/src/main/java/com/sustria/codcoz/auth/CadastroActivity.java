package com.sustria.codcoz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sustria.codcoz.MainActivity;
import com.sustria.codcoz.api.model.EstoquistaResponse;
import com.sustria.codcoz.databinding.ActivityCadastroBinding;
import com.sustria.codcoz.utils.UserDataManager;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    protected FirebaseAuth auth;
    protected FirebaseFirestore db;

    private EstoquistaResponse estoquistaData;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Use getParcelableExtra por causa do Serializable
        estoquistaData = (EstoquistaResponse) getIntent().getSerializableExtra("estoquistaData");

        // Se os dados não chegaram, não podemos continuar
        if (estoquistaData == null || estoquistaData.getEmail() == null) {
            Toast.makeText(this, "Erro: Dados do usuário não recebidos.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        binding.btnCadastrar.setOnClickListener(v -> {
            if (isProcessing) return;
            cadastrarUsuario();
        });
    }

    private void cadastrarUsuario() {
        String senha = binding.editSenha.getText() != null
                ? binding.editSenha.getText().toString().trim()
                : "";
        String confirmarSenha = binding.editConfirmarSenha.getText() != null
                ? binding.editConfirmarSenha.getText().toString().trim()
                : "";

        // Validações
        if (senha.isEmpty()) {
            binding.inputLayoutSenha.setError("Preencha a senha");
            return;
        } else if (senha.length() < 8) {
            binding.inputLayoutSenha.setError("A senha deve ter no mínimo 8 caracteres");
            return;
        } else if (!senha.equals(confirmarSenha)) {
            binding.inputLayoutConfirmarSenha.setError("As senhas não coincidem");
            return;
        } else {
            binding.inputLayoutSenha.setError(null);
            binding.inputLayoutConfirmarSenha.setError(null);
        }

        setLoading(true);
        criarUsuarioFirebase(senha);
    }

    private void criarUsuarioFirebase(String senha) {
        // Usa o e-mail que já temos do objeto estoquistaData
        auth.createUserWithEmailAndPassword(estoquistaData.getEmail(), senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            salvarUsuarioFirestore(user.getUid(), estoquistaData);
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this,
                                "Erro ao criar conta: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        setLoading(false);
                    }
                });
    }

    private void salvarUsuarioFirestore(String uid, EstoquistaResponse estoquista) {
        if (estoquista.getEmpresaId() == null) {
            Toast.makeText(this, "Empresa não identificada", Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        }

        Map<String, Object> usuario = new HashMap<>();
        usuario.put("email", estoquista.getEmail());
        usuario.put("nome", estoquista.getNome());
        usuario.put("sobrenome", estoquista.getSobrenome());
        usuario.put("dataContratacao", estoquista.getDataContratacao());
        usuario.put("status", estoquista.getStatus());
        usuario.put("empresaId", estoquista.getEmpresaId());

        // Vi que é boa prática colocar o uid do firebase no documento do usuário
        usuario.put("uid", uid);

        db.collection("usuarios").document(uid)
                .set(usuario)
                .addOnSuccessListener(aVoid -> {
                    UserDataManager.getInstance().setUserData(estoquista, CadastroActivity.this);
                    Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CadastroActivity.this, "Erro ao salvar dados do usuário", Toast.LENGTH_SHORT).show();
                    setLoading(false);
                });
    }

    private void setLoading(boolean loading) {
        isProcessing = loading;
        binding.btnCadastrar.setEnabled(!loading);
        if (binding.editSenha != null) {
            binding.editSenha.setEnabled(!loading);
        }
        if (binding.editConfirmarSenha != null) {
            binding.editConfirmarSenha.setEnabled(!loading);
        }
        binding.btnCadastrar.setAlpha(loading ? 0.6f : 1f);
    }
}