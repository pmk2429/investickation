package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;
import com.sfsu.network.rest.EntitiesApiService;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface is used to manage URL calls to the REST Api. In this interface you have to specify the type
 * of the request like POST, GET, PUT, etc.
 * Created by Pavitra on 10/6/2015.
 */

public interface ActivityApiService extends EntitiesApiService {

    /**
     * This method gets a specific activity by using ID of that resource.
     *
     * @return
     */
    @GET("/activities/{id}")
    public Call<Activities> get(@Path("id") String activityId);

    /**
     * This method returns the list of all the Entities in resource based on the resourceIdentifier.
     *
     * @return
     */
    @GET("/activities")
    public Call<List<Activities>> getAll();


    /**
     * This method is used to create the specified resource and store in database.
     *
     * @return
     */
    @POST("/activities")
    public Call<Activities> add(@Body Activities activity);


    @GET("/activities/{id}")
    public Call<Activities> update(@Path("id") String activityId, @Body Activities activity);


    /**
     * Deletes the record from database.
     *
     * @return
     */
    @GET("/activities/{id}")
    public Call<Activities> delete(@Path("id") String activityId);

}