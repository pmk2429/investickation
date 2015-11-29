package com.sfsu.network.rest;


import com.sfsu.entities.Entity;
import com.sfsu.entities.User;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface is used to manage URL calls to the REST Api. In this interface you have to specify the type
 * of the request like POST, GET, PUT, etc.
 * Created by Pavitra on 10/6/2015.
 */

public interface EntitiesApiService<T> {

    /**
     * This method gets a specific resource by using ID of that resource.
     *
     * @return
     */
    @GET("/{resource}/{resourceId}")
    public Call<Entity> get(@Path("resource") String resource, @Path("resourceId") String resourceId);

    /**
     * This method returns the list of all the Entities in resource based on the resourceIdentifier.
     *
     * @return
     */
    @GET("/{resource}")
    public Call<T> getAll(@Path("resource") String resource);


    /**
     * This method is used to create the specified resource and store in database.
     *
     * @return
     */
    @POST("/{resource}/new")
    public Call<Entity> add(@Path("resource") String resource, @Body Entity entity);


    /**
     * Use to update the entity.
     *
     * @param resource   Specifies the type of resource
     * @param resourceId Resource Id. ie UserId, TickId etc
     * @param entity     Entire entity object
     * @return
     */
    @GET("/{resource}/{resourceId}")
    public Call<Entity> update(@Path("resource") String resource, @Path("resourceId") String resourceId, @Body Entity entity);


    /**
     * destroy() when called, destroys the record from database.
     *
     * @return
     */
    @GET("/{resource}/{resourceId}")
    public Call<Entity> delete(@Path("resource") String resource, @Path("resourceId") String resourceId);

    /**
     * Login the user and get RequestToken for the User.
     *
     * @param email
     * @param password
     * @param callback
     */
    @FormUrlEncoded
    @POST("/login")
    public Call<String> login(@Field("email") String email, @Field("password") String password, Callback<User> callback);
}