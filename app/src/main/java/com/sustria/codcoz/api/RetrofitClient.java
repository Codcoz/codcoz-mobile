package com.sustria.codcoz.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL = "https://codcoz-api-postgres.onrender.com/";
    private static Retrofit retrofitInstance;

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {

            // Configurar logging para debug
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configurar OkHttpClient com timeout e logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                            (json, typeOfT, context) -> {
                        try {
                            return LocalDate.parse(json.getAsString());
                        } catch (Exception e) {
                            throw new JsonParseException("Erro ao converter data: " + json.getAsString(), e);
                        }
                    })
                    .setDateFormat("yyyy-MM-dd")
                    .create();

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitInstance;
    }
}
