package com.sfsu.network.rest.service;


import com.sfsu.entities.User;
import com.sfsu.network.login.LoginResponse;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
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
     * This method returns the list of all the Entities in resource based on the resourceIdentifier.
     *
     * @return
     */
    @GET("users")
    public Call<List<User>> getAll();


    /**
     * This method is used to create the specified resource and store in database.
     *
     * @return
     */
    @POST("users")
    public Call<User> add(@Body User user);


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