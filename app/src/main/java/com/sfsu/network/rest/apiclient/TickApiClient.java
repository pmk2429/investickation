package com.sfsu.network.rest.apiclient;

import com.sfsu.network.rest.service.TickApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class TickApiClient extends RetrofitApiClient {
    private TickApiService mService;

    public TickApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        mService = retrofit.create(TickApiService.class);
    }

    public TickApiService getService() {
        return mService;
    }
}
