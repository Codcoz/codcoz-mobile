package com.sustria.codcoz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.sustria.codcoz.actions.BaixaManualActivity;
import com.sustria.codcoz.actions.ScanActivity;
import com.sustria.codcoz.databinding.ActivityDetalhesReceitaBinding;
import com.sustria.codcoz.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ActivityMainBinding binding;
    private NavController navController;
    private boolean isPopupVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Esconder a ActionBar padrão
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        setupNavigation();
        setupPopup();

        binding.btnAdd.setOnClickListener(v -> {
            if (!isPopupVisible) {
                showPopup();
            } else {
                hidePopup();
            }
        });
    }

    private void setupNavigation() {
        try {
            // Pega o NavHostFragment
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment_activity_home);

            if (navHostFragment != null) {
                Log.d(TAG, "NavHostFragment encontrado");

                // Pega o NavController do NavHostFragment
                navController = navHostFragment.getNavController();

                if (navController != null) {
                    Log.d(TAG, "NavController obtido com sucesso");

                    binding.navView.setOnItemSelectedListener(null);
                    NavigationUI.setupWithNavController(binding.navView, navController);

                    Log.d(TAG, "Setup de navegação concluída com sucesso");
                } else {
                    Log.e(TAG, "NavController é null");
                }
            } else {
                Log.e(TAG, "NavHostFragment não encontrado");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar a navegação", e);
        }
    }


    private void setupPopup() {
        // Overlay para fechar ao clicar fora
        binding.overlay.setOnClickListener(v -> hidePopup());

        // Tabs
        binding.tabEntrada.setOnClickListener(v -> selectTab(true));
        binding.tabBaixa.setOnClickListener(v -> selectTab(false));

        // Conteúdo - Entrada
        binding.optionEscanearProdutoEntrada.setOnClickListener(v -> {
            hidePopup();
            startActivity(new Intent(this, ScanActivity.class));
        });

        binding.optionEntradaManual.setOnClickListener(v -> {
            hidePopup();
            startActivity(new Intent(this, BaixaManualActivity.class));
        });

        // Conteúdo - Baixa
        binding.optionEscanearProduto.setOnClickListener(v -> {
            hidePopup();
            startActivity(new Intent(this, ScanActivity.class));
        });
        binding.optionBaixaManual.setOnClickListener(v -> {
            hidePopup();
            startActivity(new Intent(this, BaixaManualActivity.class));
        });

        // Estado inicial
        selectTab(true);
    }

    private void selectTab(boolean entrada) {
        if (entrada) {
            binding.tabEntrada.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabBaixa.setBackgroundResource(R.drawable.bg_tab_unselected);
            binding.contentEntrada.setVisibility(View.VISIBLE);
            binding.contentBaixa.setVisibility(View.GONE);
        } else {
            binding.tabEntrada.setBackgroundResource(R.drawable.bg_tab_unselected);
            binding.tabBaixa.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.contentEntrada.setVisibility(View.GONE);
            binding.contentBaixa.setVisibility(View.VISIBLE);
        }
    }

    private void showPopup() {
        isPopupVisible = true;

        // overlay com fade in
        binding.overlay.setVisibility(View.VISIBLE);
        binding.overlay.setAlpha(0f);
        binding.overlay.animate().alpha(1f).setDuration(200).start();

        // Mostrar popup com slide up
        binding.popupCard.setVisibility(View.VISIBLE);
        binding.popupCard.setTranslationY(300f);
        binding.popupCard.animate()
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    private void hidePopup() {
        isPopupVisible = false;

        // Esconde
        binding.overlay.animate().alpha(0f).setDuration(200).withEndAction(() -> {
            binding.overlay.setVisibility(View.GONE);
        }).start();

        // Esconde popup com slide down
        binding.popupCard.animate()
                .translationY(300f)
                .setDuration(300)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .withEndAction(() -> {
                    binding.popupCard.setVisibility(View.GONE);
                }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}