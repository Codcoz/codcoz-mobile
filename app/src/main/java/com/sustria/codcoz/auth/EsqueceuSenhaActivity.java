package com.sustria.codcoz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sustria.codcoz.databinding.ActivityEsqueceuSenhaBinding;
import com.sustria.codcoz.actions.ErrorBottomSheet;

public class EsqueceuSenhaActivity extends AppCompatActivity {
    private ActivityEsqueceuSenhaBinding binding;
    private FirebaseAuth auth;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEsqueceuSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnEnviarEmail.setOnClickListener(v -> {
            if (isProcessing) return;
            enviarEmailRedefinicao();
        });

        binding.tvVoltarLogin.setOnClickListener(v -> {
            if (isProcessing) return;
            voltarParaLogin();
        });
    }

    private void enviarEmailRedefinicao() {
        String email = binding.editEmail.getText() != null
                ? binding.editEmail.getText().toString().trim()
                : "";

        // Validações
        if (email.isEmpty()) {
            binding.inputLayoutEmail.setError("Preencha o e-mail");
            return;
        } else if (!isValidEmail(email)) {
            binding.inputLayoutEmail.setError("E-mail inválido");
            return;
        } else {
            binding.inputLayoutEmail.setError(null);
        }

        setLoading(true);
        
        // Envia o e-mail de redefinição de senha
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EsqueceuSenhaActivity.this, 
                                "E-mail de redefinição enviado! Verifique sua caixa de entrada.", 
                                Toast.LENGTH_LONG).show();
                        
                        // Volta para a tela de login após sucesso
                        voltarParaLogin();
                    } else {
                        if (task.getException() != null) {
                            String exceptionMessage = task.getException().getMessage();
                            if (exceptionMessage.contains("user-not-found")) {
                                ErrorBottomSheet.show(EsqueceuSenhaActivity.this, "E-mail não encontrado", "Verifique se o e-mail está correto");
                            } else if (exceptionMessage.contains("invalid-email")) {
                                ErrorBottomSheet.show(EsqueceuSenhaActivity.this, "E-mail inválido", "Digite um e-mail válido");
                            } else if (exceptionMessage.contains("too-many-requests")) {
                                ErrorBottomSheet.show(EsqueceuSenhaActivity.this, "Muitas tentativas", "Tente novamente mais tarde");
                            } else {
                                ErrorBottomSheet.showGenericError(EsqueceuSenhaActivity.this);
                            }
                        } else {
                            ErrorBottomSheet.showGenericError(EsqueceuSenhaActivity.this);
                        }
                        setLoading(false);
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void voltarParaLogin() {
        Intent intent = new Intent(EsqueceuSenhaActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean loading) {
        isProcessing = loading;
        binding.btnEnviarEmail.setEnabled(!loading);
        binding.editEmail.setEnabled(!loading);
        binding.tvVoltarLogin.setEnabled(!loading);
        binding.btnEnviarEmail.setAlpha(loading ? 0.6f : 1f);
    }
}
