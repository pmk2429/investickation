package com.sfsu.network.rest.service;


import com.sfsu.entities.Account;
import com.sfsu.network.api.ApiResources;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The <b>Service</b> interface to manage http network calls for {@link Account} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface UserApiService {

    /**
     * Returns the current {@link Account} from the server
     *
     * @return
     */
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID)
    public Call<Account> get(@Path("id") String userId);


    /**
     * Creates the {@link Account} in the server.
     *
     * @return
     */
    @POST(ApiResources.AccountBase)
    public Call<Account> add(@Body Account user);


    /**
     * Updates the {@link Account} in the server.
     *
     * @param userId
     * @param user
     * @return
     */
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID)
    public Call<Account> update(@Path("id") String userId, @Body Account user);


    /**
     * Deletes the {@link Account} from server.
     *
     * @return
     */
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID)
    public Call<Account> delete(@Path("id") String userId);


    @GET("")
    public int totalActivities();

    @GET("")
    public int totalObservations();

    @GET("")
    public int totalLocations();


}