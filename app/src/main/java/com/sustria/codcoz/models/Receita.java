package com.sustria.codcoz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Receita {

    @SerializedName("id")
    private String id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("ingredientes")
    private List<Ingrediente> ingredientes;

    @SerializedName("instrucoes")
    private List<String> instrucoes;

    @SerializedName("tempo_preparo")
    private int tempoPreparo; // em minutos, nao sei como virá da procedure

    @SerializedName("porcoes")
    private int porcoes;

    @SerializedName("dificuldade")
    private DificuldadeReceita dificuldade;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("imagem_url")
    private String imagemUrl;

    @SerializedName("calorias_por_porcao")
    private int caloriasPorPorcao;

    @SerializedName("data_criacao")
    private String dataCriacao;

    @SerializedName("autor")
    private String autor;

    // Construtor padrão
    public Receita() {
    }

    // Construtor principal
    public Receita(String id, String nome, String descricao, List<Ingrediente> ingredientes,
                   List<String> instrucoes, int tempoPreparo, int porcoes,
                   DificuldadeReceita dificuldade, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ingredientes = ingredientes;
        this.instrucoes = instrucoes;
        this.tempoPreparo = tempoPreparo;
        this.porcoes = porcoes;
        this.dificuldade = dificuldade;
        this.categoria = categoria;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<String> getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(List<String> instrucoes) {
        this.instrucoes = instrucoes;
    }

    public int getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public int getPorcoes() {
        return porcoes;
    }

    public void setPorcoes(int porcoes) {
        this.porcoes = porcoes;
    }

    public DificuldadeReceita getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(DificuldadeReceita dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public int getCaloriasPorPorcao() {
        return caloriasPorPorcao;
    }

    public void setCaloriasPorPorcao(int caloriasPorPorcao) {
        this.caloriasPorPorcao = caloriasPorPorcao;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Enum para dificuldade da receita
     */
    public enum DificuldadeReceita {
        @SerializedName("facil")
        FACIL,
        @SerializedName("medio")
        MEDIO,
        @SerializedName("dificil")
        DIFICIL
    }

    /**
     * Classe para ingredientes da receita
     */
    public static class Ingrediente {
        @SerializedName("nome")
        private String nome;

        @SerializedName("quantidade")
        private String quantidade;

        @SerializedName("unidade")
        private String unidade;

        public Ingrediente() {
        }

        public Ingrediente(String nome, String quantidade, String unidade) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.unidade = unidade;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(String quantidade) {
            this.quantidade = quantidade;
        }

        public String getUnidade() {
            return unidade;
        }

        public void setUnidade(String unidade) {
            this.unidade = unidade;
        }
    }

    /**
     * Classe para resposta da API de receitas
     */
    public static class ReceitasResponse {
        @SerializedName("success")
        private boolean success;

        @SerializedName("data")
        private List<Receita> data;

        @SerializedName("message")
        private String message;

        @SerializedName("total")
        private int total;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<Receita> getData() {
            return data;
        }

        public void setData(List<Receita> data) {
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
    }
}



