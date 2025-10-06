package com.sustria.codcoz.api;

import com.sustria.codcoz.model.Atividade;
import com.sustria.codcoz.model.Auditoria;
import com.sustria.codcoz.model.EstoqueInfo;
import com.sustria.codcoz.model.Estoquista;
import com.sustria.codcoz.model.Produto;
import com.sustria.codcoz.model.RegistroHistorico;
import com.sustria.codcoz.model.Tarefa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Cliente Retrofit simplificado para fácil uso
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://api.codcoz.com/";
    private static RetrofitClient instance;
    private ApiService apiService;

    private RetrofitClient() {
        // Inicialização será feita quando necessário
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public void init() {
        if (apiService == null) {
            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
    }

    public ApiService getApiService() {
        if (apiService == null) {
            init();
        }
        return apiService;
    }

    /**
     * Entrada de estoque simplificada
     */
    public void entradaEstoque(RegistroHistorico registro, int quantidade, ApiCallback<RegistroHistorico> callback) {
        getApiService().entradaEstoque(quantidade, registro).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RegistroHistorico> call, Response<RegistroHistorico> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro na entrada de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegistroHistorico> call, Throwable t) {
                callback.onError("Falha na entrada de estoque: " + t.getMessage());
            }
        });
    }

    /**
     * Baixa de estoque simplificada
     */
    public void baixaEstoque(RegistroHistorico registro, int quantidade, ApiCallback<RegistroHistorico> callback) {
        getApiService().baixaEstoque(quantidade, registro).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RegistroHistorico> call, Response<RegistroHistorico> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro na baixa de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegistroHistorico> call, Throwable t) {
                callback.onError("Falha na baixa de estoque: " + t.getMessage());
            }
        });
    }


    // ===== NOVOS MÉTODOS DA API =====

    /**
     * Buscar estoquista por email (Login)
     */
    public void buscarEstoquista(String email, ApiCallback<Estoquista> callback) {
        getApiService().buscarEstoquista(email).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Estoquista> call, Response<Estoquista> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar estoquista: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Estoquista> call, Throwable t) {
                callback.onError("Falha ao buscar estoquista: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar quantidade de estoque (Home)
     */
    public void getQuantidadeEstoque(ApiCallback<EstoqueInfo> callback) {
        getApiService().getQuantidadeEstoque().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EstoqueInfo> call, Response<EstoqueInfo> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar quantidade de estoque: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EstoqueInfo> call, Throwable t) {
                callback.onError("Falha ao buscar quantidade de estoque: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar estoque baixo (Home)
     */
    public void getEstoqueBaixo(ApiCallback<EstoqueInfo> callback) {
        getApiService().getEstoqueBaixo().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EstoqueInfo> call, Response<EstoqueInfo> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar estoque baixo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EstoqueInfo> call, Throwable t) {
                callback.onError("Falha ao buscar estoque baixo: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar produtos próximos ao vencimento (Home)
     */
    public void getProximoValidade(ApiCallback<EstoqueInfo> callback) {
        getApiService().getProximoValidade().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EstoqueInfo> call, Response<EstoqueInfo> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar produtos próximos ao vencimento: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EstoqueInfo> call, Throwable t) {
                callback.onError("Falha ao buscar produtos próximos ao vencimento: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar tarefas por mês (Home - Calendário)
     */
    public void getTarefasPorMes(String mes, String ano, ApiCallback<Tarefa.TarefasResponse> callback) {
        getApiService().getTarefasPorMes(mes, ano).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Tarefa.TarefasResponse> call, Response<Tarefa.TarefasResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas por mês: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Tarefa.TarefasResponse> call, Throwable t) {
                callback.onError("Falha ao buscar tarefas por mês: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar tarefas por dia (Home - Calendário)
     */
    public void getTarefasPorDia(String dia, String mes, String ano, ApiCallback<Tarefa.TarefasResponse> callback) {
        getApiService().getTarefasPorDia(dia, mes, ano).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Tarefa.TarefasResponse> call, Response<Tarefa.TarefasResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar tarefas por dia: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Tarefa.TarefasResponse> call, Throwable t) {
                callback.onError("Falha ao buscar tarefas por dia: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar produto por código (Scanner)
     */
    public void buscarProduto(String codigo, ApiCallback<Produto> callback) {
        getApiService().buscarProduto(codigo).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar produto: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                callback.onError("Falha ao buscar produto: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar atividades do estoquista (Perfil)
     */
    public void getAtividadesEstoquista(String email, String dia, String mes, String ano, ApiCallback<Atividade.AtividadesResponse> callback) {
        getApiService().getAtividadesEstoquista(email, dia, mes, ano).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Atividade.AtividadesResponse> call, Response<Atividade.AtividadesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar atividades: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Atividade.AtividadesResponse> call, Throwable t) {
                callback.onError("Falha ao buscar atividades: " + t.getMessage());
            }
        });
    }

    /**
     * Buscar auditorias do estoquista (Perfil)
     */
    public void getAuditoriasEstoquista(String email, String dia, String mes, String ano, ApiCallback<Auditoria.AuditoriasResponse> callback) {
        getApiService().getAuditoriasEstoquista(email, dia, mes, ano).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Auditoria.AuditoriasResponse> call, Response<Auditoria.AuditoriasResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao buscar auditorias: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Auditoria.AuditoriasResponse> call, Throwable t) {
                callback.onError("Falha ao buscar auditorias: " + t.getMessage());
            }
        });
    }

    /**
     * Interface para callbacks simplificados
     */
    public interface ApiCallback<T> {
        void onSuccess(T data);

        void onError(String error);
    }
}



