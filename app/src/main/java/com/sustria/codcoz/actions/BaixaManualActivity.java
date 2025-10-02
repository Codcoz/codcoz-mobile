package com.sustria.codcoz.actions;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityBaixaManualBinding;

public class BaixaManualActivity extends AppCompatActivity {

    private ActivityBaixaManualBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaixaManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        setupHeader();

        binding.btnAvancar.setOnClickListener(v -> {
            String codigo = binding.etCodigoProduto.getText() != null ? binding.etCodigoProduto.getText().toString().trim() : "";
            if (codigo.isEmpty()) {
                binding.etCodigoProduto.setError("Informe o código do produto");
            }
        });
    }

    private void setupHeader() {
        binding.headerBaixaManual.headerActivityBackTitle.setText("Realizar Nova Baixa");
        binding.headerBaixaManual.headerActivityBackArrow.setOnClickListener(v -> finish());
    }
}




