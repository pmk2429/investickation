package investickations.com.sfsu.rest.service;

import retrofit.http.GET;

/**
 * Created by Pavitra on 10/6/2015.
 */
public interface ApiService {

    //    @Header("Cache-Control: This is an awesome header")
    @GET("/api/getDummieContent")
    public void getDummieContent();

}