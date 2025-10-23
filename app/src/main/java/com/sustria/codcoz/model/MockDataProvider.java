package com.sustria.codcoz.model;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Gera dados mockados para o cardápio semanal
     */
    public static List<DiaSemana> getMockCardapioSemanal() {
        List<DiaSemana> cardapio = new ArrayList<>();

        // Segunda-feira
        List<ItemRefeicao> segundaRefeicoes = new ArrayList<>();
        segundaRefeicoes.add(new ItemRefeicao(
                "Café da Manhã",
                List.of("Pão francês", "Manteiga", "Café com leite", "Açúcar")
        ));
        segundaRefeicoes.add(new ItemRefeicao(
                "Almoço",
                List.of("Arroz branco", "Feijão preto", "Frango grelhado", "Salada de alface", "Suco de laranja")
        ));
        segundaRefeicoes.add(new ItemRefeicao(
                "Café da Tarde",
                List.of("Bolo de chocolate", "Café", "Leite")
        ));
        cardapio.add(new DiaSemana("Segunda-feira", segundaRefeicoes));

        // Terça-feira
        List<ItemRefeicao> tercaRefeicoes = new ArrayList<>();
        tercaRefeicoes.add(new ItemRefeicao(
                "Café da Manhã",
                List.of("Pão de forma", "Geleia", "Café", "Leite")
        ));
        tercaRefeicoes.add(new ItemRefeicao(
                "Almoço",
                List.of("Arroz integral", "Feijão carioca", "Carne assada", "Salada de tomate", "Suco de maracujá")
        ));
        tercaRefeicoes.add(new ItemRefeicao(
                "Café da Tarde",
                List.of("Biscoito", "Café com leite", "Açúcar")
        ));
        cardapio.add(new DiaSemana("Terça-feira", tercaRefeicoes));

        // Quarta-feira
        List<ItemRefeicao> quartaRefeicoes = new ArrayList<>();
        quartaRefeicoes.add(new ItemRefeicao(
                "Café da Manhã",
                List.of("Pão francês", "Queijo", "Café", "Leite")
        ));
        quartaRefeicoes.add(new ItemRefeicao(
                "Almoço",
                List.of("Arroz branco", "Feijão preto", "Peixe frito", "Salada de couve", "Suco de limão")
        ));
        quartaRefeicoes.add(new ItemRefeicao(
                "Café da Tarde",
                List.of("Pudim", "Café", "Leite condensado")
        ));
        cardapio.add(new DiaSemana("Quarta-feira", quartaRefeicoes));

        // Quinta-feira
        List<ItemRefeicao> quintaRefeicoes = new ArrayList<>();
        quintaRefeicoes.add(new ItemRefeicao(
                "Café da Manhã",
                List.of("Pão de forma", "Presunto", "Café com leite", "Açúcar")
        ));
        quintaRefeicoes.add(new ItemRefeicao(
                "Almoço",
                List.of("Arroz integral", "Feijão carioca", "Frango à parmegiana", "Salada de rúcula", "Suco de uva")
        ));
        quintaRefeicoes.add(new ItemRefeicao(
                "Café da Tarde",
                List.of("Bolo de cenoura", "Café", "Leite")
        ));
        cardapio.add(new DiaSemana("Quinta-feira", quintaRefeicoes));

        // Sexta-feira
        List<ItemRefeicao> sextaRefeicoes = new ArrayList<>();
        sextaRefeicoes.add(new ItemRefeicao(
                "Café da Manhã",
                List.of("Pão francês", "Manteiga", "Café", "Leite")
        ));
        sextaRefeicoes.add(new ItemRefeicao(
                "Almoço",
                List.of("Arroz branco", "Feijão preto", "Carne moída", "Salada de alface", "Suco de abacaxi")
        ));
        sextaRefeicoes.add(new ItemRefeicao(
                "Café da Tarde",
                List.of("Biscoito recheado", "Café com leite", "Açúcar")
        ));
        cardapio.add(new DiaSemana("Sexta-feira", sextaRefeicoes));

        return cardapio;
    }

    /**
     * Gera dados mockados para receitas
     */
    public static List<Receita> getMockReceitas() {
        List<Receita> receitas = new ArrayList<>();

        // Receita 1: Arroz com Feijão
        List<Receita.Ingrediente> ingredientesArrozFeijao = Arrays.asList(
                new Receita.Ingrediente("Arroz", "2", "xícaras"),
                new Receita.Ingrediente("Feijão preto", "1", "xícara"),
                new Receita.Ingrediente("Cebola", "1", "unidade"),
                new Receita.Ingrediente("Alho", "3", "dentes"),
                new Receita.Ingrediente("Sal", "1", "colher de chá"),
                new Receita.Ingrediente("Óleo", "2", "colheres de sopa")
        );

        List<String> instrucoesArrozFeijao = Arrays.asList(
                "Cozinhe o feijão em panela de pressão por 20 minutos",
                "Refogue a cebola e o alho no óleo",
                "Adicione o feijão cozido e tempere com sal",
                "Cozinhe o arroz separadamente",
                "Sirva o arroz com o feijão por cima"
        );

        Receita arrozFeijao = new Receita(
                "1", "Arroz com Feijão", "Receita tradicional brasileira",
                ingredientesArrozFeijao, instrucoesArrozFeijao, 45, 4,
                Receita.DificuldadeReceita.FACIL, "Prato Principal"
        );
        arrozFeijao.setImagemUrl("https://example.com/arroz-feijao.jpg");
        arrozFeijao.setCaloriasPorPorcao(350);
        arrozFeijao.setDataCriacao("2024-01-15");
        arrozFeijao.setAutor("Chef João");
        receitas.add(arrozFeijao);

        // Receita 2: Frango Grelhado
        List<Receita.Ingrediente> ingredientesFrango = Arrays.asList(
                new Receita.Ingrediente("Peito de frango", "500", "g"),
                new Receita.Ingrediente("Limão", "1", "unidade"),
                new Receita.Ingrediente("Alho", "2", "dentes"),
                new Receita.Ingrediente("Azeite", "2", "colheres de sopa"),
                new Receita.Ingrediente("Sal", "1", "colher de chá"),
                new Receita.Ingrediente("Pimenta", "1", "pitada")
        );

        List<String> instrucoesFrango = Arrays.asList(
                "Tempere o frango com limão, alho, sal e pimenta",
                "Deixe marinar por 30 minutos",
                "Aqueça uma frigideira com azeite",
                "Grelhe o frango por 6 minutos de cada lado",
                "Sirva quente"
        );

        Receita frangoGrelhado = new Receita(
                "2", "Frango Grelhado", "Frango temperado e grelhado",
                ingredientesFrango, instrucoesFrango, 40, 2,
                Receita.DificuldadeReceita.FACIL, "Prato Principal"
        );
        frangoGrelhado.setImagemUrl("https://example.com/frango-grelhado.jpg");
        frangoGrelhado.setCaloriasPorPorcao(280);
        frangoGrelhado.setDataCriacao("2024-01-16");
        frangoGrelhado.setAutor("Chef Maria");
        receitas.add(frangoGrelhado);

        // Receita 3: Bolo de Chocolate
        List<Receita.Ingrediente> ingredientesBolo = Arrays.asList(
                new Receita.Ingrediente("Farinha de trigo", "2", "xícaras"),
                new Receita.Ingrediente("Açúcar", "1", "xícara"),
                new Receita.Ingrediente("Cacau em pó", "1/2", "xícara"),
                new Receita.Ingrediente("Ovos", "3", "unidades"),
                new Receita.Ingrediente("Leite", "1", "xícara"),
                new Receita.Ingrediente("Óleo", "1/2", "xícara"),
                new Receita.Ingrediente("Fermento", "1", "colher de sopa")
        );

        List<String> instrucoesBolo = Arrays.asList(
                "Pré-aqueça o forno a 180°C",
                "Misture os ingredientes secos em uma tigela",
                "Bata os ovos com o açúcar até ficar cremoso",
                "Adicione o leite e o óleo",
                "Incorpore os ingredientes secos",
                "Despeje na forma e asse por 35 minutos"
        );

        Receita boloChocolate = new Receita(
                "3", "Bolo de Chocolate", "Bolo fofinho de chocolate",
                ingredientesBolo, instrucoesBolo, 60, 8,
                Receita.DificuldadeReceita.MEDIO, "Sobremesa"
        );
        boloChocolate.setImagemUrl("https://example.com/bolo-chocolate.jpg");
        boloChocolate.setCaloriasPorPorcao(320);
        boloChocolate.setDataCriacao("2024-01-17");
        boloChocolate.setAutor("Chef Ana");
        receitas.add(boloChocolate);

        // Receita 4: Salada de Alface
        List<Receita.Ingrediente> ingredientesSalada = Arrays.asList(
                new Receita.Ingrediente("Alface", "1", "cabeça"),
                new Receita.Ingrediente("Tomate", "2", "unidades"),
                new Receita.Ingrediente("Cebola", "1/2", "unidade"),
                new Receita.Ingrediente("Azeite", "3", "colheres de sopa"),
                new Receita.Ingrediente("Vinagre", "1", "colher de sopa"),
                new Receita.Ingrediente("Sal", "1", "pitada")
        );

        List<String> instrucoesSalada = Arrays.asList(
                "Lave e corte a alface em pedaços",
                "Corte os tomates em fatias",
                "Corte a cebola em rodelas finas",
                "Misture o azeite, vinagre e sal",
                "Tempere a salada com o molho",
                "Sirva imediatamente"
        );

        Receita saladaAlface = new Receita(
                "4", "Salada de Alface", "Salada fresca e saudável",
                ingredientesSalada, instrucoesSalada, 15, 4,
                Receita.DificuldadeReceita.FACIL, "Acompanhamento"
        );
        saladaAlface.setImagemUrl("https://example.com/salada-alface.jpg");
        saladaAlface.setCaloriasPorPorcao(80);
        saladaAlface.setDataCriacao("2024-01-18");
        saladaAlface.setAutor("Chef Pedro");
        receitas.add(saladaAlface);

        // Receita 5: Macarrão à Carbonara
        List<Receita.Ingrediente> ingredientesMacarrao = Arrays.asList(
                new Receita.Ingrediente("Macarrão", "400", "g"),
                new Receita.Ingrediente("Bacon", "200", "g"),
                new Receita.Ingrediente("Ovos", "3", "unidades"),
                new Receita.Ingrediente("Queijo parmesão", "100", "g"),
                new Receita.Ingrediente("Pimenta preta", "1", "colher de chá"),
                new Receita.Ingrediente("Sal", "1", "colher de chá")
        );

        List<String> instrucoesMacarrao = Arrays.asList(
                "Cozinhe o macarrão conforme instruções da embalagem",
                "Frite o bacon até ficar crocante",
                "Bata os ovos com o queijo parmesão ralado",
                "Escorra o macarrão e misture com o bacon",
                "Adicione a mistura de ovos e queijo",
                "Tempere com sal e pimenta"
        );

        Receita macarraoCarbonara = new Receita(
                "5", "Macarrão à Carbonara", "Macarrão cremoso com bacon e queijo",
                ingredientesMacarrao, instrucoesMacarrao, 25, 4,
                Receita.DificuldadeReceita.MEDIO, "Prato Principal"
        );
        macarraoCarbonara.setImagemUrl("https://example.com/macarrao-carbonara.jpg");
        macarraoCarbonara.setCaloriasPorPorcao(450);
        macarraoCarbonara.setDataCriacao("2024-01-19");
        macarraoCarbonara.setAutor("Chef Carlos");
        receitas.add(macarraoCarbonara);

        // Receita 6: Macarrão à Bolonhesa (baseado na imagem)
        List<Receita.Ingrediente> ingredientesBolonhesa = Arrays.asList(
                new Receita.Ingrediente("Macarrão", "500", "g"),
                new Receita.Ingrediente("Carne moída", "500", "g"),
                new Receita.Ingrediente("Tomate", "4", "unid"),
                new Receita.Ingrediente("Cebola", "1", "unid"),
                new Receita.Ingrediente("Dente de alho", "2", "unid"),
                new Receita.Ingrediente("Sal", "a gosto", ""),
                new Receita.Ingrediente("Pimenta-do-reino", "a gosto", ""),
                new Receita.Ingrediente("Manjericão", "a gosto", "")
        );

        List<String> instrucoesBolonhesa = Arrays.asList(
                "Pique a cebola, refogue por alguns minutos em uma panela com óleo quente até dourar a cebola, mexendo para não queimar.",
                "Misture a carne moída, deixe cozinhar por alguns minutos.",
                "Adicione o caldo, o molho, os tomates picados, a cenoura cortada ao meio e mexa bem, deixe cozinhar por aproximadamente 40 minutos em fogo baixo com a panela semi tampada. Descarte a cenoura depois que o molho estiver pronto.",
                "Prepare o macarrão, misture o molho ao macarrão e sirva."
        );

        Receita macarraoBolonhesa = new Receita(
                "6", "Macarrão à Bolonhesa", "Delicioso macarrão com molho à bolonhesa tradicional",
                ingredientesBolonhesa, instrucoesBolonhesa, 15, 6,
                Receita.DificuldadeReceita.MEDIO, "Prato Principal"
        );
        macarraoBolonhesa.setImagemUrl("https://example.com/macarrao-bolonhesa.jpg");
        macarraoBolonhesa.setCaloriasPorPorcao(420);
        macarraoBolonhesa.setDataCriacao("2024-01-20");
        macarraoBolonhesa.setAutor("Chef Italiano");
        receitas.add(macarraoBolonhesa);

        return receitas;
    }
}

