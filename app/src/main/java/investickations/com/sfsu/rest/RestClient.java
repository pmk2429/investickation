package investickations.com.sfsu.rest;


import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.rest.service.EntitiesApi;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * TODO: The code for call the Async call to remote API goes in the Activity where it must be required.
 * <p/>
 * <p/>
 * Created by Pavitra on 10/6/2015.
 */
public class RestClient {
    private EntitiesApi apiService;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(EntitiesApi.class);
    }

    public EntitiesApi getApiService() {
        return apiService;
    }

}
