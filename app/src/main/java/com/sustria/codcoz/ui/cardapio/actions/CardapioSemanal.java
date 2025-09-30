package com.sustria.codcoz.ui.cardapio.actions;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardapioSemanal extends AppCompatActivity {

    ExpandableListView expandableListView;
    android.widget.ExpandableListAdapter adapter;
    List<String> diaSemana;
    List<String> cafeDaManha;
    List<String> almoco;
    List<String> cafeDaTarde;
    HashMap<String, List<String>> itemRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio_semanal);
        expandableListView = findViewById(R.id.expandableListView);

        diaSemana = new ArrayList<>();
        itemRefeicao = new HashMap<>();
        cafeDaManha = new ArrayList<>();
        almoco = new ArrayList<>();
        cafeDaTarde = new ArrayList<>();

        diaSemana.add("Segunda-feira");
        diaSemana.add("Terça-feira");
        diaSemana.add("Quarta-feira");
        diaSemana.add("Quinta-feira");
        diaSemana.add("Sexta-feira");

        cafeDaManha.add("Pão");
        cafeDaManha.add("Suco");
        cafeDaManha.add("Ovos mexidos");

        almoco.add("Carne assada");
        almoco.add("Arroz branco");
        almoco.add("Farofa");

        cafeDaTarde.add("Bolo");
        cafeDaTarde.add("Suco");
        cafeDaTarde.add("Pão");

        StringBuilder cafeManha = new StringBuilder();
        for (String i : cafeDaManha) {
            cafeManha.append("\n- ").append(i);
        }

        StringBuilder cafeTarde = new StringBuilder();
        for (String i : cafeDaTarde) {
            cafeTarde.append("\n- ").append(i);
        }

        StringBuilder almocoTxt = new StringBuilder();
        for (String i : almoco) {
            almocoTxt.append("\n- ").append(i);
        }

        List<String> refeicao = new ArrayList<>();
        refeicao.add("Café da manhã: " + cafeManha);
        refeicao.add("Almoço: " + almocoTxt);
        refeicao.add("Café da tarde: " + cafeTarde);

        itemRefeicao.put(diaSemana.get(0), refeicao);
        itemRefeicao.put(diaSemana.get(1), refeicao);
        itemRefeicao.put(diaSemana.get(2), refeicao);
        itemRefeicao.put(diaSemana.get(3), refeicao);
        itemRefeicao.put(diaSemana.get(4), refeicao);

        adapter = new ExpandableListAdapter(this, diaSemana, itemRefeicao);
        expandableListView.setAdapter(adapter);
    }
}