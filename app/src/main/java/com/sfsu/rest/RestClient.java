package com.sfsu.rest;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * <p/>
 * <p/>
 * Created by Pavitra on 10/6/2015.
 */
public class RestClient {
    private static EntitiesApi apiService;

    public static EntitiesApi createRetrofitService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(EntitiesApi.class);

        return apiService;
    }
}
