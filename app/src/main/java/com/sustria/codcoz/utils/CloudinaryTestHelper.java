package com.sustria.codcoz.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;

public class CloudinaryTestHelper {
    
    private static final String TAG = "CloudinaryTest";
    
    public static void testConnection(Context context) {
        try {
            MediaManager manager = MediaManager.get();
            if (manager != null) {
                Log.d(TAG, "Cloudinary conectado com sucesso");
            } else {
                Log.e(TAG, "Falha na conexão - MediaManager não disponível");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao testar conexão: " + e.getMessage());
        }
    }
    
    public static void testUpload(Context context, Uri imageUri) {
        CloudinaryManager.uploadImage(context, imageUri, "test_user", new CloudinaryManager.UploadCallbackListener() {
            @Override
            public void onSuccess(String url) {
                Log.d(TAG, "Upload de teste bem-sucedido: " + url);
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "Erro no upload de teste: " + error);
            }
        });
    }
}