package com.sustria.codcoz.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistroHistorico {
    
    @SerializedName("id")
    private String id;
    
    @SerializedName("nome")
    private String nome;
    
    @SerializedName("unidades")
    private int unidades;
    
    @SerializedName("codigo")
    private String codigo;
    
    @SerializedName("data_movimentacao")
    private long epochMillis;
    
    @SerializedName("tipo")
    private TipoMovimentacao tipo;
    
    @SerializedName("observacoes")
    private String observacoes;
    
    @SerializedName("usuario_id")
    private String usuarioId;
    
    @SerializedName("produto_id")
    private String produtoId;

    // Construtor padrão
    public RegistroHistorico() {}

    // Construtor completo
    public RegistroHistorico(String id, String nome, int unidades, String codigo, 
                           long epochMillis, TipoMovimentacao tipo, String observacoes, 
                           String usuarioId, String produtoId) {
        this.id = id;
        this.nome = nome;
        this.unidades = unidades;
        this.codigo = codigo;
        this.epochMillis = epochMillis;
        this.tipo = tipo;
        this.observacoes = observacoes;
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
    }

    // Construtor simplificado para compatibilidade
    public RegistroHistorico(String nome, int unidades, String codigo, 
                           long epochMillis, TipoMovimentacao tipo) {
        this.nome = nome;
        this.unidades = unidades;
        this.codigo = codigo;
        this.epochMillis = epochMillis;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getEpochMillis() {
        return epochMillis;
    }

    public void setEpochMillis(long epochMillis) {
        this.epochMillis = epochMillis;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    /**
     * Enum para tipos de movimentação
     */
    public enum TipoMovimentacao {
        @SerializedName("entrada")
        ENTRADA,
        @SerializedName("baixa")
        BAIXA,
        @SerializedName("ajuste")
        AJUSTE,
        @SerializedName("transferencia")
        TRANSFERENCIA
    }

    /**
     * Classe para resposta da API
     */
    public static class ApiResponse {
        @SerializedName("success")
        private boolean success;
        
        @SerializedName("data")
        private List<RegistroHistorico> data;
        
        @SerializedName("message")
        private String message;
        
        @SerializedName("total")
        private int total;
        
        @SerializedName("page")
        private int page;
        
        @SerializedName("limit")
        private int limit;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<RegistroHistorico> getData() {
            return data;
        }

        public void setData(List<RegistroHistorico> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }

    /**
     * Classe para parâmetros de filtro da API
     */
    public static class FiltroRequest {
        @SerializedName("tipo")
        private TipoMovimentacao tipo;
        
        @SerializedName("data_inicio")
        private String dataInicio;
        
        @SerializedName("data_fim")
        private String dataFim;
        
        @SerializedName("produto_id")
        private String produtoId;
        
        @SerializedName("usuario_id")
        private String usuarioId;
        
        @SerializedName("search")
        private String search;
        
        @SerializedName("page")
        private int page = 1;
        
        @SerializedName("limit")
        private int limit = 20;
        
        @SerializedName("sort")
        private String sort = "data_desc";

        public TipoMovimentacao getTipo() {
            return tipo;
        }

        public void setTipo(TipoMovimentacao tipo) {
            this.tipo = tipo;
        }

        public String getDataInicio() {
            return dataInicio;
        }

        public void setDataInicio(String dataInicio) {
            this.dataInicio = dataInicio;
        }

        public String getDataFim() {
            return dataFim;
        }

        public void setDataFim(String dataFim) {
            this.dataFim = dataFim;
        }

        public String getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(String produtoId) {
            this.produtoId = produtoId;
        }

        public String getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}




