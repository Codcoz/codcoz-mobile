package com.sustria.codcoz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityCadastroBinding;
import com.sustria.codcoz.databinding.ActivityLoginBinding;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    protected FirebaseAuth auth;
    protected FirebaseFirestore db;
    protected String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // pega o email enviado da tela de Login
        emailUsuario = getIntent().getStringExtra("email");

        binding.btnCadastrar.setOnClickListener(v -> {
            // sei lá ainda
        });

//        Aqui virá o cadastro da senha ou atualizacao da senha do usuario já cadastrado
    }
}
