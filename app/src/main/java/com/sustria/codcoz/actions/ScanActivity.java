package com.sustria.codcoz.actions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.databinding.ActivityScanBinding;

public class ScanActivity extends AppCompatActivity {

    private ActivityScanBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Configura o header da activity
        setupHeader();

//        Placeholder; integrar biblioteca de scanner posteriormente (ML Kit/ZXing)
    }

    private void setupHeader() {
        // Configura header
        binding.headerScan.headerActivityBackTitle.setText("Escanear Código");
        binding.headerScan.headerActivityBackArrow.setOnClickListener(v -> finish());
    }
}



