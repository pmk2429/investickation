package com.sfsu.network.rest.apiclient;

import android.content.Context;

import com.sfsu.network.api.ApiResources;
import com.sfsu.utils.AppUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit Service Generator class which initializes the calling Service interface passed as an input param. Depending on the
 * createService method called, this class will return the Service with or without token.
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class RetrofitApiClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TAG = "~!@#$RetrApiClient";
    // main client
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    //    private static final Context mContext = InvestickationApp.getInstance();
    private static Context mContext;
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (AppUtils.isConnectedOnline(mContext)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    private static long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MB
    // Retrofit
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiResources.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create());

    /**
     * Method to initialize the RetrofitApiClient.
     *
     * @param context
     * @param baseAPIUrl
     */
    public static void init(final Context context, String baseAPIUrl) {
        mContext = context;

        // Create Cache
        Cache cache = null;
        cache = new Cache(new File(mContext.getCacheDir(), "http"), SIZE_OF_CACHE);


        // Add Cache-Control Interceptor

        // Create Executor
        Executor executor = Executors.newCachedThreadPool();
    }

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
        if (authToken != null && !authToken.equals("invalid-auth-token")) {
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
        }

        // build the Retrofit instance with the Token Authorization OkHttpClient.
        Retrofit retrofit = builder.client(httpClient.build()).build();

        // return the ServiceClass passed.
        return retrofit.create(serviceClass);
    }


    /**
     * Helper method to cache the Response sent by the server.
     */
    private static void createCacheForOkHTTP() {
        Cache cache = null;
        cache = new Cache(getDirectory(), 1024 * 1024 * 10);
//        okHttpClient.setCache(cache);
    }

    //returns the file to store cached details
    private static File getDirectory() {
        return new File("location");
    }

}

