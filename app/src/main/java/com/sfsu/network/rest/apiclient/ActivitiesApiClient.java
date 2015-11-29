package com.sfsu.network.rest.apiclient;

import com.sfsu.network.rest.service.ActivityApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class ActivitiesApiClient extends RetrofitApiClient {
    private ActivityApiService mService;

    public ActivitiesApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        mService = retrofit.create(ActivityApiService.class);
    }
}
