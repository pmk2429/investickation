package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.Observation;

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
     * Returns a specific {@link Activities} matching id.
     *
     * @return
     */
    @GET("activities/{id}")
    public Call<Activities> get(@Path("id") String activityId);

    /**
     * Returns the list of all the {@link Activities} from the server.
     *
     * @return
     */
    @GET("activities")
    public Call<List<Activities>> getAll();


    /**
     * Adds new {@link Activities} record to the Server.
     *
     * @return
     */
    @POST("activities")
    public Call<Activities> add(@Body Activities activity);


    /**
     * Updates {@link Activities} record on the server.
     *
     * @param activityId
     * @param activity
     * @return
     */
    @GET("activities/{id}")
    public Call<Activities> update(@Path("id") String activityId, @Body Activities activity);


    /**
     * Delete and Destroy {@link Activities} from the server.
     *
     * @return
     */
    @GET("activities/{id}")
    public Call<Activities> delete(@Path("id") String activityId);


    /**
     * Get all {@link Observation} of a specific {@link Activities}.
     *
     * @param activityId
     * @return
     */
    @GET("activities/{id}/observations")
    public Call<List<Observation>> observations(@Path("id") String activityId);


    /**
     * Get all {@link EntityLocation} of a specific {@link Activities}.
     *
     * @param activityId
     * @return
     */
    @GET("activities/{id}/locations")
    public Call<List<EntityLocation>> locations(@Path("id") String activityId);


    /**
     * Returns total locations captured for this activity.
     *
     * @param activityId
     * @return
     */
    @GET("activities/{id}/locations/count")
    public Call<Integer> totalLocations(@Path("id") String activityId);

}