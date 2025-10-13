package com.sustria.codcoz.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL = "https://codcoz-api-postgres.onrender.com/";
    private static Retrofit retrofitInstance;

    private RetrofitClient() {}

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> {
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
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitInstance;
    }
}
