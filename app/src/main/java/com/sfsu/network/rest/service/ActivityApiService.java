package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface to manage http network calls for {@link Activities} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface ActivityApiService {

    /**
     * Returns a specific {@link Activities} matching .
     *
     * @return
     */
    @GET("/activities/{id}")
    public Call<Activities> get(@Path("id") String activityId);

    /**
     * Returns the list of all the {@link Activities} in the server database.
     *
     * @return
     */
    @GET("/activities")
    public Call<List<Activities>> getAll();


    /**
     * Adds new {@link Activities} record in the Server.
     *
     * @return
     */
    @POST("/activities")
    public Call<Activities> add(@Body Activities activity);


    /**
     * Updates {@link Activities} record.
     *
     * @param activityId
     * @param activity
     * @return
     */
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