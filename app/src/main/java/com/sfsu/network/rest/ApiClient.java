package com.sfsu.network.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * The singleton ApiClient that declares and initializes the Retrofit instance to build the instance and create the Retrofit
 * Service Interface.
 * <p>
 * The class enforces singleton design pattern in order to avoid creating other instances and hence avoiding
 * possible network leaks and errors.
 * </p>
 * Created by Pavitra on 11/27/2015.
 */
public class ApiClient {
    public static final String API_BASE_URL = "http://investickation.com:3000";

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

    /**
     * initializes the Retrofit.
     */
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        myEntitiesApiService = retrofit.create(EntitiesApiService.class);
    }

    /**
     * Returns EntitiesApiService instance.
     *
     * @return
     */
    public EntitiesApiService getMyRetrofitService() {
        return myEntitiesApiService;
    }
}
