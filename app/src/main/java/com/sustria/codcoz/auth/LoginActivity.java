package com.sustria.codcoz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sustria.codcoz.MainActivity;
import com.sustria.codcoz.R;
import com.sustria.codcoz.api.model.EstoquistaResponse;
import com.sustria.codcoz.api.service.EstoquistaService;
import com.sustria.codcoz.databinding.ActivityLoginBinding;
import com.sustria.codcoz.actions.ErrorBottomSheet;
import com.sustria.codcoz.utils.UserDataManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EstoquistaService estoquistaService;
    private boolean isPasswordVisible = false;
    private boolean isProcessing = false;
    private boolean isEmailValidated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        estoquistaService = new EstoquistaService();

        binding.btnAvancar.setOnClickListener(v -> {
            if (isProcessing) return;

            String txtEmail = binding.editEmail.getText() != null
                    ? binding.editEmail.getText().toString().trim()
                    : "";

            if (txtEmail.isEmpty()) {
                binding.inputLayoutEmail.setError("Preencha o e-mail");
                return;
            } else if (!isValidEmail(txtEmail)) {
                binding.inputLayoutEmail.setError("E-mail inválido");
                return;
            } else {
                binding.inputLayoutEmail.setError(null);
            }

            setLoading(true);

            if (isPasswordVisible) {
                String txtSenha = binding.editSenha.getText() != null
                        ? binding.editSenha.getText().toString().trim()
                        : "";
                if (txtSenha.isEmpty()) {
                    binding.inputLayoutSenha.setError("Preencha a senha");
                    setLoading(false);
                    return;
                } else {
                    binding.inputLayoutSenha.setError(null);
                }
                loginUser(txtEmail, txtSenha);
            } else {
                checkEmailInFirebaseThenApi(txtEmail);
            }
        });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkEmailInFirebaseThenApi(String email) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean emailExistsInFirebase = task.getResult() != null &&
                        !task.getResult().getSignInMethods().isEmpty();

                if (emailExistsInFirebase) {
                    isPasswordVisible = true;
                    isEmailValidated = true;
                    lockEmailField();
                    binding.inputLayoutSenha.setVisibility(View.VISIBLE);
                    binding.tvEsqueceuSenha.setVisibility(View.VISIBLE);
                    binding.btnAvancar.setText(R.string.entrar);
                    setLoading(false);
                } else {
                    // E-mail não existe no Firebase, verifica na API para cadastrar a senha.
                    checkEmailInApiForRegistration(email);
                }
            } else {
                ErrorBottomSheet.showGenericError(LoginActivity.this);
                setLoading(false);
            }
        });
    }

    private void checkEmailInApiForRegistration(String email) {
        estoquistaService.buscarPorEmail(email, new EstoquistaService.EstoquistaCallback<>() {
            @Override
            public void onSuccess(EstoquistaResponse result) {
                if (result != null) {
                    // E-mail encontrado na API, leva para a tela de cadastro
                    Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("estoquistaData", result);
                    startActivity(intent);
                } else {
                    ErrorBottomSheet.show(LoginActivity.this, "E-mail não encontrado", "Verifique se o e-mail está correto");
                    setLoading(false);
                }
            }

            @Override
            public void onError(String error) {
                ErrorBottomSheet.show(LoginActivity.this, "E-mail não encontrado", "Verifique se o e-mail está correto");
                setLoading(false);
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, loginTask -> {
            if (loginTask.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    fetchUserDataFromFirestore(user.getUid());
                } else {
                    ErrorBottomSheet.showGenericError(LoginActivity.this);
                    setLoading(false);
                }
            } else {
                ErrorBottomSheet.showAuthError(LoginActivity.this);
                setLoading(false);
            }
        });
    }

    private void fetchUserDataFromFirestore(String uid) {
        db.collection("usuarios").whereEqualTo("uid", uid).limit(1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        EstoquistaResponse estoquista = queryDocumentSnapshots.getDocuments().get(0).toObject(EstoquistaResponse.class);

                        if (estoquista != null && "ATIVO".equals(estoquista.getStatus())) {
                            proceedToMainWithUser(estoquista);
                        } else {
                            ErrorBottomSheet.show(LoginActivity.this, "Usuário inativo", "Entre em contato com o administrador");
                            auth.signOut(); // Desloga o usuário se ele estiver inativo
                            setLoading(false);
                        }
                    } else {
                        ErrorBottomSheet.showGenericError(LoginActivity.this);
                        setLoading(false);
                    }
                })
                .addOnFailureListener(e -> {
                    ErrorBottomSheet.showGenericError(LoginActivity.this);
                    setLoading(false);
                });
    }

    private void proceedToMainWithUser(EstoquistaResponse user) {
        UserDataManager.getInstance().setUserData(user, LoginActivity.this);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        // Garante que o usuário não possa voltar para a tela de login
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void lockEmailField() {
        binding.editEmail.setEnabled(false);
        binding.editEmail.setAlpha(0.6f);
        binding.inputLayoutEmail.setHint("E-mail validado");

        binding.inputLayoutEmail.setBoxStrokeColor(getResources().getColor(R.color.custom_green_success, null));

        binding.tvEsqueceuSenha.setText("Esqueceu a senha?");
        binding.tvEsqueceuSenha.setVisibility(View.VISIBLE);
        binding.tvEsqueceuSenha.setOnClickListener(v -> {
            if (isProcessing) return;
            Intent intent = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
            startActivity(intent);
        });

        binding.editEmail.setOnClickListener(v -> {
            if (isEmailValidated) {
                resetEmailField();
            }
        });
    }

    private void resetEmailField() {
        isEmailValidated = false;
        isPasswordVisible = false;
        binding.editEmail.setEnabled(true);
        binding.editEmail.setAlpha(1f);
        binding.inputLayoutEmail.setHint("Digite seu e-mail");

        binding.inputLayoutEmail.setBoxStrokeColor(getResources().getColor(R.color.colorOnSurfaceVariant, null));

        binding.inputLayoutSenha.setVisibility(View.GONE);
        binding.tvEsqueceuSenha.setVisibility(View.GONE);
        binding.btnAvancar.setText("Avançar");
        binding.editEmail.requestFocus();
    }

    private void setLoading(boolean loading) {
        isProcessing = loading;
        binding.btnAvancar.setEnabled(!loading);

        if (!isEmailValidated) {
            binding.editEmail.setEnabled(!loading);
        }

        binding.editSenha.setEnabled(!loading);
        binding.btnAvancar.setAlpha(loading ? 0.6f : 1f);
    }
}