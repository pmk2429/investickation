package com.sfsu.network.rest.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Pavitra on 12/5/2015.
 */
public class ApiOkHttpClient {

    /**
     * Returns an OkHttpClient containing Interceptor to send the request using Header.
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("User-Agent", "Retrofit-Sample-App").build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);

        return client;
    }
}
