package com.sustria.codcoz.auth;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.sustria.codcoz.databinding.ActivityEsqueceuSenhaBinding;
import com.sustria.codcoz.actions.ErrorBottomSheet;
import com.sustria.codcoz.utils.NotificationHelper;

public class EsqueceuSenhaActivity extends AppCompatActivity {
    private ActivityEsqueceuSenhaBinding binding;
    private FirebaseAuth auth;
    private boolean isProcessing = false;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEsqueceuSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        
        // Solicitar permissão de notificação se necessário (Android 13+)
        requestNotificationPermission();

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
                        
                        // Exibe notificação push informando que o email foi enviado
                        NotificationHelper.showPasswordResetNotification(EsqueceuSenhaActivity.this, email);
                        
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
    
    /**
     * Solicita permissão de notificação (necessário para Android 13+)
     */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
            } else {
                // Permissão negada - a notificação ainda pode ser exibida, mas pode não aparecer
                // dependendo das configurações do sistema
            }
        }
    }
}
