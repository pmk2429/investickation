package com.sfsu.network.handler;


import android.util.Base64;

import com.sfsu.network.rest.EntitiesApiService;
import com.sfsu.utils.AppUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 10/6/2015.
 */
public class ServiceManager {
    private static EntitiesApiService apiService;

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(AppUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

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

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    /**
     * Creating Service with Authentication. Uses the RestAdapter class to create the OkHttp client for any HTTP requests and
     * response handling.
     * <p>
     * A RequestInterceptor is used to set the authorization header value for any HTTP request executed with this OkHttp client.
     * But this is only done if the parameters for email and password are provided. If you don’t pass any email and password to
     * the method, it will create the same client as the first method. That’s why we can simplify the first method from the
     * ServiceManager class.
     * </p>
     * For the authentication part we adjusted the format of given email and password. Additionally, the newly created
     * (concatenated) string has to be Base64 encoded.
     * <p>
     * The webservice and API evaluates the Authorization header of the HTTP request. That’s why we set the encoded credentials
     * value to that header field.
     * </p>
     * The Accept header is important if you want to receive the server response in a specific format. In our example we want
     * to receive the response JSON formatted, since Retrofit ships with Google’s GSON to serialize objects into their JSON
     * representation and vice versa.
     *
     * @param serviceClass
     * @param username
     * @param password
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.interceptors().clear();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "applicaton/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }


    public static EntitiesApiService createRetrofitService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(EntitiesApiService.class);

        return apiService;
    }
}
