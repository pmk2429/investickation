package com.sfsu.network.rest.apiclient;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
    public static final String BASE_API_URL = "https://investickations.com:3000";
    protected static OkHttpClient httpClient = new OkHttpClient();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
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
     * @param authToken    -  Access token retrieved after Successful login by the User.
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.interceptors().clear();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authToken)
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
}

