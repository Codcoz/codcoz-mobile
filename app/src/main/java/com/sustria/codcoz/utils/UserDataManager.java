package com.sustria.codcoz.utils;

import com.sustria.codcoz.api.model.EstoquistaResponse;

/**
 * Singleton para gerenciar dados do usuário em cache
 * Evita requisições desnecessárias à API
 */
public class UserDataManager {
    
    private static UserDataManager instance;
    private EstoquistaResponse userData;
    private boolean isDataLoaded = false;
    
    private UserDataManager() {}
    
    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }
    
    /**
     * Define os dados do usuário no cache
     */
    public void setUserData(EstoquistaResponse userData) {
        this.userData = userData;
        this.isDataLoaded = true;
    }
    
    /**
     * Obtém os dados do usuário do cache
     */
    public EstoquistaResponse getUserData() {
        return userData;
    }
    
    /**
     * Verifica se os dados já foram carregados
     */
    public boolean isDataLoaded() {
        return isDataLoaded;
    }
    
    /**
     * Limpa o cache (útil para logout)
     */
    public void clearCache() {
        this.userData = null;
        this.isDataLoaded = false;
    }
    
    /**
     * Obtém o nome completo do usuário
     */
    public String getNomeCompleto() {
        if (userData != null) {
            return userData.getNome() + " " + userData.getSobrenome();
        }
        return "Usuário";
    }
    
    /**
     * Obtém a data de contratação formatada
     */
    public String getDataContratacaoFormatada() {
        if (userData != null && userData.getDataContratacao() != null) {
            try {
                java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                java.util.Date date = inputFormat.parse(userData.getDataContratacao());
                return outputFormat.format(date);
            } catch (java.text.ParseException e) {
                return userData.getDataContratacao();
            }
        }
        return "--/--/----";
    }
}

