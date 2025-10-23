package com.sustria.codcoz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sustria.codcoz.actions.ProdutosActivity;
import com.sustria.codcoz.api.model.EstoquistaResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDataManager {

    private static final String PREF_NAME = "user_data";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NOME = "nome";
    private static final String KEY_SOBRENOME = "sobrenome";
    private static final String KEY_DATA_CONTRATACAO = "data_contratacao";
    private static final String KEY_IMAGEM_PERFIL = "imagem_perfil";

    private static UserDataManager instance;
    private EstoquistaResponse userData;
    private boolean isDataLoaded = false;

    private UserDataManager() {
    }

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    public static EstoquistaResponse getInstance(ProdutosActivity produtosActivity) {
        return instance.userData;
    }

    public void setUserData(EstoquistaResponse userData, Context context) {
        this.userData = userData;
        this.isDataLoaded = true;

        // Salva em cache persistente (SharedPreferences)
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_EMAIL, userData.getEmail());
            editor.putString(KEY_NOME, userData.getNome());
            editor.putString(KEY_SOBRENOME, userData.getSobrenome());
            editor.putString(KEY_DATA_CONTRATACAO, userData.getDataContratacao());
            editor.putString(KEY_IMAGEM_PERFIL, userData.getImagemPerfil());
            editor.apply();
        }
    }

    public void loadDataFromPreferences(Context context, Runnable onLoaded) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String email = prefs.getString(KEY_EMAIL, null);

        if (email != null) {
            EstoquistaResponse cachedUser = new EstoquistaResponse();
            cachedUser.setEmail(email);
            cachedUser.setNome(prefs.getString(KEY_NOME, ""));
            cachedUser.setSobrenome(prefs.getString(KEY_SOBRENOME, ""));
            cachedUser.setDataContratacao(prefs.getString(KEY_DATA_CONTRATACAO, ""));
            cachedUser.setImagemPerfil(prefs.getString(KEY_IMAGEM_PERFIL, "https://res.cloudinary.com/dixacuf51/image/upload/v1/default_profile_avatar"));
            this.userData = cachedUser;
            this.isDataLoaded = true;
        }

        if (onLoaded != null) {
            onLoaded.run();
        }
    }

    public EstoquistaResponse getUserData() {
        return userData;
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public void clearCache(Context context) {
        this.userData = null;
        this.isDataLoaded = false;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public String getNomeCompleto() {
        if (userData != null) {
            return userData.getNome() + " " + userData.getSobrenome();
        }
        return "Usuário";
    }

    public String getEmail() {
        if (userData != null && userData.getEmail() != null && !userData.getEmail().isEmpty()) {
            return userData.getEmail();
        }
        return null;
    }

    public String getDataContratacaoFormatada() {
        if (userData != null && userData.getDataContratacao() != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(userData.getDataContratacao());
                return outputFormat.format(date);
            } catch (ParseException e) {
                return userData.getDataContratacao();
            }
        }
        return "--/--/----";
    }

    public String getImagemPerfil() {
        if (userData != null && userData.getImagemPerfil() != null && !userData.getImagemPerfil().isEmpty()) {
            return userData.getImagemPerfil();
        }
        return "https://res.cloudinary.com/dixacuf51/image/upload/v1/default_profile_avatar";
    }
}
