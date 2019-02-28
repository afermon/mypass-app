package com.cosmicode.roomie.util.network;

import com.cosmicode.roomie.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {

    private static final String ROOMIE_API_PROD = "https://prod-roomie-web.herokuapp.com/api/";
    private static final String ROOMIE_API_STAGING = "https://staging-roomie-web.herokuapp.com/api/";
    private static final String ROOMIE_API_DEV = "https://dev-roomie-web.herokuapp.com/api/";
    private static final String ROOMIE_API_LOCAL = "http://10.0.2.1:8080/api/";


    private static String getServerURL() {
        return BuildConfig.DEBUG ? ROOMIE_API_DEV : ROOMIE_API_PROD;
    }

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(getServerURL())
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String token) {
        if (token != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .header("RoomieUser-Agent", "RoomieAndroid")
                        .build();
                return chain.proceed(request);
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
