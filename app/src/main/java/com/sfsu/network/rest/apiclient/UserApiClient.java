package com.sfsu.network.rest.apiclient;

import com.sfsu.network.rest.service.UserApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class UserApiClient extends RetrofitApiClient {
    private UserApiService mService;

    public UserApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        mService = retrofit.create(UserApiService.class);
    }

    public UserApiService getService() {
        return mService;
    }

    public void get(String userId) {
        mService.get(userId);
    }

}
