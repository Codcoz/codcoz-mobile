package com.sustria.codcoz.actions;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.databinding.ActivityPerfilBinding;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHeader();
        setupUi();
    }

    private void setupHeader() {
        binding.headerPerfil.headerActivityBackTitle.setText("Perfil");
        binding.headerPerfil.headerActivityBackTitle.setOnClickListener(v -> finish());
    }

    private void setupUi() {
        // Tabs Atividades/Auditorias
        binding.tabAtividades.setOnClickListener(v -> selectTab(true));
        binding.tabAuditorias.setOnClickListener(v -> selectTab(false));

        // Período
        View.OnClickListener periodoClick = v -> {
            resetPeriodoButtons();
            v.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_selected);
        };
        binding.op7dias.setOnClickListener(periodoClick);
        binding.op30dias.setOnClickListener(periodoClick);
        binding.op90dias.setOnClickListener(periodoClick);
    }

    private void selectTab(boolean atividades) {
        if (atividades) {
            binding.tabAtividades.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_selected);
            binding.tabAuditorias.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_unselected);
        } else {
            binding.tabAtividades.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_unselected);
            binding.tabAuditorias.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_selected);
        }
    }

    private void resetPeriodoButtons() {
        binding.op7dias.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_unselected);
        binding.op30dias.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_unselected);
        binding.op90dias.setBackgroundResource(com.sustria.codcoz.R.drawable.bg_tab_unselected);
    }
}