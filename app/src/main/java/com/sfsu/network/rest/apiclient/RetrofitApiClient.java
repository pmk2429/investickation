package com.sfsu.network.rest.apiclient;

import com.sfsu.application.InvestickationApp;
import com.sfsu.network.api.ApiResources;
import com.sfsu.utils.AppUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit Service Generator class which initializes the calling Service interface passed as an input param. Depending on the
 * createService method called, this class will return the Service with or without token.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class RetrofitApiClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TAG = "~!@#$RetrApiClient";
    //    private static final Context mContext = InvestickationApp.getInstance();
    // caching
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (AppUtils.isConnectedOnline(InvestickationApp.getInstance())) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 4-weeks stale data
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    // main client
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    // size
    private static long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MB
    // Retrofit
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiResources.BASE_API_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());
    private static Cache cache;

    /**
     * Generates the Retrofit Service interface for the type of Service class passed as an input param.
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    /**
     * Generates the Service to to add OAuth 2.0 Access token as the Header to the HTTP call made by Retrofit. The Header is
     * added using an OkHttpClient Http Client and it contains the Interceptor to add the Header for incoming and outgoing
     * requests.
     *
     * @param serviceClass -  The Retrofit Service Interface class.
     * @param authToken    -  Access token retrieved after Successful login by the Account.
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        // IMP: always clear the interceptors.
        if (authToken != null && !authToken.equals("")) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header(AUTHORIZATION, authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            // add Cache to network interceptor
            httpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            // Add Cache-Control Interceptor
            //setup cache
            File httpCacheDirectory = new File(InvestickationApp.getInstance().getCacheDir(), "invest_responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            cache = new Cache(httpCacheDirectory, cacheSize);

            // set Cache to the OkHttpClient
            httpClient.cache(cache);
        }

        // build the Retrofit instance with the Token Authorization OkHttpClient.
        Retrofit retrofit = builder.client(httpClient.build()).build();

        // return the ServiceClass passed.
        return retrofit.create(serviceClass);
    }


    //returns the file to store cached details
    private static File getDirectory() {
        return new File("location");
    }

}

