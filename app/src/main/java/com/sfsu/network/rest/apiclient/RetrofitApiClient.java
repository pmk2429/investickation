package com.sfsu.network.rest.apiclient;

import android.util.Base64;

import com.sfsu.network.api.ApiDetails;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Retrofit Service Generator class which initializes the calling Service interface passed as an input param. Depending on the
 * createService method called, this class will return the Service with or without token.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class RetrofitApiClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    // main client
    protected static OkHttpClient httpClient = new OkHttpClient();
    private static long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MB
    // Retrofit
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiDetails.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create());

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

        if (authToken != null) {

            httpClient.interceptors().clear();

            httpClient.networkInterceptors().add(new Interceptor() {
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
        Retrofit retrofit = builder.client(httpClient).build();

        // return the ServiceClass passed.
        return retrofit.create(serviceClass);
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
    public static <S> S createServiceForUpload(Class<S> serviceClass, final String authToken) {

        if (authToken != null) {
            httpClient.interceptors().clear();
            httpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header(CONTENT_TYPE, "multipart/form-data")
                            .header(AUTHORIZATION, authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        // build the Retrofit instance with the Token Authorization OkHttpClient.
        Retrofit retrofit = builder.client(httpClient).build();

        // return the ServiceClass passed.
        return retrofit.create(serviceClass);
    }

    /**
     * Generates the Service to to login the Account. Wraps the Email and Password fields in the Header and requests the call.
     *
     * @param serviceClass
     * @param email
     * @param password
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, String email, String password) {
        if (email != null && password != null) {
            String credentials = email + ":" + password;
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.interceptors().clear();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic).header("Accept", "applicaton/json").method(original.method(),
                                    original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        Retrofit retrofit = builder.client(httpClient).build();
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

