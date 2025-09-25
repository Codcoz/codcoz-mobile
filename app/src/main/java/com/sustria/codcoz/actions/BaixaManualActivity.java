package com.sustria.codcoz.actions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.databinding.ActivityBaixaManualBinding;

public class BaixaManualActivity extends AppCompatActivity {

    private ActivityBaixaManualBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaixaManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.headerBaixaManual.headerActivityBackTitle.setText("Realizar Nova Baixa");
        binding.headerBaixaManual.headerActivityBackTitle.setOnClickListener(v -> finish());

        binding.btnAvancar.setOnClickListener(v -> {
            String codigo = binding.etCodigoProduto.getText() != null ? binding.etCodigoProduto.getText().toString().trim() : "";
            if (codigo.isEmpty()) {
                binding.etCodigoProduto.setError("Informe o código do produto");
            }
        });
    }
}




