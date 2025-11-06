package com.sustria.codcoz.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sustria.codcoz.MainActivity;
import com.sustria.codcoz.R;
import com.sustria.codcoz.auth.LoginActivity;

public class NotificationHelper {
    
    private static final String TAG = "NotificationHelper";
    private static final String CHANNEL_ID = "codcoz_notifications";
    private static final String CHANNEL_NAME = "Notificações CodCoz";
    private static final String CHANNEL_DESCRIPTION = "Notificações do aplicativo CodCoz";
    
    private static boolean channelCreated = false;
    
    /**
     * Cria o canal de notificação (necessário para Android 8.0+)
     */
    public static void createNotificationChannel(Context context) {
        if (channelCreated) {
            return;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.enableLights(true);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                channelCreated = true;
                Log.d(TAG, "Canal de notificação criado com sucesso");
            }
        } else {
            channelCreated = true;
        }
    }
    
    /**
     * Exibe uma notificação de reset de senha
     */
    public static void showPasswordResetNotification(Context context, String email) {
        createNotificationChannel(context);
        
        // Intent para abrir a tela de login quando o usuário tocar na notificação
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Criar a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Ícone de notificação
                .setContentTitle("E-mail de redefinição enviado")
                .setContentText("Verifique sua caixa de entrada: " + email)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Um e-mail de redefinição de senha foi enviado para " + email + ". Verifique sua caixa de entrada e spam."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 300, 200, 300});
        
        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            Log.d(TAG, "Notificação de reset de senha exibida");
        } catch (SecurityException e) {
            Log.e(TAG, "Erro ao exibir notificação: permissão não concedida", e);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao exibir notificação: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exibe uma notificação genérica
     */
    public static void showNotification(Context context, String title, String message) {
        createNotificationChannel(context);
        
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        
        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException e) {
            Log.e(TAG, "Erro ao exibir notificação: permissão não concedida", e);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao exibir notificação: " + e.getMessage(), e);
        }
    }
}

