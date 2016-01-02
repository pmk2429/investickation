package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

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
    @GET("observations/{id}")
    public Call<Observation> get(@Path("id") String observationId);

    /**
     * Returns list of {@link Observation} from server.
     *
     * @return
     */
    @GET("observations")
    public Call<List<Observation>> getAll();


    /**
     * Gets all {@link Observation} of a specific {@link Activities}.
     *
     * @param activityId
     * @return
     */
    @GET("activities/{id}/observations")
    public Call<List<Observation>> observationsOfActivity(@Path("id") String activityId);


    /**
     * Adds new {@link Observation} to the server.
     *
     * @return
     */
    @POST("observations")
    public Call<Observation> add(@Body Observation observation);


    /**
     * Updates the {@link Observation} in the server.
     *
     * @param observationId
     * @param observation
     * @return
     */
    @GET("observations/{id}")
    public Call<Observation> update(@Path("id") String observationId, @Body Observation observation);


    /**
     * Deletes the {@link Observation} record from the server.
     *
     * @return
     */
    @GET("observations/{id}")
    public Call<Observation> delete(@Path("id") String observationId);

    /**
     * Uploads the Observation image to the server.
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("observations/upload_tick_pic")
    Call<Observation> upload(@Part("myfile\"; filename=\"image.png\" ") RequestBody file);

}