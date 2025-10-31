package com.sustria.codcoz.utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static final String COLLECTION_ESTOQUE = "estoque_percentual";
    private static final String OCUPACAO_ESTOQUE = "ocupacaoEstoque";
    private static final String DATA = "data";
    private static final String EMPRESA_ID = "empresaId";
    private static final int DIAS_MANTER = 7;
    
    private static FirebaseHelper instance;
    private FirebaseFirestore db;
    
    private FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
    }
    
    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }
    
    public interface PercentualCallback {
        void onSuccess(Double percentual);
        void onError(String error);
    }
    
    public interface PercentualDiarioCallback {
        void onSuccess(Double percentual, LocalDate data);
        void onError(String error);
    }

    public void savePercentualAtual(Double percentual, Integer empresaId) {
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        Map<String, Object> dados = new HashMap<>();
        dados.put(OCUPACAO_ESTOQUE, percentual);
        dados.put(DATA, dataAtual);
        dados.put(EMPRESA_ID, empresaId);
        
        // Criar ID único combinando empresaId e data
        String documentoId = empresaId + "_" + dataAtual;
        
        // Salva o documento
        db.collection(COLLECTION_ESTOQUE)
                .document(documentoId)
                .set(dados)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Percentual salvo com sucesso para " + dataAtual + ": " + percentual + "%");
                    // Após salvar, limpar dados antigos
                    limparDadosAntigos(empresaId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao salvar percentual", e);
                });
    }
    
    private void limparDadosAntigos(Integer empresaId) {
        // Calcular data limite (8 dias atrás)
        LocalDate dataLimite = LocalDate.now().minusDays(8);
        String dataLimiteStr = dataLimite.format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        // Buscar documentos da empresa
        db.collection(COLLECTION_ESTOQUE)
                .whereEqualTo(EMPRESA_ID, empresaId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QueryDocumentSnapshot> documentosParaDeletar = new java.util.ArrayList<>();

                    // Filtrar documentos mais antigos que 8 dias
                    for (DocumentSnapshot documento : queryDocumentSnapshots.getDocuments()) {
                        String dataDoc = documento.getString(DATA);
                        if (dataDoc != null && dataDoc.compareTo(dataLimiteStr) < 0) {
                            documentosParaDeletar.add((QueryDocumentSnapshot) documento);
                        }
                    }

                    if (!documentosParaDeletar.isEmpty()) {
                        Log.d(TAG, "Encontrados " + documentosParaDeletar.size() + " documentos antigos para deletar");
                        
                        // Deletar cada documento
                        for (QueryDocumentSnapshot documento : documentosParaDeletar) {
                            documento.getReference()
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Documento antigo deletado: " + documento.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Erro ao deletar documento antigo", e);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao buscar documentos antigos", e);
                });
    }
    

    public void searchDatePercentual(Integer empresaId, LocalDate data, PercentualCallback callback) {
        String dataStr = data.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String documentoId = empresaId + "_" + dataStr;
        
        db.collection(COLLECTION_ESTOQUE)
                .document(documentoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double percentual = documentSnapshot.getDouble(OCUPACAO_ESTOQUE);
                        if (percentual != null) {
                            callback.onSuccess(percentual);
                        } else {
                            callback.onError("Percentual não encontrado no documento");
                        }
                    } else {
                        callback.onError("Documento não encontrado para a data: " + dataStr);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao buscar percentual", e);
                    callback.onError("Erro ao buscar percentual: " + e.getMessage());
                });
    }

    public void searchPreviousDayPercentual(Integer empresaId, PercentualDiarioCallback callback) {
        LocalDate ontem = LocalDate.now().minusDays(1);
        String dataOntem = ontem.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String documentoId = empresaId + "_" + dataOntem;
        
        db.collection(COLLECTION_ESTOQUE)
                .document(documentoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double percentual = documentSnapshot.getDouble(OCUPACAO_ESTOQUE);
                        if (percentual != null) {
                            callback.onSuccess(percentual, ontem);
                        } else {
                            callback.onError("Percentual não encontrado no documento");
                        }
                    } else {
                        callback.onError("Sem dados para o dia anterior");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao buscar percentual do dia anterior", e);
                    callback.onError("Erro ao buscar percentual do dia anterior: " + e.getMessage());
                });
    }
    

    public void searchMostRecentPercentual(Integer empresaId, PercentualCallback callback) {
        db.collection(COLLECTION_ESTOQUE)
                .whereEqualTo(EMPRESA_ID, empresaId)
                .orderBy(DATA, Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        Double percentual = document.getDouble(OCUPACAO_ESTOQUE);
                        if (percentual != null) {
                            callback.onSuccess(percentual);
                        } else {
                            callback.onError("Percentual não encontrado no documento");
                        }
                    } else {
                        callback.onError("Nenhum dado de estoque encontrado");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao buscar percentual mais recente", e);
                    callback.onError("Erro ao buscar percentual mais recente: " + e.getMessage());
                });
    }

    public String calculateStatus(Double percentual) {
        if (percentual == null) return "Indefinido";
        
        if (percentual >= 70) {
            return "Ótimo!";
        } else if (percentual >= 40) {
            return "Moderado";
        } else {
            return "Atenção!";
        }
    }
}

