package com.sustria.codcoz.api.model;

import java.io.Serializable;
import java.util.List;

public class ReceitaResponse implements Serializable {
    private String id;
    private String nome;
    private String descricao;
    private String urlImagem;
    private List<IngredienteApi> ingredientes;
    private List<ModoPreparoApi> modoPreparo;
    private Integer tempoPreparoMinutos;
    private Integer tempoCozimentoMinutos;
    private Integer porcoes;
    private String dataCriacao;

    // Construtores
    public ReceitaResponse() {}

    public ReceitaResponse(String id, String nome, String descricao, String urlImagem,
                           List<IngredienteApi> ingredientes, List<ModoPreparoApi> modoPreparo,
                           Integer tempoPreparoMinutos, Integer tempoCozimentoMinutos,
                           Integer porcoes, String dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.tempoPreparoMinutos = tempoPreparoMinutos;
        this.tempoCozimentoMinutos = tempoCozimentoMinutos;
        this.porcoes = porcoes;
        this.dataCriacao = dataCriacao;
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

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public List<IngredienteApi> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteApi> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<ModoPreparoApi> getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(List<ModoPreparoApi> modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public Integer getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(Integer tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }

    public Integer getTempoCozimentoMinutos() {
        return tempoCozimentoMinutos;
    }

    public void setTempoCozimentoMinutos(Integer tempoCozimentoMinutos) {
        this.tempoCozimentoMinutos = tempoCozimentoMinutos;
    }

    public Integer getPorcoes() {
        return porcoes;
    }

    public void setPorcoes(Integer porcoes) {
        this.porcoes = porcoes;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // Classe interna para Ingrediente
    public static class IngredienteApi implements Serializable {
        private String nome;
        private Integer quantidade;
        private String receita_id;
        private String unidade_medida;

        public IngredienteApi() {}

        public IngredienteApi(String nome, Integer quantidade, String receita_id, String unidade_medida) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.receita_id = receita_id;
            this.unidade_medida = unidade_medida;
        }

        // Getters e Setters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public String getReceita_id() {
            return receita_id;
        }

        public void setReceita_id(String receita_id) {
            this.receita_id = receita_id;
        }

        public String getUnidade_medida() {
            return unidade_medida;
        }

        public void setUnidade_medida(String unidade_medida) {
            this.unidade_medida = unidade_medida;
        }
    }

    // Classe interna para Modo de Preparo
    public static class ModoPreparoApi implements Serializable {
        private Integer ordem;
        private String passo;

        public ModoPreparoApi() {}

        public ModoPreparoApi(Integer ordem, String passo) {
            this.ordem = ordem;
            this.passo = passo;
        }

        // Getters e Setters
        public Integer getOrdem() {
            return ordem;
        }

        public void setOrdem(Integer ordem) {
            this.ordem = ordem;
        }

        public String getPasso() {
            return passo;
        }

        public void setPasso(String passo) {
            this.passo = passo;
        }
    }
}
