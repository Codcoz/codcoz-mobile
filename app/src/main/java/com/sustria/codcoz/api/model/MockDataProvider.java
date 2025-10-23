package com.sustria.codcoz.api.model;

import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {

    public static List<RegistroHistorico> getMockRegistrosHistorico() {
        List<RegistroHistorico> registros = new ArrayList<>();
        long now = System.currentTimeMillis();

        registros.add(new RegistroHistorico(
                "1", "Arroz Integral", 10, "ARR001",
                now - 60 * 60 * 1000, // 1 hora atrás
                RegistroHistorico.TipoMovimentacao.ENTRADA,
                "Entrada de estoque - compra semanal",
                "user001", "prod001"
        ));

        registros.add(new RegistroHistorico(
                "2", "Feijão Preto", 5, "FEI002",
                now - 26 * 60 * 60 * 1000, // 1 dia atrás
                RegistroHistorico.TipoMovimentacao.BAIXA,
                "Baixa para preparo do almoço",
                "user002", "prod002"
        ));

        registros.add(new RegistroHistorico(
                "3", "Macarrão Espaguete", 12, "MAC003",
                now - 5 * 24 * 60 * 60 * 1000, // 5 dias atrás
                RegistroHistorico.TipoMovimentacao.ENTRADA,
                "Entrada de estoque - pedido mensal",
                "user001", "prod003"
        ));

        registros.add(new RegistroHistorico(
                "4", "Açúcar Cristal", 2, "ACU004",
                now - 9 * 24 * 60 * 60 * 1000, // 9 dias atrás
                RegistroHistorico.TipoMovimentacao.BAIXA,
                "Baixa para preparo de sobremesas",
                "user003", "prod004"
        ));

        registros.add(new RegistroHistorico(
                "5", "Sal Refinado", 18, "SAL005",
                now - 20 * 24 * 60 * 60 * 1000, // 20 dias atrás
                RegistroHistorico.TipoMovimentacao.BAIXA,
                "Baixa para temperos diversos",
                "user002", "prod005"
        ));

        registros.add(new RegistroHistorico(
                "6", "Óleo de Soja", 7, "OLE006",
                now - 29L * 24 * 60 * 60 * 1000, // 29 dias atrás
                RegistroHistorico.TipoMovimentacao.ENTRADA,
                "Entrada de estoque - compra mensal",
                "user001", "prod006"
        ));

        registros.add(new RegistroHistorico(
                "7", "Café em Pó", 3, "CAF007",
                now - 2 * 24 * 60 * 60 * 1000, // 2 dias atrás
                RegistroHistorico.TipoMovimentacao.BAIXA,
                "Baixa para café da manhã",
                "user003", "prod007"
        ));

        registros.add(new RegistroHistorico(
                "8", "Leite Integral", 15, "LEI008",
                now - 3 * 24 * 60 * 60 * 1000, // 3 dias atrás
                RegistroHistorico.TipoMovimentacao.ENTRADA,
                "Entrada de estoque - compra diária",
                "user001", "prod008"
        ));

        return registros;
    }

    public static List<DiaSemanaResponse> getMockCardapioSemanal() {
        List<DiaSemanaResponse> cardapio = new ArrayList<>();

        // Segunda-feira
        List<ItemRefeicaoResponse> segundaRefeicoes = new ArrayList<>();
        segundaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Manhã",
                List.of("Pão francês", "Manteiga", "Café com leite", "Açúcar")
        ));
        segundaRefeicoes.add(new ItemRefeicaoResponse(
                "Almoço",
                List.of("Arroz branco", "Feijão preto", "Frango grelhado", "Salada de alface", "Suco de laranja")
        ));
        segundaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Tarde",
                List.of("Bolo de chocolate", "Café", "Leite")
        ));
        cardapio.add(new DiaSemanaResponse("Segunda-feira", segundaRefeicoes));

        // Terça-feira
        List<ItemRefeicaoResponse> tercaRefeicoes = new ArrayList<>();
        tercaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Manhã",
                List.of("Pão de forma", "Geleia", "Café", "Leite")
        ));
        tercaRefeicoes.add(new ItemRefeicaoResponse(
                "Almoço",
                List.of("Arroz integral", "Feijão carioca", "Carne assada", "Salada de tomate", "Suco de maracujá")
        ));
        tercaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Tarde",
                List.of("Biscoito", "Café com leite", "Açúcar")
        ));
        cardapio.add(new DiaSemanaResponse("Terça-feira", tercaRefeicoes));

        // Quarta-feira
        List<ItemRefeicaoResponse> quartaRefeicoes = new ArrayList<>();
        quartaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Manhã",
                List.of("Pão francês", "Queijo", "Café", "Leite")
        ));
        quartaRefeicoes.add(new ItemRefeicaoResponse(
                "Almoço",
                List.of("Arroz branco", "Feijão preto", "Peixe frito", "Salada de couve", "Suco de limão")
        ));
        quartaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Tarde",
                List.of("Pudim", "Café", "Leite")
        ));
        cardapio.add(new DiaSemanaResponse("Quarta-feira", quartaRefeicoes));

        // Quinta-feira
        List<ItemRefeicaoResponse> quintaRefeicoes = new ArrayList<>();
        quintaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Manhã",
                List.of("Pão de forma", "Presunto", "Café", "Leite")
        ));
        quintaRefeicoes.add(new ItemRefeicaoResponse(
                "Almoço",
                List.of("Arroz integral", "Feijão preto", "Frango grelhado", "Salada de alface", "Suco de laranja")
        ));
        quintaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Tarde",
                List.of("Bolo de cenoura", "Café", "Leite")
        ));
        cardapio.add(new DiaSemanaResponse("Quinta-feira", quintaRefeicoes));

        // Sexta-feira
        List<ItemRefeicaoResponse> sextaRefeicoes = new ArrayList<>();
        sextaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Manhã",
                List.of("Pão francês", "Manteiga", "Café", "Leite")
        ));
        sextaRefeicoes.add(new ItemRefeicaoResponse(
                "Almoço",
                List.of("Arroz branco", "Feijão carioca", "Carne assada", "Salada de tomate", "Suco de maracujá")
        ));
        sextaRefeicoes.add(new ItemRefeicaoResponse(
                "Café da Tarde",
                List.of("Biscoito", "Café com leite", "Açúcar")
        ));
        cardapio.add(new DiaSemanaResponse("Sexta-feira", sextaRefeicoes));

        return cardapio;
    }
}

