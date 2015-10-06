package investickations.com.sfsu.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import investickations.com.sfsu.rest.service.ApiService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Pavitra on 10/6/2015.
 */
public class RestClient {
    private static final String BASE_URL = "your base url";
    private ApiService apiService;

    public RestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(null) // This is the important line ;)
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

}
