package com.sustria.codcoz.ui.cardapio.actions;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sustria.codcoz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardapioSemanal extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter adapter;
    List<String> diaSemana;
    HashMap<String, List<String>> itemRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio_semanal);
        expandableListView = findViewById(R.id.expandableListView);

        diaSemana = new ArrayList<>();
        itemRefeicao = new HashMap<>();

        diaSemana.add("Segunda-feira");
        diaSemana.add("Terça-feira");
        diaSemana.add("Quarta-feira");
        diaSemana.add("Quinta-feira");
        diaSemana.add("Sexta-feira");

        List<String> frutas = new ArrayList<>();
        frutas.add("Maçã");
        frutas.add("Banana");
        frutas.add("Laranja");

        List<String> legumes = new ArrayList<>();
        legumes.add("Cenoura");
        legumes.add("Batata");
        legumes.add("Tomate");

        itemRefeicao.put(diaSemana.get(0), frutas);
        itemRefeicao.put(diaSemana.get(1), legumes);

        adapter = new ExpandableListAdapterr(this, diaSemana, itemRefeicao);
        expandableListView.setAdapter(adapter);

        // Clique no filho
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String item = itemRefeicao.get(diaSemana.get(groupPosition)).get(childPosition);
            Toast.makeText(this, "Selecionado: " + item, Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}