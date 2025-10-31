package com.sustria.codcoz.utils;

import com.sustria.codcoz.api.model.AlmocoResponse;
import com.sustria.codcoz.api.model.CardapioResponse;
import com.sustria.codcoz.api.model.DiaSemanaResponse;
import com.sustria.codcoz.api.model.FrutaResponse;
import com.sustria.codcoz.api.model.ItemReceitaIngredienteCompletoResponse;
import com.sustria.codcoz.api.model.ItemReceitaIngredienteResponse;
import com.sustria.codcoz.api.model.ItemReceitaResponse;
import com.sustria.codcoz.api.model.LancheResponse;

import java.util.ArrayList;
import java.util.List;

public class CardapioMockData {

    public static List<CardapioResponse> getMockCardapios() {
        List<CardapioResponse> cardapios = new ArrayList<>();

        // Cardápio Semanal 1
        CardapioResponse cardapio1 = new CardapioResponse();
        cardapio1.setId("mock_cardapio_1");
        cardapio1.setDataInicio("2025-10-27");
        cardapio1.setDataFim("2025-10-31");
        cardapio1.setEmpresaId("1");
        cardapio1.setNomeCardapio("Cardápio Semanal - 27/10/2025 a 31/10/2025");
        cardapio1.setPeriodicidade("Semanal");
        cardapio1.setCardapioSemanal(createCardapioSemanal());

        cardapios.add(cardapio1);

        // Cardápio Semanal 2
        CardapioResponse cardapio2 = new CardapioResponse();
        cardapio2.setId("mock_cardapio_2");
        cardapio2.setDataInicio("2025-11-03");
        cardapio2.setDataFim("2025-11-07");
        cardapio2.setEmpresaId("1");
        cardapio2.setNomeCardapio("Cardápio Semanal - 03/11/2025 a 07/11/2025");
        cardapio2.setPeriodicidade("Semanal");
        cardapio2.setCardapioSemanal(createCardapioSemanal2());

        cardapios.add(cardapio2);

        return cardapios;
    }

    private static List<DiaSemanaResponse> createCardapioSemanal() {
        List<DiaSemanaResponse> semana = new ArrayList<>();

        // Segunda-feira
        DiaSemanaResponse segunda = new DiaSemanaResponse();
        segunda.setDiaSemana("Segunda-feira");
        segunda.setData("2025-10-27");
        segunda.setAlmoco(createAlmoco1());
        segunda.setLancheManha(createLancheManha1());
        segunda.setLancheTarde(createLancheTarde1());
        semana.add(segunda);

        // Terça-feira
        DiaSemanaResponse terca = new DiaSemanaResponse();
        terca.setDiaSemana("Terça-feira");
        terca.setData("2025-10-28");
        terca.setAlmoco(createAlmoco2());
        terca.setLancheManha(createLancheManha2());
        terca.setLancheTarde(createLancheTarde2());
        semana.add(terca);

        // Quarta-feira
        DiaSemanaResponse quarta = new DiaSemanaResponse();
        quarta.setDiaSemana("Quarta-feira");
        quarta.setData("2025-10-29");
        quarta.setAlmoco(createAlmoco3());
        quarta.setLancheManha(createLancheManha1());
        quarta.setLancheTarde(createLancheTarde1());
        semana.add(quarta);

        // Quinta-feira
        DiaSemanaResponse quinta = new DiaSemanaResponse();
        quinta.setDiaSemana("Quinta-feira");
        quinta.setData("2025-10-30");
        quinta.setAlmoco(createAlmoco4());
        quinta.setLancheManha(createLancheManha2());
        quinta.setLancheTarde(createLancheTarde2());
        semana.add(quinta);

        // Sexta-feira
        DiaSemanaResponse sexta = new DiaSemanaResponse();
        sexta.setDiaSemana("Sexta-feira");
        sexta.setData("2025-10-31");
        sexta.setAlmoco(createAlmoco5());
        sexta.setLancheManha(createLancheManha1());
        sexta.setLancheTarde(createLancheTarde1());
        semana.add(sexta);

        return semana;
    }

    private static List<DiaSemanaResponse> createCardapioSemanal2() {
        List<DiaSemanaResponse> semana = new ArrayList<>();

        // Segunda-feira
        DiaSemanaResponse segunda = new DiaSemanaResponse();
        segunda.setDiaSemana("Segunda-feira");
        segunda.setData("2025-11-03");
        segunda.setAlmoco(createAlmoco1());
        segunda.setLancheManha(createLancheManha1());
        segunda.setLancheTarde(createLancheTarde1());
        semana.add(segunda);

        // Terça-feira
        DiaSemanaResponse terca = new DiaSemanaResponse();
        terca.setDiaSemana("Terça-feira");
        terca.setData("2025-11-04");
        terca.setAlmoco(createAlmoco2());
        terca.setLancheManha(createLancheManha2());
        terca.setLancheTarde(createLancheTarde2());
        semana.add(terca);

        // Quarta-feira
        DiaSemanaResponse quarta = new DiaSemanaResponse();
        quarta.setDiaSemana("Quarta-feira");
        quarta.setData("2025-11-05");
        quarta.setAlmoco(createAlmoco3());
        quarta.setLancheManha(createLancheManha1());
        quarta.setLancheTarde(createLancheTarde1());
        semana.add(quarta);

        // Quinta-feira
        DiaSemanaResponse quinta = new DiaSemanaResponse();
        quinta.setDiaSemana("Quinta-feira");
        quinta.setData("2025-11-06");
        quinta.setAlmoco(createAlmoco4());
        quinta.setLancheManha(createLancheManha2());
        quinta.setLancheTarde(createLancheTarde2());
        semana.add(quinta);

        // Sexta-feira
        DiaSemanaResponse sexta = new DiaSemanaResponse();
        sexta.setDiaSemana("Sexta-feira");
        sexta.setData("2025-11-07");
        sexta.setAlmoco(createAlmoco5());
        sexta.setLancheManha(createLancheManha1());
        sexta.setLancheTarde(createLancheTarde1());
        semana.add(sexta);

        return semana;
    }

    // Almoço 1 - Segunda-feira
    private static AlmocoResponse createAlmoco1() {
        AlmocoResponse almoco = new AlmocoResponse();

        ItemReceitaIngredienteResponse arrozIntegral = new ItemReceitaIngredienteResponse();
        arrozIntegral.setReceitaId("69016db421e9c2e6c505f2c7");
        arrozIntegral.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArrozIntegral(arrozIntegral);

        ItemReceitaIngredienteResponse arroz = new ItemReceitaIngredienteResponse();
        arroz.setReceitaId("69016db321e9c2e6c505f2bf");
        arroz.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArroz(arroz);

        ItemReceitaIngredienteResponse feijao = new ItemReceitaIngredienteResponse();
        feijao.setReceitaId("69016db421e9c2e6c505f2cc");
        feijao.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setFeijao(feijao);

        List<ItemReceitaResponse> proteinas = new ArrayList<>();
        ItemReceitaResponse proteina = new ItemReceitaResponse();
        proteina.setPrioridade(1);
        proteina.setReceitaId("69016db321e9c2e6c505f2be");
        proteinas.add(proteina);
        almoco.setProteinas(proteinas);

        ItemReceitaIngredienteResponse guarnicao = new ItemReceitaIngredienteResponse();
        guarnicao.setReceitaId("69016db321e9c2e6c505f2ba");
        guarnicao.setIngredienteId("68f78a47e858426ca23481bb");
        almoco.setGuarnicao(guarnicao);

        List<ItemReceitaResponse> saladas = new ArrayList<>();
        ItemReceitaResponse salada = new ItemReceitaResponse();
        salada.setPrioridade(1);
        salada.setReceitaId("69016db321e9c2e6c505f2b8");
        saladas.add(salada);
        almoco.setSaladas(saladas);

        ItemReceitaIngredienteResponse molhoSalada = new ItemReceitaIngredienteResponse();
        molhoSalada.setReceitaId("69016db321e9c2e6c505f2bc");
        molhoSalada.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setMolhoSalada(molhoSalada);

        ItemReceitaIngredienteResponse sobremesa = new ItemReceitaIngredienteResponse();
        sobremesa.setReceitaId("69016db421e9c2e6c505f2d8");
        sobremesa.setIngredienteId("68f78a3ee858426ca23481ba");
        almoco.setSobremesa(sobremesa);

        return almoco;
    }

    // Almoço 2 - Terça-feira
    private static AlmocoResponse createAlmoco2() {
        AlmocoResponse almoco = new AlmocoResponse();

        ItemReceitaIngredienteResponse arrozIntegral = new ItemReceitaIngredienteResponse();
        arrozIntegral.setReceitaId("69016db421e9c2e6c505f2c7");
        arrozIntegral.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArrozIntegral(arrozIntegral);

        ItemReceitaIngredienteResponse arroz = new ItemReceitaIngredienteResponse();
        arroz.setReceitaId("69016db321e9c2e6c505f2c0");
        arroz.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArroz(arroz);

        ItemReceitaIngredienteResponse feijao = new ItemReceitaIngredienteResponse();
        feijao.setReceitaId("69016db421e9c2e6c505f2cc");
        feijao.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setFeijao(feijao);

        List<ItemReceitaResponse> proteinas = new ArrayList<>();
        ItemReceitaResponse proteina = new ItemReceitaResponse();
        proteina.setPrioridade(1);
        proteina.setReceitaId("69016db321e9c2e6c505f2bf");
        proteinas.add(proteina);
        almoco.setProteinas(proteinas);

        ItemReceitaIngredienteResponse guarnicao = new ItemReceitaIngredienteResponse();
        guarnicao.setReceitaId("69016db321e9c2e6c505f2ba");
        guarnicao.setIngredienteId("68f78a47e858426ca23481bb");
        almoco.setGuarnicao(guarnicao);

        List<ItemReceitaResponse> saladas = new ArrayList<>();
        ItemReceitaResponse salada = new ItemReceitaResponse();
        salada.setPrioridade(1);
        salada.setReceitaId("69016db321e9c2e6c505f2b8");
        saladas.add(salada);
        almoco.setSaladas(saladas);

        ItemReceitaIngredienteResponse molhoSalada = new ItemReceitaIngredienteResponse();
        molhoSalada.setReceitaId("69016db321e9c2e6c505f2bc");
        molhoSalada.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setMolhoSalada(molhoSalada);

        ItemReceitaIngredienteResponse sobremesa = new ItemReceitaIngredienteResponse();
        sobremesa.setReceitaId("69016db421e9c2e6c505f2d8");
        sobremesa.setIngredienteId("68f78a3ee858426ca23481ba");
        almoco.setSobremesa(sobremesa);

        return almoco;
    }

    // Almoço 3 - Quarta-feira
    private static AlmocoResponse createAlmoco3() {
        AlmocoResponse almoco = new AlmocoResponse();

        ItemReceitaIngredienteResponse arrozIntegral = new ItemReceitaIngredienteResponse();
        arrozIntegral.setReceitaId("69016db421e9c2e6c505f2c7");
        arrozIntegral.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArrozIntegral(arrozIntegral);

        ItemReceitaIngredienteResponse arroz = new ItemReceitaIngredienteResponse();
        arroz.setReceitaId("69016db321e9c2e6c505f2bf");
        arroz.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArroz(arroz);

        ItemReceitaIngredienteResponse feijao = new ItemReceitaIngredienteResponse();
        feijao.setReceitaId("69016db421e9c2e6c505f2cc");
        feijao.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setFeijao(feijao);

        List<ItemReceitaResponse> proteinas = new ArrayList<>();
        ItemReceitaResponse proteina = new ItemReceitaResponse();
        proteina.setPrioridade(1);
        proteina.setReceitaId("69016db321e9c2e6c505f2be");
        proteinas.add(proteina);
        almoco.setProteinas(proteinas);

        ItemReceitaIngredienteResponse guarnicao = new ItemReceitaIngredienteResponse();
        guarnicao.setReceitaId("69016db321e9c2e6c505f2ba");
        guarnicao.setIngredienteId("68f78a47e858426ca23481bb");
        almoco.setGuarnicao(guarnicao);

        List<ItemReceitaResponse> saladas = new ArrayList<>();
        ItemReceitaResponse salada = new ItemReceitaResponse();
        salada.setPrioridade(1);
        salada.setReceitaId("69016db421e9c2e6c505f2d9");
        saladas.add(salada);
        almoco.setSaladas(saladas);

        ItemReceitaIngredienteResponse molhoSalada = new ItemReceitaIngredienteResponse();
        molhoSalada.setReceitaId("69016db321e9c2e6c505f2bc");
        molhoSalada.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setMolhoSalada(molhoSalada);

        ItemReceitaIngredienteResponse sobremesa = new ItemReceitaIngredienteResponse();
        sobremesa.setReceitaId("69016db421e9c2e6c505f2d8");
        sobremesa.setIngredienteId("68f78a3ee858426ca23481ba");
        almoco.setSobremesa(sobremesa);

        return almoco;
    }

    // Almoço 4 - Quinta-feira
    private static AlmocoResponse createAlmoco4() {
        AlmocoResponse almoco = new AlmocoResponse();

        ItemReceitaIngredienteResponse arrozIntegral = new ItemReceitaIngredienteResponse();
        arrozIntegral.setReceitaId("69016db421e9c2e6c505f2d6");
        arrozIntegral.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArrozIntegral(arrozIntegral);

        ItemReceitaIngredienteResponse arroz = new ItemReceitaIngredienteResponse();
        arroz.setReceitaId("69016db321e9c2e6c505f2c0");
        arroz.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArroz(arroz);

        ItemReceitaIngredienteResponse feijao = new ItemReceitaIngredienteResponse();
        feijao.setReceitaId("69016db421e9c2e6c505f2cc");
        feijao.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setFeijao(feijao);

        List<ItemReceitaResponse> proteinas = new ArrayList<>();
        ItemReceitaResponse proteina = new ItemReceitaResponse();
        proteina.setPrioridade(1);
        proteina.setReceitaId("69016db321e9c2e6c505f2bf");
        proteinas.add(proteina);
        almoco.setProteinas(proteinas);

        ItemReceitaIngredienteResponse guarnicao = new ItemReceitaIngredienteResponse();
        guarnicao.setReceitaId("69016db321e9c2e6c505f2ba");
        guarnicao.setIngredienteId("68f78a47e858426ca23481bb");
        almoco.setGuarnicao(guarnicao);

        List<ItemReceitaResponse> saladas = new ArrayList<>();
        ItemReceitaResponse salada = new ItemReceitaResponse();
        salada.setPrioridade(1);
        salada.setReceitaId("69016db321e9c2e6c505f2b8");
        saladas.add(salada);
        almoco.setSaladas(saladas);

        ItemReceitaIngredienteResponse molhoSalada = new ItemReceitaIngredienteResponse();
        molhoSalada.setReceitaId("69016db321e9c2e6c505f2bc");
        molhoSalada.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setMolhoSalada(molhoSalada);

        ItemReceitaIngredienteResponse sobremesa = new ItemReceitaIngredienteResponse();
        sobremesa.setReceitaId("69016db421e9c2e6c505f2d8");
        sobremesa.setIngredienteId("68f78a3ee858426ca23481ba");
        almoco.setSobremesa(sobremesa);

        return almoco;
    }

    // Almoço 5 - Sexta-feira
    private static AlmocoResponse createAlmoco5() {
        AlmocoResponse almoco = new AlmocoResponse();

        ItemReceitaIngredienteResponse arrozIntegral = new ItemReceitaIngredienteResponse();
        arrozIntegral.setReceitaId("69016db421e9c2e6c505f2c7");
        arrozIntegral.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArrozIntegral(arrozIntegral);

        ItemReceitaIngredienteResponse arroz = new ItemReceitaIngredienteResponse();
        arroz.setReceitaId("69016db321e9c2e6c505f2c1");
        arroz.setIngredienteId("68f78a51e858426ca23481bc");
        almoco.setArroz(arroz);

        ItemReceitaIngredienteResponse feijao = new ItemReceitaIngredienteResponse();
        feijao.setReceitaId("69016db321e9c2e6c505f2c2");
        feijao.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setFeijao(feijao);

        List<ItemReceitaResponse> proteinas = new ArrayList<>();
        ItemReceitaResponse proteina = new ItemReceitaResponse();
        proteina.setPrioridade(1);
        proteina.setReceitaId("69016db321e9c2e6c505f2b7");
        proteinas.add(proteina);
        almoco.setProteinas(proteinas);

        ItemReceitaIngredienteResponse guarnicao = new ItemReceitaIngredienteResponse();
        guarnicao.setReceitaId("69016db321e9c2e6c505f2ba");
        guarnicao.setIngredienteId("68f78a47e858426ca23481bb");
        almoco.setGuarnicao(guarnicao);

        List<ItemReceitaResponse> saladas = new ArrayList<>();
        ItemReceitaResponse salada = new ItemReceitaResponse();
        salada.setPrioridade(1);
        salada.setReceitaId("69016db421e9c2e6c505f2d9");
        saladas.add(salada);
        almoco.setSaladas(saladas);

        ItemReceitaIngredienteResponse molhoSalada = new ItemReceitaIngredienteResponse();
        molhoSalada.setReceitaId("69016db321e9c2e6c505f2bc");
        molhoSalada.setIngredienteId("68f78a31e858426ca23481b8");
        almoco.setMolhoSalada(molhoSalada);

        ItemReceitaIngredienteResponse sobremesa = new ItemReceitaIngredienteResponse();
        sobremesa.setReceitaId("69016db421e9c2e6c505f2d8");
        sobremesa.setIngredienteId("68f78a3ee858426ca23481ba");
        almoco.setSobremesa(sobremesa);

        return almoco;
    }

    // Lanche Manhã 1
    private static LancheResponse createLancheManha1() {
        LancheResponse lanche = new LancheResponse();

        List<ItemReceitaIngredienteCompletoResponse> opcoes = new ArrayList<>();
        ItemReceitaIngredienteCompletoResponse opcao = new ItemReceitaIngredienteCompletoResponse();
        opcao.setPrioridade(1);
        opcao.setReceitaId("69016db421e9c2e6c505f2ca");
        opcao.setIngredienteId("68f78a3ee858426ca23481ba");
        opcoes.add(opcao);
        lanche.setOpcoes(opcoes);

        FrutaResponse fruta = new FrutaResponse();
        fruta.setIngredienteId("68f78a3ae858426ca23481b9");
        lanche.setFruta(fruta);

        ItemReceitaIngredienteCompletoResponse opcoesFixas = new ItemReceitaIngredienteCompletoResponse();
        opcoesFixas.setPrioridade(1);
        opcoesFixas.setReceitaId("69016db321e9c2e6c505f2b9");
        opcoesFixas.setIngredienteId("68f78a31e858426ca23481b8");
        lanche.setOpcoesFixas(opcoesFixas);

        return lanche;
    }

    // Lanche Manhã 2
    private static LancheResponse createLancheManha2() {
        LancheResponse lanche = new LancheResponse();

        List<ItemReceitaIngredienteCompletoResponse> opcoes = new ArrayList<>();
        ItemReceitaIngredienteCompletoResponse opcao = new ItemReceitaIngredienteCompletoResponse();
        opcao.setPrioridade(1);
        opcao.setReceitaId("69016db421e9c2e6c505f2c9");
        opcao.setIngredienteId("68f78a3ee858426ca23481ba");
        opcoes.add(opcao);
        lanche.setOpcoes(opcoes);

        FrutaResponse fruta = new FrutaResponse();
        fruta.setIngredienteId("68f78a3ae858426ca23481b9");
        lanche.setFruta(fruta);

        ItemReceitaIngredienteCompletoResponse opcoesFixas = new ItemReceitaIngredienteCompletoResponse();
        opcoesFixas.setPrioridade(1);
        opcoesFixas.setReceitaId("69016db321e9c2e6c505f2b9");
        opcoesFixas.setIngredienteId("68f78a31e858426ca23481b8");
        lanche.setOpcoesFixas(opcoesFixas);

        return lanche;
    }

    // Lanche Tarde 1
    private static LancheResponse createLancheTarde1() {
        LancheResponse lanche = new LancheResponse();

        List<ItemReceitaIngredienteCompletoResponse> opcoes = new ArrayList<>();
        ItemReceitaIngredienteCompletoResponse opcao = new ItemReceitaIngredienteCompletoResponse();
        opcao.setPrioridade(1);
        opcao.setReceitaId("69016db321e9c2e6c505f2b5");
        opcao.setIngredienteId("68f78a3ee858426ca23481ba");
        opcoes.add(opcao);
        lanche.setOpcoes(opcoes);

        FrutaResponse fruta = new FrutaResponse();
        fruta.setIngredienteId("68f78a3ae858426ca23481b9");
        lanche.setFruta(fruta);

        ItemReceitaIngredienteCompletoResponse opcoesFixas = new ItemReceitaIngredienteCompletoResponse();
        opcoesFixas.setPrioridade(1);
        opcoesFixas.setReceitaId("69016db421e9c2e6c505f2d7");
        opcoesFixas.setIngredienteId("68f78a31e858426ca23481b8");
        lanche.setOpcoesFixas(opcoesFixas);

        return lanche;
    }

    // Lanche Tarde 2
    private static LancheResponse createLancheTarde2() {
        LancheResponse lanche = new LancheResponse();

        List<ItemReceitaIngredienteCompletoResponse> opcoes = new ArrayList<>();
        ItemReceitaIngredienteCompletoResponse opcao = new ItemReceitaIngredienteCompletoResponse();
        opcao.setPrioridade(1);
        opcao.setReceitaId("69016db321e9c2e6c505f2b5");
        opcao.setIngredienteId("68f78a3ee858426ca23481ba");
        opcoes.add(opcao);
        lanche.setOpcoes(opcoes);

        FrutaResponse fruta = new FrutaResponse();
        fruta.setIngredienteId("68f78a3ae858426ca23481b9");
        lanche.setFruta(fruta);

        ItemReceitaIngredienteCompletoResponse opcoesFixas = new ItemReceitaIngredienteCompletoResponse();
        opcoesFixas.setPrioridade(1);
        opcoesFixas.setReceitaId("69016db421e9c2e6c505f2d7");
        opcoesFixas.setIngredienteId("68f78a31e858426ca23481b8");
        lanche.setOpcoesFixas(opcoesFixas);

        return lanche;
    }
}

