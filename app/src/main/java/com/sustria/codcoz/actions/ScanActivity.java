package com.sustria.codcoz.actions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sustria.codcoz.databinding.ActivityScanBinding;

public class ScanActivity extends AppCompatActivity implements BarcodeCallback {
    private static final int CAMERA_REQUEST_CODE = 1888;
    private ActivityScanBinding binding;
    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        setupScanner();
        solicitarPermissaoCamera();
    }

    private void setupScanner() {
        barcodeView = binding.barcodeScanner;
        barcodeView.decodeContinuous(this);
        barcodeView.setStatusText("");
    }

    private void setupHeader() {
        binding.headerScan.headerActivityBackTitle.setText("Realizar Nova Baixa");
        binding.headerScan.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    public void solicitarPermissaoCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permissão da câmera necessária para escanear códigos", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (result != null && result.getText() != null) {
            String scannedCode = result.getText();
            Toast.makeText(this, "Código escaneado: " + scannedCode, Toast.LENGTH_LONG).show();

            // Processa o código escaneado
            processScannedCode(scannedCode);
        }
    }

    private void processScannedCode(String code) {
        // Pausa o scanner antes de finalizar para evitar scans múltiplos
        barcodeView.pause();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("scanned_code", code);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

}