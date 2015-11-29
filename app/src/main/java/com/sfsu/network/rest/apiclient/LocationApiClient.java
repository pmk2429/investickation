package com.sfsu.network.rest.apiclient;

import com.sfsu.network.rest.service.LocationApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class LocationApiClient extends RetrofitApiClient {
    private LocationApiService mService;

    public LocationApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        mService = retrofit.create(LocationApiService.class);
    }

    public LocationApiService getService() {
        return mService;
    }
}
