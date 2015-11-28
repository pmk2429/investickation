package com.sfsu.network.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class ApiClient {
    public static final String API_ROOT = "https://api.github.com";

    private static volatile ApiClient instance;

    private EntitiesApiService myEntitiesApiService;

    private ApiClient() {
        init();
    }

    public static ApiClient getInstance() {
        ApiClient localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiClient();
                }
            }
        }
        return localInstance;
    }

    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        myEntitiesApiService = retrofit.create(EntitiesApiService.class);
    }

    public EntitiesApiService getMyRetrofitService() {
        return myEntitiesApiService;
    }
}
