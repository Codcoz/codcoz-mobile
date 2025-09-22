package com.sustria.codcoz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sustria.codcoz.MainActivity;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        // Botão avançar
        binding.btnAvancar.setOnClickListener(v -> {
            String txtEmail = binding.editEmail.getText() != null
                    ? binding.editEmail.getText().toString().trim()
                    : "";

            // Valida o e-mail
            if (txtEmail.isEmpty()) {
                binding.inputLayoutEmail.setError("Preencha o e-mail");
                return;
            } else if (!isValidEmail(txtEmail)) {
                binding.inputLayoutEmail.setError("E-mail inválido");
                return;
            } else {
                binding.inputLayoutEmail.setError(null);
            }

            // Tenta fazer login
            if (isPasswordVisible) {
                String txtSenha = binding.editSenha.getText() != null
                        ? binding.editSenha.getText().toString().trim()
                        : "";
                if (txtSenha.isEmpty()) {
                    binding.inputLayoutSenha.setError("Preencha a senha");
                    return;
                } else {
                    binding.inputLayoutSenha.setError(null);
                }

                loginUser(txtEmail, txtSenha);
            } else {
                // Verifica se o e-mail existe
                checkEmailExists(txtEmail);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkEmailExists(String email) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean usuarioExiste = task.getResult() != null &&
                        !task.getResult().getSignInMethods().isEmpty();
                System.out.println("Usuário existe? " + usuarioExiste + " | E-mail: " + email);

                if (usuarioExiste) {
                    // E-mail existe, mostra o campo de senha
                    isPasswordVisible = true;
                    binding.inputLayoutSenha.setVisibility(View.VISIBLE);
                    binding.tvEsqueceuSenha.setVisibility(View.VISIBLE);
                    binding.btnAvancar.setText(R.string.entrar);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Erro ao verificar o e-mail: " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this,
                        "Erro ao verificar o e-mail: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this,
                    "Falha na conexão com o Firebase: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, loginTask -> {
            if (loginTask.isSuccessful()) {
                FirebaseUser userLogin = auth.getCurrentUser();
                if (userLogin != null) {
                    String uid = userLogin.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("empresas").get().addOnSuccessListener(empresasSnapshot -> {
                        if (empresasSnapshot.isEmpty()) {
                            Toast.makeText(LoginActivity.this,
                                    "Nenhuma empresa encontrada no sistema",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final int totalEmpresas = empresasSnapshot.size();
                        final int[] empresasVerificadas = {0};
                        final boolean[] usuarioEncontrado = {false};

                        for (var empresaDoc : empresasSnapshot.getDocuments()) {
                            String empresaId = empresaDoc.getId();

                            db.collection("empresas").document(empresaId)
                                    .collection("usuarios").document(uid)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        empresasVerificadas[0]++;

                                        if (documentSnapshot.exists() && !usuarioEncontrado[0]) {
                                            usuarioEncontrado[0] = true;
                                            Intent intent;
                                            intent = new Intent(LoginActivity.this, MainActivity.class);
                                            Toast.makeText(LoginActivity.this,
                                                    "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                                            startActivity(intent);
                                            finish();
                                        }

                                        // Se terminou de verificar todas as empresas e não achou
                                        if (empresasVerificadas[0] == totalEmpresas && !usuarioEncontrado[0]) {
                                            Toast.makeText(LoginActivity.this,
                                                    "Usuário não encontrado em nenhuma empresa",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        empresasVerificadas[0]++;
                                        if (empresasVerificadas[0] == totalEmpresas && !usuarioEncontrado[0]) {
                                            Toast.makeText(LoginActivity.this,
                                                    "Erro ao verificar usuários: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this,
                                "Erro ao buscar empresas: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                binding.inputLayoutSenha.setError("E-mail ou senha incorretos");
            }
        });
    }
}