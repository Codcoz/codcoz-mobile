package com.sustria.codcoz.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.imageViewLogo);

        // Animação
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        logo.startAnimation(zoomIn);

        new Handler().postDelayed(this::abrirTelaPrincipal, 1800);
    }

    private void abrirTelaPrincipal() {
        Intent rota = new Intent(this, LoginActivity.class);

        ImageView logo = findViewById(R.id.imageViewLogo);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, logo, "logo_shared");
        startActivity(rota, options.toBundle());
        finish();
    }
}
