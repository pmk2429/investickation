package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.sfsu.entities.response.ObservationResponse;
import com.sfsu.entities.response.ResponseCount;
import com.sfsu.network.api.ApiResources;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The <b>Service</b> interface to manage http network calls for {@link Observation} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface ObservationApiService {

    /**
     * Returns specific {@link Observation} matching id.
     *
     * @return
     */
    //@GET("observations/{id}")
    @GET(ApiResources.ObservationBase + "/" + ApiResources.ID)
    public Call<Observation> get(@Path("id") String observationId);

    /**
     * Returns list of {@link Observation} from server.
     *
     * @return
     */
//    @GET("accounts/{id}/observations")
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID + "/" + ApiResources.ObservationBase)
    public Call<List<Observation>> getAll(@Path("id") String user_id);


    /**
     * Gets all {@link Observation} of a specific {@link Activities}.
     *
     * @param activityId
     * @return
     */
//    @GET("activities/{id}/observations")
    @GET(ApiResources.ActivitiesBase + "/" + ApiResources.ID + "/" + ApiResources.ObservationBase)
    public Call<List<Observation>> observationsOfActivity(@Path("id") String activityId, @Query("filter") String
            includeFilter);


    /**
     * Adds new {@link Observation} to the server.
     *
     * @return
     */
//    @POST("observations")
    @POST(ApiResources.ObservationBase)
    public Call<Observation> add(@Body Observation observation);


    /**
     * Updates the {@link Observation} in the server for the ID.
     *
     * @param observationId
     * @param observation
     * @return
     */
    @PUT(ApiResources.ObservationBase + "/" + ApiResources.ID)
    public Call<Observation> update(@Path("id") String observationId, @Body Observation observation);


    /**
     * Deletes the {@link Observation} record from the server.
     *
     * @return
     */
    @DELETE(ApiResources.ObservationBase + "/" + ApiResources.ID)
    public Call<ResponseCount> delete(@Path("id") String observationId);


    /**
     * Returns a wrapper {@link ObservationResponse} object which <tt>included</tt> {@link com.sfsu.entities.Tick} and {@link
     * Activities} for the current Observation
     *
     * @param observationId
     * @return
     */
    @GET(ApiResources.ObservationBase + "/" + ApiResources.ID)
    public Call<ObservationResponse> getObservationWrapper(@Path("id") String observationId, @Query("filter") String filter);


    /**
     * Returns total Activities recorded by the user.
     *
     * @param activityId
     * @return
     */
    @GET(ApiResources.ObservationBase + "/" + "count")
    public Call<ResponseCount> count(@Query("where") String whereClause);


}