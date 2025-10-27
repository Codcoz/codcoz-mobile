package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

public class HistoricoListRequest {
    
    @SerializedName("Content-Type")
    private String contentType;
    
    public HistoricoListRequest() {
        this.contentType = "application/json";
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
