package com.sustria.codcoz.actions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sustria.codcoz.R;
import com.sustria.codcoz.databinding.ActivityPowerBiBinding;

public class PowerBIActivity extends AppCompatActivity {

    private ActivityPowerBiBinding binding;
    private static final String POWER_BI_EMBED_URL = "https://app.powerbi.com/view?r=eyJrIjoiMTMzYzdjYTgtZDY5Ni00Yjk4LTgyZjUtN2QwOWI1MDQwOWNiIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9";
    private static final String POWER_BI_HTML_WRAPPER = 
        "<!DOCTYPE html>" +
        "<html>" +
        "<head>" +
        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=yes\">" +
        "<style>" +
        "body { margin: 0; padding: 8px; background-color: #f5f5f5; overflow: auto; }" +
        ".container { position: relative; padding-bottom: 250%; height: 0; overflow: hidden; width: 100%; max-width: 100%; margin: auto; }" +
        "iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: none; }" +
        "</style>" +
        "</head>" +
        "<body>" +
        "<div class=\"container\">" +
        "<iframe title=\"CodCoz - Mobile\" src=\"" + POWER_BI_EMBED_URL + "\" frameborder=\"0\" allowfullscreen=\"true\"></iframe>" +
        "</div>" +
        "</body>" +
        "</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita o modo Edge-to-Edge (tela inteira)
        EdgeToEdge.enable(this);
        binding = ActivityPowerBiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Define a cor da status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setupHeader();
        setupWebView();
    }

    private void setupHeader() {
        binding.headerPowerBi.headerActivityBackTitle.setText("Relatório");
        binding.headerPowerBi.headerActivityBackArrow.setOnClickListener(v -> finish());
        // Esconder o ícone de relatórios já que estamos na tela de relatórios
        binding.headerPowerBi.headerActivityReportsIcon.setVisibility(View.GONE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebView webView = binding.webviewPowerBi;
        WebSettings webSettings = webView.getSettings();
        
        // Habilita JavaScript (necessário para Power BI)
        webSettings.setJavaScriptEnabled(true);
        
        // Outras configurações importantes
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        
        // Configura WebViewClient para carregar páginas dentro do WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Carrega o HTML wrapper com o iframe responsivo
        webView.loadDataWithBaseURL("https://app.powerbi.com", POWER_BI_HTML_WRAPPER, "text/html", "UTF-8", null);
    }

    @Override
    public void onBackPressed() {
        WebView webView = binding.webviewPowerBi;
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

