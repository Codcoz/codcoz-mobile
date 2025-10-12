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
import com.sustria.codcoz.api.EstoquistaApi;
import com.sustria.codcoz.api.RetrofitClient;
import com.sustria.codcoz.api.model.EstoquistaResponse;
import com.sustria.codcoz.databinding.ActivityLoginBinding;
import com.sustria.codcoz.utils.UserDataManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EstoquistaApi estoquistaApi;
    private boolean isPasswordVisible = false;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        estoquistaApi = RetrofitClient.getInstance().create(EstoquistaApi.class);

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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkEmailInFirebaseThenApi(String email) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean emailExistsInFirebase = task.getResult() != null &&
                        !task.getResult().getSignInMethods().isEmpty();

                if (emailExistsInFirebase) {
                    isPasswordVisible = true;
                    binding.inputLayoutSenha.setVisibility(View.VISIBLE);
                    binding.tvEsqueceuSenha.setVisibility(View.VISIBLE);
                    binding.btnAvancar.setText(R.string.entrar);
                    setLoading(false);
                } else {
                    // E-mail não existe no Firebase, verifica na API para cadastrar a senha.
                    checkEmailInApiForRegistration(email);
                }
            } else {
                binding.inputLayoutEmail.setError("Erro ao verificar e-mail. Tente novamente");
                setLoading(false);
            }
        });
    }

    private void checkEmailInApiForRegistration(String email) {
        estoquistaApi.buscarPorEmail(email).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EstoquistaResponse> call, Response<EstoquistaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EstoquistaResponse estoquista = response.body();

                    if (!"ATIVO".equals(estoquista.getStatus())) {
                        Toast.makeText(LoginActivity.this, "Usuário inativo. Entre em contato com o gestor", Toast.LENGTH_LONG).show();
                        setLoading(false);
                        return;
                    }

                    // Leva para a tela de cadastro
                    Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("estoquistaData", estoquista);
                    startActivity(intent);
                    setLoading(false);

                } else {
                    binding.inputLayoutEmail.setError("E-mail não encontrado no sistema");
                    setLoading(false);
                }
            }

            @Override
            public void onFailure(Call<EstoquistaResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de conexão. Verifique sua internet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "Erro ao obter usuário. Tente novamente", Toast.LENGTH_SHORT).show();
                    setLoading(false);
                }
            } else {
                binding.inputLayoutSenha.setError("E-mail ou senha incorretos");
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
                            Toast.makeText(LoginActivity.this, "Usuário inativo ou com dados inválidos.", Toast.LENGTH_LONG).show();
                            auth.signOut(); // Desloga o usuário se ele estiver inativo
                            setLoading(false);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show();
                        setLoading(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Erro ao carregar dados do usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setLoading(false);
                });
    }

    private void proceedToMainWithUser(EstoquistaResponse user) {
        UserDataManager.getInstance().setUserData(user);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        // Garante que o usuário não possa voltar para a tela de login
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean loading) {
        isProcessing = loading;
        binding.btnAvancar.setEnabled(!loading);
        binding.editEmail.setEnabled(!loading);
        binding.editSenha.setEnabled(!loading);
        binding.btnAvancar.setAlpha(loading ? 0.6f : 1f);
    }
}