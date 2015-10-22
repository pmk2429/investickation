package com.sfsu.rest;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * TODO: The code for call the Async call to remote API goes in the Activity where it must be required.
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
