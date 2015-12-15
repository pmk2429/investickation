package com.sfsu.network.rest.service;


import com.sfsu.entities.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface to manage http network calls for {@link User} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface UserApiService {

    /**
     * Returns the current {@link User} from the server
     *
     * @return
     */
    @GET("users/{id}")
    public Call<User> get(@Path("id") String userId);


    /**
     * Creates the {@link User} in the server.
     *
     * @return
     */
    @POST("users")
    public Call<User> add(@Body User user);


    /**
     * Updates the {@link User} in the server.
     *
     * @param userId
     * @param user
     * @return
     */
    @GET("users/{id}")
    public Call<User> update(@Path("id") String userId, @Body User user);


    /**
     * Deletes the {@link User} from server.
     *
     * @return
     */
    @GET("users/{id}")
    public Call<User> delete(@Path("id") String userId);


    @GET("")
    public int getTotalActivities();

    @GET("")
    public int getTotalObservations();

    @GET("")
    public int getTotalLocations();


}