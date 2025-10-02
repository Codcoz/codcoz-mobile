package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityPerfilBinding;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita o modo Edge-to-Edge (tela inteira)
        EdgeToEdge.enable(this);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // da a cor para a parte que fica de status(onde fica a bateria, rede, etc...)
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupHeader();
        setupUi();
    }

    private void setupHeader() {
        binding.headerPerfil.headerActivityBackTitle.setText("Perfil");
        binding.headerPerfil.headerActivityBackArrow.setOnClickListener(v -> finish());
    }

    private void setupUi() {
        // Tabs Atividades/Auditorias
        binding.tabAtividades.setOnClickListener(v -> selectTab(true));
        binding.tabAuditorias.setOnClickListener(v -> selectTab(false));

        // Período
        View.OnClickListener periodoClick = v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(R.drawable.bg_tab_selected);
        };
        binding.op7dias.setOnClickListener(periodoClick);
        binding.op30dias.setOnClickListener(periodoClick);
        binding.op90dias.setOnClickListener(periodoClick);
    }

    private void selectTab(boolean atividades) {
        if (atividades) {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_unselected);
        } else {
            binding.tabAtividades.setBackgroundResource(R.drawable.bg_tab_unselected);
            binding.tabAuditorias.setBackgroundResource(R.drawable.bg_tab_selected);
        }
    }

    private void resetPeriodoButtons() {
        binding.op7dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op30dias.setBackgroundResource(R.drawable.bg_tab_unselected);
        binding.op90dias.setBackgroundResource(R.drawable.bg_tab_unselected);
    }
}