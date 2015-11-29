package com.sfsu.network.rest.service;


import com.sfsu.entities.Observation;

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

public interface ObservationApiService {

    /**
     * Returns specific Observation
     *
     * @return
     */
    @GET("/observations/{id}")
    public Call<Observation> get(@Path("id") String observationId);

    /**
     * Returns list of Observation
     *
     * @return
     */
    @GET("/observations")
    public Call<List<Observation>> getAll();


    /**
     * Add new Observation.
     *
     * @return
     */
    @POST("/observations")
    public Call<Observation> add(@Body Observation observation);


    /**
     * Update the Observation for specific Id.
     *
     * @param observationId
     * @param observation
     * @return
     */
    @GET("/observations/{id}")
    public Call<Observation> update(@Path("id") String observationId, @Body Observation observation);


    /**
     * Delete the Observation record.
     *
     * @return
     */
    @GET("/observations/{id}")
    public Call<Observation> delete(@Path("id") String observationId);

}