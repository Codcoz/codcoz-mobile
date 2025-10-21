package com.sustria.codcoz.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePickerManager {
    
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    public interface ImagePickerCallback {
        void onImageSelected(Uri imageUri);
        void onError(String error);
    }
    
    private AppCompatActivity activity;
    private ImagePickerCallback callback;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    
    public ImagePickerManager(AppCompatActivity activity, ImagePickerCallback callback) {
        this.activity = activity;
        this.callback = callback;
        setupLaunchers();
    }
    
    private void setupLaunchers() {
        galleryLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null && callback != null) {
                            callback.onImageSelected(selectedImage);
                        }
                    }
                }
        );
        
        cameraLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri capturedImage = result.getData().getData();
                        if (capturedImage != null && callback != null) {
                            callback.onImageSelected(capturedImage);
                        }
                    }
                }
        );
    }
    
    public void showImagePickerOptions() {
        String[] options = {"Câmera", "Galeria"};
        
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle("Selecionar Imagem")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openCamera();
                            break;
                        case 1:
                            openGallery();
                            break;
                    }
                })
                .show();
    }
    
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, 
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            return;
        }
        
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            if (callback != null) {
                callback.onError("Câmera não disponível");
            }
        }
    }
    
    private void openGallery() {
        String permission = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU 
                ? Manifest.permission.READ_MEDIA_IMAGES 
                : Manifest.permission.READ_EXTERNAL_STORAGE;
                
        if (ContextCompat.checkSelfPermission(activity, permission) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, 
                    new String[]{permission}, PERMISSION_REQUEST_CODE);
            return;
        }
        
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }
    
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, mas não sabemos se era para câmera ou galeria
                // O usuário terá que tentar novamente
            } else {
                if (callback != null) {
                    callback.onError("Permissão necessária para acessar atividades de mídia");
                }
            }
        }
    }
}
