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
     * This method gets a specific user by using ID of that resource.
     *
     * @return
     */
    @GET("users/{id}")
    public Call<User> get(@Path("id") String userId);


    /**
     * Create the {@link User}
     *
     * @return
     */
    @POST("users")
    public Call<User> add(@Body User user);


    /**
     * Updates the {@link User}.
     *
     * @param userId
     * @param user
     * @return
     */
    @GET("users/{id}")
    public Call<User> update(@Path("id") String userId, @Body User user);


    /**
     * destroy() when called, destroys the record from database.
     *
     * @return
     */
    @GET("users/{id}")
    public Call<User> delete(@Path("id") String userId);

}