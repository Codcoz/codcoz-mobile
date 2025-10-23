package com.sustria.codcoz.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class CloudinaryManager {
    
    private static final String TAG = "CloudinaryManager";
    private static final String CLOUD_NAME = "dixacuf51";
    private static final String API_KEY = "936697122518985";
    private static final String API_SECRET = "npPS-b35v6-Z1H8GE8JlANezMDs";
    
    public static void init(Context context) {
        try {
            Map<String, Object> config = new java.util.HashMap<>();
            config.put("cloud_name", CLOUD_NAME);
            config.put("api_key", API_KEY);
            config.put("api_secret", API_SECRET);
            
            MediaManager.init(context, config);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar Cloudinary: " + e.getMessage());
        }
    }
    
    public interface UploadCallbackListener {
        void onSuccess(String url);
        void onError(String error);
    }
    
    public static void uploadImage(Context context, Uri imageUri, String userId, UploadCallbackListener callback) {
        try {
            String publicId = "profile_" + userId + "_" + System.currentTimeMillis();
            
            MediaManager.get().upload(imageUri)
                    .option("public_id", publicId)
                    .option("folder", "user_profiles")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // Upload iniciado
                        }
                        
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // Progresso do upload
                        }
                        
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String url = (String) resultData.get("secure_url");
                            if (callback != null) {
                                callback.onSuccess(url);
                            }
                        }
                        
                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            if (callback != null) {
                                callback.onError("Erro no upload: " + error.getDescription());
                            }
                        }
                        
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // Reagendar upload se necessário
                        }
                    })
                    .dispatch();
        } catch (Exception e) {
            if (callback != null) {
                callback.onError("Erro ao iniciar upload: " + e.getMessage());
            }
        }
    }
}
